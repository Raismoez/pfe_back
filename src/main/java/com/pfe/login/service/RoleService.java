package com.pfe.login.service;

import com.pfe.login.model.Role;
import com.pfe.login.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Récupérer tous les rôles
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    // Trouver un rôle par ID
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    // Trouver un rôle par role_name
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}