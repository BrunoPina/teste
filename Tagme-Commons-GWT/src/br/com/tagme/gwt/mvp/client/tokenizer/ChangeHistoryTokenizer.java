package br.com.tagme.gwt.mvp.client.tokenizer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import br.com.tagme.gwt.mvp.client.base.AbstractPlace;

import com.google.gwt.place.shared.Place;

public abstract class ChangeHistoryTokenizer extends AbstractTokenizer{
	
    @Override
    public String getToken(Place place) {
    	//TODO: google crawler, html snapshot https://developers.google.com/webmasters/ajax-crawling/docs/specification?hl=de
    	if(place instanceof AbstractPlace){
    		AbstractPlace defaultPlace = (AbstractPlace) place;
    		
    		String token = defaultPlace.getLocator();
    		HashMap<String, String> params = defaultPlace.getParams();
    		
    		if(params != null && !params.isEmpty()){
    			token += "?";
    			
    			for(Iterator<Entry<String, String>> ite = params.entrySet().iterator(); ite.hasNext();){
    				Entry<String,String> entry = ite.next();
    				token += entry.getKey() + "=" + entry.getValue();
    				if(ite.hasNext()){
    					token += "&";
    				}
    			}
    			
    		}
    		
    		return token;
    	}
    	
    	return null;
    }

}