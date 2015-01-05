package br.com.tagme.gwt.core.client.activity;

import br.com.tagme.gwt.commons.client.integracao.SkwHelper;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IBreadcumbPlace;
import br.com.tagme.gwt.core.client.place.BoletoPlace;
import br.com.tagme.gwt.core.client.ui.pages.BoletoPage;
import br.com.tagme.gwt.core.client.ui.pages.DefaultInnerPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class MovFinActivity extends DefaultActivity{

	BoletoPage page = null;
	
	public MovFinActivity(IBreadcumbPlace place){
		super((AbstractPlace) place);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		DefaultInnerPage.getInstance().setPresenter(this);
		
		if(getContextPlace() instanceof BoletoPlace){
			page = new BoletoPage(this);
			DefaultInnerPage.getInstance().setView(page);
		}
		
		panel.setWidget(DefaultInnerPage.getInstance());
	}
	
	@Override
	public String mayStop() {
		if(page != null){
			new SkwHelper().logoutSWProxy(page.getCurrSkwSessionId());
		}
		return null;
	}
	
}
