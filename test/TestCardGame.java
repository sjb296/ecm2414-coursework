import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class TestCardGame {
    // user inputs valid amount of players (4)
    @Test
    public void TestValidNumberOfPlayers()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame game = new CardGame(4, "test/testpacks/valid_pack.txt");
        assertEquals(4, game.getNumberOfPlayers());
    }

    // players get the right number according to creation order
    @Test
    public void TestPlayersGivenRightNumber() throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame game = new CardGame(4, "test/testpacks/valid_pack.txt");
        game.setupGame();

        for (int i=0;i<4;i++) {
            assertEquals(i+1, game.getPlayers().get(i).getNumber());
        }
    }

    // user inputs lower bound of players (2)
    @Test
    public void TestLowerBoundNumberOfPlayers()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame game = new CardGame(2, "test/testpacks/valid_pack.txt");
        assertEquals(2, game.getNumberOfPlayers());
    }

    // user inputs invalid amount of players (negative)
    @Test
    public void TestNegativeNumberOfPlayers()
            throws InvalidPackException {
        try {
            CardGame game = new CardGame(-1, "test/testpacks/valid_pack.txt");
            fail("Expected InvalidNumberOfPlayersException");
        } catch (InvalidNumberOfPlayersException e) {
            assertEquals("Number of players must be two or more!", e.getMessage());
        }
    }

    // user inputs invalid amount of players (zero)
    @Test
    public void TestZeroPlayers()
            throws InvalidPackException {
        try {
            CardGame game = new CardGame(0, "test/testpacks/valid_pack.txt");
            fail("Expected InvalidNumberOfPlayersException");
        } catch (InvalidNumberOfPlayersException e) {
            assertEquals("Number of players must be two or more!", e.getMessage());
        }
    }

    // user inputs invalid amount of players (1)
    @Test
    public void TestOnePlayer()
            throws InvalidPackException {
        try {
            CardGame game = new CardGame(1, "test/testpacks/valid_pack.txt");
            fail("Expected InvalidNumberOfPlayersException");
        } catch (InvalidNumberOfPlayersException e) {
            assertEquals("Number of players must be two or more!", e.getMessage());
        }
    }

    // user inputs valid directory
    @Test
    public void TestValidDirectory()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
    }

    // user inputs invalid directory
    @Test
    public void TestInvalidDirectory()
            throws InvalidNumberOfPlayersException, InvalidPackException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        try {
            cardGame.setupGame();
            fail("InvalidPackException should have been thrown!");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // user inputs valid directory, but invalid pack
    @Test
    public void TestValidDirectoryInvalidPack()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/negative_number.txt");
        try {
            cardGame.setupGame();
            fail("InvalidPackException should have been thrown!");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // n players are created
    @Test
    public void TestNumberOfPlayersCreated()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        assertEquals(4, cardGame.getPlayers().size());
    }

    // n threads created
    @Test
    public void TestNumberOfThreadsCreated()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        assertEquals(4, cardGame.getPlayerThreads().size());
    }

    // test threads all start on unwinnable game
    @Test
    public void TestAllThreadsStart() throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/unwinnable_game.txt");
        cardGame.setupGame();
        cardGame.playGame();
        // Check they're all alive
        for (Thread thread : cardGame.getPlayerThreads()) {
            assert(thread.isAlive());
        }
        // kill them all by faking a winner
        cardGame.getPlayers().get(0).win();
    }

    // n decks created
    @Test
    public void TestNumberOfDecksCreated()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        assertEquals(4, cardGame.getDecks().size());
    }

    // decks given the right number by creation order
    @Test
    public void TestDecksGivenRightNumber() throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        for (int i=0;i<4;i++) {
            assertEquals(i+1, cardGame.getDecks().get(i).getNumber());
        }
    }

    // 8n cards created
    @Test
    public void Test8nCardsCreatedTotal()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        int cards = 0;
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // count across players' hands
        for (Player player : cardGame.getPlayers()) {
            cards += player.getHand().length;
        }

        // count across decks
        for (Deck deck : cardGame.getDecks()) {
            cards += deck.length();
        }

        assertEquals(cards % 8, 0);
        assertEquals(32, cards);
    }

    // card values match values in the pack
    @Test
    public void TestCardValuesMatchPack()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(2, "test/testpacks/valid_pack_2p.txt");
        cardGame.setupGame();

        Card[] pack = new Card[16];

        // merge hands then decks into one "pack" array

        // index incrementing, start at 0, take 1 from each player THEN increment
        for (int i=0;i<4;i++) {
            pack[i] = cardGame.getPlayers().get(0).getHand()[i];
            pack[i] = cardGame.getPlayers().get(1).getHand()[i];
        }

        // then do same for decks
        for (int i=0;i<4;i++) {
            pack[15 - i] = cardGame.getDecks().get(1).poll();
            pack[15 - i] = cardGame.getDecks().get(0).poll();
        }

        // assert contents of pack match contents of file
        for (int i=1;i<17;i++) {
            assertEquals(i%9, pack[i].getValue());
        }
    }

    // 4n cards distributed to players
    @Test
    public void Test4nCardsDistributedToPlayers()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // count across players' hands
        int cards = 0;
        for (Player player : cardGame.getPlayers()) {
            cards += player.getHand().length;
        }

        assertEquals(cards % 4, 0);
        assertEquals(16, cards);
    }

    // cards distributed round robin to players
    @Test
    public void TestCardsDistributedRoundRobinPlayers()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // Loop through all the players
        for (int i=0;i<4;i++) {
            Card[] actualHand = cardGame.getPlayers().get(i).getHand();
            // Their hand should be this e.g. [1,5,1,5] in valid_pack.txt for player 1
            Card[] expectedHand = new Card[]{new Card(i+1), new Card(i+5),
                    new Card(i+9), new Card(i+13)};

            // Compare expected and actual hands elementwise
            for (int j=0;j<4;j++) {
                assertEquals(expectedHand[j].getValue(), actualHand[j].getValue());
            }
        }
    }

    // cards from index of 4n onwards are distributed to decks
    @Test
    public void TestCards4nOnwardsDistributedToDecks() throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(2, "test/testpacks/nonrepeating_pack.txt");
        cardGame.setupGame();

        // assert that no deck contains a card whose value < 9 or > 16
        for (Deck deck : cardGame.getDecks()) {
            for (int i=0;i<deck.length();i++) {
                Card card = deck.poll();
                assert((card.getValue() > 8) && (card.getValue() < 17));
            }
        }
    }

    // 4n cards are distributed to decks
    @Test
    public void Test4nCardsDistributedToDecks()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // count across decks
        int cards = 0;
        for (Deck deck : cardGame.getDecks()) {
            cards += deck.length();
        }

        assertEquals(cards % 4, 0);
        assertEquals(16, cards);
    }

    // cards distributed round robin to decks
    @Test
    public void TestCardsDistributedRoundRobinDecks()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // Loop through decks
        for (int i=0;i<4;i++) {
            Deck deck = cardGame.getDecks().get(i);
            for (int j=0;j<4;j++) {
                assertEquals(deck.poll().getValue(), (i + 1) + (j * 4));
            }
        }
    }

    // all players are assigned a left deck
    @Test
    public void TestAllPlayersAssignedLeftDeck()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        for (Player player : cardGame.getPlayers()) {
            assertNotNull(player.getLeftDeck());
        }
    }

    // all players are assigned a right deck
    @Test
    public void TestAllPlayersAssignedRightDeck()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        for (Player player : cardGame.getPlayers()) {
            assertNotNull(player.getRightDeck());
        }
    }

    // all left decks are assigned to a right deck
    // no two players can have the same left deck
    // no two players can have the same right deck
    @Test
    public void TestCorrectGameTopology()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        ArrayList<Deck> decksLeftwards  = new ArrayList<>();
        ArrayList<Deck> decksRightwards = new ArrayList<>();

        // loop through all players decks going left and make a list of references
        for (Player player : cardGame.getPlayers()) {
            decksLeftwards.add(player.getLeftDeck());
        }
        // loop through all players decks going right and make a list of references
        for (Player player : cardGame.getPlayers()) {
            decksRightwards.add(player.getRightDeck());
        }
        // they must be the reverse of each other (compare elementwise)
        Collections.reverse(decksRightwards);

        for (int i=0;i<decksLeftwards.size();i++) {
            assertEquals(decksLeftwards.get(i), decksRightwards.get(i));
        }
    }

    // each player's left deck can't be the same as their right deck
    @Test
    public void TestPlayersLeftAndRightDecksAreDifferent()
            throws InvalidPackException, InvalidNumberOfPlayersException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        for (Player player : cardGame.getPlayers()) {
            assertNotEquals(player.getLeftDeck(), player.getRightDeck());
        }
    }

    // game with a possible ending, has a winner
    @Test
    public void TestWinnableGameHasWinner()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException {
        CardGame cardGame = new CardGame(2, "test/testpacks/p1_wins_in_1_turn.txt");
        cardGame.setupGame();
        cardGame.playGame();

        sleep(200);
        assert(cardGame.getPlayers().get(0).hasWon());
    }


    // game with initial winning hand, has a winner
    @Test
    public void TestInitialWinningHandPlayerWins()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException {
        CardGame cardGame = new CardGame(2, "test/testpacks/pack_of_ones.txt");
        cardGame.setupGame();
        cardGame.playGame();

        sleep(200);
        assert(cardGame.getPlayers().get(0).hasWon());
    }
}
