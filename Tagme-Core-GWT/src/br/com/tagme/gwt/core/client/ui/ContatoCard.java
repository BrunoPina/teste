package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Image;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.commons.utils.client.FormatterUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ContatoCard extends Composite {

	private static ContatoCardUiBinder	uiBinder	= GWT.create(ContatoCardUiBinder.class);

	interface ContatoCardUiBinder extends UiBinder<Widget, ContatoCard> {
	}

	@UiField Image imgContato;
	@UiField Heading hdNome;
	@UiField Heading hdCargo;
	@UiField Heading hdEmail;
	@UiField Label labelAtivoInativo;
	@UiField Tooltip tooltipEmail;
	
	private boolean isAtivo = false;
	private String codUsu = "";
	private String codContato = "";
	private String email;
	
	public ContatoCard(String codContato, String codUsu, String nome, String cargo, String email, boolean isAtivoW) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.codUsu = codUsu;
		this.codContato = codContato;
		this.email = email;
		imgContato.setUrl(SchemeReference.SKP_RELATIVEURL + "image/user/" + codUsu + ".png?w=80&h=80&r=true");
		hdNome.setText(formatNome(nome));
		hdCargo.setText(cargo);
		hdEmail.setText(formatOverflowEmail(email));
		setLabelValue(isAtivoW);
	}
	
	public ContatoCard(String codContato, String nome, String cargo, String email, boolean isAtivoW){
		initWidget(uiBinder.createAndBindUi(this));

		this.codContato = codContato;
		
		imgContato.setUrl(SchemeReference.SKP_RELATIVEURL + "image/user/0.png?w=80&h=80&r=true");
		hdNome.setText(formatNome(nome));
		hdCargo.setText(cargo);
		hdEmail.setText(formatOverflowEmail(email));
		setLabelValue(isAtivoW);
	}
	
	private void setLabelValue(boolean isAtivoW){
		if(isAtivoW){
			isAtivo = true;
			labelAtivoInativo.setText("Ativo");
			labelAtivoInativo.setType(LabelType.SUCCESS);
		}else{
			isAtivo = false;
			labelAtivoInativo.setText("Inativo");
			labelAtivoInativo.setType(LabelType.DANGER);
		}
		
	}

	public boolean isContatoAtivo(){
		return this.isAtivo;
	}
	
	public String getCodUsu(){
		return this.codUsu;
	}
	
	public String getCodContato(){
		return this.codContato;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String formatNome(String nome){
		String[] arrNome = nome.split(" ");
		if(arrNome.length > 1){
			String formNome = arrNome[0] + " " + arrNome[arrNome.length -1];
			return formNome;
		}else{
			return nome;
		}
	}
	
	public String formatOverflowEmail(String email){
		
		String formattedEmail = FormatterUtils.formatOverflowText(email, 28);
		
		if(!email.equals(formattedEmail)){
			tooltipEmail.setTitle(email);
			tooltipEmail.setIsAnimated(false);
		}
		return formattedEmail;
	}

}
