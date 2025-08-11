package api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.entities.Role;
import api.exceptions.EntityNotFoundException;
import api.repositories.RoleRepository;

/**
 * RoleService.
 */
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get role from role name.
     *
     * @param role Role name
     * @return {@link Role}
     */
    @Transactional(readOnly = true)
    public Role getRole(String role) {
        return roleRepository.findByName(role)
            .orElseThrow(() -> new EntityNotFoundException("Role '" + role + "' not found."));
    }
}
