public class Card {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private static void checkNonNegative(int value) {
        if (value < 0) {
            throw new NumberFormatException("Card value must be non-negative integer!");
        }
    }

    public Card(int value) {
        Card.checkNonNegative(value);
        this.value = value;
    }

    public String toString() {
        return Integer.toString(value);
    }
}
