package br.com.tagme.gwt.mvp.client.activity;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.auth.Role;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;

import com.google.gwt.activity.shared.AbstractActivity;

public abstract class DefaultActivity extends AbstractActivity{
	
	private AbstractPlace activityContextPlace;
	
	public DefaultActivity(AbstractPlace activityContextPlace){
		this.activityContextPlace = activityContextPlace;
	}
	
	public String getContextPrefix(){
		return activityContextPlace == null ? null : activityContextPlace.getPrefix();
	}
	
	public String getContextLocator(){
		return activityContextPlace == null ? null : activityContextPlace.getLocator();
	}
	
	public HashMap<String, String> getContextParams(){
		return activityContextPlace == null ? null : activityContextPlace.getParams();
	}
	
	public AbstractPlace getContextPlace(){
		return activityContextPlace;
	}
	
	public boolean hasAuthority(Role requiredAccess){
		return AppAuth.getInstance().hasAuthority(requiredAccess);
	}
	
	public boolean hasAllAlthority(ArrayList<Role> requiredAccess){
		return AppAuth.getInstance().hasAllAuthority(requiredAccess);
	}
	
	public void goTo(AbstractPlace place){
		AppPlaceController.goTo(place);
	}
	
}