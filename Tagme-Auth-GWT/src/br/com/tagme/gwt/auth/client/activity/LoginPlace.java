package br.com.tagme.gwt.auth.client.activity;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.DefaultPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;

import com.google.gwt.place.shared.Place;

public class LoginPlace extends DefaultPlace{
	
	public static final String PREFIX = "login";
	
	public LoginPlace(String token){
		super(PREFIX, token);
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new LoginPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof LoginPlace){
				return new LoginActivity(targetPlace);
			}
			return null;
		}

	} 
	
}