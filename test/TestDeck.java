import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestDeck {
    @Test
    // add card to deck
    public void TestAddCard() {
        Deck deck = new Deck(2, 1);
        Card card = new Card(1);
        deck.add(card);
        assertEquals(card, deck.poll());
    }

    // add card to deck upper limit
    @Test
    public void TestAddTooManyCards() {
      Deck deck = new Deck(2, 1);
      Card card1 = new Card(1);
      Card card2 = new Card(2);
      Card card3 = new Card(3);
      Card card4 = new Card(4);
      Card card5 = new Card(5);
      Card card6 = new Card(6);
      Card card7 = new Card(7);
      Card card8 = new Card(8);
      Card card9 = new Card(9);
      try {
        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
        deck.add(card6);
        deck.add(card7);
        deck.add(card8);
        deck.add(card9);
        fail("IllegalStateException should be thrown");
      } catch (IllegalStateException e) {
        // pass
      }
    }

    // add two cards to deck
    @Test
    public void TestAddTwoCards() {
      Deck deck = new Deck(2, 1);
      Card card1 = new Card(1);
      Card card2 = new Card(2);
      deck.add(card1);
      deck.add(card2);
      assertEquals(card1, deck.poll());
      assertEquals(card2, deck.poll());
    }

    // remove card from empty deck
    @Test
    public void TestRemoveCardFromEmptyDeck() {
      Deck deck = new Deck(2, 1);
        assertNull(deck.poll());
    }

    // remove card from deck
    @Test
    public void TestRemoveCardFromDeck() {
      Deck deck = new Deck(2, 1);
      Card card = new Card(1);
      deck.add(card);
      assertEquals(card, deck.poll());
      assertEquals(null, deck.poll());
    }

    // remove two cards from deck
    @Test
    public void TestRemoveTwoCardsFromDeck() {
      Deck deck = new Deck(2, 1);
      Card card1 = new Card(1);
      Card card2 = new Card(2);
      deck.add(card1);
      deck.add(card2);
      assertEquals(card1, deck.poll());
      assertEquals(card2, deck.poll());
      assertEquals(null, deck.poll());
    }

    // remove card from deck upper limit
    @Test
    public void TestRemoveTooManyCards() {
      Deck deck = new Deck(2, 1);
      Card card1 = new Card(1);
      Card card2 = new Card(2);
      deck.add(card1);
      for (int i = 0; i < 7; i++) {
        deck.add(card2);
      }
      assertEquals(card1, deck.poll());
    }

    // output deck to a file
    @Test
    public void TestOutputDeckToFile() throws IOException {
      Deck deck = new Deck(2, 1);
      Card card1 = new Card(1);
      deck.add(card1);
      deck.writeToFile();
      BufferedReader br = new BufferedReader(new FileReader("deck1_output.txt"));
      String contents;
      try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        sb.append(line);
        contents = sb.toString();
      }finally {
        br.close();
      }
      assertEquals("deck1 contents: 1", contents);
    }

    // output an empty deck to a file
    @Test
    public void TestOutputEmptyDeckToFile() throws IOException {
      Deck deck = new Deck(2, 1);
      deck.writeToFile();
      BufferedReader br = new BufferedReader(new FileReader("deck1_output.txt"));
      String contents;
      try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        sb.append(line);
        contents = sb.toString();
      }finally {
        br.close();
      }
      assertEquals("deck1 contents: ", contents);
    }

    // output a full deck to a file
    @Test
    public void TestOutputFullDeckToFile() throws IOException {
      Deck deck = new Deck(2, 1);
      Card card1 = new Card(1);
      for (int i = 0; i < 8; i++) {
        deck.add(card1);
      }
      deck.writeToFile();
      BufferedReader br = new BufferedReader(new FileReader("deck1_output.txt"));
      String contents;
      try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        sb.append(line);
        contents = sb.toString();
      }finally {
        br.close();
      }
      assertEquals("deck1 contents: 1 1 1 1 1 1 1 1", contents);
    }


}
