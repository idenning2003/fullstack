package api.controllers;

import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.ErrorDto;
import api.dtos.UserDto;
import api.entities.User;
import api.mapper.UserMapper;
import api.services.UserService;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
     * Get me.
     *
     * @return {@link UserDto}
     */
    @Transactional(readOnly = true)
    @GetMapping("/me")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
        )
    })
    public UserDto getMe() {
        // TODO
        throw new UnsupportedOperationException();
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
            content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
        )
    })
    public UserDto updateMe(@RequestBody UserDto userDto) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Delete me.
     */
    @Transactional
    @DeleteMapping("/me")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200"
        )
    })
    public void deleteMe() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Get users.
     *
     * @return User ids
     */
    @Transactional(readOnly = true)
    @GetMapping("")
    @PreAuthorize("hasAuthority('USER_READ')")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = Integer.class)),
                mediaType = "application/json"
            )
        )
    })
    public Set<Integer> getUsers() {
        // TODO: Paginate and return DTO
        return userService.getAllIds();
    }

    /**
     * Get user.
     *
     * @param id User id
     * @return User
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
        )
    })
    public User updateUser(@PathVariable int id, @RequestBody UserDto userDto) {
        // TODO
        throw new UnsupportedOperationException();
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
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")
        )
    })
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }
}
