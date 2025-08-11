package api.controllers;

import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.AuthorityDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * AuthorityController.
 */
@RestController
@RequestMapping("/authorities")
public class AuthorityController {
    /**
     * Get authorities.
     *
     * @return Authorities
     */
    @Transactional(readOnly = true)
    @GetMapping("")
    @PreAuthorize("hasAuthority('AUTHORITY_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = AuthorityDto.class)),
                mediaType = "application/json"
            )
        )
    })
    public Set<AuthorityDto> getAuthorities() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Get.
     *
     * @return {@link AuthorityDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('AUTHORITY_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = AuthorityDto.class), mediaType = "application/json")
        )
    })
    public AuthorityDto getAuthority(@PathVariable int id) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
