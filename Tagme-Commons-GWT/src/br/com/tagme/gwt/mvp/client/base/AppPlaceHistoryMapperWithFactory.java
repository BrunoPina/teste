package br.com.tagme.gwt.mvp.client.base;

import br.com.tagme.gwt.mvp.client.place.ErrorPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.TokenizerFactory;

import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AppPlaceHistoryMapperWithFactory extends AbstractPlaceHistoryMapper<TokenizerFactory>{

	public AppPlaceHistoryMapperWithFactory(){
		
	}
	
	/**
	 * token.toString();
	 */
	@Override
	protected com.google.gwt.place.impl.AbstractPlaceHistoryMapper.PrefixAndToken getPrefixAndToken(Place newPlace) {
		return buildPrefixAndToken(newPlace);
	}

	/**
	 * PlaceTokenizer.getPlace();
	 */
	@Override
	protected PlaceTokenizer<Place> getTokenizer(String prefix) {
		PlaceTokenizer<Place> tokenizer = factory.getTokenizer(prefix);
		if(tokenizer == null && !"".equals(prefix)){
			tokenizer = factory.getTokenizer(ErrorPlace.PREFIX);
		}
		
		return tokenizer;
	}
	
	@Override
	public Place getPlace(String token) {
		//tenta pegar do tipo prefix:token, se nao houver ":" tenta buscar pela token direto
		Place place = super.getPlace(token);
		if(place == null){
			PlaceTokenizer<?> tokenizer = factory.getTokenizer(token);
			if (tokenizer != null) {
				place = tokenizer.getPlace(token);
		    }
		}
		return place;
	}
	
	public static PrefixAndToken buildPrefixAndToken(Place newPlace){
		if(newPlace != null && newPlace instanceof AbstractPlace){
			AbstractPlace defaultPlace = (AbstractPlace) newPlace;
			
			String prefix = defaultPlace.getPrefix();
			
			String token = TokenizerFactory.getTokenizer(prefix).getToken(defaultPlace);
			if(token == null){
				return null;
			}
			
			if(prefix.equals(token) || "home".equals(token)){
				token = prefix;
				prefix = "";
			}
			
			return new PrefixAndToken(prefix, token);
		}
		
		return null;
	}

}
