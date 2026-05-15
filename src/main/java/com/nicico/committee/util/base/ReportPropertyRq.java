package com.nicico.committee.util.base;


import com.nicico.committee.util.base.enumeration.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.sf.jasperreports.engine.type.OrientationEnum;

@Getter
@Setter
@ToString(exclude = {"width", "height"})
public class ReportPropertyRq {
    private ExportType type;
    private PhysicalSize size;
    private OrientationEnum orientation;
    private Resoluation resoluation;
    private String title;
    private String subtitle;

    private Boolean autoJustify = true;
    private Float justificationRatio = 0.25F;
    private int width;
    private int height;

    public int getWidth() {
        if (size == PhysicalSize.A4 && orientation == OrientationEnum.PORTRAIT) {
            return 595;
        }
        if (size == PhysicalSize.A4 && orientation == OrientationEnum.LANDSCAPE) {
            return 842;
        }
        return 0;
    }

    public int getHeight() {
        if (size == PhysicalSize.A4 && orientation == OrientationEnum.PORTRAIT) {
            return 842;
        }
        if (size == PhysicalSize.A4 && orientation == OrientationEnum.LANDSCAPE) {
            return 595;
        }
        return 0;
    }
}
