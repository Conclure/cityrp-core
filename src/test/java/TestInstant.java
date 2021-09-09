import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class TestInstant {
    @Test
    void testInstant() {
        Instant now = Instant.now();
        Assertions.assertDoesNotThrow(() -> Thread.sleep(5_000L));
        Instant then = Instant.now();
        Assertions.assertTrue(then.toEpochMilli()-now.toEpochMilli() > 0);
    }
}
