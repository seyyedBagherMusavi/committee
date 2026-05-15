package com.nicico.committee.util.base.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExportType {
    PDF("pdf"),
    EXCEL("excel");

    private final String type;
}