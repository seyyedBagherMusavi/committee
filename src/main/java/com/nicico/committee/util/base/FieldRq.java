package com.nicico.committee.util.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldRq {
    private String fontName;
    private Float fontSize;
    private String backColor = "#000000";
    private String headerFontName;
    private Float headerFontSize;
    private String headerBackColor = "#EFEFEF";
    private Boolean italic = false;
    private Boolean bold = false;
    private String name;
    private String farsiName;
    private String type;
    private Double width = 50D;
    private short priority = -1;
}