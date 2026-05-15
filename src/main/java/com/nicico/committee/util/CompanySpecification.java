package com.nicico.committee.util;

import com.nicico.copper.common.dto.search.EOperator;
import com.nicico.copper.common.dto.search.SearchDTO;
import com.nicico.copper.common.enumeration.date.EUniCalendar;
import com.nicico.copper.common.enumeration.date.EUniDateField;
import com.nicico.copper.common.util.NICICOBaseContext;
import com.nicico.copper.common.util.StringUtil;
import com.nicico.copper.common.util.date.DateUtil;
import com.nicico.copper.common.util.date.UniDate;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CompanySpecification<T> implements Specification<T> {
	private NICICOBaseContext currentContext = new NICICOBaseContext();

	private final SearchDTO.CriteriaRq criteria;
	private final List<SearchDTO.SortByRq> sortByList;
	private final Boolean distinct;

	// ------------------------------

	private CompanySpecification(SearchDTO.CriteriaRq criteria, List<SearchDTO.SortByRq> sortByList, Boolean distinct) {
		this.criteria = criteria;
		this.sortByList = sortByList;
		this.distinct = distinct;
	}

	// ------------------------------

	public static <T> CompanySpecification<T> of(SearchDTO.CriteriaRq criteria) {
		return new CompanySpecification<>(criteria, null, false);
	}

	public static <T> CompanySpecification<T> of(SearchDTO.CriteriaRq criteria, List<SearchDTO.SortByRq> sortByList) {
		return new CompanySpecification<>(criteria, sortByList, false);
	}

	public static <T> CompanySpecification<T> of(SearchDTO.SearchRq request) {
		if (request.getCriteria() != null)
			return new CompanySpecification<>(request.getCriteria(), request.getSortBy(), request.getDistinct());
		else
			return new CompanySpecification<>(null, request.getSortBy(), request.getDistinct());
	}

	// ------------------------------

	private static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
//		for (Join<?, ?> join : from.getJoins()) {
//			boolean sameName = join.getAttribute().getName().equals(attribute);
//			if (sameName && join.getJoinType().equals(JoinType.LEFT)) {
//				return join;
//			}
//		}

		return from.join(attribute, JoinType.LEFT);
	}

	// ------------------------------

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate predicate = null;

		if (criteria != null) {
			predicate = createPredicate(root, builder, criteria);
		}

		if (sortByList != null && !sortByList.isEmpty()) {
			final List<Order> orderList = sortByList.stream().map(order -> {
				final Path<Object> path = findPath(root, order.getFieldName(),builder);
				return order.getDescendingSafe() ? builder.desc(path) : builder.asc(path);
			}).collect(Collectors.toList());
			query.orderBy(orderList);
		}

		query.distinct(distinct);

		return predicate;
	}

	private Predicate createPredicate(Root<T> root, CriteriaBuilder builder, SearchDTO.CriteriaRq criteria) {
		Predicate predicate = null;

		switch (criteria.getOperator()) {
			case and:
				List<Predicate> andPredicates = new ArrayList<>();
				if (!CollectionUtils.isEmpty(criteria.getCriteria())) {
					andPredicates = criteria.getCriteria().stream().map(subCriteria ->
							createPredicate(root, builder, subCriteria)).filter(subPredicate ->
							subPredicate != null).collect(Collectors.toList());
				}

				if (andPredicates.size() != 0)
					predicate = builder.and(andPredicates.toArray(new Predicate[0]));
				break;
			case or:
				List<Predicate> orPredicates = new ArrayList<>();
				if (!CollectionUtils.isEmpty(criteria.getCriteria())) {
					orPredicates = criteria.getCriteria().stream().map(subCriteria ->
							createPredicate(root, builder, subCriteria)).filter(subPredicate ->
							subPredicate != null).collect(Collectors.toList());
				}

				if (orPredicates.size() != 0)
					predicate = builder.or(orPredicates.toArray(new Predicate[0]));
				break;
			case not:
				break;
			default:
				return createComparisonPredicate(root, builder, criteria);
		}

		return predicate;
	}

	private <P> Path<P> findPath(Root<T> root, String property, CriteriaBuilder builder) {
		final String[] split = property.split("\\.");

		Path path = root;
		for (int i = 0; i < split.length; i++) {
			String part = split[i];

			if (Collection.class.isAssignableFrom(path.get(part).getJavaType())) {
				path = getOrCreateJoin(root, part);
			} else if (Map.class.isAssignableFrom(path.get(part).getJavaType())) {

				// This is the Map field (e.g., 'extraData')
				Path<Map> mapPath = path.get(part);

				// The next part must be the Map key (e.g., 'descCtlQuality')
				if (i + 1 < split.length) {
					return path.get(part);
				}} else {
					path = path.get(part);
				}
			}

			if (path == null) {
				throw new RuntimeException("Invalid property: " + property + ", " + split[0]);
			}

			return path;

	}


	private Comparable convertAsSingle(SearchDTO.CriteriaRq criteria, Path<? extends Comparable> path, Boolean caseInsensitive) {
		return convertAsSingle(criteria, path, 0, caseInsensitive);
	}

	private Comparable convertAsSingle(SearchDTO.CriteriaRq criteria, Path<? extends Comparable> path, int index, Boolean caseInsensitive) {
		if (criteria.getValue() != null && criteria.getValue().size() > index) {
			return convert(path.getJavaType(), criteria.getValue().get(index), caseInsensitive);
		}

		return null;
	}

	private List<Comparable> convertAsList(SearchDTO.CriteriaRq criteria, Path<? extends Comparable> path, Boolean caseInsensitive) {
		if (criteria.getValue() != null && !criteria.getValue().isEmpty()) {
			return criteria.getValue().stream().map(v -> convert(path.getJavaType(), v, caseInsensitive)).collect(Collectors.toList());
		}

		return null;
	}

	private Comparable convert(Class<?> cls, Object value, Boolean caseInsensitive) {
		if (cls.equals(String.class)) {
			final String str = StringUtil.replaceSpecialArabic((String) value);
			return caseInsensitive ? str.toLowerCase() : str;
		}

		if (cls.equals(BigDecimal.class)) {
			return new BigDecimal(String.valueOf(value));
		}

		if (cls.equals(LocalDate.class)) {
			return LocalDate.parse(DateUtil.convertKhToMi1((String) value));
		}

		if (cls.isEnum()) {
			for (Object constant : cls.getEnumConstants()) {
				if (constant.toString().equals(value)) {
					return (Comparable) constant;
				}
			}
		}

		if (cls.equals(Date.class) && value instanceof Number) {
			final Date date = new Date();
			date.setTime(Long.parseLong(value.toString()));
			return date;
		}

		if (cls.equals(Timestamp.class) && value instanceof Number) {
			return Timestamp.from(Instant.ofEpochMilli(Long.parseLong(value.toString())));
		}

		return (Comparable) value;
	}

	private class DateRange {
		private Date fromDate;
		private Date toDate;
	}

	private DateRange getDateRange(String dateTimeStr) {
		currentContext.setCalendar("Persian").setTimeZoneId("Asia/Tehran");

		final Pattern dateTimePattern = Pattern.compile("(?<year>\\d{4})([/](?<month>\\d{1,2}))?([/](?<day>\\d{1,2}))?([ ](?<hour>\\d{1,2})([:](?<minute>\\d{1,2}))?([:](?<second>\\d{1,2}))?)?");

		EUniCalendar calendar = EUniCalendar.Gregorian;
		switch (currentContext.getCalendarSafely()) {
			case Persian:
				calendar = EUniCalendar.Persian;
				break;
			case Islamic:
				calendar = EUniCalendar.Islamic;
				break;
		}

		final EUniDateField[] fields = {EUniDateField.YEAR, EUniDateField.MONTH, EUniDateField.DAY_OF_MONTH, EUniDateField.HOUR_OF_DAY, EUniDateField.MINUTE, EUniDateField.SECOND};
		final String[] names = {"year", "month", "day", "hour", "minute", "second"};

		final Integer fromDateTime[] = {1, 1, 1, 0, 0, 0};

		final DateRange dateRange = new DateRange();

		EUniCalendar finalCalendar = calendar;
		final Matcher dateTimeMatcher = dateTimePattern.matcher(dateTimeStr);

		if (dateTimeMatcher.find()) {
			Integer idx = 0;
			for (idx = 0; idx < names.length; idx++) {
				if (!StringUtils.isEmpty(dateTimeMatcher.group(names[idx])))
					fromDateTime[idx] = Integer.valueOf(dateTimeMatcher.group(names[idx]));
				else
					break;
			}

			final UniDate fromUniDate = UniDate
					.of(finalCalendar, TimeZone.getTimeZone(currentContext.getTimeZoneIdSafely()))
					.setDate(fromDateTime[0], fromDateTime[1], fromDateTime[2])
					.setTime(fromDateTime[3], fromDateTime[4], fromDateTime[5]);

			dateRange.fromDate = fromUniDate.toDate();

			final UniDate toUniDate = fromUniDate.update(fields[idx - 1], 1);
			dateRange.toDate = toUniDate.toDate();
		}

		return dateRange;
	}
	private Expression<? extends Comparable> jsonValue(CriteriaBuilder cb,
										 String mapPath,
										 String jsonPath) {


		// Tell Hibernate: treat this as inline SQL, not a parameter
		return cb.function("JSON_VALUE", Comparable.class,
				new HibernateInlineExpression(cb, mapPath),
				new HibernateInlineExpression(cb, jsonPath));
	}

	// ---------------------------------------------------------------
