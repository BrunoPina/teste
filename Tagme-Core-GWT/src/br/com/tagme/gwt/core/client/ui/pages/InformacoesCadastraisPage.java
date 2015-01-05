package br.com.tagme.gwt.core.client.ui.pages;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.DescriptionTitle;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.components.GenericData;
import br.com.tagme.gwt.commons.client.components.formitem.FormItem;
import br.com.tagme.gwt.commons.client.xml.EntityParser;
import br.com.tagme.gwt.commons.client.xml.EntityParser.FormItemBuilder;
import br.com.tagme.gwt.commons.utils.client.FormatterUtils;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class InformacoesCadastraisPage extends DefaultCompositeWithPresenter {

	private static InformacoesCadastraisPageUiBinder	uiBinder	= GWT.create(InformacoesCadastraisPageUiBinder.class);

	
	interface InformacoesCadastraisPageUiBinder extends UiBinder<Widget, InformacoesCadastraisPage> {
	}

	@UiField Modal modalEdit;
	@UiField Button botaoEditar;
	
	@UiField DescriptionTitle nome;
	@UiField DescriptionTitle dtFund;
	@UiField DescriptionTitle cnpj;
	
	@UiField DescriptionTitle endereco;
	@UiField DescriptionTitle endNum;
	@UiField DescriptionTitle complemento;
	@UiField DescriptionTitle bairro;
	@UiField DescriptionTitle cidade;
	@UiField DescriptionTitle uf;
	@UiField DescriptionTitle cep;
	
	@UiField DescriptionTitle segPrinc;
	@UiField DescriptionTitle segSec;
	@UiField DescriptionTitle ativPrincipal;
	@UiField DescriptionTitle regTrib;
	@UiField DescriptionTitle faturamento;
	@UiField DescriptionTitle funcionarios;
	@UiField DescriptionTitle nvlProc;
	
	@UiField FormItem<String> formEndereco;
	@UiField FormItem<String> formEndNumero;
	@UiField FormItem<String> formComplemento;
	@UiField FormItem<String> formBairro;
	@UiField FormItem<String> formCep;
	@UiField FormItem<String> formCidade;
	@UiField FormItem<String> formUF;
	
	@UiField Select formSegmento;
	
	@UiField Button botaoSolicAlteracao;
	@UiField Button botaoCancelSolic;
	
	@UiField Option optSegAtacDist;
	@UiField Option optSegInd;
	@UiField Option optSegServ;
	@UiField Option optSegVar;
	
	@UiField FormItem<String> formRamoAtacDist;
	@UiField FormItem<String> formRamoInd;
	@UiField FormItem<String> formRamoServ;
	@UiField FormItem<String> formRamoVar;
	
	@UiField FormItem<String> formRegimeTrib;
	
	private String initialSegPrinc;

	public InformacoesCadastraisPage() {
		initWidget(uiBinder.createAndBindUi(this));
		
		buscaInfosParceiro();
	}
	
	public InformacoesCadastraisPage(DefaultActivity presenter) {
		super(presenter);
		initWidget(uiBinder.createAndBindUi(this));
		modalEdit.getElement().setAttribute("tabindex", "-1");
		
		buscaInfosParceiro();
	}
	
	
	private void buscaInfosParceiro(){
		
		XMLServiceProxy service = new XMLServiceProxy(this);

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");
		doc.appendChild(parametros);
		
		service.call("PORCLI@BuscaInfosParceiroService", doc, new XMLCallback(){

			@Override
			public void onResponseReceived(Element response) {
				EntityParser.buildFromXml(response, new FormItemBuilder() {

					@Override
					public void buildItem(GenericData genericData) {
						
						nome.setText(FormatterUtils.capitalize(genericData.getValueAsString("RAZAOSOCIAL")));
						dtFund.setText(FormatterUtils.formatStringDate(genericData.getValueAsString("DTFUNDACAO")));
						cnpj.setText(genericData.getValueAsString("CNPJ"));
						
						String enderecoValue = genericData.getValueAsString("TIPOEND") + " " + genericData.getValueAsString("NOMEEND");
						endereco.setText(FormatterUtils.capitalize(enderecoValue));
						formEndereco.setValue(FormatterUtils.capitalize(enderecoValue));
						
						String endNumValue = genericData.getValueAsString("NUMEND");
						endNum.setText(FormatterUtils.capitalize(endNumValue));
						formEndNumero.setValue(FormatterUtils.capitalize(endNumValue));
						
						String complementoValue = genericData.getValueAsString("COMPLEMENTO");
						complemento.setText(FormatterUtils.capitalize(complementoValue));
						formComplemento.setValue(FormatterUtils.capitalize(complementoValue));
						
						String bairroValue = genericData.getValueAsString("BAIRRO"); 
						bairro.setText(FormatterUtils.capitalize(bairroValue));
						formBairro.setValue(FormatterUtils.capitalize(bairroValue));
						
						String cidadeValue = genericData.getValueAsString("CIDADE"); 
						cidade.setText(FormatterUtils.capitalize(cidadeValue));
						formCidade.setValue(FormatterUtils.capitalize(cidadeValue));
						
						String ufValue = genericData.getValueAsString("UF"); 
						uf.setText(ufValue);
						formUF.setValue(ufValue);
						
						String cepValue = genericData.getValueAsString("CEP");
						cep.setText(cepValue);
						formCep.setValue(cepValue);
						
						initialSegPrinc = FormatterUtils.capitalize(genericData.getValueAsString("SEG_PRINC"));
						formSegmento.refresh();
						segPrinc.setText(initialSegPrinc);
						formSegmento.setValue(initialSegPrinc);
						formSegmento.refresh();
						
						StringBuffer segmentosSecundarios = new StringBuffer();
						if("S".equals(genericData.getValueAsString("IS_ATACADO"))){
							segmentosSecundarios.append("Atacado Distribuidor");
							formRamoAtacDist.setValue("S");
						}else{
							formRamoAtacDist.setValue("N");
						}
						if("S".equals(genericData.getValueAsString("IS_INDUSTRIA"))){
							if(segmentosSecundarios.length() != 0){
								segmentosSecundarios.append(", ");
							}
							segmentosSecundarios.append("Indústria");
							formRamoInd.setValue("S");
						}else{
							formRamoInd.setValue("N");
						}
						if("S".equals(genericData.getValueAsString("IS_SERVICO"))){
							if(segmentosSecundarios.length() != 0){
								segmentosSecundarios.append(", ");
							}
							segmentosSecundarios.append("Serviços");
							formRamoServ.setValue("S");
						}else{
							formRamoServ.setValue("N");
						}
						if("S".equals(genericData.getValueAsString("IS_VAREJO"))){
							if(segmentosSecundarios.length() != 0){
								segmentosSecundarios.append(", ");
							}
							segmentosSecundarios.append("Varejo");
							formRamoVar.setValue("S");
						}else{
							formRamoVar.setValue("N");
						}
						
						segSec.setText(FormatterUtils.capitalize(segmentosSecundarios.toString()));
						regTrib.setText("S".equals(genericData.getValueAsString("SIMPLES")) ? "Sim" : "Não");
						formRegimeTrib.setValue("S".equals(genericData.getValueAsString("SIMPLES")) ? "S" : "N");
						ativPrincipal.setText(FormatterUtils.capitalize(genericData.getValueAsString("ATVPRINC")));
						faturamento.setText(FormatterUtils.capitalize(genericData.getValueAsString("FATURAMENTO")));
						funcionarios.setText(FormatterUtils.capitalize(genericData.getValueAsString("NUMFUNC")));
						nvlProc.setText(FormatterUtils.capitalize(genericData.getValueAsString("NVLPROC")));
						
					}
				});
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}
			
		});
	}
	
	@UiHandler("botaoSolicAlteracao")
	void solicitaAlteracaoCadastral(ClickEvent event){
		
		XMLServiceProxy service = new XMLServiceProxy(this);
		
		Document doc = XMLParser.createDocument();
		
		Element parametros = doc.createElement("parametros");

		String endForm = formEndereco.hasChanged() ? formEndereco.getValueAsString() : null;
		if(endForm != null){
			Element enderecoElem = doc.createElement("ENDERECO");
			enderecoElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(endForm) == null ? "REMOVER ENDEREÇO" : endForm));
			parametros.appendChild(enderecoElem);
		}
		
		String endNumForm = formEndNumero.hasChanged() ? formEndNumero.getValueAsString() : null;
		if(endNumForm != null){
			Element endNumeroElem = doc.createElement("ENDNUMERO");
			endNumeroElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(endNumForm) == null ? "REMOVER NÚMERO" : endNumForm));
			parametros.appendChild(endNumeroElem);
		}
		
		String complementoForm = formComplemento.hasChanged() ? formComplemento.getValueAsString() : null;
		if(complementoForm != null){
			Element complementoElem = doc.createElement("COMPLEMENTO");
			complementoElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(complementoForm) == null ? "REMOVER COMPLEMENTO" : complementoForm));
			parametros.appendChild(complementoElem);
		}
		
		String bairroForm = formBairro.hasChanged() ? formBairro.getValueAsString() : null;
		if(bairroForm != null){
			Element bairroElem = doc.createElement("BAIRRO");
			bairroElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(bairroForm) == null ? "REMOVER BAIRRO" : bairroForm));
			parametros.appendChild(bairroElem);
		}
		
		String cepForm = formCep.hasChanged() ? formCep.getValueAsString() : null;
		if(cepForm != null){
			Element cepElem = doc.createElement("CEP");
			cepElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(cepForm) == null ? "REMOVER CEP" : cepForm));
			parametros.appendChild(cepElem);
		}
		
		String cidadeForm = formCidade.hasChanged() ? formCidade.getValueAsString() : null;
		if(cidadeForm != null){
			Element cidadeElem = doc.createElement("CIDADE");
			cidadeElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(cidadeForm) == null ? "REMOVER CIDADE" : cidadeForm));
			parametros.appendChild(cidadeElem);
		}

		String ufForm = formUF.hasChanged() ? formUF.getValueAsString() : null;
		if(ufForm != null){
			Element ufElem = doc.createElement("UF");
			ufElem.appendChild(doc.createTextNode(StringUtils.getEmptyAsNull(ufForm) == null ? "REMOVER UF" : ufForm));
			parametros.appendChild(ufElem);
		}
		
		String segPrincForm = StringUtils.getNullAsEmpty(formSegmento.getValue());
		String oldSegPrincForm = StringUtils.getNullAsEmpty(initialSegPrinc);
		if(!segPrincForm.equals(oldSegPrincForm)){
			Element segPrincElem = doc.createElement("SEGPRINC");
			segPrincElem.appendChild(doc.createTextNode(segPrincForm));
			parametros.appendChild(segPrincElem);
		}
		
		if(formRamoAtacDist.hasChanged() || formRamoInd.hasChanged() || formRamoServ.hasChanged() || formRamoVar.hasChanged()){
			Element isAtacDistElem = doc.createElement("IS_ATACADO");
			isAtacDistElem.appendChild(doc.createTextNode(formRamoAtacDist.getValueAsString()));
			parametros.appendChild(isAtacDistElem);
			
			Element isIndElem = doc.createElement("IS_INDUSTRIA");
			isIndElem.appendChild(doc.createTextNode(formRamoInd.getValueAsString()));
			parametros.appendChild(isIndElem);
			
			Element isServElem = doc.createElement("IS_SERVICO");
			isServElem.appendChild(doc.createTextNode(formRamoServ.getValueAsString()));
			parametros.appendChild(isServElem);
			
			Element isVarElem = doc.createElement("IS_VAREJO");
			isVarElem.appendChild(doc.createTextNode(formRamoVar.getValueAsString()));
			parametros.appendChild(isVarElem);
			
			Element isRamoChanged = doc.createElement("RAMO_CHANGED");
			isRamoChanged.appendChild(doc.createTextNode("S"));
			parametros.appendChild(isRamoChanged);
		}
		
		if(formRegimeTrib.hasChanged()){
			Element isSimplesElem = doc.createElement("SIMPLES");
			isSimplesElem.appendChild(doc.createTextNode(formRegimeTrib.getValueAsString()));
			parametros.appendChild(isSimplesElem);
			
			Element isSimplesElemChanged = doc.createElement("SIMPLES_CHANGED");
			isSimplesElemChanged.appendChild(doc.createTextNode("S"));
			parametros.appendChild(isSimplesElemChanged);
		}
		
		doc.appendChild(parametros);
		
		service.call("PORCLI@SolicitaAlteracaoInfoCadastralService", doc, new XMLCallback(){

			@Override
			public void onResponseReceived(Element response) {
				Alert.showSuccess("Solicitação de alteração de dados cadatrais", "Sua solicitação foi realizada. Assim que aprovada seus dados serão atualizados.");
				modalEdit.hide();
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}
			
		});
		
	}
	
	@UiHandler("botaoEditar")
	void showModalEditar(ClickEvent e){
		modalEdit.show();
	}
	
	@UiHandler("botaoCancelSolic")
	void closeModalEditar(ClickEvent e){
		modalEdit.hide();
	}

	
}
