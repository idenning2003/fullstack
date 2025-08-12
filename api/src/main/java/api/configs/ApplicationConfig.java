package api.configs;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link ApplicationConfig}.
 */
@Configuration
public class ApplicationConfig {
    /**
     * clock.
     *
     * @return {@link Clock}.
     */
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }
}
