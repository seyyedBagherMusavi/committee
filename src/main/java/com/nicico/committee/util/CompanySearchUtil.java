package com.nicico.companies.util;

import com.nicico.companies.entities.Company;
import com.nicico.companies.service.dto.CompanySearchDTO;
import com.nicico.copper.common.domain.criteria.NICICOSpecification;
import com.nicico.copper.common.dto.search.SearchDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author seyyed
 * for search completter
 * powered by A.I.
 */
@Component
@RequiredArgsConstructor
public class CompanySearchUtil {

    private final EntityManager entityManager;

    /**
     * Performs a search on the Entity with dynamic field selection.
     *
     * @param request The search request containing criteria and the fields to select.
     * @return A SearchRs containing a list of maps, where each map represents a result row with only the selected fields.
     */
    @Transactional(readOnly = true)
    public <T> SearchDTO.SearchRs<Map<String, Object>> search(CompanySearchDTO request, Class<T> clazz) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<T> root = query.from(clazz);

        NICICOSpecification<T> spec = NICICOSpecification.of(request.getSearch());

        Predicate predicate = spec.toPredicate(root, query, builder);
        if (predicate != null) {
            query.where(predicate);
        }

        if (request.getSelectFields() != null && !request.getSelectFields().isEmpty()) {
            List<Selection<?>> selections = request.getSelectFields().stream()
                    .map(fieldName -> findPath(root, fieldName).alias(fieldName))
                    .collect(Collectors.toList());
            query.multiselect(selections);
        }

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);

        int startIndex = request.getSearch().getStartIndex() != null ? request.getSearch().getStartIndex() : 0;
        int count = request.getSearch().getCount() != null ? request.getSearch().getCount() : 100;
        typedQuery.setFirstResult(startIndex);
        typedQuery.setMaxResults(count);

        List<Tuple> resultTuples = typedQuery.getResultList();

        List<Map<String, Object>> collect = resultTuples.stream()
                .map(tuple -> tuple.getElements().stream()
                        .collect(Collectors.toMap(
                                TupleElement::getAlias,
                                e -> (Object) tuple.get(e)   // force Object
                        )))
                .toList();

        // Total count query
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(clazz);
        countQuery.select(builder.count(countRoot));
        if (predicate != null) {
            countQuery.where(predicate);
        }
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        // Build response
        SearchDTO.SearchRs< Map<String, Object>> response = new SearchDTO.SearchRs<>();
        response.setList(collect);
        response.setTotalCount(totalCount);

        return response;
    }

    private <T> Path<T> findPath(Root<T> root, String property) {
        String[] split = property.split("\\.");
        Path path = root;

        for(String part : split) {
            if (Collection.class.isAssignableFrom(path.get(part).getJavaType())) {
                path = getOrCreateJoin(root, part);
            } else {
                path = path.get(part);
            }
        }

        if (path == null) {
            throw new RuntimeException("Invalid property: " + property + ", " + split[0]);
        } else {
            return path;
        }
    }
    private static Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        return from.join(attribute, JoinType.LEFT);
    }

}