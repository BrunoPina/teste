package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Tooltip;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ContatoCardBack extends Composite {

	private static ContatoCardBackUiBinder	uiBinder	= GWT.create(ContatoCardBackUiBinder.class);

	interface ContatoCardBackUiBinder extends UiBinder<Widget, ContatoCardBack> {
	}

	@UiField Heading lblTopTelefone;
	@UiField Heading lblCenterTelefone;
	
	public ContatoCardBack(String telefone) {
		initWidget(uiBinder.createAndBindUi(this));
		lblCenterTelefone.setText(telefone);
	}

}
