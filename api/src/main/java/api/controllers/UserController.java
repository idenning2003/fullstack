package api.controllers;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Create user.
     *
     * @return User id
     */
    @Transactional
    @PostMapping(path = "")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
        )
    })
    public UserDto createUser() {
        User user = userService.createUser();
        Hibernate.initialize(user);
        return userMapper.toDto(user);
    }

    /**
     * Get user.
     *
     * @param id User id
     * @return User info
     */
    @Transactional(readOnly = true)
    @GetMapping(path = "/{id}")
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
     * Update user.
     *
     * @param updated Updated user info
     * @return User info
     */
    @Transactional
    @PutMapping(path = "")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = UserDto.class), mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "400",
            content = @Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            content = @Content(schema = @Schema(implementation = ErrorDto.class), mediaType = "application/json")
        )
    })
    public UserDto updateUser(@RequestBody UserDto updated) {
        if (updated.getId() == null) {
            throw new IllegalArgumentException("User id must be provided.");
        }
        User user = userService.updateUser(updated);
        Hibernate.initialize(user);
        return userMapper.toDto(user);
    }

    /**
     * Delete user.
     *
     * @param id User id
     */
    @Transactional
    @DeleteMapping(path = "/{id}")
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
