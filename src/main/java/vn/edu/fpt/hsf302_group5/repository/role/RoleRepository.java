package vn.edu.fpt.hsf302_group5.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.hsf302_group5.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String roleName);
}
