package api.controllers;

import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.ErrorDto;
import api.dtos.UserDto;
import api.entities.User;
import api.mapper.UserMapper;
import api.services.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * UserController.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * Get user.
     *
     * @param id User id
     * @return User info
     */
    @Transactional(readOnly = true)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")
        )
    })
    public UserDto getUser(@PathVariable int id) {
        User user = userService.getUser(id);
        Hibernate.initialize(user);
        return userMapper.toDto(user);
    }

    /**
     * Get user ids.
     *
     * @return User ids
     */
    @Transactional(readOnly = true)
    @GetMapping("")
    @PreAuthorize("hasAuthority('USER_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = Set.class), mediaType = "application/json")
        )
    })
    public Set<Integer> getUserIds() {
        return userService.getUserIds();
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
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")
        )
    })
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
