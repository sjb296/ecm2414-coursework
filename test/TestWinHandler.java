import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestWinHandler {
    // Test winhandler initialises with -1
    @Test
    public void TestWinHandlerInitialises() {
        WinHandler winHandler = new WinHandler();
        assertEquals(-1, winHandler.getWinner());
    }

    // Test winhandler correctly sets the winner value
    @Test
    public void TestWinHandler() {
        WinHandler winHandler = new WinHandler();
        winHandler.winner(3);
        assertEquals(3, winHandler.getWinner());
    }
}
