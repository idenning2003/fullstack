package api.services;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.dtos.AuthenticationDto;
import api.dtos.LoginDto;
import api.dtos.RegisterDto;
import api.entities.Role;
import api.entities.User;
import api.exceptions.DuplicateEntityException;

/**
 * AuthenticationService.
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
        if (StringUtils.isEmpty(register.getUsername())) {
            throw new IllegalArgumentException("Username must not be null.");
        }
        if (StringUtils.isEmpty(register.getPassword())) {
            throw new IllegalArgumentException("Password must not be null.");
        }
        if (userService.exists(register.getUsername())) {
            throw new DuplicateEntityException("Username '" + register.getUsername() + "' already taken.");
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
