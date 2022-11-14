public class Player implements Runnable{
    private Deck leftDeck;
    private Deck rightDeck;
    private Card[] hand;    // 4 cards
    private int number;
    private int discardIndex = 0;

    public void drawDiscard() {

    }

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

    public boolean hasWon(){
        return true;
    }

    public Player(Deck leftDeck, Deck rightDeck, Card[] hand, int number) {
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.hand = hand;
        this.number = number;
    }

    @Override
    public void run() {
        //pass
    }
}
