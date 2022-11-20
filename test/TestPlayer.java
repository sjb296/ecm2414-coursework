import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestPlayer {
    // draw and discard from a non-empty left deck and empty right deck
    @Test
    public void TestDrawDiscardPreferredCard() {
        // give player starting hand
        Deck left = new Deck(1, 1);
        left.add(new Card(1));
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        p1.drawDiscard();

        assert (left.poll() == null);
        assert (right.poll().getValue() == 2);
        assert (p1.getHand()[0].getValue() == 1);
    }

    // draw a non-preferred card and discard the next one on
    @Test
    public void TestDrawDiscardOneNonPreferred() {
        // give player starting hand
        Deck left = new Deck(1, 1);
        left.add(new Card(3));
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        p1.drawDiscard();

        assert (left.poll() == null);
        assert (right.poll().getValue() == 2);
        assert (p1.getHand()[0].getValue() == 3);
    }

    // draw 2 non-preferred cards & discard hand cards 0 and 1
    public void TestDrawDiscardTwoNonPreferred() {
        // give player starting hand
        Deck left = new Deck(1, 1);
        left.add(new Card(6));
        left.add(new Card(7));
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(3), new Card(4), new Card(5)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard twice
        p1.drawDiscard();
        p1.drawDiscard();

        assert (left.poll() == null);
        assert (right.poll().getValue() == 2);
        assert (right.poll().getValue() == 3);
        assert (p1.getHand()[0].getValue() == 6);
        assert (p1.getHand()[1].getValue() == 7);
    }

    // draw that it loops over the entire hand to discard non-preferred cards
    @Test
    public void TestDrawDiscardFiveNonPreferred() {
        // give player starting hand
        Deck left = new Deck(2, 1);
        left.add(new Card(2));
        left.add(new Card(3));
        left.add(new Card(4));
        left.add(new Card(5));
        left.add(new Card(6));
        Deck right = new Deck(2, 2);
        Card[] startingHand = {
                new Card(7),
                new Card(8),
                new Card(9),
                new Card(10)
        };
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard five times to loop over
        p1.drawDiscard();
        p1.drawDiscard();
        p1.drawDiscard();
        p1.drawDiscard();
        p1.drawDiscard();

        assert (left.poll() == null);
        assert (right.poll().getValue() == 7);
        assert (right.poll().getValue() == 8);
        assert (right.poll().getValue() == 9);
        assert (right.poll().getValue() == 10);
        assert (right.poll().getValue() == 2);
        assert (p1.getHand()[0].getValue() == 6);
        assert (p1.getHand()[1].getValue() == 3);
        assert (p1.getHand()[2].getValue() == 4);
        assert (p1.getHand()[3].getValue() == 5);
    }

    // test draw and discard from empty deck
    @Test
    public void TestDrawDiscardEmptyDeck() {
        // give player starting hand
        Deck left = new Deck(1, 1);
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        p1.drawDiscard();

        // Assert that he doesn't play (so the decks don't change)
        assert (left.poll() == null);
        assert (right.poll() == null);
    }

    // test draw and discard to full deck
    @Test
    public void TestDrawDiscardFullDeck() {
        // give player starting hand
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Deck left = new Deck(1, 1);
        left.add(new Card(9));
        Deck right = new Deck(1, 2);
        right.add(new Card(1));
        right.add(new Card(2));
        right.add(new Card(3));
        right.add(new Card(4));
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        try {
            p1.drawDiscard();
            assertEquals(9, left.peek().getValue());
        } catch (IllegalStateException e) {
            //pass
        }
    }

    // Test to see if prioritised cards are ever discarded
    @Test
    public void TestKeepPrioritisedCard() {
        Card[] startingHand = {new Card(2), new Card(1), new Card(1), new Card(1)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        // 4 cards for 4 cycles
        left.add(new Card(3));
        left.add(new Card(4));
        left.add(new Card(5));
        left.add(new Card(6));
        Player p1 = new Player(left, right, startingHand, 1);
        // draw and discard
        for (int i = 0; i < 4; i++) {
            p1.drawDiscard();
        }

        assert (p1.getHand()[0].getValue() == 6);
        assert (p1.getHand()[1].getValue() == 1);
        assert (p1.getHand()[2].getValue() == 1);
        assert (p1.getHand()[3].getValue() == 1);
        assert (left.poll() == null);
        assert (right.length() == 4);
    }

    // Starting hand is a win
    @Test
    public void TestStartWinningHand() throws InterruptedException, IOException {
        Card[] startingHand = {new Card(1), new Card(1), new Card(1), new Card(1)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        Player p1 = new Player(left, right, startingHand, 1);
        Thread p1Thread = new Thread(p1);
        p1Thread.start();
        p1Thread.join();
        // Output file should have 4 lines as no draw and discards have occurred
        String contents = OutputFileHelper.readOutputFile("player1");
        assertEquals("player 1 initial hand 1 1 1 1\n"
                + "player 1 wins\n"
                + "player 1 exits\n"
                + "player 1 final hand 1 1 1 1",contents);
    }

    // External win event
    @Test
    public void TestExternalWinEvent() throws InterruptedException, IOException {
        Card[] startingHand = {new Card(1), new Card(2), new Card(3), new Card(4)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        Player p1 = new Player(left, right, startingHand, 1);
        Player p2 = new Player(null, null, new Card[]{new Card(0), new Card(0), new Card(0), new Card(0)}, 2);

        ArrayList<Player> otherPlayers = new ArrayList<>();
        otherPlayers.add(p2);
        p1.setOtherPlayers(otherPlayers);
        p2.win();

        // Should die without ever drawing or discarding
        Thread p1Thread = new Thread(p1);
        p1Thread.start();
        p1Thread.join();
        // Output file should have 4 lines as no draw and discards have occurred
        String contents = OutputFileHelper.readOutputFile("player1");
        assertEquals("player 1 initial hand 1 2 3 4\n"
                + "player 2 has informed player 1 that player 2 has won\n"
                + "player 1 exits\n"
                + "player 1 final hand 1 2 3 4",contents);
    }

    @Test
    public void TestLogInternalWin() throws IOException, InterruptedException {
        Card[] startingHand = {new Card(1), new Card(1), new Card(1), new Card(1)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);

        Player p1 = new Player(left, right, startingHand, 1);
        Thread p1Thread = new Thread(p1);
        p1Thread.start();
        p1Thread.join();

        String contents = OutputFileHelper.readOutputFile("player1");
        assertEquals("player 1 initial hand 1 1 1 1\nplayer 1 wins\nplayer 1 exits\nplayer 1 final hand 1 1 1 1", contents);
    }

    @Test
    public void TestLogExternalWin() throws InterruptedException, IOException {
        Card[] startingHand = {new Card(1), new Card(2), new Card(3), new Card(4)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        Player p1 = new Player(left, right, startingHand, 1);
        Player p2 = new Player(null, null, new Card[]{new Card(0), new Card(0), new Card(0), new Card(0)}, 2);

        ArrayList<Player> otherPlayers = new ArrayList<>();
        otherPlayers.add(p2);
        p1.setOtherPlayers(otherPlayers);
        p2.win();

        // Should die without ever drawing or discarding
        Thread p1Thread = new Thread(p1);
        p1Thread.start();
        p1Thread.join();

        String contents = OutputFileHelper.readOutputFile("player1");
        assertEquals(("player 1 initial hand 1 2 3 4\n" +
                        "player 2 has informed player 1 that player 2 has won\n" +
                        "player 1 exits\n" +
                        "player 1 final hand 1 2 3 4"),
                contents);
    }

    @Test
    public void TestDrawDiscardLog() throws InterruptedException, IOException {
        // set up the game so one draw/discard happens
        Card[] startingHand = {new Card(1), new Card(1), new Card(1), new Card(2)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        left.add(new Card(1));
        Player p1 = new Player(left, right, startingHand, 1);
        Thread p1Thread = new Thread(p1);
        p1Thread.start();
        p1Thread.join();

        // test it logs the right thing
        assertEquals("player 1 initial hand 1 1 1 2\nplayer 1 draws a 1 from deck 1\nplayer 1 discards a 2 to deck 2\nplayer 1 current hand is 1 1 1 1\nplayer 1 wins\nplayer 1 exits\nplayer 1 final hand 1 1 1 1", OutputFileHelper.readOutputFile("player1"));
    }
}