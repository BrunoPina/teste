package br.com.tagme.gwt.commons.client.xml;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class MetadataParser {
	
	public static Map<String, MetadataField> parse(Node metadata){
		
		Map<String, MetadataField> mapMetadataFields = new LinkedHashMap<String, MetadataField>();
		
		NodeList metadataFields = metadata.getChildNodes();

		for (int i = 0; i < metadataFields.getLength(); i++) {
			if (i % 2 == 0) {
				i = i + 1;
			}
			Node field = metadataFields.item(i);
			if (field != null) {
				if (field.getNodeType() == Node.ELEMENT_NODE) {
					Element fieldElem = (Element) field;
					String fieldName = fieldElem.getAttribute("fieldName");
					mapMetadataFields.put(fieldName, new MetadataField(fieldName, fieldElem.getAttribute("label"), fieldElem.getAttribute("visible")));
				}
			}
		}
		return mapMetadataFields;
	}
	
public static class MetadataField{
		
		private String fieldName;
		private String label;
		private String visible;
		
		public MetadataField(String fieldName, String label, String visible){
			this.fieldName = fieldName;
			this.label = label;
			this.visible = visible;
		}
		
		public String getFieldName() {
			return fieldName;
		}

		public String getLabel() {
			return label;
		}

		public boolean isVisible() {
			return Boolean.parseBoolean(visible);
		}
		
	}

}
