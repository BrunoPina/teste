package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.html.Small;

import br.com.tagme.gwt.core.client.CoreEntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Footer extends Composite{

	private static FooterUiBinder	uiBinder	= GWT.create(FooterUiBinder.class);
	
	@UiField Small email;
	@UiField Small tel;
	@UiField Small freetel;
	@UiField Small site;
	
	interface FooterUiBinder extends UiBinder<Widget, Footer> {
	}

	public Footer() {
		initWidget(uiBinder.createAndBindUi(this));
		
		if(CoreEntryPoint.isJiva()){
			email.setText("administrativo@jiva.com.br");
			site.setText("www.jiva.com.br");
		}
	}


}
