import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestCard {
    @Test
    public void TestValidCardNumber() {
        Card card = new Card(3);
        assertEquals(card.getValue(), 3);
    }

    @Test
    public void TestZero() {
        Card card = new Card(0);
        assertEquals(card.getValue(), 0);
    }

    @Test
    public void TestLowerBound() {
        try {
            Card card = new Card(-2147483648);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertEquals("Expected NumberFormatException with message" +
                            "\"Card value must be non-negative integer!\"",
                    "Card value must be non-negative integer!",
                    e.getMessage());
        }
    }

    @Test
    public void TestUpperBound() {
        Card card = new Card(2147483647);
    }

    @Test
    public void TestTooBig() {
        try {
            Card card = new Card(2147483647 + 1);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // pass
        }
    }

    @Test
    public void TestCheckNonNegative() {
        try {
            Card card = new Card(-1);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            assertEquals(e.getMessage(), "Card value must be non-negative integer!");
        }
    }
}
