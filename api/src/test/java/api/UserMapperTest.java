package api;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import api.mapper.UserMapper;

/**
 * UserMapperTest.
 */
@SpringBootTest
public class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void shouldReturnNull_whenGivenNull() {
        assertNull(userMapper.toDto(null));
    }
}
