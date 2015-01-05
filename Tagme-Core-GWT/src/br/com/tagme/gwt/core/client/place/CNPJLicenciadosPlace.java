package br.com.tagme.gwt.core.client.place;

import java.util.ArrayList;

import br.com.tagme.gwt.commons.client.auth.Role;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.base.AuthBreadcumbPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.core.client.activity.InformacoesCadastraisActivity;

import com.google.gwt.place.shared.Place;

public class CNPJLicenciadosPlace extends AuthBreadcumbPlace{
	
	public static final String PREFIX = "cnpjlic";
	
	public CNPJLicenciadosPlace(String token){
		
		super(PREFIX, token, "CNPJs Licenciados");
		
		ArrayList<Role> requiredAuthorities = new ArrayList<Role>();
		requiredAuthorities.add(new Role("SKW", PREFIX, "_"));
		super.setRequiredAuthorities(requiredAuthorities);
		
		super.addPathItem(new IndexPlace("home"));
	}
	
	public static class Initializer extends ChangeHistoryTokenizer implements IActivityFactory{
		
		@Override
		public Place getPlace(String token) {
			return new CNPJLicenciadosPlace(token);
		}
		
		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof CNPJLicenciadosPlace){
				return new InformacoesCadastraisActivity((AuthBreadcumbPlace) targetPlace);
			}
			return null;
		}

	} 
	
}