package vn.edu.fpt.hsf302_group5.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.CandidateProfile;
import vn.edu.fpt.hsf302_group5.entity.Permission;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;
import vn.edu.fpt.hsf302_group5.repository.candidate.CandidateProfileRepository;
import vn.edu.fpt.hsf302_group5.repository.role.RoleRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CandidateProfileRepository candidateProfileRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        if (email == null) {
            throw new OAuth2AuthenticationException("Không tìm thấy email từ nhà cung cấp dịch vụ Google!");
        }

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            // Đăng ký mới ứng viên nếu chưa có tài khoản
            user = User.builder()
                    .email(email)
                    .fullName(name != null ? name : "Google User")
                    .avatarUrl(picture)
                    .passwordHash("") // Đăng nhập Google không dùng mật khẩu dạng text thường
                    .role(roleRepository.findByRoleName(UserRole.CANDIDATE.name()))
                    .status(UserStatus.ACTIVE) // Đăng ký qua Google tự động ACTIVE
                    .build();
            user = userRepository.save(user);

            // Tạo CandidateProfile đi kèm
            CandidateProfile candidateProfile = CandidateProfile.builder()
                    .candidateId(user.getUserId())
                    .gender(user.getGender())
                    .build();
            candidateProfileRepository.save(candidateProfile);
        } else {
            // Cập nhật ảnh đại diện nếu ảnh từ Google có sự thay đổi
            if (picture != null && !picture.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(picture);
                userRepository.save(user);
            }
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user.getRole().getPermissions() != null) {
            for (Permission permission : user.getRole().getPermissions()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
            }
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));

        return CustomUserDetailsResponse.builder()
                .id(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .authorities(grantedAuthorities)
                .attributes(attributes)
                .build();
    }
}
