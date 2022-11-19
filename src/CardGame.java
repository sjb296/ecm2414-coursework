import java.io.IOException;
import java.util.ArrayList;

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

    // Throw if bad pack
    public void validatePack(String packFileName)
            throws InvalidPackException {

    }

    public void setupGame()
            throws InvalidPackException, IOException, InvalidNumberOfPlayersException {
        ArrayList<Card> cards = CardPackReader.readCardPack(this.packFileName, this.numberOfPlayers);
        // populate players & threads & decks
        for (int i=0; i<this.numberOfPlayers; i++) {
            Deck deck = new Deck(this.numberOfPlayers, i+1);
            this.decks.add(deck);
        }
        // give deck cards
        for (int i=(4*this.numberOfPlayers);i<cards.size();i++) {
            decks.get(i%this.numberOfPlayers).add(cards.get(i));
        }

        // give players decks
        for (int i=0;i<(this.numberOfPlayers-1);i++) {
            int rightDeckNumber = i + 1;
            Card[] hand = new Card[]{
                    cards.get(i),
                    cards.get(i+this.numberOfPlayers),
                    cards.get(i+(2*this.numberOfPlayers)),
                    cards.get(i+(3*this.numberOfPlayers)),
            };
            this.players.add(new Player(this.decks.get(i),
                                        this.decks.get(rightDeckNumber),
                                        hand,
                                        i+1));
        }
        // do the last player manually
        Card[] hand = new Card[]{
                cards.get(this.numberOfPlayers-1),
                cards.get((2*this.numberOfPlayers)-1),
                cards.get((3*this.numberOfPlayers)-1),
                cards.get((4*this.numberOfPlayers)-1),
        };
        Player lastPlayer =new Player(
                this.decks.get(this.numberOfPlayers-1),
                this.decks.get(0),
                hand,
                this.numberOfPlayers);
        this.players.add(lastPlayer);

        // give each player all the other players for their list
        for (int i=0;i<this.numberOfPlayers;i++) {
            Player currentPlayer = this.players.get(i);
            this.players.remove(currentPlayer);

            // now this player is gone add the others to his list
            currentPlayer.setOtherPlayers(this.players);

            // put him back for the others
            this.players.add(i, currentPlayer);
        }

        // make threads & put in list
        for (Player player : this.players) {
            this.playerThreads.add(new Thread(player));
        }
    }

    public void playGame() {
        // start all the players
        for (Thread thread : this.playerThreads) {
            thread.start();
        }
    }

    public CardGame(int numberOfPlayers, String packFileName)
            throws InvalidNumberOfPlayersException, InvalidPackException {
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
}
