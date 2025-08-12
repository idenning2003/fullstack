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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * {@link RoleController}.
 */
@RestController
@RequestMapping("/roles")
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
        @ApiResponse(
            responseCode = "409",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
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
    @PreAuthorize("hasAuthority('ROLE_WRITE')")
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
        @ApiResponse(
            responseCode = "409",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public RoleDto updateRole(@PathVariable int id, @RequestBody RoleDto roleDto) {
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
