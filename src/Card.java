public class Card {
    private int value;

    public int getValue() {
        return value;
    }

    /**
     * Checks card value is a positive integer
     *
     * @param value
     * @throws NumberFormatException
     */
    private static void checkNonNegative(int value) {
        if (value < 0) {
            throw new NumberFormatException("Card value must be non-negative integer!");
        }
    }

    /**
     * Constructor.
     *
     * @param value
     */
    public Card(int value) {
        Card.checkNonNegative(value);
        this.value = value;
    }

    public String toString() {
        return Integer.toString(value);
    }
}
