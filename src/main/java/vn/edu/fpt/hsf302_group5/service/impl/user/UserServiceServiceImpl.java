package vn.edu.fpt.hsf302_group5.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;
import vn.edu.fpt.hsf302_group5.mapper.UserMapper;
import vn.edu.fpt.hsf302_group5.repository.role.RoleRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.user.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Boolean registerUser(UserRequertDTO user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu không khớp!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trên hệ thống!");
        }
        User newUser = userMapper.toUserEntity(user);
        newUser.setStatus(UserStatus.INACTIVE); // xác thực mail xong mới cho dùng
        newUser.setPasswordHash(passwordEncoder.encode(newUser.getPasswordHash()));
        newUser.setRole(roleRepository.findByRoleName(UserRole.CANDIDATE.name()));
        userRepository.save(newUser);
        return true;
    }
}
