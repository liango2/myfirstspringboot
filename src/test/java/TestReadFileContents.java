import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author liango
 * @version 1.0
 * @since 2017-12-25 1:10
 */
@RunWith(MockitoJUnitRunner.class)
public class TestReadFileContents {
    @Test
    public void testJava7() throws IOException {
//        In Java 7:
//
//        new String(Files.readAllBytes(...)) or Files.readAllLines(...)

        String content = new String(Files.readAllBytes(Paths.get("sample.txt")), "UTF-8");
    }


    @Test
    public void testJava8ReadFile() throws IOException {
//        In Java 8:
//
//        Files.lines(..).forEach(...)

//        Files.lines(Paths.get("sample.txt")).forEach(s ->).

    }


    @Test
    public void testGoodEnough() throws IOException {
//        My favorite way to read a small file is to use a BufferedReader and a StringBuilder.It is very simple and to
//        the point (though not particularly effective, but good enough for most cases):
        BufferedReader br = new BufferedReader(new FileReader("file.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
        } finally {
            br.close();
        }

    }


    @Test
    public void testGoodEnoughJava7() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
        }
    }

//    http://www.docjar.com/html/api/org/apache/commons/io/IOUtils.java.html
//
//    FileInputStream inputStream = new FileInputStream("foo.txt");
//try {
//        String everything = IOUtils.toString(inputStream);
//    } finally {
//        inputStream.close();
//    }
//    And even simpler with Java 7:
//
//            try(FileInputStream inputStream = new FileInputStream("foo.txt")) {
//        String everything = IOUtils.toString(inputStream);
//        // do something with everything string
//    }
}


