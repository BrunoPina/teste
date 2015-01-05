package br.com.tagme.gwt.commons.client.components;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.utils.client.StringUtils;

import com.google.gwt.event.dom.client.ClickHandler;

public class AuthButton extends Button{
	
	private String requiredAuthzRole;
	
	public AuthButton() {
    }

    /**
     * Creates button with specified text
     *
     * @param text Text contents of button
     */
    public AuthButton(final String text) {
        super(text);
    }

    public AuthButton(final String text, final ClickHandler handler) {
        super(text, handler);
    }

    public AuthButton(final String text, final IconType iconType, final ClickHandler clickHandler) {
    	super(text, iconType, clickHandler);
    }
    
    private void verifyAuthz(){
    	if(!AppAuth.getInstance().hasAuthority(requiredAuthzRole)){
    		this.setVisible(false);
    	}
    }
    
    public void setRequiredAuthz(String role){
    	this.requiredAuthzRole = role;
    	if(!StringUtils.isEmpty(requiredAuthzRole)){
    		verifyAuthz();
    	}
    }

}
