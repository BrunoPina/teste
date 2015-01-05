package br.com.tagme.gwt.auth.client.ui.pages;

import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.components.Alert.ConfirmTextCallback;
import br.com.tagme.gwt.commons.client.components.formitem.FormItem;
import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.auth.client.ui.CriarContaModal;
import br.com.tagme.gwt.auth.client.ui.LoginForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;

public class LoginPage extends Composite {

	private static LoginPageUiBinder	uiBinder	= GWT.create(LoginPageUiBinder.class);

	interface LoginPageUiBinder extends UiBinder<Widget, LoginPage> {
	}

	@UiField LoginForm loginForm;
	
	@UiField FormPanel reenviarEmailForm;
	
	private FormItem<String> formEmail;
	
	public LoginPage() {
		initWidget(uiBinder.createAndBindUi(this));

		reenviarEmailForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		reenviarEmailForm.setAction(SchemeReference.SKP_RELATIVEURL + "reenviarEmailValidacao");
		reenviarEmailForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				Alert.hideWaitMessage();
				
				String[] arrResult = event.getResults().split(";");
				if("1".equals(arrResult[0].trim())){
					Alert.showSuccess("Verifique seu e-mail", "Dentro de alguns instantes você receberá um link para ativação de sua conta.");
				}else{
					Alert.showError("E-mail de confirmação não enviado",arrResult[1]);
				}
			}
		});
		reenviarEmailForm.addSubmitHandler(new SubmitHandler(){

			@Override
			public void onSubmit(SubmitEvent event) {
				Alert.showWaitMessage();
			}
		});
		
		formEmail = new FormItem<String>("E-mail", FormItem.TYPE_TEXTBOX);
		formEmail.setVisible(false);
		formEmail.setName("emailReenv");
		reenviarEmailForm.add(formEmail);
	}
	
	@UiHandler("btnCriarConta")
	void onBtnCriarContaClick(ClickEvent event){
		CriarContaModal modal = new CriarContaModal();
		modal.show();
	}
	
	@UiHandler("btnReenviarEmail")
	void onbtnReenviarEmailClick(ClickEvent event){
		Alert.prompt("Reenviar e-mail de confirmação", "Especifique o e-mail utilizado para criar sua conta que reenviaremos um novo link de confirmação.", "E-mail:", new ConfirmTextCallback() {
			
			@Override
			public void onConfirm(String email) {
				formEmail.setValue(email);
				reenviarEmailForm.submit();
			}
			
			@Override
			public void onCancel() {
			}
		});
	}
	
}
