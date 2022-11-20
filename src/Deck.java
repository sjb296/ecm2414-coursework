import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;

public class Deck {

    ArrayBlockingQueue<Card> cardQueue;
    int number;

    /**
     * Constructor.
     *
     * @param players
     * @param number
     */
    public Deck(int players, int number) {
        // Creates an empty queue of cards
        this.number = number;
        int amountOfCards = 8 * players;
        cardQueue = new ArrayBlockingQueue<Card>(amountOfCards - (players * 4));
    }

    public int getNumber() {
        return number;
    }

    public void add(Card card) {
        cardQueue.add(card);
    }

    public Card poll() {
        return cardQueue.poll();
    }

    public String toString() {
        return "Deck " + this.number;
    }

    /**
     * Writes output file.
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException {
        String output = "deck" + number + " contents: " + cardQueue.toString()
                .replace("[", "")
                .replace("]", "").replace(",", "");

        OutputFileHelper.writeOutputFile("deck" + number, output);
    }

    public int length() {
        return cardQueue.size();
    }

    public Card peek() {
        return cardQueue.peek();
    }

    public boolean isFull() {
        return cardQueue.remainingCapacity() == 0;
    }
}
