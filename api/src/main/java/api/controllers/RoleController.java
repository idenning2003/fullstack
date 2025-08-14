package api.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import api.entities.Role;
import api.exceptions.DuplicateEntityException;
import api.mapper.RoleMapper;
import api.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * {@link RoleController}.
 */
@RestController
@RequestMapping("/roles")
@Tag(
    name = "Roles",
    description = "Roles contain a set of authorities which athorize users access different functionality."
)
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;

    /**
     * Get roles.
     *
     * @return {@link List} of {@link RoleDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    @Operation(
        summary = "Get Roles",
        description = "Get list of all role's information."
    )
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
    })
    public List<RoleDto> getRoles() {
        return roleService.getAll().stream()
            .map(roleMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get role.
     *
     * @param id Role id
     * @return {@link RoleDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    @Operation(
        summary = "Get Role",
        description = "Get specific role's information."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = RoleDto.class),
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
    public RoleDto getRole(@PathVariable int id) {
        return roleMapper.toDto(roleService.get(id));
    }

    /**
     * Create role.
     *
     * @param roleDto Role
     * @return {@link RoleDto}
     */
    @Transactional
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_WRITE')")
    @Operation(
        summary = "Create Role",
        description = "Create a new role."
            + "<ul>"
                + "<li>The name of the role must not be empty.</li>"
                + "<li>The name of the role must not conflict with an existing role.</li>"
            + "</ul>"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = RoleDto.class),
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
            responseCode = "403",
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
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        if (!StringUtils.hasText(roleDto.getName())) {
            throw new IllegalArgumentException("Role name must not be empty.");
        }
        if (roleService.exists(roleDto.getName())) {
            throw new DuplicateEntityException("Role '" + roleDto.getName() + "' already exists.");
        }
        return roleMapper.toDto(roleService.save(roleMapper.toEntity(roleDto)));
    }

    /**
     * Update role.
     *
     * @param id Id of role to update
     * @param roleDto Updated information
     * @return {@link RoleDto}
     */
    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ') and hasAuthority('ROLE_WRITE')")
    @Operation(
        summary = "Update Role",
        description = "Update a specific role."
            + "<ul>"
                + "<li>The name of the role must not be empty.</li>"
                + "<li>The name of the role must not conflict with an existing role.</li>"
                + "<li>Ignores <em>id</em> from request body.</li>"
                + "<li>Any field missing or null from request body will be left unchanged in user information.</li>"
            + "</ul>"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = RoleDto.class),
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
    public RoleDto updateRole(@PathVariable int id, @RequestBody RoleDto roleDto) {
        if (roleDto.getName() != null && !StringUtils.hasText(roleDto.getName())) {
            throw new IllegalArgumentException("Role name must not be empty.");
        }

        Role role = roleService.get(id);
        if (StringUtils.hasText(roleDto.getName())
            && !Objects.equals(roleDto.getName(), role.getName())
            && roleService.exists(roleDto.getName())
        ) {
            throw new DuplicateEntityException("Role '" + roleDto.getName() + "' already exists.");
        }

        roleMapper.update(role, roleDto);
        return roleMapper.toDto(roleService.save(role));
    }

    /**
     * Delete role.
     */
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_WRITE')")
    @Operation(
        summary = "Delete Role",
        description = "Delete a specific role."
    )
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
    public void deleteRole(@PathVariable int id) {
        roleService.delete(id);
    }
}
