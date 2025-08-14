package api.controllers.users;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.ErrorDto;
import api.dtos.RoleDto;
import api.entities.User;
import api.mapper.RoleMapper;
import api.services.RoleService;
import api.services.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * {@link UserRoleController}.
 */
@RestController
@RequestMapping("/users/roles")
public class UserRoleController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * Get user's roles.
     *
     * @param userId User id
     * @return {@link List} of {@link RoleDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_READ') and hasAuthority('ROLE_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = RoleDto.class)),
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
    public List<RoleDto> getUserRoles(@PathVariable int userId) {
        return userService.get(userId).getRoles().stream()
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Set user's roles.
     *
     * @param userId User id
     * @param roleIds Role ids
     * @return {@link List} of {@link RoleDto}
     * @apiNote Does not throw error if roles not found
     */
    @Transactional(readOnly = true)
    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_WRITE') and hasAuthority('ROLE_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = RoleDto.class)),
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
    public List<RoleDto> setUserRoles(@PathVariable int userId, @RequestBody List<Integer> roleIds) {
        User user = userService.get(userId);
        user.setRoles(new HashSet<>(roleService.get(roleIds)));
        return userService.save(user).getRoles().stream()
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Add roles to user.
     *
     * @param userId User id
     * @param roleIds Role ids
     * @return {@link List} of {@link RoleDto}
     * @apiNote Does not throw error if user already has specified role
     * @apiNote Does not throw error if roles not found
     */
    @Transactional(readOnly = true)
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_READ') and hasAuthority('USER_WRITE') and hasAuthority('ROLE_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = RoleDto.class)),
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
    public List<RoleDto> addUserRoles(@PathVariable int userId, @RequestBody List<Integer> roleIds) {
        User user = userService.get(userId);
        user.getRoles().addAll(roleService.get(roleIds));
        return userService.save(user).getRoles().stream()
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Delete roles from user.
     *
     * @param userId User id
     * @param roleIds Role ids
     * @return {@link List} of {@link RoleDto}
     * @apiNote Does not throw error if user does not have specified role
     */
    @Transactional(readOnly = true)
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_READ') and hasAuthority('USER_WRITE') and hasAuthority('ROLE_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = RoleDto.class)),
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
    public List<RoleDto> removeUserRoles(@PathVariable int userId, @RequestBody List<Integer> roleIds) {
        User user = userService.get(userId);
        user.getRoles().removeAll(roleService.get(roleIds));
        return userService.save(user).getRoles().stream()
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }
}
