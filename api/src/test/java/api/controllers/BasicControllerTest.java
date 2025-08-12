package api.controllers;

import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Baseline configurations for all controller tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BasicControllerTest {
    @LocalServerPort
    private int port;
    private static final String HOSTNAME = "localhost";

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected String url;

    @MockitoBean
    protected Clock clock;
    protected ZoneId zone = ZoneId.of("UTC");
    protected Instant instant = Instant.EPOCH;

    @BeforeEach
    public void setup() {
        url = "http://" + HOSTNAME + ":" + port;

        when(clock.getZone()).thenAnswer(inv -> zone);
        when(clock.instant()).thenAnswer(inv -> instant);
    }
}
