package api.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Heartbeat test.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HeartbeatTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String HOSTNAME = "localhost";

    @LocalServerPort
    private int port;
    private String url;

    @BeforeEach
    public void setup() {
        url = "http://" + HOSTNAME + ":" + port;
    }

    @Test
    public void shouldReturnTrue_whenHeartbeat() {
        ResponseEntity<Boolean> responseEntity = testRestTemplate.exchange(
            url + "/", HttpMethod.GET, null, Boolean.class
        );

        assertAll(
            () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
            () -> assertEquals(true, responseEntity.getBody())
        );
    }
}
