import java.util.ArrayList;

public class CardGame {
    private int numberOfPlayers;
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

    // Throw if bad pack
    public void validatePack(String packFileName)
            throws InvalidPackException {

    }

    public void setupGame() throws InvalidPackException {
    }

    public void playGame() {
    }

    public CardGame(int numberOfPlayers, String packFileName)
            throws InvalidNumberOfPlayersException, InvalidPackException {
        this.numberOfPlayers = numberOfPlayers;

        this.players = new ArrayList<Player>();
        // populate

        this.playerThreads = new ArrayList<Thread>();
        // make threads & put in here

        this.decks = new ArrayList<Deck>();
        // make decks & assign to correct players

        // do stuff with packFileName
    }

}
