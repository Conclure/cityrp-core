import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestSystem {
    @Test
    void testSystemMillis() {
        long now = System.currentTimeMillis();
        Assertions.assertDoesNotThrow(() -> Thread.sleep(5_000L));
        long then = System.currentTimeMillis();
        Assertions.assertTrue(then-now > 0);
    }

    @Test
    void testSystemNanos() {
        long now = System.nanoTime();
        Assertions.assertDoesNotThrow(() -> Thread.sleep(5_000L));
        long then = System.nanoTime();
        Assertions.assertTrue(then-now > 0);
    }


}
