/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui;

import java.awt.Color;
import java.io.PrintStream;
import java.util.Arrays;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Console {
    public static PrintStream out;
    public static PrintStream err;
    public static JTextPane console;
    public static JScrollPane consoleScrollPane;
    
    private static StyledDocument doc;
    
    public static void init() {
        console = new JTextPane();
        consoleScrollPane = new JScrollPane(console);
        
        doc = Console.console.getStyledDocument();
        out = new PrintStream(new TextPaneOutputStream(consoleScrollPane, console, TextPaneOutputStream.OUT));
        err = new PrintStream(new TextPaneOutputStream(consoleScrollPane, console, TextPaneOutputStream.ERR));
    }
    
    public static String formatException(Exception e) {
        String s = "1 ";
        s += e;
        s += "\n2 ";
        s += e.getMessage();
        s += "\n3 ";
        s += e.getLocalizedMessage();
        s += "\n4 ";
        s += e.getCause();
        s += "\n5 ";
        s += Arrays.toString(e.getStackTrace());
        s += "\n6 ";
        s += e.getStackTrace();
        return s;
    }
}