package api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dtos.UserDto;
import api.entities.User;
import api.exceptions.EntityNotFoundException;
import api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * UserService.
 */
@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Create new user.
     *
     * @return User id
     */
    @Transactional
    public User createUser() {
        User user = new User();
        userRepository.save(user);
        log.info("User created: " + user);
        return user;
    }

    /**
     * Get user.
     *
     * @param id User id
     * @return User
     */
    @Transactional(readOnly = true)
    public User getUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User " + id + " not found.");
        }
        User user = userRepository.getReferenceById(id);
        return user;
    }

    /**
     * Update user.
     *
     * @param updated Updated user info
     * @return User
     */
    @Transactional
    public User updateUser(UserDto updated) {
        User user = getUser(updated.getId());
        user.setName(updated.getName());
        log.info("User update: " + updated);
        return user;
    }

    /**
     * Delete user.
     *
     * @param id User id
     */
    @Transactional
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User " + id + " not found.");
        }
        userRepository.deleteById(id);
        log.info("User deleted: " + id);
    }
}
