package br.com.tagme.gwt.core.client.place;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.base.AuthBreadcumbPlace;
import br.com.tagme.gwt.mvp.client.base.AuthPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activity.IndexActivity;

import com.google.gwt.place.shared.Place;

public class IndexPlace extends AuthBreadcumbPlace{
	
	public static final String PREFIX = "index";
	
	public IndexPlace(String token){
		super(PREFIX, token, "Início");
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new IndexPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof IndexPlace){
				return new IndexActivity((AuthPlace) targetPlace);
			}
			return null;
		}

	} 
	
}