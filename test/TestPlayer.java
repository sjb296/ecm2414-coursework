import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import org.junit.Test;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

        assert(left.poll() == null);
        assert(right.poll().getValue() == 2);
        assert(p1.getHand()[0].getValue() == 1);
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

        assert(left.poll() == null);
        assert(right.poll().getValue() == 2);
        assert(p1.getHand()[0].getValue() == 3);
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

        assert(left.poll() == null);
        assert(right.poll().getValue() == 2);
        assert(right.poll().getValue() == 3);
        assert(p1.getHand()[0].getValue() == 6);
        assert(p1.getHand()[1].getValue() == 7);
    }

    // draw that it loops over the entire hand to discard non-preferred cards
    @Test
    public void TestDrawDiscardFiveNonPreferred() {
        // give player starting hand
        Deck left = new Deck(1, 1);
        left.add(new Card(2));
        left.add(new Card(3));
        left.add(new Card(4));
        left.add(new Card(5));
        left.add(new Card(6));
        Deck right = new Deck(1, 2);
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

        assert(left.poll() == null);
        assert(right.poll().getValue() == 7);
        assert(right.poll().getValue() == 8);
        assert(right.poll().getValue() == 9);
        assert(right.poll().getValue() == 10);
        assert(right.poll().getValue() == 2);
        assert(p1.getHand()[0].getValue() == 6);
        assert(p1.getHand()[1].getValue() == 3);
        assert(p1.getHand()[2].getValue() == 4);
        assert(p1.getHand()[3].getValue() == 5);
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
        assert(left.poll() == null);
        assert(right.poll() == null);
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
        right.add(new Card(5));
        right.add(new Card(6));
        right.add(new Card(7));
        right.add(new Card(8));
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        try {
            p1.drawDiscard();
            fail("IllegalStateException should be thrown!");
        } catch (IllegalStateException e) {
            //pass
        }
    }

    // Test to see if prioritised cards are ever discarded
    @Test
    public void TestKeepPrioritisedCard(){
        Card[] startingHand = {new Card(2), new Card(1), new Card(1), new Card(1)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        // 10 cards for 10 cycles
        left.add(new Card(2));
        left.add(new Card(3));
        left.add(new Card(4));
        left.add(new Card(5));
        left.add(new Card(6));
        left.add(new Card(7));
        left.add(new Card(8));
        left.add(new Card(9));
        left.add(new Card(10));
        left.add(new Card(11));
        Player p1 = new Player(left, right, startingHand, 1);
        // draw and discard
        for(int i=0; i<11; i++){
            p1.drawDiscard();
        }
        assert(p1.getHand()[0].getValue() == 11);
        assert(p1.getHand()[1].getValue() == 1);
        assert(p1.getHand()[2].getValue() == 1);
        assert(p1.getHand()[3].getValue() == 1);
        assert(left.poll() == null);
        assert(right.length() == 10);
    }

    // Starting hand is a win
    @Test
    public void TestStartWinningHand() throws IOException {
        Card[] startingHand = {new Card(1), new Card(1), new Card(1), new Card(1)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        Player p1 = new Player(left, right, startingHand, 1);
        if(p1.hasWon()){
            //pass
        }else {
            fail("Player 1 should have won!");
        }
        BufferedReader br = new BufferedReader(new FileReader("player1_output.txt"));
        String contents = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = "";
            for(int i=0; i<4; i++){
                line = br.readLine();
                sb.append(line);
            }
            contents = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        assertEquals("player 1 initial hand 1 1 1 1\nplayer 1 wins\nplayer 1 exits\nplayer 1 final hand 1 1 1 1", contents);
    }

    // External win event
    @Test
    public void TestExternalWinEvent() throws InterruptedException, IOException {
        Card[] startingHand = {new Card(1), new Card(2), new Card(3), new Card(4)};
        Deck right = new Deck(1, 2);
        Deck left = new Deck(1, 1);
        Thread p1 = new Thread(new Player(left, right, startingHand, 1));
        WinHandler winHandler;
        winHandler.sendWinEvent(2);
        sleep(100);
        if(p1.isAlive()){
            fail("Thread should be killed!");
        }
        BufferedReader br = new BufferedReader(new FileReader("player1_output.txt"));
        String contents = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = "";
            for(int i=0; i<4; i++){
                line = br.readLine();
                sb.append(line);
            }
            contents = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        assertEquals("player 1 initial hand 1 2 3 4\nplayer 2 wins\nplayer 1 exits\nplayer 1 final hand 1 2 3 4", contents);
    }

    // Check hand is logged with a timestamp
    @Test
    public void TestHandIsLogged(){
        // give player starting hand
        Deck left = new Deck(1, 1);
        left.add(new Card(3));
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        p1.drawDiscard();

        assert(left.poll() == null);
        assert(right.poll().getValue() == 2);
        assert(p1.getHand()[0].getValue() == 3);
        assert(p1.getLog[1][0] == new Card[]{new Card(3), new Card(2), new Card(2), new Card(2)});
        assert(p1.getLog[1][1] instanceof Date);
    }

    // Check hand is logged with a timestamp
    @Test
    public void TestHandIsLogged3Cycles(){
        // give player starting hand
        Deck left = new Deck(1, 1);
        left.add(new Card(3));
        left.add(new Card(4));
        left.add(new Card(5));
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        for(int i=0; i<3; i++){
            p1.drawDiscard();
        }

        assert(left.poll() == null);
        assert(right.poll().getValue() == 2);
        assert(p1.getHand()[0].getValue() == 3);
        assert(p1.getLog[1][0] == new Card[]{new Card(3), new Card(2), new Card(2), new Card(2)});
        assert(p1.getLog[2][0] == new Card[]{new Card(3), new Card(4), new Card(2), new Card(2)});
        assert(p1.getLog[3][0] == new Card[]{new Card(3), new Card(4), new Card(5), new Card(2)});
        for(int i=0; i<3; i++){
            assert(p1.getLog[i][1] instanceof Date);
        }
    }

    // Hand is reverted to last hand before win event
    @Test
    public void TestHandIsReverted(){
        // give player starting hand
        Deck left = new Deck(16, 1);
        for(int i=0; i<64; i++){
            left.add(new Card(i));
        }
        Deck right = new Deck(1, 2);
        Card[] startingHand = {new Card(2), new Card(2), new Card(2), new Card(2)};
        Player p1 = new Player(left, right, startingHand, 1);
        // draw & discard
        p1.drawDiscard();
        // win event
        WinHandler.sendWinEvent(2);
        // simulated game loop
        //TODO might need to go on a thread for the loop to stop
        for(int i=0; i<63; i++){
            p1.drawDiscard();
        }
        // check hand is reverted
        assert(p1.getHand()[0].getValue() == 3);
        assert(p1.getHand()[1].getValue() == 4);
        assert(p1.getHand()[2].getValue() == 5);
        assert(p1.getHand()[3].getValue() == 2);
    }
}
