package br.com.tagme.gwt.mvp.client.tokenizer;

import java.util.HashMap;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TokenizerFactory {
	
	private static final HashMap<String, AbstractTokenizer> tokenizersInstances = new HashMap<String, AbstractTokenizer>();
	
	public static void addTokenizer(String prefix, AbstractTokenizer tokenizer){
		tokenizersInstances.put(prefix, tokenizer);
	}
	
	public static PlaceTokenizer<Place> getTokenizer(String prefix){
		return tokenizersInstances.get(prefix);
	}
	
}