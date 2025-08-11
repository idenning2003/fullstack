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
import api.services.AuthorityService;
import api.services.RoleService;
import api.services.UserService;
import jakarta.annotation.PostConstruct;

/**
 * DbConfig.
 */
@Component
public class DbConfig {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthorityService authorityService;

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
        Authority userRead = authorityService.find("USER_READ")
            .orElseGet(() -> authorityService.save(Authority.builder().authority("USER_READ").build()));

        Authority userWrite = authorityService.find("USER_WRITE")
            .orElseGet(() -> authorityService.save(Authority.builder().authority("USER_WRITE").build()));

        // Setup Roles
        Role adminRole = roleService.find("ADMIN")
            .orElseGet(() -> roleService.save(
                Role.builder()
                    .name("ADMIN")
                    .authorities(Set.of(userRead, userWrite))
                    .build()
            ));
        Role userRole = roleService.find("USER")
            .orElseGet(() -> roleService.save(Role.builder().name("USER").build()));

        // Setup Users
        userService.find("admin")
            .orElseGet(() -> userService.save(
                User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .roles(Set.of(adminRole, userRole))
                    .build()
            ));
    }
}
