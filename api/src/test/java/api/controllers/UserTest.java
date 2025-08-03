package api.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import api.dtos.UserDto;

/**
 * User Test.
 */
@SuppressWarnings("null")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String HOSTNAME = "localhost";

    @LocalServerPort
    private int port;
    private String url;
    private UserDto user;

    @BeforeEach
    public void setup() {
        url = "http://" + HOSTNAME + ":" + port;

        // Request creating new user
        URI uri = UriComponentsBuilder.fromUriString(url)
            .path("/users")
            .build()
            .toUri();
        ResponseEntity<UserDto> responseEntity = testRestTemplate.exchange(
            uri, HttpMethod.POST, null, UserDto.class
        );

        assertAll(
            () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
            () -> assertNotNull(responseEntity.getBody())
        );
        user = responseEntity.getBody();
    }

    @Nested
    class CreateUser {
        @Test
        public void shouldCreateUser() {
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users")
                .build()
                .toUri();

            ResponseEntity<UserDto> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.POST, null, UserDto.class
            );

            assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertNotEquals(user.getId(), responseEntity.getBody().getId())
            );
        }
    }

    @Nested
    class GetUser {
        @Test
        public void shouldFail_whenIdInvalid() {
            int id = user.getId() + 1;
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users/{id}")
                .buildAndExpand(id)
                .toUri();

            ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.GET, null, String.class
            );

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        public void shouldGetUser() {
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();

            ResponseEntity<UserDto> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.GET, null, UserDto.class
            );

            assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(user.getId(), responseEntity.getBody().getId())
            );
        }
    }

    @Nested
    class UpdateUser {
        @Test
        public void shouldFail_whenIdInvalid() {
            int id = user.getId() + 1;
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users")
                .build()
                .toUri();
            UserDto updated = UserDto.builder()
                .id(id)
                .build();

            HttpEntity<UserDto> requestEntity = new HttpEntity<>(updated);
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.PUT, requestEntity, String.class
            );

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        public void shouldFail_whenBodyEmpty() {
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users")
                .build()
                .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(
                String.format("{}", user.getId()),
                headers
            );
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.PUT, requestEntity, String.class
            );

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        public void shouldUpdateUser() {
            String name = "test";
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users")
                .build()
                .toUri();
            UserDto updated = UserDto.builder()
                .id(user.getId())
                .name(name)
                .build();

            HttpEntity<UserDto> requestEntity = new HttpEntity<>(updated);
            ResponseEntity<UserDto> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.PUT, requestEntity, UserDto.class
            );

            assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertNotNull(responseEntity.getBody()),
                () -> assertEquals(user.getId(), responseEntity.getBody().getId()),
                () -> assertEquals(name, responseEntity.getBody().getName())
            );
        }
    }

    @Nested
    class DeleteTest {
        @Test
        public void shouldFail_whenIdInvalid() {
            int id = user.getId() + 1;
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users/{id}")
                .buildAndExpand(id)
                .toUri();

            ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                uri, HttpMethod.DELETE, null, String.class
            );

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        public void shouldDeleteUser() {
            URI uri = UriComponentsBuilder.fromUriString(url)
                .path("/users/{id}")
                .buildAndExpand(user.getId())
                .toUri();

            ResponseEntity<String> deleteResponseEntity = testRestTemplate.exchange(
                uri, HttpMethod.DELETE, null, String.class
            );
            ResponseEntity<String> getResponseEntity = testRestTemplate.exchange(
                uri, HttpMethod.GET, null, String.class
            );

            assertAll(
                () -> assertEquals(HttpStatus.OK, deleteResponseEntity.getStatusCode()),
                () -> assertNull(deleteResponseEntity.getBody()),
                () -> assertEquals(HttpStatus.NOT_FOUND, getResponseEntity.getStatusCode())
            );
        }
    }
}
