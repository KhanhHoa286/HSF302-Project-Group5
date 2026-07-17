package vn.edu.fpt.hsf302_group5.util;

import java.math.BigDecimal;

public class JobPostUtil {


    public static String getSalaryDisplay(BigDecimal salaryMin, BigDecimal salaryMax) {
        if (salaryMin == null && salaryMax == null) {
            return "Thỏa thuận";
        }

        if (salaryMin != null && salaryMax != null) {
            return salaryMin.divide(BigDecimal.valueOf(1_000_000)).intValue()
                    + " - "
                    + salaryMax.divide(BigDecimal.valueOf(1_000_000)).intValue()
                    + " triệu";
        }

        return "Thỏa thuận";
    }
}
