package br.com.tagme.gwt.core.client.activity;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IBreadcumbPlace;
import br.com.tagme.gwt.core.client.place.CNPJLicenciadosPlace;
import br.com.tagme.gwt.core.client.place.ContatosAutorizadosPlace;
import br.com.tagme.gwt.core.client.place.InformacoesCadastraisPlace;
import br.com.tagme.gwt.core.client.ui.pages.CNPJLicenciadosPage;
import br.com.tagme.gwt.core.client.ui.pages.ContatosAutorizadosPage;
import br.com.tagme.gwt.core.client.ui.pages.DefaultInnerPage;
import br.com.tagme.gwt.core.client.ui.pages.InformacoesCadastraisPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class InformacoesCadastraisActivity extends DefaultActivity{

	public InformacoesCadastraisActivity(IBreadcumbPlace place){
		super((AbstractPlace) place);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		DefaultInnerPage.getInstance().setPresenter(this);
		
		if(super.getContextPlace() instanceof InformacoesCadastraisPlace){
			DefaultInnerPage.getInstance().setView(new InformacoesCadastraisPage(this));
		}else if(super.getContextPlace() instanceof CNPJLicenciadosPlace){
			CNPJLicenciadosPage page = new CNPJLicenciadosPage(this);
			DefaultInnerPage.getInstance().setView(page);
		}else if(super.getContextPlace() instanceof ContatosAutorizadosPlace){
			ContatosAutorizadosPage page = new ContatosAutorizadosPage(this);
			DefaultInnerPage.getInstance().setView(page);
		}
		panel.setWidget(DefaultInnerPage.getInstance());
	}
	
}
