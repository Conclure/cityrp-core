import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMath {
    @Test
    void testFloorZero() {
        Assertions.assertEquals(0, Math.floor(0d));
    }
    @Test
    void testFloorOne() {
        Assertions.assertEquals(0, Math.floor(0.1d));
    }
    @Test
    void testCeilZero() {
        Assertions.assertEquals(0, Math.ceil(0d));
    }
    @Test
    void testCeilOne() {
        Assertions.assertEquals(1, Math.ceil(0.1d));
    }
}
