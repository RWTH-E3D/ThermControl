/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import ui.Console;

public class TextFileReader {
    public static String readTextFile(String fileName) {
        String text = "";
        try {
            FileReader reader = new FileReader(fileName);
            
            BufferedReader bReader = new BufferedReader(reader);
            String line;
            while ((line = bReader.readLine()) != null) {
                text = text + line + "\n";
            }
            bReader.close();
            
        } catch (IOException e) {
            Console.err.println(e);
        }
        
        return text;
    }
}
