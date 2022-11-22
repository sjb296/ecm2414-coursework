public class WinHandler {
    private int winner = -1;

    public int getWinner() {
        return winner;
    }

    public void winner(final int playerNumber) {
        this.winner = playerNumber;
    }
}
