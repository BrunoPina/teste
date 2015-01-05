package br.com.tagme.gwt.commons.client.auth;

import java.util.ArrayList;

import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.ActivityController;
import br.com.tagme.gwt.mvp.client.base.AuthPlace;
import br.com.tagme.gwt.mvp.client.base.DefaultPlace;
import br.com.tagme.gwt.mvp.client.place.RedirectPlace;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;

public class AppAuth implements HasHandlers{

	private static AppAuth 			INSTANCE = new AppAuth(); 
	private static HandlerManager 	handlerManager;
	
	private boolean 			isAuthenticated = false;
	private String				name;
	private String 				username;
	private ArrayList<Role> 	authorities = new ArrayList<Role>();
	
	private String 				swSessionId;
	
	private AppAuth(){
		handlerManager = new HandlerManager(this);
	}
	
	public static AppAuth getInstance(){
		return INSTANCE;
	}
	
	private static void setInstance(boolean isAuthenticated, String name, String username, ArrayList<Role> grantedAuthorities){
		INSTANCE.setAuthenticated(isAuthenticated);
		INSTANCE.setName(name);
		INSTANCE.setUsername(username);
		INSTANCE.setAuthorities(grantedAuthorities);
	}
	
	private void setLoginSuccess(String name, String username, ArrayList<Role> grantedAuthorities){
		setInstance(true, name, username, grantedAuthorities);
		
		int eventType = AppAuthEvent.TYPE_LOGIN_SUCCESS;
		AppAuthEvent event = new AppAuthEvent(eventType, null);
		INSTANCE.fireEvent(event);
	}
	
	private void setLoginFail(String message){
		setInstance(false, null, null, null);
		
		int eventType = AppAuthEvent.TYPE_LOGIN_FAIL;
		AppAuthEvent event = new AppAuthEvent(eventType, message);
		INSTANCE.fireEvent(event);
	}
	
	private void setLoginVerified(String name, String username, ArrayList<Role> grantedAuthorities){
		setInstance(true, name, username, grantedAuthorities);
		
		int eventType = AppAuthEvent.TYPE_LOGIN_VERIFIED;
		AppAuthEvent event = new AppAuthEvent(eventType, null);
		INSTANCE.fireEvent(event);
	}
	
	public void setLoginNotVerified(String message){
		setInstance(false, null, null, null);
		
		int eventType = AppAuthEvent.TYPE_LOGIN_NOT_VERIFIED;
		AppAuthEvent event = new AppAuthEvent(eventType, message);
		INSTANCE.fireEvent(event);
	}
	
	private void setLoginError(String message){
		setInstance(false, null, null, null);
		
		int eventType = AppAuthEvent.TYPE_ERROR;
		AppAuthEvent event = new AppAuthEvent(eventType, message);
		INSTANCE.fireEvent(event);
	}
	
	private void setAuthorities(ArrayList<Role> grantedAuthorities){
		authorities = grantedAuthorities;
	}
	
	private void setAuthenticated(boolean isAuthenticated){
		this.isAuthenticated = isAuthenticated;
	}
	
