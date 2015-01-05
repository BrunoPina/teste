package br.com.tagme.gwt.core.client.activity;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IBreadcumbPlace;
import br.com.tagme.gwt.core.client.place.AtendimentoOnlinePlace;
import br.com.tagme.gwt.core.client.ui.pages.AtendimentoOnlinePage;
import br.com.tagme.gwt.core.client.ui.pages.DefaultInnerPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class AtendimentoOnlineActivity extends DefaultActivity{

	public AtendimentoOnlineActivity(IBreadcumbPlace place){
		super((AbstractPlace) place);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		DefaultInnerPage.getInstance().setPresenter(this);
		
		if(getContextPlace() instanceof AtendimentoOnlinePlace){
			DefaultInnerPage.getInstance().setView(new AtendimentoOnlinePage(this));
		}
		
		panel.setWidget(DefaultInnerPage.getInstance());
	}
	
}
