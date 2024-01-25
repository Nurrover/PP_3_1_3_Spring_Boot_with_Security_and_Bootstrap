package web.springboot.service;

import web.springboot.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAllRole();

    void saveRole(Role role);
    List<Role> findRole(String roleUser);
}
