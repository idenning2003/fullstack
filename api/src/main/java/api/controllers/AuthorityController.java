package api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.AuthorityDto;
import api.dtos.ErrorDto;
import api.mapper.AuthorityMapper;
import api.services.AuthorityService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * {@link AuthorityController}.
 */
@RestController
@RequestMapping("/authorities")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private AuthorityMapper authorityMapper;

    /**
     * Get authorities.
     *
     * @return {@link List} of {@link AuthorityDto}
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
        ),
        @ApiResponse(
            responseCode = "403",
            content = @Content(
                schema = @Schema(implementation = ErrorDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public List<AuthorityDto> getAuthorities() {
        return authorityService.getAll().stream()
            .map(authorityMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get authority.
     *
     * @return {@link AuthorityDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('AUTHORITY_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = AuthorityDto.class),
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
    public AuthorityDto getAuthority(@PathVariable int id) {
        return authorityMapper.toDto(authorityService.get(id));
    }
}
