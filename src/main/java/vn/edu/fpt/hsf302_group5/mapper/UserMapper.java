package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequest;
import vn.edu.fpt.hsf302_group5.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "password", target = "passwordHash")
    User toUserEntity(UserRequest userRequest);

    @Mapping(source = "passwordHash", target = "password")
    UserRequest toUserDTO(User user);
}
