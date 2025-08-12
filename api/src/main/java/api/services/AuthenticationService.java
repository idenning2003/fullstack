package api.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import api.dtos.AuthenticationDto;
import api.dtos.LoginDto;
import api.dtos.RegisterDto;
import api.entities.Role;
import api.entities.User;
import api.exceptions.DuplicateEntityException;

/**
 * {@link AuthenticationService}.
 */
@Service
public class AuthenticationService {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private TokenService tokenGenerator;

    /**
     * Login as a user.
     *
     * @param login {@link LoginDto}
     * @return {@link AuthenticationDto}
     */
    @Transactional(readOnly = true)
    public AuthenticationDto login(LoginDto login) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenGenerator.generateToken(authentication);
    }

    /**
     * Register a new user.
     *
     * @param register {@link RegisterDto}
     * @return {@link AuthenticationDto}
     */
    @Transactional
    public AuthenticationDto register(RegisterDto register) {
        if (!StringUtils.hasText(register.getUsername())) {
            throw new IllegalArgumentException("Username must not be empty.");
        }
        if (!StringUtils.hasText(register.getPassword())) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
        if (userService.exists(register.getUsername())) {
            throw new DuplicateEntityException("Username '" + register.getUsername() + "' already exists.");
        }

        Role userRole = roleService.get("USER");

        User user = User.builder()
            .username(register.getUsername())
            .password(encoder.encode(register.getPassword()))
            .roles(Set.of(userRole))
            .build();

        userService.save(user);

        LoginDto login = new LoginDto(register.getUsername(), register.getPassword());

        return login(login);
    }
}
