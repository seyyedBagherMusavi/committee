package com.nicico.committee.util.base;

import com.nicico.copper.common.dto.search.SearchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRq {
    private Class<?> clazz;
    private String serviceName;
    private SearchDTO.SearchRq query;
    private String method;
    private FieldRq[] fields;
    private Boolean filterBox = false;
    private ReportPropertyRq propertyRq;
    private String image;
    private String number;

}
