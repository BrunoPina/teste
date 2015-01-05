package br.com.tagme.gwt.commons.client.components;

import java.util.HashMap;
import java.util.Map;

public class GenericData {
	private Map<String, String>			data;

	public GenericData() {
		data = new HashMap<String, String>();
	}

	public void addValue(String key, String value) {
		data.put(key, value);
	}

	public String getValueAsString(String key) {
		return data.get(key);
	}
	
	public boolean containsKey(String key) {
		return data.containsKey(key);
	}
	
	public void clear() {
		data.clear();
	}
	
}