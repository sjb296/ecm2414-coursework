import java.io.*;

/**
 * Helper class for writing to output files.
 */
public class OutputFileHelper {
    /**
     * Writes the given string to the file objectName + '_output.txt'.
     *
     * @param objectName The name of the object that is being written to file.
     * @param output    The string to be written to file.
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void writeOutputFile(String objectName, String output)
            throws FileNotFoundException, UnsupportedEncodingException {
        String filename = objectName + "_output.txt";
        PrintWriter out = new PrintWriter(filename, "UTF-8");
        out.print(output);
        out.close();
    }

    /**
     * Returns the contents of the file objectName + '_output.txt'.
     *
     * @param objectName
     * @return The contents of the file.
     * @throws IOException
     */
    public static String readOutputFile(String objectName) throws IOException {
        String filename = objectName + "_output.txt";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        // Converts the contents of the file into a string
        String contents = "";
        try {
            String line;
            while ((line = br.readLine()) != null) {
                contents += line + "\n";
            }
            // Strip newline if not empty
            if (contents != "") {
                contents = contents.substring(0, contents.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }
        return contents;
    }
}
