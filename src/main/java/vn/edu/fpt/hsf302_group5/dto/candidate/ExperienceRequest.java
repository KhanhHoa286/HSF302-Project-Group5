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
public class ExperienceRequest {
    @NotBlank(message = "Tên công ty không được để trống")
    private String companyName;

    @NotBlank(message = "Vị trí không được để trống")
    private String position;

    @NotBlank(message = "Mô tả công việc không được để trống")
    private String description;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    private LocalDate endDate;
}
