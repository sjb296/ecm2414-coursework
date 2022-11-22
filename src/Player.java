import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * The functionalities the player needs to play the game that is run on a thread.
 */
public class Player implements Runnable {
    private Deck leftDeck;
    private Deck rightDeck;
    private Card[] hand;    // 4 cards
    private int number;
    private int discardIndex = 0;
    private boolean victory = false;
    private String log = "";
    private ArrayList<Player> otherPlayers = new ArrayList<>();
    private WinHandler winHandler;

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

    /**
     * Appends each action to the log needed to be written to the output file.
     *
     * @param s The log message.
     */
    public void appendToLog(String s) {
        log += s;
    }

    /**
     * Sets its own victory condition to true and prints to screen it has won.
     */
    public void win() {
        this.winHandler.winner(this.number);
        System.out.println("player " + this.number + " wins");
    }

    /**
     * Check if the player has won.
     *
     * @return True if the player has won.
     */
    public boolean hasWon() {
        return this.number == this.winHandler.getWinner();
    }

    public void setOtherPlayers(final ArrayList<Player> players) {
        this.otherPlayers = players;
    }

    /**
     * Constructor.
     *
     * @param playerLeftDeck
     * @param playerRightDeck
     * @param playerHand
     * @param playerNumber
     */
    public Player(final Deck playerLeftDeck, final Deck playerRightDeck, final Card[] playerHand,
            final int playerNumber, final WinHandler playerWinHandler) {
        this.leftDeck = playerLeftDeck;
        this.rightDeck = playerRightDeck;
        this.hand = playerHand;
        this.number = playerNumber;
        this.winHandler = playerWinHandler;
        this.log = "player " + this.number + " initial hand " + playerHand[0].getValue() + " "
                + playerHand[1].getValue() + " " + playerHand[2].getValue() + " "
                + playerHand[3].getValue() + "\n";
    }

    /**
     * Writes the player's log to its output file.
     *
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void writeOutputFile() throws FileNotFoundException, UnsupportedEncodingException {
        OutputFileHelper.writeOutputFile("player" + number, log);
        this.leftDeck.writeToFile();
    }

    /**
     * Draw and discard from the left deck and discard non preferred card to the right deck.
     */
    public void drawDiscard() {
        Card pickedCard;
        // If left deck is empty or the right deck is full it will not attempt a draw and discard
        if ((leftDeck.peek() != null) && !(rightDeck.isFull())) {
            pickedCard = leftDeck.poll();
            Card selectedCard = hand[discardIndex];
            // If the picked card is non preferred it will be discarded to the right deck
            for (int i = 0; i < 4; i++) {
                selectedCard = this.hand[discardIndex];
                if (selectedCard.getValue() == this.number) {
                    discardIndex++;
                    if (discardIndex > 3) {
                        discardIndex = 0;
                    }
                } else {
                    rightDeck.add(selectedCard);
                    this.hand[discardIndex] = pickedCard;
                    break;
                }
            }
            // Loops through all 4 cards so a non preferred card isn't kept indefinitely
            discardIndex++;
            if (discardIndex > 3) {
                discardIndex = 0;
            }
            // Appends the log message
            this.log += "player " + this.number + " draws a " + pickedCard.getValue() + " from deck " + this.leftDeck.getNumber() + "\nplayer " + this.number + " discards a " + selectedCard.getValue() + " to deck " + this.rightDeck.getNumber() + "\n" + "player " + this.number + " current hand is " + this.hand[0].getValue() + " " + this.hand[1].getValue() + " " + this.hand[2].getValue() + " " + this.hand[3].getValue() + "\n";
        }
    }

    /**
     * Checks all other players to see if they have won.
     *
     * @return winning players number or -1 if no one has won.
     */
    public int checkSomeoneElseHasWon() {
        return this.winHandler.getWinner();
    }

    /**
     * Compares all the values in its hand to see if the winning condition has been met.
     *
     * @return True if all cards in their hand have the same value.
     */
    public boolean checkSelfHasWon() {
        // If all values in the hand are the same, the player wins the game
        if (this.hand[0].getValue() == this.hand[1].getValue()
                && this.hand[1].getValue() == this.hand[2].getValue()
                && this.hand[2].getValue() == this.hand[3].getValue()) {
            this.win();
            return true;
        } else {
            return false;
        }
    }

    /**
     * The thread runs a loop that checks if another player has won, then if its player has won and
     * finally draws and discard if there are no winners.
     */
    @Override
    public void run() {
        while (true) {
            // Check if someone else has won
            int winningPlayer = this.checkSomeoneElseHasWon();
            if (winningPlayer != -1) {
                // Appends the final log message and writes the output file
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

            // Check if I've won
            if (this.checkSelfHasWon()) {
                // Appends the final log message and writes the output file
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

            // Draw & discard
            this.drawDiscard();
        }
    }
}
