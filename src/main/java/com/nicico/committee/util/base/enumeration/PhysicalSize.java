package com.nicico.companies.util.base.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PhysicalSize {
    A10("A10"),
    A9("A9"),
    A8("A8"),
    A7("A7"),
    A6("A6"),
    A5("A5"),
    A4("A4"),
    A3("A3"),
    A2("A2"),
    A1("A1"),
    A0("A0");

    private final transient String type;
}
