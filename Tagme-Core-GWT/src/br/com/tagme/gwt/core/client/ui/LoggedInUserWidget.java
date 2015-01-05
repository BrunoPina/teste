package br.com.tagme.gwt.core.client.ui;

import java.util.HashMap;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.DropDownHeader;
import org.gwtbootstrap3.client.ui.Image;

import br.com.tagme.gwt.auth.client.activity.LoginPlace;
import br.com.tagme.gwt.commons.client.DefaultComposite;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.commons.utils.client.XMLUtils;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;
import br.com.tagme.gwt.core.client.CoreEntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class LoggedInUserWidget extends DefaultComposite {

	private static LoggedInUserWidgetUiBinder uiBinder = GWT.create(LoggedInUserWidgetUiBinder.class);

	@UiField FormPanel logoutForm;
	@UiField DropDownHeader userEmail;
	@UiField AnchorButton userName;
	@UiField Image userImage;
	@UiField DropDownHeader nomeParc;
	
	private static int counter = 0;
	
	interface LoggedInUserWidgetUiBinder extends UiBinder<Widget, LoggedInUserWidget> {
	}
	
	public LoggedInUserWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		
		logoutForm.setAction(SchemeReference.SKP_RELATIVEURL + "j_spring_security_logout");
		
		logoutForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				AppAuth.getInstance().setLoginNotVerified("Sessão Finalizada");
				AppPlaceController.goTo(new LoginPlace("home"));
			}
		});
	}
	
	@UiHandler("btnSair")
	public void onBtnSairClick(ClickEvent event){
		logoutForm.submit();
	}
	
	@UiHandler("btnAjustesConta")
	public void onBtnAjustesContaClick(ClickEvent event){
		new AjustesUsuarioForm().show();
	}

	
	public void refresh(){
		counter++;
		userName.setText(AppAuth.getInstance().getName());
		userEmail.setText(AppAuth.getInstance().getUsername());
		XMLServiceProxy service = new XMLServiceProxy(this);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("getNotAuthzReasons", "true");
		
		service.call("PORCLI@BuscaInfosContatoService", null, params, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				String strNomeParc = XMLUtils.getNodeValue(XMLUtils.getFirstChild(response, "NOMEPARC"));
				nomeParc.setText(strNomeParc);
				
				NodeList listReasonLostAccess = response.getElementsByTagName("RLA");
				
				if(listReasonLostAccess.getLength() > 0){
					
					String rlaHtml = "";
				
					for(int i = 0; i  < listReasonLostAccess.getLength() ; i++){
						Element rla = (Element) listReasonLostAccess.item(i);
						String lostAccess = rla.getAttribute("lostAccess");
						String reason = rla.getAttribute("reason");
						if(CoreEntryPoint.isJiva() && reason.contains("Sankhya")){
							reason = reason.replaceAll("Sankhya", "Jiva");
						}
						rlaHtml += "<br/><br/><span style='text-align:justify;'><b>" + lostAccess + " </b> " + reason + "</span>";
						
					}
					
					Alert.showInfo("Acessos bloqueados", rlaHtml);
				
				}
				
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}
			
			
		});
		
		userImage.setUrl(SchemeReference.SKP_RELATIVEURL + "image/user/currUser"+counter+".png?w=70&h=70&r=true&s=true");
	}
	
}
