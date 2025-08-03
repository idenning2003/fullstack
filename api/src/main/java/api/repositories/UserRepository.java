package api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api.entities.User;

/**
 * UserRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
