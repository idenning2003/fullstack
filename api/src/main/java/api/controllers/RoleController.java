package api.controllers;

import java.util.Set;

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

import api.dtos.RoleDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * RoleController.
 */
@RestController
@RequestMapping("/roles")
public class RoleController {
    /**
     * Get roles.
     *
     * @return Roles
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
        )
    })
    public Set<RoleDto> getRoles() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Get.
     *
     * @return {@link RoleDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = RoleDto.class), mediaType = "application/json")
        )
    })
    public RoleDto getRole(@PathVariable int id) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Create.
     */
    @Transactional
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_WRITE')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = RoleDto.class), mediaType = "application/json")
        )
    })
    public RoleDto createRole(@RequestBody RoleDto roleDto) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Update.
     */
    @Transactional
    @PutMapping("")
    @PreAuthorize("hasAuthority('ROLE_WRITE')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = RoleDto.class), mediaType = "application/json")
        )
    })
    public RoleDto updateRole(@RequestBody RoleDto roleDto) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Delete.
     */
    @Transactional
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_WRITE')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200"
        )
    })
    public void deleteRole(@PathVariable int id) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
