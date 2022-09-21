/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package parsers;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import system.CommandExecutor;
import system.Message;
import system.data.DataAggregator;
import system.data.SystemDb;
import ui.Console;

public class XmlMessageParser {
    private Message requestMessage;
    private Message sendMessage;
    private CommandExecutor commandExecutor;
    private String protocolVersion = "1.0";
    
    public XmlMessageParser() {
    
    }
    
    public void parseXml(String xml) {
        this.requestMessage = new Message();
        this.sendMessage = new Message();
        this.commandExecutor = new CommandExecutor();
  
        DocumentBuilder documentBuilder;
        Document document;
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = docFactory.newDocumentBuilder();
           
            document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
            document.normalize();

            // Check if new version is requested:
            NamedNodeMap dataAttribs = document.getElementsByTagName("data").item(0).getAttributes();
            if (dataAttribs.getLength() > 0) {
                if (dataAttribs.getNamedItem("protocolVersion").getNodeValue().equals("2.0")) {
                	protocolVersion = "2.0";
                	parseProtocolVersion2(document);
                }
            } else { // No protocol version given, fall back to version 1.0
            	protocolVersion = "1.0";
            	parseProtocolVersion1(document);
            }
        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException  e) {
	        e.printStackTrace();
	    }
    }
    
    private void parseProtocolVersion1(Document document) {
    	Console.out.println("Parsing protocol version 1.0 ...");
    	
        NodeList sends = document.getElementsByTagName("send");
        NodeList requests = document.getElementsByTagName("request");
        
        if (sends.getLength() > 0) {
            for (int i = 0; i < sends.item(0).getChildNodes().getLength(); i++) {
                Node node = sends.item(0).getChildNodes().item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals("command")) {
                        // Command node:
                        this.parseCommandV1(node);
                    } else if (node.getNodeName().equals("signal")) {
                        // Signal node:
                        this.sendMessage.addSignal(signalFromNodeV1(node));
                    }
                }
            }
        }
        
        if (requests.getLength() > 0) {
        	NamedNodeMap attributes = requests.item(0).getAttributes();
        	Node all = attributes.getNamedItem("all");
        	
        	if (all != null) {
        		if (all.getNodeValue().equals("true")) {
        			// User requested all signals:
        			for (Map.Entry<String, LinkedHashMap<String, String>> entry : DataAggregator.dataContainer.getCurrentSignals().entrySet()) {
        				this.requestMessage.addSignal(entry.getValue()); // Copy the signal to the request message
        			}
        		}
        	} else {
        		// Add requested signals:
                for (int i = 0; i < requests.item(0).getChildNodes().getLength(); i++) {
                    Node node = requests.item(0).getChildNodes().item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        this.requestMessage.addSignal(signalFromNodeV1(node));
                    }                
                }
        	}
        }
    }
    
    private void parseProtocolVersion2(Document document) {
    	Console.out.println("Parsing protocol version 2.0 ...");
    	
    	NodeList sendNodes = document.getElementsByTagName("send");
        NodeList requestNodes = document.getElementsByTagName("request");
        
        // Parse sends:
        if (sendNodes.getLength() > 0) {
        	NodeList sendChildren = sendNodes.item(0).getChildNodes();
        	for (int i = 0; i < sendChildren.getLength(); i++) {
        		// Parse signals:
        		if (sendChildren.item(i).getNodeName().equals("signal")) {
        			sendMessage.addSignal(signalFromNodeV2(sendChildren.item(i)));
        		}
        		
        		// Parse commands:
        		if (sendChildren.item(i).getNodeName().equals("command")) {
        			parseCommandV2(sendChildren.item(i));
        		}
        	}
        }
        
        // Parse requests:
        if (requestNodes.getLength() > 0) {
        	NamedNodeMap attributes = requestNodes.item(0).getAttributes();
        	Node all = attributes.getNamedItem("all");
        	
        	if (all != null) {
        		if (all.getNodeValue().equals("true")) {
        			// User requested all signals:
        			for (Map.Entry<String, LinkedHashMap<String, String>> entry : DataAggregator.dataContainer.getCurrentSignals().entrySet()) {
        				this.requestMessage.addSignal(entry.getValue()); // Copy the signal to the request message
        			}
        		}
        	} else {
            	NodeList requestChildren = requestNodes.item(0).getChildNodes();
            	for (int i = 0; i < requestChildren.getLength(); i++) {
            		if (requestChildren.item(i).getNodeName().equals("signal")) {
            			requestMessage.addSignal(signalFromNodeV2(requestChildren.item(i)));
            		}
            	}
        	}
        }
    }
    
    private LinkedHashMap<String, String> signalFromNodeV2(Node node) {
    	LinkedHashMap<String, String> signal = new LinkedHashMap<String, String>();
    	NamedNodeMap attribs = node.getAttributes();
    	for (int i = 0; i < attribs.getLength(); i++) {
    		signal.put(attribs.item(i).getNodeName(), attribs.item(i).getNodeValue());
    	}
    	return signal;
    }
    
    private void parseCommandV2(Node node) {
    	HashMap<String, String> command = new HashMap<String, String>();
    	NamedNodeMap attribs = node.getAttributes();
    	for (int i = 0; i < attribs.getLength(); i++) {
    		command.put(attribs.item(i).getNodeName(), attribs.item(i).getNodeValue());
    	}
    	commandExecutor.executeCommand(command);
    }
    
    private void parseCommandV1(Node node) {
        HashMap<String, String> command = new HashMap<String, String>();
        NodeList childNodes = node.getChildNodes(); 
        
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String name = childNode.getNodeName();
                String value = childNode.getTextContent().trim();
                command.put(name, value);
            }
        }
        
        commandExecutor.executeCommand(command);
    }
    
    private LinkedHashMap<String, String> signalFromNodeV1(Node node) {
        NodeList childNodes = node.getChildNodes();
        LinkedHashMap<String, String> signal = new LinkedHashMap<String, String>();
        
        // Parse signal body:
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                String name = childNode.getNodeName();
                String value = childNode.getTextContent().trim();
                signal.put(name, value);
            }
        }
        return signal;
    }
    
    public String xmlFromSignalList(ArrayList<LinkedHashMap<String, String>> signals) {
    	ArrayList<String> xml = new ArrayList<String>();
    	
    	if (protocolVersion.equals("1.0")) {
            // Create response string:
            xml.add("<?xml version=\"1.0\" ?>");
            xml.add("<data>");
            xml.add("    <send>");
            
            for (HashMap<String, String> requestedSignal : signals) {
                LinkedHashMap<String, String> signal = DataAggregator.dataContainer.getCurrentSignals().get(requestedSignal.get("name"));
                
                if (signal != null) {
                    String name = signal.get("name");
                    String value = signal.get("value");
                    xml.add("        <signal>");
                    xml.add("            <name>" +  ((name == null) ? "" : name) + "</name>");
                    xml.add("            <value>" + ((value == null) ? "" : value) + "</value>");
                    xml.add("        </signal>");
                }
            }
            xml.add("    </send>");
            xml.add("</data>");
    	} else if (protocolVersion.equals("2.0")) {
    		xml.add("<?xml version=\"1.0\" ?>");
            xml.add("<data protocolVersion=\"2.0\">");
            xml.add("    <send>");
            
            for (HashMap<String, String> requestedSignal : signals) {
                LinkedHashMap<String, String> signal = DataAggregator.dataContainer.getCurrentSignals().get(requestedSignal.get("name"));
                
                if (signal != null) {
                    String name = signal.get("name");
                    String value = signal.get("value");
                    xml.add("        <signal name=\"" + ((name == null) ? "" : name) + "\" value=\"" + ((value == null) ? "" : value) + "\" />");
                }
            }
            
            
            xml.add("    </send>");
            xml.add("</data>");
    	}
    	
    	// Concatenate the XML string:
    	String xmlString = "";
    	for (String s : xml) {
    		xmlString = xmlString + s + "\n";
    	}
    	
    	return xmlString;
    }

    public Message getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(Message requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Message getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Message sendMessage) {
        this.sendMessage = sendMessage;
    }
}