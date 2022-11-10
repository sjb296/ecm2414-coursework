import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class Deck {

  ArrayBlockingQueue<Card> cardQueue;
  int number;

  public Deck(int amountOfCards, int players, int number) {
    // Creates an empty queue of cards
    this.number = number;
    cardQueue = new ArrayBlockingQueue<Card>(amountOfCards - (players * 4));
  }

  public void add(Card card) {
    cardQueue.add(card);
  }

  public Card poll() {
    return cardQueue.poll();
  }

  public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException {
    String output = "deck" + number + " contents: " + cardQueue.toString()
        .replace("[", "")
        .replace("]", "").replace(",", "");
    String filename = "deck" + number + "_output.txt";

    PrintWriter out = new PrintWriter(filename, "UTF-8");
    out.print(output);
    out.close();

  }
}
