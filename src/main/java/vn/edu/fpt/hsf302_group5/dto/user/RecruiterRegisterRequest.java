package vn.edu.fpt.hsf302_group5.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterRegisterRequest {

    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.format}")
    private String email;

    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 6, max = 255, message = "{validation.password.size}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&*()!]).+$", message = "{validation.password.pattern}")
    private String password;
    private String confirmPassword;

    @NotBlank(message = "{validation.fullName.notblank}")
    private String fullName;

    @NotNull(message = "{recruiter.gender.notnull}")
    private Gender gender;

    @NotBlank(message = "{recruiter.phoneNumber.notblank}")
    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "{recruiter.phoneNumber.pattern}")
    private String phoneNumber;

    @NotNull(message = "{recruiter.companyName.notnull}")
    private String companyName;

    @NotNull(message = "{recruiter.provinceId.notnull}")
    private Integer provinceId;

    @NotNull(message = "{recruiter.administratorUnitId.notnull}")
    private Integer administratorUnitId;

    @NotBlank(message = "{recruiter.addressSpecific.notblank}")
    private String addressSpecific;

    private String website;
    private String logoUrl;
    private MultipartFile logoFile;

}
