import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class TestOutputFileHelper {
    @Test
    public void TestReadFile() throws IOException {
        assertEquals("TEST 1\nTEST 2\nTEST 3", OutputFileHelper.readOutputFile("test/testpacks/test"));
    }

    @Test
    public void TestReadFileDoesNotExist() {
        try {
            String contents = OutputFileHelper.readOutputFile("AAAAAAA");
            fail("IOException should have been thrown");
        } catch (IOException e) {
            //pass
        }
    }

    @Test
    public void TestReadEmptyFile() {
        try {
            String contents = OutputFileHelper.readOutputFile("test/testpacks/empty");
            assertEquals("", contents);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void TestWriteOutputFile() {
        try {
            OutputFileHelper.writeOutputFile("test/testpacks/testwrite", "TEST");
            String contents = OutputFileHelper.readOutputFile("test/testpacks/testwrite");
            assertEquals("TEST", contents);
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}
