import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Player implements Runnable {
    private Deck leftDeck;
    private Deck rightDeck;
    private Card[] hand;    // 4 cards
    private int number;
    private int discardIndex = 0;
    private boolean victory = false;
    private String log = "";
    private ArrayList<Player> otherPlayers;

    public Deck getLeftDeck() {
        return leftDeck;
    }

    public Deck getRightDeck() {
        return rightDeck;
    }

    public Card[] getHand() {
        return hand;
    }

    public int getNumber() {
        return number;
    }

    public String getLog() {
        return log;
    }

    public void appendToLog(String s) {
        log += s;
    }

    public void win() {
        this.victory = true;
    }

    public boolean hasWon() {
        return this.victory;
    }

    public void addOtherPlayer(Player player) {
        otherPlayers.add(player);
    }

    public Player(Deck leftDeck, Deck rightDeck, Card[] hand, int number,
                  ArrayList<Player> otherPlayers) {
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.hand = hand;
        this.number = number;
        this.otherPlayers = otherPlayers;
    }

    public void writeOutputFile() throws FileNotFoundException, UnsupportedEncodingException {
        String filename = "player" + number + "_output.txt";

        PrintWriter out = new PrintWriter(filename, "UTF-8");
        out.print(this.log);
        out.close();
    }

    public void drawDiscard() {


        // TODO logging as we go
    }

    public int checkSomeoneElseHasWon() {
        for (Player player : this.otherPlayers) {
            if (player.hasWon()) {
                return player.getNumber();
            }
        }
        return -1;
    }

    public boolean checkSelfHasWon() {
        if (this.hand[0].getValue() == this.hand[1].getValue() &&
                this.hand[1].getValue() == this.hand[2].getValue() &&
                this.hand[2].getValue() == this.hand[3].getValue()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        while (true) {
            // check if someone else has won
            int winningPlayer = this.checkSomeoneElseHasWon();
            if (winningPlayer != -1) {
                this.appendToLog("player " + winningPlayer + " has informed player " + this.number + " that player " + winningPlayer + " has won");
                this.appendToLog("player " + this.number + " exits");
                this.appendToLog("player " + this.number + " final hand: " + this.hand[0] + " " + this.hand[1] + " " + this.hand[2] + " " + this.hand[3] + "\n");
                try {
                    this.writeOutputFile();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return;
            }

            // check if I've won
            if (this.checkSelfHasWon()) {
                this.appendToLog("player " + this.number + " has won");
                this.appendToLog("player " + this.number + " exits");
                this.appendToLog("player " + this.number + " final hand: " + this.hand[0] + " " + this.hand[1] + " " + this.hand[2] + " " + this.hand[3] + "\n");
                try {
                    this.writeOutputFile();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return;
            }

            // draw discard
            this.drawDiscard();
        }
    }
}
