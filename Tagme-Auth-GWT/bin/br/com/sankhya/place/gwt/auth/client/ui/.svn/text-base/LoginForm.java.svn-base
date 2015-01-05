package br.com.tagme.gwt.auth.client.ui;

import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.SubmitButton;
import org.gwtbootstrap3.client.ui.TextBox;

import br.com.tagme.gwt.commons.client.DefaultComposite;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.auth.AppAuthEvent;
import br.com.tagme.gwt.commons.client.auth.AppAuthEventHandler;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;
import br.com.tagme.gwt.mvp.client.tokenizer.TokenizerFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;

public class LoginForm extends DefaultComposite{

	private static LoginPageUiBinder	uiBinder	= GWT.create(LoginPageUiBinder.class);
	private static AppAuthEventHandler authHandler;
	
	interface LoginPageUiBinder extends UiBinder<Widget, LoginForm> {
	}

	@UiField SubmitButton btnLogin;
	@UiField FormPanel loginForm;
	@UiField TextBox username;
	@UiField Input password;
	
	public LoginForm() {
		initWidget(uiBinder.createAndBindUi(this));
		if(authHandler == null){
			authHandler = new AppAuthEventHandler() {
				
				@Override
				public void onAppAuthChange(AppAuthEvent event) {
					
					if(event.typeLoginSuccess()){
						AppPlaceController.goTo((AbstractPlace) TokenizerFactory.getTokenizer("redirect").getPlace("lastOrIndex"));
					}else if(event.typeLoginFail()){
						String message = event.getMessage();
						Alert.showError("Autenticação não realizada", message == null ? "Não foi possível autenticar seu usuário." : message);
					}
					
					Alert.hideWaitMessage();
					btnLogin.state().reset();
				}
			};
			
			AppAuth.getInstance().addAppAuthHandler(authHandler);
		}
		
		loginForm.setAction(SchemeReference.SKP_RELATIVEURL + "j_spring_security_check");
		loginForm.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				Alert.showWaitMessage();
				btnLogin.state().loading();
			}
		});
		
		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				AppAuth.getInstance().tryAuth(event.getResults());
			}
		});
		
	}
	
	public String getUsernameValue(){
		return username.getValue();
	}
	
	public String getPasswordValue(){
		return password.getValue();
	}
	
}
