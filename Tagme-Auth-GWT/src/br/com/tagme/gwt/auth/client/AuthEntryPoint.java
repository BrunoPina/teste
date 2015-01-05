package br.com.tagme.gwt.auth.client;

import br.com.tagme.gwt.commons.client.DefaultEntryPoint;
import br.com.tagme.gwt.auth.client.activity.LoginPlace;

public class AuthEntryPoint extends DefaultEntryPoint {

	@Override
	public void onModuleLoad() {
		
		addApplicationActivityFactory(new LoginPlace.Initializer());
		addApplicationTokenizer(LoginPlace.PREFIX, new LoginPlace.Initializer());
		
	}

}
