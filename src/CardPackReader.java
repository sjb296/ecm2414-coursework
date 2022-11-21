import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardPackReader {
    /**
     * Reads a card pack from a file, creates cards and returns them in an ArrayList.
     *
     * @param filename The name of the pack file.
     * @return An ArrayList of Cards.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static ArrayList<Card> readCardPack(String filename, int players)
            throws IOException, FileNotFoundException, InvalidPackException,
            InvalidNumberOfPlayersException {
        // Check number of players is 2 or more
        if (players < 2) {
            throw new InvalidNumberOfPlayersException("Number of players must be at least 2");
        }

        // Ensures filetype is .txt
        if (!filename.substring(filename.length() - 4).equals(".txt")) {
            throw new InvalidPackException("Filetype must be .txt");
        }
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        ArrayList<Card> cards = new ArrayList<>();
        // Ensures pack is not empty
        if (!scanner.hasNextLine()) {
            throw new InvalidPackException("Pack file is empty");
        }
        while (scanner.hasNextLine()) {
            // Make sure each card is a non-negative integer
            try {
                Card card = new Card(Integer.parseInt(scanner.nextLine()));
                cards.add(card);
            } catch (NumberFormatException e) {
                throw new InvalidPackException("Number is not valid integer!");
            }
        }
        scanner.close();

        // Ensures pack has enough cards for the number of players
        if (!((cards.size() % (8 * players)) == 0)) {
            throw new InvalidPackException("Number of cards is not a multiple of 8 * players!");
        }

        return cards;
    }
}
