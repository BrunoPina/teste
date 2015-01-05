package br.com.tagme.gwt.commons.utils.client;

import com.google.gwt.xml.client.CharacterData;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class XMLUtils {
	
	public static String getNodeValue(Node node) {
		StringBuffer value = new StringBuffer();
		
		if(node != null) {
			for(int i = 0; i < node.getChildNodes().getLength(); i++) {
				Node child = node.getChildNodes().item(i);
				short nodeType = child.getNodeType();
				if(nodeType == Node.TEXT_NODE) {
					value.append(child.getNodeValue());
				}else if(nodeType == Node.CDATA_SECTION_NODE){
					CharacterData cData = (CharacterData) child;
					value.append(cData.getData());
				}
			}
		}
		
		return value.toString();
	}
	
	public static void setNodeValue(Node node, String value) {
		if(node.getChildNodes().getLength() > 0) {
			node.normalize(); 
			node.getChildNodes().item(0).setNodeValue(value);
		}
	}
	
	public static boolean hasChild(Element source, String child) {
		return source.getElementsByTagName(child).getLength() > 0;
	}
	
	public static Element getFirstChild(Element source, String child) {
		NodeList nodes = source.getChildNodes();
		
		for(int i = 0; i < nodes.getLength(); i++) {
			if(nodes.item(i).getNodeName().equalsIgnoreCase(child)) {
				return (Element) nodes.item(i);
			}
		}
		
		return null;
	}
	
}
