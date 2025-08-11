package api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.entities.Role;
import api.exceptions.EntityNotFoundException;
import api.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * RoleService.
 */
@Slf4j
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Get role.
     *
     * @param role Role name
     * @return {@link Role}
     */
    @Transactional(readOnly = true)
    public Role get(String role) {
        return roleRepository.findByName(role)
            .orElseThrow(() -> new EntityNotFoundException("Role '" + role + "' not found."));
    }

    /**
     * Find role.
     *
     * @param role Role name
     * @return {@link Optional} {@link Role}
     */
    @Transactional(readOnly = true)
    public Optional<Role> find(String role) {
        return roleRepository.findByName(role);
    }

    /**
     * Save role.
     *
     * @param role Role
     * @return Role
     */
    @Transactional
    public Role save(Role role) {
        Role saved = roleRepository.save(role);
        log.info("Role saved: " + role);
        return saved;
    }
}
