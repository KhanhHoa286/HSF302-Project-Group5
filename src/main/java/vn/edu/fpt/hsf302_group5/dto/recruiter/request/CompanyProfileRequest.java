package vn.edu.fpt.hsf302_group5.dto.recruiter.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyProfileRequest {

    @NotBlank(message = "Tên công ty không được để trống!")
    @Size(max = 200, message = "Tên công ty không vượt quá 200 ký tự!")
    private String companyName;

    @Size(max = 255, message = "Đường dẫn website không vượt quá 255 ký tự!")
    private String website;

    @NotBlank(message = "Email liên hệ không được để trống!")
    @Email(message = "Email liên hệ không đúng định dạng!")
    @Size(max = 255, message = "Email không vượt quá 255 ký tự!")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống!")
    @Size(max = 20, message = "Số điện thoại không vượt quá 20 ký tự!")
    private String phone;

    private String description;

    @Size(max = 255, message = "Địa chỉ chi tiết không vượt quá 255 ký tự!")
    private String addressDetail;

    private MultipartFile logoFile;

    private String logoUrl;
}
