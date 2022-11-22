/**
 * Objects of this class hold the number of the winning player (initially -1
 * because nobody has won yet) and are asked it by Players so that they
 * can tell if any other Player has won the game.
 */
public class WinHandler {
    private int winner = -1;

    public int getWinner() {
        return winner;
    }

    public void winner(final int playerNumber) {
        this.winner = playerNumber;
    }
}
