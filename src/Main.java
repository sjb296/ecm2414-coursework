import java.io.IOException;

public class Main {
    /**
     * Takes user input and uses it to construct an instance of CardGame and play the game.
     *
     * @param args
     * @throws IOException
     * @throws InvalidPackException
     * @throws InvalidNumberOfPlayersException
     */
    public static void main(final String[] args)
            throws IOException, InvalidPackException, InvalidNumberOfPlayersException {
        int players = CardGame.UserInputPlayers();
        String directory = CardGame.UserInputDirectory(players);
        CardGame cardGame = new CardGame(players, directory);
        cardGame.setupGame();
        cardGame.playGame();
    }
}
