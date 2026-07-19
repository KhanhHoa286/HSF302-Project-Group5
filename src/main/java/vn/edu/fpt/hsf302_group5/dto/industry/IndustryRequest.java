package vn.edu.fpt.hsf302_group5.dto.industry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;

@Getter
@Setter
@NoArgsConstructor
public class IndustryRequest {

    private Integer industryId;

    @NotBlank(message = "Tên ngành nghề không được để trống")
    @Size(max = 100, message = "Tên ngành nghề không được vượt quá 100 ký tự")
    private String industryName;

    @NotNull(message = "Trạng thái không được để trống")
    private IndustryStatus status = IndustryStatus.ACTIVE;
}
