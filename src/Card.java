public class Card {
    private int value;

    public int getValue() {
        return value;
    }

    /**
     * Checks card value is a positive integer.
     *
     * @param value of the card.
     * @throws NumberFormatException
     */
    private static void checkNonNegative(final int value) {
        if (value < 0) {
            throw new NumberFormatException("Card value must be non-negative integer!");
        }
    }

    /**
     * Constructor.
     *
     * @param cardValue of the card.
     */
    public Card(final int cardValue) {
        Card.checkNonNegative(cardValue);
        this.value = cardValue;
    }

    public String toString() {
        return Integer.toString(value);
    }
}
