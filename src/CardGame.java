import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Sets up and runs the entire game.
 */
public class CardGame {
    private int numberOfPlayers;
    private String packFileName;
    private ArrayList<Player> players;
    private ArrayList<Thread> playerThreads;
    private ArrayList<Deck> decks;

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
        ArrayList<Card> cards = CardPackReader.readCardPack(this.packFileName, this.numberOfPlayers);
        // Create decks
        for (int i=0; i<this.numberOfPlayers; i++) {
            Deck deck = new Deck(this.numberOfPlayers, i+1);
            this.decks.add(deck);
        }
        // Starting at 4N, distribute cards round-robin to decks
        for (int i=(4*this.numberOfPlayers);i<cards.size();i++) {
            decks.get(i%this.numberOfPlayers).add(cards.get(i));
        }

        // Create players assigning their decks and hands
        for (int i=0;i<(this.numberOfPlayers-1);i++) {
            int rightDeckNumber = i + 1;
            // Hand distributed round-robin
            Card[] hand = new Card[]{
                    cards.get(i),
                    cards.get(i+this.numberOfPlayers),
                    cards.get(i+(2*this.numberOfPlayers)),
                    cards.get(i+(3*this.numberOfPlayers)),
            };
            // Creates players
            this.players.add(new Player(this.decks.get(i),
                                        this.decks.get(rightDeckNumber),
                                        hand,
                                        i+1));
        }
        // Decks and cards of the last player assigned outside of loop
        Card[] hand = new Card[]{
                cards.get(this.numberOfPlayers-1),
                cards.get((2*this.numberOfPlayers)-1),
                cards.get((3*this.numberOfPlayers)-1),
                cards.get((4*this.numberOfPlayers)-1),
        };
        // The last players right deck is the first deck
        Player lastPlayer = new Player(
                this.decks.get(this.numberOfPlayers-1),
                this.decks.get(0),
                hand,
                this.numberOfPlayers);
        this.players.add(lastPlayer);

        // Give each player all the other players for their list
        for (int i=0;i<this.numberOfPlayers;i++) {
            Player currentPlayer = this.players.get(i);
            this.players.remove(currentPlayer);

            // Now this player is gone add the others to his list
            currentPlayer.setOtherPlayers(this.players);

            // Put him back for the others
            this.players.add(i, currentPlayer);
        }

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
     * @param numberOfPlayers
     * @param packFileName
     * @throws InvalidNumberOfPlayersException
     */
    public CardGame(int numberOfPlayers, String packFileName)
            throws InvalidNumberOfPlayersException {
        if (numberOfPlayers > 1) {
            this.numberOfPlayers = numberOfPlayers;
        } else {
            throw new InvalidNumberOfPlayersException("Number of players must be two or more!");
        }
        this.players = new ArrayList<Player>();
        this.playerThreads = new ArrayList<Thread>();
        this.decks = new ArrayList<Deck>();
        this.packFileName = packFileName;
    }

    /**
     * Validates the number of players is 2 or more and integer.
     *
     * @param playersAsString
     * @return players or -1
     */
    public static int ValidateNumberOfPlayers(String playersAsString){
        try {
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
    public static boolean ValidateDirectory(String directory, int players){
        if (directory != null) {
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
        String playersAsString = "";
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
    public static String UserInputDirectory(int players) throws IOException {
        String directory = "";
        boolean validDirectory = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!validDirectory) {
            System.out.println("Please enter location of pack to load:");
            directory = reader.readLine();
            validDirectory = ValidateDirectory(directory,players);
        }
        return directory;
    }
}
