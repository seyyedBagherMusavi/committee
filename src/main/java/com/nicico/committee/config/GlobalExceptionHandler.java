package com.nicico.companies.config;

import com.nicico.companies.client.hrm.HrmApiException;
import com.nicico.copper.common.AbstractExceptionHandlerControllerAdvice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * advice controller for handling exceptions
 * @author Seyyed
 */
@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends AbstractExceptionHandlerControllerAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "Invalid request body format");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private Throwable getRootCause(Throwable ex) {
        Throwable rootCause = ex;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    @Override
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        this.printLog(ex, true, true);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            Integer index = extractIndex(fieldName);       // 5
            String rootField = extractRootField(fieldName); // p
            String fieldValue;

            try {
                // Try Persian from messageSource
                fieldValue = messageSource.getMessage(rootField, null, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e) {
                // Fallback to English field name
                fieldValue = rootField;
            }

            String result;
            if (index != null) {
                result = "ردیف " + index + " " + fieldValue;
            } else {
                result = fieldValue;
            }
            String errorMessage = messageSource.getMessage(error.getDefaultMessage(), null, LocaleContextHolder.getLocale());
            errors.put(result, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    private Integer extractIndex(String fieldPath) {
        Matcher matcher = Pattern.compile("\\[(\\d+)]").matcher(fieldPath);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
    }

    private String extractRootField(String fieldPath) {
        int dotIndex = fieldPath.indexOf('.');
        String root = dotIndex > 0 ? fieldPath.substring(0, dotIndex) : fieldPath;
        return root.replaceAll("\\[\\d+\\]", "");
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, String>> handleApplicationException(ApplicationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", messageSource.getMessage(ex.getMessage(), ex.getArgs(), LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(errors, ex.getStatus());
    }

    @ExceptionHandler(HrmApiException.class)
    public ResponseEntity<Map<String, String>> handleHrmApiException(HrmApiException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "HRM API Error: " + ex.getMessage());
        errors.put("hrm_error_code", String.valueOf(ex.getCode()));
        errors.put("hrm_error_body", ex.getErrorBody());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        Throwable rootCause = getRootCause(ex);
        String constraintName = null;

        if (rootCause instanceof SQLIntegrityConstraintViolationException) {
            constraintName = extractConstraintNameFromMessage(rootCause.getMessage());
        } else if (rootCause instanceof org.hibernate.exception.ConstraintViolationException) {
            constraintName = ((org.hibernate.exception.ConstraintViolationException) rootCause).getConstraintName();
        }else if (rootCause instanceof org.hibernate.exception.ConstraintViolationException) {
            constraintName = ((org.hibernate.exception.ConstraintViolationException) rootCause).getConstraintName();
        }else if (rootCause instanceof OracleDatabaseException) {

            constraintName = extractConstraintNameFromMessage(ex.getMostSpecificCause().getMessage());
        }

        if (constraintName != null && !constraintName.isEmpty()) {
            String messageKey = "unique.constraint.violation." + constraintName.toLowerCase();
            String message = messageSource.getMessage(messageKey, null, messageKey, LocaleContextHolder.getLocale());

            if (message.equals(messageKey)) {
                errors.put("origin_error", message);
                message = messageSource.getMessage("error.duplicate.record", null,
                        "A record with this information already exists.", LocaleContextHolder.getLocale());
            }
            errors.put("error", message);

        } else {
            errors.put("error", messageSource.getMessage("error.data.integrity.violation", null,
                    "Data integrity violation.", LocaleContextHolder.getLocale()));
        }

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<Object> handleOracleDatabaseExceptionn(JpaSystemException ex) {
        Map<String, String> errors = new HashMap<>();
        Throwable rootCause = getRootCause(ex);

        if (rootCause instanceof OracleDatabaseException &&
                ((OracleDatabaseException) rootCause).getOracleErrorNumber()==12899) {
            return handleValueTooLargeException((OracleDatabaseException) rootCause);
        }


        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
    private ResponseEntity<Object> handleValueTooLargeException(OracleDatabaseException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = ex.getMessage();
        Pattern pattern = Pattern.compile("ORA-12899: value too large for column \\\"(.*?)\\\"\\.\\\"(.*?)\\\"\\.\\\"(.*?)\\\" \\(actual: (\\d+), maximum: (\\d+)\\)");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String columnName = matcher.group(3);
            String sized = matcher.group(5);
            try {
                String fieldName = messageSource.getMessage(columnName.toLowerCase(), null, LocaleContextHolder.getLocale());
                errors.put("error", messageSource.getMessage("error.value.too.large", new Object[]{fieldName, sized}, LocaleContextHolder.getLocale()));
            } catch (NoSuchMessageException e) {
                errors.put("error", messageSource.getMessage("error.value.too.large.no.field", new Object[]{sized}, LocaleContextHolder.getLocale()));
            }
        } else {
            errors.put("error", messageSource.getMessage("error.data.integrity.violation", null,
                    "Data integrity violation.", LocaleContextHolder.getLocale()));
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private String extractConstraintNameFromMessage(String message) {
        if (message == null) {
            return null;
        }
        // Oracle: ORA-00001: unique constraint (SCHEMA.CONSTRAINT_NAME) violated
        Pattern oraclePattern = Pattern.compile("\\.(.*?)\\) ");
        Matcher oracleMatcher = oraclePattern.matcher(message);
        if (oracleMatcher.find()) {
            return oracleMatcher.group(1).trim();
        }

        return null;
    }
}
