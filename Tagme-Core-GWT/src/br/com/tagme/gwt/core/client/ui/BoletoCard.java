package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import br.com.tagme.gwt.commons.utils.client.FormatterUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class BoletoCard extends Composite {

	private static DoubleFaceThumbnailWidgetUiBinder	uiBinder	= GWT.create(DoubleFaceThumbnailWidgetUiBinder.class);


	interface DoubleFaceThumbnailWidgetUiBinder extends UiBinder<Widget, BoletoCard> {
	}

	@UiField Icon iconBoleto;
	@UiField Heading lblTop;
	@UiField Heading lblCenter;
	@UiField Tooltip tooltipNatureza;
	@UiField Heading lblBottom;
	@UiField Label labelStatus;
	
	public BoletoCard(IconType iconType, String data, String natureza, String valor, String status, LabelType label) {
		initWidget(uiBinder.createAndBindUi(this));
		
		iconBoleto.setType(iconType);
		
		lblTop.setText(data);
		String formattedNatureza = FormatterUtils.formatOverflowText(natureza, 20);
		
		if(!natureza.equals(formattedNatureza)){
			tooltipNatureza.setTitle(natureza);
			tooltipNatureza.setIsAnimated(false);
		}
		lblCenter.setText(formattedNatureza);
		lblBottom.setText(valor);
		labelStatus.setText(status);
		labelStatus.setType(label);
		
	}
	
}
