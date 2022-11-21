import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ArrayBlockingQueue;

public class Deck {

    private ArrayBlockingQueue<Card> cardQueue;
    private int number;

    /**
     * Constructor.
     *
     * @param players
     * @param deckNumber
     */
    public Deck(final int players, final int deckNumber) {
        // Creates an empty queue of cards
        this.number = deckNumber;
        int amountOfCards = 8 * players;
        // The queue's capacity is (total amount of cards - all the players' hands)
        cardQueue = new ArrayBlockingQueue<Card>(amountOfCards - (players * 4));
    }

    public int getNumber() {
        return number;
    }

    public void add(final Card card) {
        cardQueue.add(card);
    }

    public Card poll() {
        return cardQueue.poll();
    }

    /**
     * Creates the contents for its output file and then writes one.
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException {
        String output = "deck" + this.number + " contents: " + cardQueue.toString()
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
