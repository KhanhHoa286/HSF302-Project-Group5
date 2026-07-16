package vn.edu.fpt.hsf302_group5.dto.candidate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationRequest {
    @NotBlank(message = "Tên trường học không được để trống")
    private String schoolName;

    @NotBlank(message = "Bằng cấp không được để trống")
    private String degree;

    @NotBlank(message = "Chuyên ngành không được để trống")
    private String major;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    private LocalDate endDate;
}
