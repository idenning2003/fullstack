package api.controllers.users;

import java.util.List;
import java.util.Objects;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.ErrorDto;
import api.dtos.UserDto;
import api.entities.User;
import api.exceptions.DuplicateEntityException;
import api.mapper.UserMapper;
import api.services.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * {@link UserController}.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @Value("${pagination.default-page-size:10}")
    private int defaultPageSize;
    @Value("${pagination.max-page-size:100}")
    private int maxPageSize;

    /**
     * Get me.
     *
     * @return {@link UserDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/me")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = UserDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public UserDto getMe(@AuthenticationPrincipal User user) {
        return userMapper.toDto(user);
    }

    /**
     * Update me.
     *
     * @return {@link UserDto}
     */
    @Transactional
    @PutMapping("/me")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = UserDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "400",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "409",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public UserDto updateMe(@AuthenticationPrincipal User user, @RequestBody UserDto userDto) {
        if (userDto.getUsername() != null && !StringUtils.hasText(userDto.getUsername())) {
            throw new IllegalArgumentException("Username must not be empty.");
        }

        if (StringUtils.hasText(userDto.getUsername())
            && !Objects.equals(userDto.getUsername(), user.getUsername())
            && userService.exists(userDto.getUsername())
        ) {
            throw new DuplicateEntityException("Username '" + userDto.getUsername() + "' already exists.");
        }

        userMapper.update(user, userDto);
        return userMapper.toDto(userService.save(user));
    }

    /**
     * Delete me.
     */
    @Transactional
    @DeleteMapping("/me")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200"
        ),
    })
    public void deleteMe(@AuthenticationPrincipal User user) {
        userService.delete(user);
    }

    /**
     * Get users.
     *
     * @return {@link List} of {@link UserDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("")
    @PreAuthorize("hasAuthority('USER_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = UserDto.class)),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "403",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public List<UserDto> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(required = false) Integer size
    ) {
        int requestedSize = (size != null) ? size : defaultPageSize;
        int safeSize = Math.min(requestedSize, maxPageSize);

        Pageable pageable = PageRequest.of(page, safeSize, Sort.by("id").ascending());
        return userService.getAll(pageable)
            .map(userMapper::toDto)
            .getContent();
    }

    /**
     * Get user.
     *
     * @param id User id
     * @return {@link UserDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = UserDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "403",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "404",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public UserDto getUser(@PathVariable int id) {
        User user = userService.get(id);
        Hibernate.initialize(user);
        return userMapper.toDto(user);
    }

    /**
     * Update user.
     *
     * @return {@link UserDto}
     */
    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ') and hasAuthority('USER_WRITE')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = Integer.class)),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "403",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "404",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "409",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public UserDto updateUser(@PathVariable int id, @RequestBody UserDto userDto) {
        if (userDto.getUsername() != null && !StringUtils.hasText(userDto.getUsername())) {
            throw new IllegalArgumentException("Username must not be empty.");
        }

        User user = userService.get(id);
        if (StringUtils.hasText(userDto.getUsername())
            && !Objects.equals(userDto.getUsername(), user.getUsername())
            && userService.exists(userDto.getUsername())
        ) {
            throw new DuplicateEntityException("Username '" + userDto.getUsername() + "' already exists.");
        }

        userMapper.update(user, userDto);
        userService.save(user);
        return userMapper.toDto(user);
    }

    /**
     * Delete user.
     *
     * @param id User id
     */
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200"
        ),
        @ApiResponse(
            responseCode = "403",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
        @ApiResponse(
            responseCode = "404",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }
}
