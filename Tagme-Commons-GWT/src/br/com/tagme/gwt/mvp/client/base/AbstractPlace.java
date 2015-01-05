package br.com.tagme.gwt.mvp.client.base;

import java.util.HashMap;

import com.google.gwt.place.shared.Place;

public abstract class AbstractPlace extends Place{

	private String prefix;
	private String locator;
	private HashMap<String, String> params;
	private String descriptor;
	
	public AbstractPlace(String prefix, String token, String descriptor){
		this(prefix, token);
		this.descriptor = descriptor;
	}
	
	public AbstractPlace(String prefix, String token){
		this.prefix = prefix;
		
		String[] locatorAndParams = token.split("\\?");
		this.locator = locatorAndParams[0];
		
		if(locatorAndParams.length > 1){
			
			String[] arrParams = locatorAndParams[1].split("\\&");
			this.params = new HashMap<String, String>();
			
			for(int i=0;i<arrParams.length;i++){
				
				String param = arrParams[i];
				int equalsPos = param.indexOf("=");
				
				params.put(new String(param).substring(0, equalsPos), new String(param).substring(equalsPos+1));
			}
			
		}
		
	}
	
	public String getLocator(){
		return locator;
	}
	
	public HashMap<String, String> getParams(){
		return params;
	}
	
	public String getPrefix(){
		return prefix;
	}
	
	public String getDescriptor(){
		return descriptor;
	}
	
}