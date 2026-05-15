package com.nicico.committee.util.base.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Resoluation {
    PPI72(72),
    PPI96(96),
    PPI150(150),
    PPI300(300),
    PPI600(600),
    PPI720(720),
    PPI1200(1200),
    PPI1440(1440),
    PPI2400(2400),
    PPI2880(2880);

    private final transient int ppi;
}
