package vn.edu.fpt.hsf302_group5.dto.user;

import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsResponse implements UserDetails, OAuth2User {

    private Integer id;
    private String fullName;
    private String password;
    private String phone;
    private UserStatus status;
    private Gender gender;
    private String avatarUrl;
    private String email;
    private List<GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE == status;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getName() {
        return email;
    }
}
