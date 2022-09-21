package parsers;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ui.Console;

public class XmlDocumentLoader {
    public Document loadXml(String path) {
        File xmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document document;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(xmlFile);
            document.getDocumentElement().normalize();
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            e.printStackTrace(Console.err);
        } catch (SAXException e) {
            e.printStackTrace();
            e.printStackTrace(Console.err);
        } catch (IOException e) {
            e.printStackTrace();
            e.printStackTrace(Console.err);
        }
        
        return null;
    }
}