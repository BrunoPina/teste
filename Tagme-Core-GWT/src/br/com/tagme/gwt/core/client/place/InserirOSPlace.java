package br.com.tagme.gwt.core.client.place;

import java.util.ArrayList;

import br.com.tagme.gwt.commons.client.auth.Role;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.base.AuthBreadcumbPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activity.IFrameActivity;

import com.google.gwt.place.shared.Place;

public class InserirOSPlace extends AuthBreadcumbPlace{
	
	public static final String PREFIX = "inseriros";
	
	public InserirOSPlace(String token){
		
		super(PREFIX, token, "Incluir Solicitação");
		
		ArrayList<Role> requiredAuthorities = new ArrayList<Role>();
		requiredAuthorities.add(new Role("SKW", PREFIX, "_"));
		super.setRequiredAuthorities(requiredAuthorities);
		
		super.addPathItem(new IndexPlace("home"));
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new InserirOSPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof InserirOSPlace){
				return new IFrameActivity((AuthBreadcumbPlace) targetPlace);
			}
			return null;
		}

	} 
	
}