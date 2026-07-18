package vn.edu.fpt.hsf302_group5.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "{validation.email.notblank}")
    @Email(message = "{validation.email.format}")
    @Size(max = 255, message = "{validation.email.size}")
    private String email;

    @NotBlank(message = "{validation.password.notblank}")
    @Size(min = 6, max = 255, message = "{validation.password.size}")
    @Pattern(regexp = "^(?=.*(\\d))(?=.*[a-z])(?=.*[!#$@$%^&*()_])(?=.*[A-Z]).+$", message = "{validation.password.pattern}")
    private String password;

    @NotBlank(message = "{validation.confirmPassword.notblank}")
    private String confirmPassword;

    @NotBlank(message = "{validation.fullName.notblank}")
    @Size(max = 100, message = "{validation.fullName.size}")
    private String fullName;

}