// 3. Replace the whole createComparisonPredicate method
// ---------------------------------------------------------------
	private Predicate createComparisonPredicate(Root<T> root,
												CriteriaBuilder builder,
												SearchDTO.CriteriaRq criteria) {

		if (!(criteria.getOperator() == EOperator.isBlank ||
				criteria.getOperator() == EOperator.notBlank ||
				criteria.getOperator() == EOperator.isNull ||
				criteria.getOperator() == EOperator.notNull) &&
				(criteria.getValue() == null || criteria.getValue().isEmpty())) {
			return null;
		}

		final String fieldName = criteria.getFieldName();
		Path<?> rawPath = findPath(root, fieldName, builder);
		if (rawPath == null) {
			throw new RuntimeException("Invalid property: " + fieldName);
		}

		boolean isJsonField = Map.class.isAssignableFrom(rawPath.getJavaType());
		String jsonPath = null;
		Expression<? extends Comparable> jsonExpr = null;

		if (isJsonField) {
			String mapAttr = ((SingularAttributePath<?>) rawPath).getAttribute().getName();
			String keyPath = fieldName.substring(mapAttr.length() + 1); // after "extraData."
			jsonPath = "'$." + keyPath.replace(".", "\".\"")+"'";
			jsonExpr = jsonValue(builder, mapAttr, jsonPath);
		}

		// Use JSON expression if available, otherwise fall back to normal path
		Expression<? extends Comparable> stringExpr = isJsonField ? jsonExpr :
				(rawPath.getJavaType() == String.class ? (Expression<Comparable>) rawPath : null);

		Path<? extends Comparable> comparablePath = (Path<Comparable>) rawPath;

		Predicate p = null;

		switch (criteria.getOperator()) {
			case equalsField:
				p = builder.equal(root.get(criteria.getFieldName()),
						root.get((String) criteria.getValue().get(0)));
				break;

			case notEqualField:
				p = builder.notEqual(root.get(criteria.getFieldName()),
						root.get((String) criteria.getValue().get(0)));
				break;

			case equals:
				if (isJsonField) {
					p = builder.equal(jsonExpr,convertAsList(criteria, comparablePath, false));
				}else
				p = comparablePath.in(convertAsList(criteria, comparablePath, false));
				break;

			case notEqual:
				p = builder.not(comparablePath.in(convertAsList(criteria, comparablePath, false)));
				break;

			case iEquals:
				if (isJsonField) {
					p = builder.lower((Expression<String>) jsonExpr).in(convertAsList(criteria, comparablePath, true));
				} else if (String.class.equals(rawPath.getJavaType())) {
					p = builder.lower((Expression<String>) rawPath)
							.in(convertAsList(criteria, comparablePath, true));
				} else {
					p = comparablePath.in(convertAsList(criteria, comparablePath, false));
				}
				break;

			case lessThan:
				p = builder.lessThan(comparablePath, convertAsSingle(criteria, comparablePath, false));
				break;
			case lessOrEqual:
				p = builder.lessThanOrEqualTo(comparablePath, convertAsSingle(criteria, comparablePath, false));
				break;
			case greaterThan:
				if (isJsonField) {
					p = builder.greaterThan(jsonExpr,convertAsSingle(criteria, comparablePath, false));
				}else
				p = builder.greaterThan(comparablePath, convertAsSingle(criteria, comparablePath, false));
				break;
			case greaterOrEqual:
				p = builder.greaterThanOrEqualTo(comparablePath, convertAsSingle(criteria, comparablePath, false));
				break;

			// LIKE FAMILY — ONLY use stringExpr / jsonExpr
			case iContains: case contains:
			case iStartsWith: case startsWith:
			case iEndsWith: case endsWith:
			case iNotContains: case notContains:
			case iNotStartsWith: case notStartsWith:
			case iNotEndsWith: case notEndsWith:

				if (stringExpr == null && !isJsonField) {
					throw new IllegalStateException("LIKE operator used on non-string field: " + fieldName);
				}

				Expression<String> target = isJsonField ? (Expression<String>) jsonExpr :(Expression<String>) stringExpr;
				String pattern = (String) convertAsSingle(criteria, comparablePath, true);
				boolean insensitive = criteria.getOperator().name().startsWith("i");
				if (insensitive) {
					target = builder.lower(target);
				}

				String likePattern;
				switch (criteria.getOperator()) {
					case iContains: case contains:
						likePattern = "%" + pattern + "%"; break;
					case iStartsWith: case startsWith:
						likePattern = pattern + "%"; break;
					case iEndsWith: case endsWith:
						likePattern = "%" + pattern; break;
					case iNotContains: case notContains:
						likePattern = "%" + pattern + "%"; p = builder.notLike(target, likePattern); return p;
					case iNotStartsWith: case notStartsWith:
						likePattern = pattern + "%"; p = builder.notLike(target, likePattern); return p;
					case iNotEndsWith: case notEndsWith:
						likePattern = "%" + pattern; p = builder.notLike(target, likePattern); return p;
					default:
						likePattern = "%" + pattern + "%";
				}

				p = builder.like(target, likePattern);
				if (criteria.getOperator().name().startsWith("iNot") || criteria.getOperator().name().startsWith("not")) {
					p = builder.not(p);
				}
				break;

			case isNull:
				p = builder.isNull(comparablePath);
				break;
			case notNull:
				p = builder.isNotNull(comparablePath);
				break;

			case isBlank:
				if (isJsonField) {
					p = builder.or(builder.isNull(jsonExpr), builder.equal(jsonExpr, ""));
				} else if (rawPath.getJavaType() == String.class) {
					p = builder.equal((Expression<String>) rawPath, "");
				} else {
					p = builder.isNull(comparablePath);
				}
				break;

			case notBlank:
				if (isJsonField) {
					p = builder.and(builder.isNotNull(jsonExpr), builder.notEqual(jsonExpr, ""));
				} else if (rawPath.getJavaType() == String.class) {
					p = builder.notEqual((Expression<String>) rawPath, "");
				} else {
					p = builder.isNotNull(comparablePath);
				}
				break;

			case inSet:
				p = comparablePath.in(convertAsList(criteria, comparablePath, false));
				break;
			case notInSet:
				p = builder.not(comparablePath.in(convertAsList(criteria, comparablePath, false)));
				break;

			default:
				throw new RuntimeException("Unsupported operator: " + criteria.getOperator());
		}

		return p;
	}
}
