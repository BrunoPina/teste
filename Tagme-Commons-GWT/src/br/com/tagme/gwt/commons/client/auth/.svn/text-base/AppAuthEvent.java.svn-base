package br.com.tagme.gwt.commons.client.auth;

import com.google.gwt.event.shared.GwtEvent;

public class AppAuthEvent extends GwtEvent<AppAuthEventHandler>{
	
	public static final int TYPE_ERROR        			= 0;
	public static final int TYPE_LOGIN_SUCCESS        	= 1;
	public static final int TYPE_LOGIN_FAIL				= 2;
	public static final int TYPE_LOGIN_VERIFIED 		= 3;
	public static final int TYPE_LOGIN_NOT_VERIFIED 	= 4;
	public static final int TYPE_INFOS_CHANGED			= 5;
	
	public static Type<AppAuthEventHandler> TYPE = new Type<AppAuthEventHandler>();

	private int eventType;
	private String message;
	
	public AppAuthEvent(int eventType, String message){
		this.eventType = eventType;
		this.message = message;
	}
	
	@Override
	public Type<AppAuthEventHandler> getAssociatedType() {
	    return TYPE;
	}
	
	@Override
	protected void dispatch(AppAuthEventHandler handler) {
	    handler.onAppAuthChange(this);
	}

	public boolean typeLoginSuccess() {
		return eventType == TYPE_LOGIN_SUCCESS;
	}
	
	public boolean typeLoginFail() {
		return eventType == TYPE_LOGIN_FAIL;
	}
	
	public boolean typeLoginVerified() {
		return eventType == TYPE_LOGIN_VERIFIED;
	}
	
	public boolean typeLoginNotVerified() {
		return eventType == TYPE_LOGIN_NOT_VERIFIED;
	}
	
	public boolean typeError() {
		return eventType == TYPE_ERROR;
	}
	
	public boolean typeInfosChanged() {
		return eventType == TYPE_INFOS_CHANGED;
	}
	
	public String getMessage(){
		return message;
	}

}