package br.com.tagme.gwt.auth.client.activity;

import br.com.tagme.gwt.commons.client.DefaultEntryPoint;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.auth.client.ui.pages.LoginPage;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LoginActivity extends DefaultActivity{

	private static LoginPage loginPage;
	
	public LoginActivity(AbstractPlace contextPlace){
		super(contextPlace);
		
		if(loginPage == null){
			loginPage = new LoginPage();
			loginPage.addAttachHandler(new Handler() {
				
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(event.isAttached()){
						DefaultEntryPoint.setCenteredContentPage();
						DefaultEntryPoint.addMainContainerStyleName("min-height-login-page");
					}else{
						DefaultEntryPoint.removeCenteredContentPage();
						DefaultEntryPoint.removeMainContainerStyleName("min-height-login-page");
					}
				}
			});
		}
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(loginPage);
	}
	
}
