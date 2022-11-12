import org.junit.Test;

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
}
