package api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.AuthenticationDto;
import api.dtos.ErrorDto;
import api.dtos.LoginDto;
import api.dtos.RegisterDto;
import api.services.AuthenticationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * {@link AuthenticationController}.
 */
@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Login.
     *
     * @return {@link AuthenticationDto}
     */
    @PostMapping("/login")
    @Transactional(readOnly = true)
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = AuthenticationDto.class),
                mediaType = "application/json"
            )
        ),
    })
    public AuthenticationDto login(@RequestBody LoginDto login) {
        return authenticationService.login(login);
    }

    /**
     * Register.
     *
     * @return {@link AuthenticationDto}
     */
    @PostMapping("/register")
    @Transactional
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                schema = @Schema(implementation = AuthenticationDto.class),
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
    public AuthenticationDto register(@RequestBody RegisterDto register) {
        return authenticationService.register(register);
    }
}
