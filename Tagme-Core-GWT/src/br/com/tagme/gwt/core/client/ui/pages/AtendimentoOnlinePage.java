package br.com.tagme.gwt.core.client.ui.pages;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class AtendimentoOnlinePage extends DefaultCompositeWithPresenter {

	private static AtendimentoOnlinePageUiBinder	uiBinder	= GWT.create(AtendimentoOnlinePageUiBinder.class);

	interface AtendimentoOnlinePageUiBinder extends UiBinder<Widget, AtendimentoOnlinePage> {
	}

	public AtendimentoOnlinePage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));
			
		ScriptInjector.fromUrl("https://server.iad.liveperson.net/hc/74241001/?cmd=mTagRepstate&site=74241001&buttonID=12&divID=lpButDivID-1341326352588&bt=1&c=1").setCallback(
				
			new Callback<Void, Exception>() {
				
			    public void onFailure(Exception reason) {
			    	
			    }	
			    
			    public void onSuccess(Void result) {
			    	
			    }
			    
			}
			
		).inject();
	}
	
	@UiHandler("btnIniciarAtendimento")
	void btnIniciarAtendimentoClick(ClickEvent event){
		Window.open("https://server.iad.liveperson.net/hc/74241001/?cmd=file&file=visitorWantsToChat&site=74241001&imageUrl=http://www.sankhya.com.br/images&referrer=atendimento.place.sankhya.com.br", "chat74241001", "width=475,height=400,resizable=yes");
	}
}
