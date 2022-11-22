import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void TestPlayersGivenRightNumber() throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
    }

    // user inputs invalid directory
    @Test
    public void TestInvalidDirectory()
            throws InvalidNumberOfPlayersException, InvalidPackException {
        CardGame cardGame = new CardGame(4, "invalidDirectory.txt");
        try {
            cardGame.setupGame();
            fail("InvalidPackException should have been thrown!");
        } catch (InvalidPackException e) {
            // pass
        } catch (IOException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // n players are created
    @Test
    public void TestNumberOfPlayersCreated()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        assertEquals(4, cardGame.getPlayers().size());
    }

    // n threads created
    @Test
    public void TestNumberOfThreadsCreated()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        assertEquals(4, cardGame.getPlayerThreads().size());
    }

    // n decks created
    @Test
    public void TestNumberOfDecksCreated()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        assertEquals(4, cardGame.getDecks().size());
    }

    // decks given the right number by creation order
    @Test
    public void TestDecksGivenRightNumber() throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();
        for (int i=0;i<4;i++) {
            assertEquals(i+1, cardGame.getDecks().get(i).getNumber());
        }
    }

    // 8n cards created
    @Test
    public void Test8nCardsCreatedTotal()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(2, "test/testpacks/valid_pack_2p.txt");
        cardGame.setupGame();

        Card[] pack = new Card[16];

        // merge hands then decks into one "pack" array
        // index incrementing, start at 0, take 1 from each player THEN increment
        int j = 0;
        for (int i=0;i<8;i+=2) {
            pack[i]   = cardGame.getPlayers().get(0).getHand()[j%4];
            pack[i+1] = cardGame.getPlayers().get(1).getHand()[j%4];
            j++;
        }

        // then do same for decks
        for (int i=8;i<16;i+=2) {
            pack[i]   = cardGame.getDecks().get(0).poll();
            pack[i+1] = cardGame.getDecks().get(1).poll();
        }

        // assert contents of pack match contents of file
        for (int i=0;i<16;i++) {
            assertEquals((i%8)+1, pack[i].getValue());
        }
    }

    // 4n cards distributed to players
    @Test
    public void Test4nCardsDistributedToPlayers()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // Loop through all the players
        for (int i=0;i<4;i++) {
            Card[] actualHand = cardGame.getPlayers().get(i).getHand();
            // Their hand should be this e.g. [1,5,1,5] in valid_pack.txt for player 1
            Card[] expectedHand = new Card[]{
                    new Card(i+1), new Card(i+5),
                    new Card(i+1), new Card(i+5)
            };

            // Compare expected and actual hands elementwise
            for (int j=0;j<4;j++) {
                assertEquals(expectedHand[j].getValue(), actualHand[j].getValue());
            }
        }
    }

    // cards from index of 4n onwards are distributed to decks
    @Test
    public void TestCards4nOnwardsDistributedToDecks() throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        // Loop through decks
        for (int i=0;i<4;i++) {
            Deck deck = cardGame.getDecks().get(i);
            // cards are i+j+1, i+j+4, i+j+1, i+j+4
            int k;
            for (int j=0;j<4;j++) {
                if ((j%2) == 0) {
                    k = 1;
                } else {
                    k = 4;
                }
                assertEquals(i + (j % 2) + k, deck.poll().getValue());
            }
        }
    }

    // all players are assigned a left deck
    @Test
    public void TestAllPlayersAssignedLeftDeck()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        for (Player player : cardGame.getPlayers()) {
            assertNotNull(player.getLeftDeck());
        }
    }

    // all players are assigned a right deck
    @Test
    public void TestAllPlayersAssignedRightDeck()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
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

        // The rightwards decks will be the leftwards but with the first element at the end
        decksLeftwards.add(decksLeftwards.remove(0));

        for (int i=0;i<decksLeftwards.size();i++) {
            assertEquals(decksLeftwards.get(i), decksRightwards.get(i));
        }
    }

    // each player's left deck can't be the same as their right deck
    @Test
    public void TestPlayersLeftAndRightDecksAreDifferent()
            throws InvalidPackException, InvalidNumberOfPlayersException, IOException {
        CardGame cardGame = new CardGame(4, "test/testpacks/valid_pack.txt");
        cardGame.setupGame();

        for (Player player : cardGame.getPlayers()) {
            assertNotEquals(player.getLeftDeck(), player.getRightDeck());
        }
    }

    // Game with a possible ending, has a winner
    @Test
    public void TestWinnableGameHasWinner()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException, IOException {
        // Sets up and plays game
        CardGame cardGame = new CardGame(2, "test/testpacks/p1_wins_in_1_turn.txt");
        cardGame.setupGame();
        cardGame.playGame();

        // Join all threads
        for (Thread thread : cardGame.getPlayerThreads()) {
            thread.join();
        }
        // Assert player 1 has won
        assert(cardGame.getPlayers().get(0).hasWon());
    }


    // Game with initial winning hand, has a winner
    @Test
    public void TestInitialWinningHandPlayerWins()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException, IOException {
        // Setup game and play
        CardGame cardGame = new CardGame(2, "test/testpacks/pack_of_ones.txt");
        cardGame.setupGame();
        cardGame.playGame();

        // Join all threads
        for (Thread thread : cardGame.getPlayerThreads()) {
            thread.join();
        }
        assert(cardGame.getPlayers().get(0).hasWon());
    }

    // Player replaces entire hand
    @Test
    public void TestPlayerReplacesEntireHand()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException, IOException {
        // Setup game and play
        CardGame cardGame = new CardGame(2, "test/testpacks/replaces_full_hand_pack.txt");
        cardGame.setupGame();
        cardGame.playGame();

        // Join all threads
        for (Thread thread : cardGame.getPlayerThreads()) {
            thread.join();
        }

        // Checks player 1 has won with a final hand of 1 1 1 1
        Player player1 = cardGame.getPlayers().get(0);
        assert(player1.hasWon());
        for (Card card : player1.getHand()) {
            assertEquals(1, card.getValue());
        }
    }

    // Upper bound player wins (4)
    @Test
    public void TestUpperboundPlayerWins()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException, IOException {
        // Sets up and plays game
        CardGame cardGame = new CardGame(4, "test/testpacks/p4_wins.txt");
        cardGame.setupGame();
        cardGame.playGame();

        // Join all threads
        for (Thread thread : cardGame.getPlayerThreads()) {
            thread.join();
        }
        // Assert player 1 has won
        assert(cardGame.getPlayers().get(3).hasWon());
    }

    // Lots of players (32)
    @Test
    public void Test32Players()
            throws InvalidPackException, InvalidNumberOfPlayersException, InterruptedException, IOException {
        // Setup game and play
        CardGame cardGame = new CardGame(32, "test/testpacks/32_players.txt");
        cardGame.setupGame();
        cardGame.playGame();

        // Join all threads
        for (Thread thread : cardGame.getPlayerThreads()) {
            thread.join();
        }

        // Checks a player has won
        boolean someoneHasWon = false;
        for (Player player : cardGame.getPlayers()) {
            if(player.hasWon()){
                return;
            }
        }
        fail("No players have won a winnable game");
    }

    // User inputs valid number of players
    @Test
    public void TestValidNumberOfPlayersInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        // Fake input stream
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("4".getBytes());
        System.setIn(in);
        // Check 4 players are sent through
        int players = CardGame.UserInputPlayers();
        assertEquals(4,players);

        // Restore system in
        System.setIn(sysInBackup);
    }

    // User inputs valid number of players (lower bound)
    @Test
    public void TestValidNumberOfPlayersInputLowerBound()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        // Fake input stream
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes());
        System.setIn(in);
        // Check 2 players are sent through
        int players = CardGame.UserInputPlayers();
        assertEquals(2,players);

        // Restore system in
        System.setIn(sysInBackup);
    }

    // User inputs negative one number of players
    @Test
    public void TestNegativeOneNumberOfPlayersInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("-1"));
    }

    // User inputs negative number of players
    @Test
    public void TestNegativeNumberOfPlayersInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("-5"));
    }

    // User inputs 0 players
    @Test
    public void TestZeroPlayersInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("0"));
    }

    // User inputs 1 player
    @Test
    public void TestOnePlayerInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("1"));
    }

    // User inputs float
    @Test
    public void TestFloatNumberOfPlayerInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("1.1"));
    }

    // User inputs char
    @Test
    public void TestCharNumberOfPlayerInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("a"));
    }

    // User inputs string
    @Test
    public void TestStringNumberOfPlayerInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assertEquals(-1,CardGame.ValidateNumberOfPlayers("aaaa"));
    }

    // User inputs valid directory
    @Test
    public void TestValidDirectoryInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assert(CardGame.ValidateDirectory("test/testpacks/valid_pack.txt",4));
    }

    // User inputs directory that does not exist
    @Test
    public void TestDirectoryDoesNotExistInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assert(!CardGame.ValidateDirectory("invalidDirectory.txt",4));
    }

    // User inputs valid directory but invalid pack
    @Test
    public void TestInvalidDirectoryInput()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        assert(!CardGame.ValidateDirectory("test/testpacks/wrong_multiple.txt",4));
    }
}
