import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestCardPackReader {
    // test with completely valid pack file
    @Test
    public void TestValidPackFile() {
        try {
            ArrayList<Card> cards = CardPackReader.readCardPack("test/testpacks/valid_pack.txt", 4);
            int i = 0;
            for (Card card : cards) {
                i += 1;
                if (i == 9) {
                    i = 1;
                }
                assertEquals(i, card.getValue());
            }
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        }
    }

    // test with an image
    @Test
    public void TestInvalidFileType() {
        try {
            CardPackReader.readCardPack("test/testpacks/test_image.jpg",4);
            fail("InvalidPackException should be thrown");
        }catch (InvalidPackException e){
            // pass
        }catch (IOException e){
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        }
    }

    // test with nonexistent file
    @Test
    public void TestFileDoesNotExist() {
        try {
            CardPackReader.readCardPack("test/testpacks/nonexistent.txt", 4);
            fail("IOException should be thrown");
        } catch (IOException e) {
            // pass
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        }
    }

    // test with empty file
    @Test
    public void TestEmptyFile() {
        try {
            CardPackReader.readCardPack("test/testpacks/empty.txt", 4);
            fail("InvalidPackException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidPackException e) {
            // pass
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        }
    }

    // test with negative players
    @Test
    public void TestNegativePlayers() {
        try {
            CardPackReader.readCardPack("test/testpacks/valid_pack.txt", -1);
            fail("InvalidNumberOfPlayersException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            // pass
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }
    }

    // test with players = 0
    @Test
    public void TestNoPlayers() {
        try {
            CardPackReader.readCardPack("test/testpacks/valid_pack.txt", 0);
            fail("InvalidNumberOfPlayersException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            // pass
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }
    }

    // test with players = 1
    @Test
    public void TestOnePlayer() {
        try {
            CardPackReader.readCardPack("test/testpacks/valid_pack.txt", 1);
            fail("InvalidNumberOfPlayersException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            // pass
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }

    }

    // test with players = 2 (lower bound)
    @Test
    public void TestTwoPlayers() {
        try {
            CardPackReader.readCardPack("test/testpacks/valid_pack_2p.txt", 2);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }
    }

    // test with players = 3
    @Test
    public void TestThreePlayers() {
        try {
            CardPackReader.readCardPack("test/testpacks/valid_pack_3p.txt", 3);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }
    }

    // test number of rows not a multiple of 8 * number of players
    @Test
    public void TestWrongMultiple() {
        try {
            CardPackReader.readCardPack("test/testpacks/wrong_multiple.txt", 4);
            fail("InvalidPackException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // test file has negative number
    @Test
    public void TestNegativeNumber() {
        try {
            CardPackReader.readCardPack("test/testpacks/negative_number.txt", 4);
            fail("InvalidPackException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // test file has decimals
    @Test
    public void TestDecimalNumber() {
        try {
            CardPackReader.readCardPack("test/testpacks/decimal_number.txt", 4);
            fail("InvalidPackException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // test file has characters
    @Test
    public void TestCharacterNumber() {
        try {
            CardPackReader.readCardPack("test/testpacks/character_number.txt", 4);
            fail("InvalidPackException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // test file has multiple numbers per line
    @Test
    public void TestMultipleNumbers() {
        try {
            CardPackReader.readCardPack("test/testpacks/multiple_numbers.txt", 4);
            fail("InvalidPackException should be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            // pass
        }
    }

    // resultant arraylist is the same size as the number of rows in the file
    @Test
    public void TestArrayListSize() {
        try {
            ArrayList<Card> cards = CardPackReader.readCardPack("test/testpacks/valid_pack.txt", 4);
            assertEquals(32, cards.size());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }
    }

    // returns arraylist of Cards
    @Test
    public void TestArrayListOfCards() {
        try {
            ArrayList<Card> cards = CardPackReader.readCardPack("test/testpacks/valid_pack.txt", 4);
            for (Card card : cards) {
                assertTrue(card instanceof Card);
            }
        } catch (IOException e) {
            fail("IOException should not be thrown");
        } catch (InvalidNumberOfPlayersException e) {
            fail("InvalidNumberOfPlayersException should not be thrown");
        } catch (InvalidPackException e) {
            fail("InvalidPackException should not be thrown");
        }
    }
}
