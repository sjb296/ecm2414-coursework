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
    private ArrayList<Player> otherPlayers = new ArrayList<>();

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
        System.out.println("player " + number + " wins");
    }

    public boolean hasWon() {
        return this.victory;
    }

    public void setOtherPlayers(ArrayList<Player> otherPlayers) {
        this.otherPlayers= otherPlayers;
    }

    public Player(Deck leftDeck, Deck rightDeck, Card[] hand, int number) {
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.hand = hand;
        this.number = number;
        this.log = "player " + this.number + " initial hand " + hand[0].getValue() + " " + hand[1].getValue() + " " + hand[2].getValue() + " " + hand[3].getValue() + "\n";
    }

    public void writeOutputFile() throws FileNotFoundException, UnsupportedEncodingException {
        OutputFileHelper.writeOutputFile("player" + number, log);
    }

    // draw and discard from the left deck and discard non preferred card to the right deck
    public void drawDiscard() {
        Card pickedCard;
        // if left deck is empty skip go
        if ((leftDeck.peek() != null) && !(rightDeck.isFull())) {
            pickedCard = leftDeck.poll();
            Card selectedCard = hand[discardIndex];
            for (int i=0; i < 4; i++) {
                selectedCard = this.hand[discardIndex];
                if (selectedCard.getValue() == this.number) {
                    discardIndex++;
                    if (discardIndex > 3) {
                        discardIndex = 0;
                    }
                }else {
                    rightDeck.add(selectedCard);
                    this.hand[discardIndex] = pickedCard;
                    break;
                }
            }
            // loops through all 4 cards so a non preferred card isn't kept indefinitely
            discardIndex++;
            if (discardIndex > 3) {
                discardIndex = 0;
            }
            this.log += "player " + this.number + " draws a " + pickedCard.getValue() + " from deck " + this.leftDeck.getNumber() + "\nplayer " + this.number + " discards a " + selectedCard.getValue() + " to deck " + this.rightDeck.getNumber() + "\n" + "player " + this.number + " current hand is " + this.hand[0].getValue() + " " + this.hand[1].getValue() + " " + this.hand[2].getValue() + " " + this.hand[3].getValue() + "\n";
        }
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
            this.win();
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
                this.appendToLog("player " + winningPlayer + " has informed player " + this.number + " that player " + winningPlayer + " has won" + "\n");
                this.appendToLog("player " + this.number + " exits" + "\n");
                this.appendToLog("player " + this.number + " final hand " + this.hand[0] + " " + this.hand[1] + " " + this.hand[2] + " " + this.hand[3] + "\n");
                try {
                    this.writeOutputFile();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return;
            }

            // check if I've won
            if (this.checkSelfHasWon()) {
                this.appendToLog("player " + this.number + " wins" + "\n");
                this.appendToLog("player " + this.number + " exits" + "\n");
                this.appendToLog("player " + this.number + " final hand " + this.hand[0] + " " + this.hand[1] + " " + this.hand[2] + " " + this.hand[3] + "\n");
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
