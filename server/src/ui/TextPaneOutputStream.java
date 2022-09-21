/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package ui;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TextPaneOutputStream extends OutputStream {
    
    public static final int OUT = 0;
    public static final int ERR = 1;
    
    public static final int BUFFER_SIZE = 10000;
    
    private int type;
    private Style outStyle;
    private Style errStyle;
    private StyledDocument doc;
    JScrollBar vertical;
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private int numLines;
    private Semaphore mutex;
    private ExecutorService executor;
    
    public TextPaneOutputStream(JScrollPane scrollPane, JTextPane textPane, int type) {
        this.type = type;
        this.doc = textPane.getStyledDocument();
        this.textPane = textPane;
        this.scrollPane = scrollPane;
        this.numLines = 0;
        this.mutex = new Semaphore(1);
        
        executor = Executors.newFixedThreadPool(2);
        
        this.textPane.setFont(new Font("monospaced", Font.PLAIN, 12));
        
        // Prepare the out style:
        outStyle = this.doc.addStyle("out", null);
        StyleConstants.setForeground(outStyle, Color.BLACK);
        
        // Prepare the error style:
        errStyle = this.doc.addStyle("err", null);
        StyleConstants.setForeground(errStyle, Color.RED);
    }
    
    public void write(int c) throws IOException {
        Style style = outStyle;
        if (type == ERR) {
            style = errStyle;
        }
        StyledDocument doc = TextPaneOutputStream.this.doc;
        
        try {
            doc.insertString(doc.getLength(), String.valueOf((char) c), style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        insertUpdate();
    }
    
    private void insertUpdate() {
        StyledDocument doc = TextPaneOutputStream.this.doc; 
        Element root = doc.getDefaultRootElement();
        
        try {
            while (doc.getLength() > BUFFER_SIZE) {
                Element first = root.getElement(0);
                doc.remove(first.getStartOffset(), first.getEndOffset());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        // Set caret position after last letter:
        textPane.setCaretPosition(doc.getLength());
    }
}