package api.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import api.entities.Authority;
import api.entities.Role;
import api.entities.User;
import api.repositories.AuthorityRepository;
import api.repositories.RoleRepository;
import api.repositories.UserRepository;
import jakarta.annotation.PostConstruct;

/**
 * DbConfig.
 */
@Component
public class DbConfig {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${api.admin.username:admin}")
    String adminUsername;
    @Value("${api.admin.password:password}")
    String adminPassword;

    /**
     * Initialize the database with necessary fields.
     */
    @PostConstruct
    @Transactional
    public void init() {
        // Setup Authorities
        Authority userRead = authorityRepository.findByAuthority("USER_READ")
            .orElseGet(() -> authorityRepository.save(Authority.builder().authority("USER_READ").build()));

        Authority userWrite = authorityRepository.findByAuthority("USER_WRITE")
            .orElseGet(() -> authorityRepository.save(Authority.builder().authority("USER_WRITE").build()));

        // Setup Roles
        Role adminRole = roleRepository.findByName("ADMIN")
            .orElseGet(() -> roleRepository.save(
                Role.builder()
                    .name("ADMIN")
                    .authorities(Set.of(userRead, userWrite))
                    .build()
            ));
        Role userRole = roleRepository.findByName("USER")
            .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

        // Setup Users
        userRepository.findByUsername("admin")
            .orElseGet(() -> userRepository.save(
                User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(adminRole, userRole))
                    .build()
            ));
    }
}
