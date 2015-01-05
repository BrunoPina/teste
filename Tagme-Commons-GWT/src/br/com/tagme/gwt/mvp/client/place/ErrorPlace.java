package br.com.tagme.gwt.mvp.client.place;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.BreadcumbPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;

import com.google.gwt.place.shared.Place;

public class ErrorPlace extends BreadcumbPlace{
	
	public static final String PREFIX = "error";
	
	public ErrorPlace(String token){
		super(PREFIX, token, "Erro");
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new ErrorPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			return null;//core module handle this
		}

	} 
	
}