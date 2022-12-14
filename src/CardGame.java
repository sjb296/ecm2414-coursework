import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Sets up and runs the entire game.
 */
public class CardGame {
    private final int numberOfPlayers;
    private final String packDirectory;
    private final ArrayList<Player> players;
    private final ArrayList<Thread> playerThreads;
    private final ArrayList<Deck> decks;
    private final WinHandler winHandler;

    public WinHandler getWinHandler() {
        return winHandler;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Thread> getPlayerThreads() {
        return playerThreads;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    /**
     * Creates players, decks and threads and distributes cards round-robin to players and decks.
     *
     * @throws InvalidPackException
     * @throws IOException
     * @throws InvalidNumberOfPlayersException
     */
    public void setupGame()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        ArrayList<Card> cards = CardPackReader.readCardPack(this.packDirectory, this.numberOfPlayers);
        // Create decks
        for (int i = 0; i < this.numberOfPlayers; i++) {
            Deck deck = new Deck(this.numberOfPlayers, i + 1);
            this.decks.add(deck);
        }
        // Starting at 4N, distribute cards round-robin to decks
        for (int i = (4 * this.numberOfPlayers); i < cards.size(); i++) {
            decks.get(i % this.numberOfPlayers).add(cards.get(i));
        }

        // Create players assigning their decks and hands
        for (int i = 0; i < (this.numberOfPlayers - 1); i++) {
            int rightDeckNumber = i + 1;
            // Hand distributed round-robin
            Card[] hand = new Card[]{
                    cards.get(i),
                    cards.get(i + this.numberOfPlayers),
                    cards.get(i + (2 * this.numberOfPlayers)),
                    cards.get(i + (3 * this.numberOfPlayers)),
            };
            // Creates players
            this.players.add(new Player(this.decks.get(i),
                                        this.decks.get(rightDeckNumber),
                                        hand,
                                        i + 1, this.winHandler));
        }
        // Decks and cards of the last player assigned outside of loop
        Card[] hand = new Card[]{
                cards.get(this.numberOfPlayers - 1),
                cards.get((2 * this.numberOfPlayers) - 1),
                cards.get((3 * this.numberOfPlayers) - 1),
                cards.get((4 * this.numberOfPlayers) - 1),
        };
        // The last players right deck is the first deck
        Player lastPlayer = new Player(
                this.decks.get(this.numberOfPlayers - 1),
                this.decks.get(0),
                hand,
                this.numberOfPlayers, this.winHandler);
        this.players.add(lastPlayer);

        // Make threads & put in Array-list
        for (Player player : this.players) {
            this.playerThreads.add(new Thread(player));
        }
    }

    /**
     * Starts all the threads so the game can be played.
     */
    public void playGame() {
        // Start all the players
        for (Thread thread : this.playerThreads) {
            thread.start();
        }
    }

    /**
     * Constructor.
     *
     * @param amountOfPlayers
     * @param packFileDirectory
     * @throws InvalidNumberOfPlayersException
     */
    public CardGame(final int amountOfPlayers, final String packFileDirectory)
            throws InvalidNumberOfPlayersException {
        if (amountOfPlayers > 1) {
            this.numberOfPlayers = amountOfPlayers;
        } else {
            throw new InvalidNumberOfPlayersException("Number of players must be two or more!");
        }
        this.players = new ArrayList<Player>();
        this.playerThreads = new ArrayList<Thread>();
        this.decks = new ArrayList<Deck>();
        this.packDirectory = packFileDirectory;
        this.winHandler = new WinHandler();
    }

    /**
     * Validates the number of players is 2 or more and integer.
     *
     * @param playersAsString
     * @return players or -1 if invalid.
     */
    public static int ValidateNumberOfPlayers(final String playersAsString) {
        try {
            // Converts string to integer and checks it is 2 or more
            int players = Integer.parseInt(playersAsString);
            if (players > 1) {
                return players;
            }
        } catch (NumberFormatException e) {
            //pass
        }
        return -1;
    }

    /**
     * Validates the directory.
     *
     * @param directory
     * @param players
     * @return valid directory
     */
    public static boolean ValidateDirectory(final String directory, final int players) {
        if (directory != null) {
            // CardPackReader will throw an exception if the directory is invalid
            try {
                CardPackReader.readCardPack(directory, players);
                return true;
            } catch (InvalidPackException | InvalidNumberOfPlayersException | IOException e) {
                //pass
            }
        }
        return false;
    }

    /**
     * Loops through taking user input on number of players until its valid.
     *
     * @return players
     * @throws IOException
     */
    public static int UserInputPlayers() throws IOException {
        int players = -1;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String playersAsString;

        // Prompt user for number of players until valid input is given
        while (players == -1) {
            System.out.println("Please enter the number of players:");
            playersAsString = reader.readLine();
            players = ValidateNumberOfPlayers(playersAsString);
        }
        return players;
    }

    /**
     * Loops through taking user input on number of players until its valid.
     *
     * @param players
     * @return directory
     * @throws IOException
     */
    public static String UserInputDirectory(final int players) throws IOException {
        String directory = "";
        boolean validDirectory = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Prompt user for directory until valid input is given
        while (!validDirectory) {
            System.out.println("Please enter location of pack to load:");
            directory = reader.readLine();
            validDirectory = ValidateDirectory(directory, players);
        }
        return directory;
    }
}
