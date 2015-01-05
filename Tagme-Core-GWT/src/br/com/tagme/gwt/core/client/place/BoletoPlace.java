package br.com.tagme.gwt.core.client.place;

import java.util.ArrayList;

import br.com.tagme.gwt.commons.client.auth.Role;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.base.AuthBreadcumbPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activity.MovFinActivity;

import com.google.gwt.place.shared.Place;

public class BoletoPlace extends AuthBreadcumbPlace{
	
	public static final String PREFIX = "boleto";
	
	public BoletoPlace(String token){
		
		super(PREFIX, token, "2ª Via de Boleto");
		
		ArrayList<Role> requiredAuthorities = new ArrayList<Role>();
		requiredAuthorities.add(new Role("SKW", PREFIX, "_"));
		super.setRequiredAuthorities(requiredAuthorities);
		
		super.addPathItem(new IndexPlace("home"));
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new BoletoPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof BoletoPlace){
				return new MovFinActivity((AuthBreadcumbPlace) targetPlace);
			}
			return null;
		}

	} 
	
}