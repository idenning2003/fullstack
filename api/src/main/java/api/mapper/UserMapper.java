package api.mapper;

import org.mapstruct.Mapper;

import api.dtos.UserDto;
import api.entities.User;

/**
 * UserMapper.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Convert to dto.
     *
     * @param user User model
     * @return UserDto
     */
    UserDto toDto(User user);
}
