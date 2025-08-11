package api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api.entities.Role;

/**
 * RoleRepository.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Find role object by role name.
     *
     * @param role role name
     * @return {@link Optional} {@link Role}
     */
    Optional<Role> findByName(String role);
}
