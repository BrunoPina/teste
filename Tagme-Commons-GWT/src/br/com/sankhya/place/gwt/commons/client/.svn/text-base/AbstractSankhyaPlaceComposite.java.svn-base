package br.com.tagme.gwt.commons.client;

import java.util.ArrayList;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.auth.Role;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;

import com.google.gwt.user.client.ui.Composite;

public abstract class AbstractSankhyaPlaceComposite extends Composite{
	
	private DefaultActivity activity;
	
	protected AbstractSankhyaPlaceComposite(){
	}
	
	protected AbstractSankhyaPlaceComposite(DefaultActivity activity){
		this.activity = activity;
	}
	
	protected void setActivity(DefaultActivity activity){
		this.activity = activity;
	}
	
	protected DefaultActivity getActivity(){
		return activity;
	}
	
	public boolean hasAuthority(Role requiredAccess){
		return AppAuth.getInstance().hasAuthority(requiredAccess);
	}
	
	public boolean hasAllAuthority(ArrayList<Role> requiredAccess){
		return AppAuth.getInstance().hasAllAuthority(requiredAccess);
	}
	
	public void goTo(AbstractPlace place){
		AppPlaceController.goTo(place);
	}
	
}