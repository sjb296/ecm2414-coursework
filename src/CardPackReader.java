import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CardPackReader {
    /**
     * Reads a card pack from a file.
     * @param filename The name of the pack file.
     * @return A cards of integers of the cards in the pack file.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static ArrayList<Card> readCardPack(String filename, int players)
            throws IOException, FileNotFoundException, InvalidPackException,
            InvalidNumberOfPlayersException {
        // Check number of players
        if (players < 2) {
            throw new InvalidNumberOfPlayersException("Number of players must be at least 2");
        }

        // Read the file
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        ArrayList<Card> cards = new ArrayList<>();
        if (!scanner.hasNextLine()) {
            throw new InvalidPackException("Pack file is empty");
        }
        while (scanner.hasNextLine()) {
            // Make sure each one is non-negative
            try {
                Card card = new Card(Integer.parseInt(scanner.nextLine()));
                cards.add(card);
            } catch (NumberFormatException e) {
                throw new InvalidPackException("Number is not valid integer!");
            }
        }
        scanner.close();

        if (!((cards.size() % (8 * players)) == 0)) {
            throw new InvalidPackException("Number of cards is not a multiple of 8 * players!");
        }

        return cards;
    }

    /*
    public static void main(String[] args) throws IOException, InvalidPackException {
        int[] ints =  CardPackReader.readCardPack("sample_pack_1.txt");
        System.out.println(Arrays.toString(ints));
    }

     */
}
