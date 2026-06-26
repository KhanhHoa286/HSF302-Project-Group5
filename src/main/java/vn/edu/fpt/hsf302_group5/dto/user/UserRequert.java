package vn.edu.fpt.hsf302_group5.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequert {
    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không đúng định dạng!")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự!")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự!")
    @Pattern(regexp = "^(?=.*(\\d))(?=.*([a-z]))(?=.*([@#$@$%^&*()_]))(?=.*([A-Z])).+$", message = "Mật khẩu phải chứa cả chữ hoa, chữ thường, chữ số và cả kí tự đặc biệt!")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống!")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự!")
    private String fullName;

    @Size(max = 20, message = "Số điện thoại không được vượt quá 12 ký tự!")
    private String phone;

    @Size(max = 500, message = "Đường dẫn ảnh đại diện không được vượt quá 500 ký tự!")
    private String avatarUrl;

    private UserStatus status;
}
