package br.com.tagme.gwt.mvp.client.base;

import java.util.ArrayList;

import br.com.tagme.gwt.commons.client.auth.Role;

public class AuthPlace extends AbstractPlace{
	
	private ArrayList<Role> requiredAuthorities;

	public AuthPlace(String prefix, String token){
		super(prefix, token);
	}
	
	public AuthPlace(String prefix, String token, String descriptor){
		super(prefix, token, descriptor);
	}
	
	public void setRequiredAuthorities(ArrayList<Role> requiredAuthorities){
		this.requiredAuthorities = requiredAuthorities;
	}
	
	public ArrayList<Role> getRequiredAuthorities(){
		return this.requiredAuthorities;
	}
	
}