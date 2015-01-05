package br.com.tagme.gwt.core.client.ui.pages;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import org.gwtbootstrap3.client.ui.gwt.CellTable;
import org.gwtbootstrap3.client.ui.html.Italic;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.components.Alert.ConfirmCallback;
import br.com.tagme.gwt.commons.client.components.GenericData;
import br.com.tagme.gwt.commons.client.components.formitem.FormItem;
import br.com.tagme.gwt.commons.client.components.formitem.FormItemTextBox;
import br.com.tagme.gwt.commons.client.xml.EntityParser;
import br.com.tagme.gwt.commons.utils.client.ValidadorCpfCnpj;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.core.client.CoreEntryPoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class CNPJLicenciadosPage extends DefaultCompositeWithPresenter {

	private static CNPJLicenciadosPageUiBinder	uiBinder	= GWT.create(CNPJLicenciadosPageUiBinder.class);

	interface CNPJLicenciadosPageUiBinder extends UiBinder<Widget, CNPJLicenciadosPage> {
	}

	private CellTable<GenericData>	grid1;
	private CellTable<GenericData>	grid2;
	@UiField Column									containerGrid1;
	@UiField Column									containerGrid2;

	@UiField FormItem<String>						formCnpj;
	@UiField FormItem<String>						formNomeFantasia;
	@UiField FormItem<String>						formRazaoSocial;
	@UiField Button									botaoInativar;
	@UiField Button									botaoSolic;
	@UiField Button									botaoCancelarSolic;
	@UiField Button									btnSalvar;
	@UiField Modal									modalSolicLicenciamento;
	@UiField Italic									avisoCnpjs;
	
	private GenericData grid1SelectedData;
	private GenericData grid2SelectedData;

	public CNPJLicenciadosPage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));
		
		if(CoreEntryPoint.isJiva()){
			avisoCnpjs.setText("Apenas CNPJs licenciados junto à Jiva estão autorizados a operar e faturar utilizando o ERP Jiva.");	
		}
		
		modalSolicLicenciamento.getElement().setAttribute("tabindex", "-1");
		
		botaoInativar.setEnabled(false);
		botaoCancelarSolic.setEnabled(false);

		FormItemTextBox formItemCnpj = (FormItemTextBox) formCnpj.getFormItemWidget();
		setCnpj(formItemCnpj);
		
		formCnpj.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String cleanCnpjText = formCnpj.getValueAsString().replace(".", "").replace("/", "").replace("-", "");
				ValidadorCpfCnpj validador = new ValidadorCpfCnpj(cleanCnpjText);
				if(validador.isValid()){
					formCnpj.createAndShowTooltip("Válido", ValidationState.SUCCESS, Placement.RIGHT);
				}else{
					formCnpj.createAndShowTooltip("Inválido", ValidationState.ERROR, Placement.RIGHT);
				}
				
			}
		});
		
		buscaEmpresasParceiro();
		buscaSolicitacoes();
	}

	@UiHandler("botaoInativar")
	public void onBotaoInativarClick(ClickEvent event){
		Alert.confirm("Será feita uma solicitação para remover o licenciamento do CNPJ selecionado. Deseja continuar?", new ConfirmCallback() {
			
			@Override
			public void onConfirm() {
				inativarCnpj(grid1SelectedData.getValueAsString("CNPJCPF"), grid1SelectedData.getValueAsString("RAZAOSOCIAL"), grid1SelectedData.getValueAsString("NOMEEMP"));
			}
			
			@Override
			public void onCancel() {
			}
		});
	}
	
	@UiHandler("botaoSolic")
	public void onBotaoSolicClick(ClickEvent event){
		modalSolicLicenciamento.show();
	}
	
	@UiHandler("botaoCancelarSolic")
	public void onBotaoCancelarSolicClick(ClickEvent event){
		Alert.confirm("Confirma o cancelamento desta solicitação?", new ConfirmCallback() {
			
			@Override
			public void onConfirm() {
				cancelarSolicitacao(grid2SelectedData.getValueAsString("CODSOLIC"));
			}
			
			@Override
			public void onCancel() {
			}
		});
	}
	
	@UiHandler("btnSalvar")
	public void onBtnSalvarClick(ClickEvent event) {
		btnSalvar.state().loading();
		novaSolicitacao();
	}

	private void buscaEmpresasParceiro() {

		XMLServiceProxy service = new XMLServiceProxy(this);

		service.call("PORCLI@BuscaEmpresasLicenciadasService", null, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				grid1 = EntityParser.buildCellTable(response);
				if(grid1.getRowCount() == 0){
					containerGrid1.add(Alert.buildLeadMessage("Nenhum CNPJ está licenciado."));
				}else{
					grid1.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
					
					final SingleSelectionModel<GenericData> selectionModel = new SingleSelectionModel<GenericData>();
					grid1.setSelectionModel(selectionModel);
					
					selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						
						@Override
						public void onSelectionChange(SelectionChangeEvent event) {
							grid1SelectedData = selectionModel.getSelectedObject();
							if(grid1SelectedData!=null){
								botaoInativar.setEnabled(true);
							}
						}
					});
					
					containerGrid1.add(grid1);
				}
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}
		});
	}

	private void novaSolicitacao() {

		String cnpjcpf = formCnpj.getValueAsString();
		String razaoSocial = formRazaoSocial.getValueAsString();
		String nome = formNomeFantasia.getValueAsString();

		XMLServiceProxy service = new XMLServiceProxy(this);

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");

		Element cnpjCpfElem = doc.createElement("cnpjCpf");
		cnpjCpfElem.appendChild(doc.createTextNode(cnpjcpf));
		parametros.appendChild(cnpjCpfElem);

		Element razaoSocialElem = doc.createElement("razaoSocial");
		razaoSocialElem.appendChild(doc.createTextNode(razaoSocial));
		parametros.appendChild(razaoSocialElem);

		Element nomeFantasiaElem = doc.createElement("nomeFantasia");
		nomeFantasiaElem.appendChild(doc.createTextNode(nome));
		parametros.appendChild(nomeFantasiaElem);
		
		Element tipoSolicElem = doc.createElement("tipoSolic");
		tipoSolicElem.appendChild(doc.createTextNode("nova"));
		parametros.appendChild(tipoSolicElem);

		doc.appendChild(parametros);

		service.call("PORCLI@SolicitacaoCNPJsLicenciadosService", doc, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				Alert.alert("Solicitação enviada com sucesso.", new ConfirmCallback(){

					@Override
					public void onConfirm() {
						modalSolicLicenciamento.hide();
						btnSalvar.state().reset();
						refreshGridSolicitacoes();
					}

					@Override
					public void onCancel() {
					}
					
				});
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}

		});

	}
	
	public void cancelarSolicitacao(String codSolic){
		
		XMLServiceProxy service = new XMLServiceProxy(this);
		
		Document doc = XMLParser.createDocument();
		
		Element parametros = doc.createElement("parametros");
		
		Element tipoSolicElem = doc.createElement("tipoSolic");
		tipoSolicElem.appendChild(doc.createTextNode("cancelar"));
		parametros.appendChild(tipoSolicElem);
		
		Element codSolicElem = doc.createElement("codSolic");
		codSolicElem.appendChild(doc.createTextNode(codSolic));
		parametros.appendChild(codSolicElem);
		
		doc.appendChild(parametros);
		
		service.call("PORCLI@SolicitacaoCNPJsLicenciadosService", doc, new XMLCallback(){

			@Override
			public void onResponseReceived(Element response) {
				refreshGridSolicitacoes();
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
	}
	
	private void inativarCnpj(String cnpjCpf, String razaoSocial, String nomeFantasia){
		
		XMLServiceProxy service = new XMLServiceProxy(this);
		
		Document doc = XMLParser.createDocument();
		
		Element parametros = doc.createElement("parametros");
		
		Element tipoSolicElem = doc.createElement("tipoSolic");
		tipoSolicElem.appendChild(doc.createTextNode("inativar"));
		parametros.appendChild(tipoSolicElem);
		
		Element cnpjCpfElem = doc.createElement("cnpjCpf");
		cnpjCpfElem.appendChild(doc.createTextNode(cnpjCpf));
		parametros.appendChild(cnpjCpfElem);
		
		Element razaoSocialElem = doc.createElement("razaoSocial");
		razaoSocialElem.appendChild(doc.createTextNode(razaoSocial));
		parametros.appendChild(razaoSocialElem);
		
		Element nomeFantasiaElem = doc.createElement("nomeFantasia");
		nomeFantasiaElem.appendChild(doc.createTextNode(nomeFantasia));
		parametros.appendChild(nomeFantasiaElem);
		
		doc.appendChild(parametros);
		
		service.call("PORCLI@SolicitacaoCNPJsLicenciadosService", doc, new XMLCallback(){

			@Override
			public void onResponseReceived(Element response) {
				refreshGridSolicitacoes();
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		
		
	}

	private void buscaSolicitacoes() {
		XMLServiceProxy service = new XMLServiceProxy(this);

		service.call("PORCLI@BuscaSolicitacoesCNPJService", null, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				grid2 = EntityParser.buildCellTable(response);
				if(grid2.getRowCount() == 0){
					containerGrid2.add(Alert.buildLeadMessage("Sem solicitações pendentes."));
				}else{
					grid2.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
					
					final SingleSelectionModel<GenericData> selectionModel = new SingleSelectionModel<GenericData>();
					grid2.setSelectionModel(selectionModel);
					
					selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						
						@Override
						public void onSelectionChange(SelectionChangeEvent event) {
							grid2SelectedData = selectionModel.getSelectedObject();
							if(grid2SelectedData!=null){
								botaoCancelarSolic.setEnabled(true);
							}
						}
					});
					
					containerGrid2.add(grid2);
				}
				
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}
		});
		
	}
	
	private void refreshGridSolicitacoes(){
		containerGrid2.remove(containerGrid2.getWidget(0));
		buscaSolicitacoes();
	}
	
	 public void setCpf(final TextBox cpfText) {
         cpfText.addKeyPressHandler(new KeyPressHandler() {
        	 public void onKeyPress(KeyPressEvent event) {
        		 if(event.getNativeEvent().getKeyCode() != 8) {
        			 if(cpfText.getValue().length() == 3 || cpfText.getValue().length() == 7) {
        				 cpfText.setValue(cpfText.getValue() + ".");
        			 }
        			 if(cpfText.getValue().length() == 11) {
        				 cpfText.setValue(cpfText.getValue() + "-");
        			 }
        		 }
        	 }
         });
	 }
	 
	 public void setCnpj(final TextBox cnpjText){
		 cnpjText.setMaxLength(18);
		 cnpjText.addKeyPressHandler(new KeyPressHandler(){

			@Override
			public void onKeyPress(KeyPressEvent event) {
				String input = cnpjText.getValue();
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ZERO ||			
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ONE || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TWO || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_THREE || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_FOUR || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_FIVE || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SIX || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_SEVEN || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_EIGHT || 
				   event.getNativeEvent().getKeyCode() == KeyCodes.KEY_NINE
				 ){
					if(event.getNativeEvent().getKeyCode() != 7){
						if(input.length() == 2 || input.length() == 6){
							cnpjText.setValue(input + ".");
						}
						if(input.length() == 10){
							cnpjText.setValue(input + "/");
						}
						if(input.length() == 15){
							cnpjText.setValue(input + "-");
						}
					}
				}else{
					event.getNativeEvent().preventDefault();
				}
			}
		 });
	 }

}
