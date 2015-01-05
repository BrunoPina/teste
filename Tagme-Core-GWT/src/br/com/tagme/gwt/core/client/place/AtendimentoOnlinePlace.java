package br.com.tagme.gwt.core.client.place;

import java.util.ArrayList;

import br.com.tagme.gwt.commons.client.auth.Role;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.base.AuthBreadcumbPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activity.AtendimentoOnlineActivity;

import com.google.gwt.place.shared.Place;

public class AtendimentoOnlinePlace extends AuthBreadcumbPlace{
	
	public static final String PREFIX = "chatonline";
	
	public AtendimentoOnlinePlace(String token){
		
		super(PREFIX, token, "Atendimento Online");
		
		ArrayList<Role> requiredAuthorities = new ArrayList<Role>();
		requiredAuthorities.add(new Role("SKW", PREFIX, "_"));
		super.setRequiredAuthorities(requiredAuthorities);
		
		super.addPathItem(new IndexPlace("home"));
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new AtendimentoOnlinePlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof AtendimentoOnlinePlace){
				return new AtendimentoOnlineActivity((AuthBreadcumbPlace) targetPlace);
			}
			return null;
		}

	} 
	
}