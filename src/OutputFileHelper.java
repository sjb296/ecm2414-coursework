import java.io.*;

public class OutputFileHelper {
    public static void writeOutputFile(String objectName, String output)
            throws FileNotFoundException, UnsupportedEncodingException {
        String filename = objectName + "_output.txt";

        PrintWriter out = new PrintWriter(filename, "UTF-8");
        out.print(output);
        out.close();
    }

    public static String readOutputFile(String objectName) throws IOException {
        String filename = objectName + "_output.txt";

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String contents = "";
        try {
            String line = "";
            while ((line = br.readLine()) != null) {
                contents += line + "\n";
            }
            // strip newline if not empty
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
