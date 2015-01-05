package br.com.tagme.gwt.core.client.activity;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.core.client.ui.pages.IndexPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class IndexActivity extends DefaultActivity{

	public IndexActivity(AbstractPlace place){
		super(place);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(new IndexPage(this));
	}
	
}
