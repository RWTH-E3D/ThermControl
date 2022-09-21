/**
 * @author Henning Metzmacher <metzmacher@e3d.rwth-aachen.de>
 */
package config;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import parsers.XmlDocumentLoader;

public class Config {
	private static Document configXml;
	
	public static void init() {
		Config.configXml = (new XmlDocumentLoader()).loadXml(System.getProperty("user.dir") + "/config.xml");
	}
	
	public static String attribute(String element, String attribute) {
		Node node = Config.configXml.getElementsByTagName(element).item(0);
		NamedNodeMap nodeMap = node.getAttributes();
		return nodeMap.getNamedItem(attribute).getNodeValue();
	}
}