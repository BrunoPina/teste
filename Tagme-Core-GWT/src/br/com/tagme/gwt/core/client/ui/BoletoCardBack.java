package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Heading;

import br.com.tagme.gwt.commons.utils.client.FormatterUtils;
import br.com.tagme.gwt.commons.utils.client.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BoletoCardBack extends Composite {

	private static BoletoCardBackUiBinder	uiBinder	= GWT.create(BoletoCardBackUiBinder.class);

	interface BoletoCardBackUiBinder extends UiBinder<Widget, BoletoCardBack> {
	}

	@UiField Heading lblCenterNatureza;
	@UiField Heading lblCenterDtVencOrig;
	@UiField Heading lblTopContatoReneg;
	@UiField Heading lblCenterContatoReneg;
	
	
	public BoletoCardBack(String natureza, String dtVencOrig, String nomeContatoReneg) {
		initWidget(uiBinder.createAndBindUi(this));
		
		lblCenterNatureza.setText(FormatterUtils.capitalize(natureza));
		lblCenterDtVencOrig.setText(dtVencOrig);
		
		if(StringUtils.isEmpty(nomeContatoReneg)){
			lblTopContatoReneg.setText("");
		}else{
			lblCenterContatoReneg.setText(FormatterUtils.capitalize(nomeContatoReneg));
		}
		
	}

}