	public boolean isAuthenticated(){
		return isAuthenticated;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void fireInfosChangeEvent(){
		int eventType = AppAuthEvent.TYPE_INFOS_CHANGED;
		AppAuthEvent event = new AppAuthEvent(eventType, null);
		INSTANCE.fireEvent(event);
	}
	
	private void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	/*public void goToPartialLoginPlace(AbstractPlace previousTargetPlace){
		this.previousTargetPlace = previousTargetPlace;
		AppPlaceController.goTo(new PartialLoginPlace("home"));
	}*/
	
	public boolean hasAuthority(String authorityToVerify) {
		if(!isAuthenticated()){
			return false;
		}

		for (Role authority : authorities) {
			if (authority.implies(new Role(authorityToVerify))) {
				return true;
			}
		}

		return false;
	}
	
	public boolean hasAuthority(Role authorityToVerify) {
		return hasAuthority(authorityToVerify.toString());
	}
	
	public boolean hasAllAuthority(ArrayList<Role> authoritiesToVerify){
		for(Role authorityToVerivy : authoritiesToVerify){
			boolean authorized = hasAuthority(authorityToVerivy);
			if(!authorized){
				return false;
			}
		}
		return true;
	}
	
	public String getSWSession(){
		return this.swSessionId;
	}
	
	public void tryAuth(String authcAuthzString){
		
		String arrAuthcAuthz[] = authcAuthzString.trim().split(";");
		String status = arrAuthcAuthz[0];
		String usernameOrErrorMsg = null;
		String name = null;
		ArrayList<Role> grantedAuthorities = new ArrayList<Role>();
		
		if(arrAuthcAuthz.length > 1){
			usernameOrErrorMsg = arrAuthcAuthz[1];
		}
		
		if(arrAuthcAuthz.length > 2){
			name = arrAuthcAuthz[2];
		}
		
		if(arrAuthcAuthz.length > 3){
			for(int i = 3; i < arrAuthcAuthz.length; i++){
				grantedAuthorities.add(new Role(arrAuthcAuthz[i]));
			}
		}
		
		if(String.valueOf(AppAuthEvent.TYPE_LOGIN_SUCCESS).equals(status)){
			AppAuth.getInstance().setLoginSuccess(name, usernameOrErrorMsg, grantedAuthorities);
		}else if(String.valueOf(AppAuthEvent.TYPE_LOGIN_VERIFIED).equals(status)){
			AppAuth.getInstance().setLoginVerified(name, usernameOrErrorMsg, grantedAuthorities);
		}else if(String.valueOf(AppAuthEvent.TYPE_LOGIN_FAIL).equals(status)){
			AppAuth.getInstance().setLoginFail(usernameOrErrorMsg);
		}else if(String.valueOf(AppAuthEvent.TYPE_LOGIN_NOT_VERIFIED).equals(status)){
			AppAuth.getInstance().setLoginNotVerified(usernameOrErrorMsg);
		}else{
			AppAuth.getInstance().setLoginError(status);
		}
	}
	
	public int checkAuthorization(AbstractPlace place){
		boolean appAuthHasAccess = false;
		
		if(place instanceof RedirectPlace){
			return ActivityController.AUTHZ_TYPE_REDIRECT;
		}
		
		if(isAuthenticated && "login".equals(place.getPrefix())){
			return ActivityController.AUTHZ_TYPE_ALREADY_LOGGED_IN;
		}
		
		if(place instanceof AuthPlace){
			AuthPlace requiresLoginPlace = (AuthPlace) place;
			if(requiresLoginPlace.getRequiredAuthorities() == null){
				appAuthHasAccess = isAuthenticated;
			}else{
				appAuthHasAccess = hasAllAuthority(requiresLoginPlace.getRequiredAuthorities());
			}
		}
		
		if(place instanceof DefaultPlace || appAuthHasAccess){
			return ActivityController.AUTHZ_TYPE_AUTHORIZED;//tem acesso
		}else if(isAuthenticated){
			return ActivityController.AUTHZ_TYPE_NOT_AUTHORIZED;//nao tem acesso mas ja esta autenticado
		}else if(place instanceof AuthPlace){
			return ActivityController.AUTHZ_TYPE_LOGIN_REQUIRED;//nao tem acesso, nao esta autenticado e a tela nao pode ser visualizada sem login
		}
		
		return ActivityController.AUTHZ_TYPE_NOT_AUTHORIZED;
	}
	
	@Override
    public void fireEvent(GwtEvent<?> event) {
        handlerManager.fireEvent(event);
    }
	
	public void addAppAuthHandler(AppAuthEventHandler handler) {
        handlerManager.addHandler(AppAuthEvent.TYPE, handler);
    }
	
	public void removeAppAuthHandler(AppAuthEventHandler handler){
		handlerManager.removeHandler(AppAuthEvent.TYPE, handler);
	}
	
}