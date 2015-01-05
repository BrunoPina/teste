package br.com.tagme.gwt.core.client.ui.pages;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.html.Italic;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerLanguage;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.base.constants.DateTimePickerView;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.components.GenericData;
import br.com.tagme.gwt.commons.client.components.card.SelectableCardEventHandler;
import br.com.tagme.gwt.commons.client.components.card.SelectionEvent;
import br.com.tagme.gwt.commons.client.components.formitem.FormItem;
import br.com.tagme.gwt.commons.client.xml.EntityParser;
import br.com.tagme.gwt.commons.client.xml.EntityParser.FormItemBuilder;
import br.com.tagme.gwt.commons.utils.client.FormatterUtils;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.core.client.CoreEntryPoint;
import br.com.tagme.gwt.core.client.ui.ContatoCard;
import br.com.tagme.gwt.core.client.ui.ContatoCardBack;
import br.com.tagme.gwt.core.client.ui.SelectableCard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class ContatosAutorizadosPage extends DefaultCompositeWithPresenter {

	private static ContatosAutorizadosPageUiBinder	uiBinder	= GWT.create(ContatosAutorizadosPageUiBinder.class);

	interface ContatosAutorizadosPageUiBinder extends UiBinder<Widget, ContatosAutorizadosPage> {
	}

	private Map<String, SelectableCard>			mapCardWidgets	= new HashMap<String, SelectableCard>();
	private Map<String, GenericData>			dataProvider	= new HashMap<String, GenericData>();

	@UiField Button								botaoAtivar;
	@UiField Button								botaoInativar;
	@UiField Button								botaoModalNovo;
	@UiField Button								botaoModalEditar;
	@UiField Row								rowThumbnails;
	@UiField Modal								modalContato;
	@UiField Button								botaoSalvarContato;

	@UiField FormItem<String>					formNome;
	@UiField DateTimePicker						formDtNasc;
	@UiField FormItem<String>					formCargo;
	@UiField FormItem<String>					formTelefone;
	@UiField FormItem<String>					formRamal;
	@UiField FormItem<String>					formEmail;

	@UiField CheckBox							formAbreOS;
	@UiField CheckBox							formChat;
	@UiField CheckBox							formDownload;
	@UiField CheckBox							formAcessoBT;
	@UiField CheckBox							formBoletoEmail;
	@UiField CheckBox							formNotaEmail;
	@UiField CheckBox							formRecAviCob;
	@UiField CheckBox							formAlteraContatos;
	@UiField CheckBox							formAlteraInfoParceiro;
	@UiField CheckBox							formLiberaCNPJ;

	@UiField Italic 							textExplicaContato;
	@UiField Italic 							textPossuiConta;
	@UiField Italic 							textNaoPossuiConta;
	
	private String								selectedContato;

	public ContatosAutorizadosPage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));

		if(CoreEntryPoint.isJiva()){
			textExplicaContato.setText("Contatos são pessoas que, por possuírem um relacionamento mais próximo com a Jiva, devem garantir ações e comunicações efetivas entre envolvidos com a solução. Ainda, contribuem e acessam o portal do cliente e seus diversos benefícios.");
			textPossuiConta.setText("* Possui conta no portal do cliente, mas está inativo.");
			textNaoPossuiConta.setText("** Não possui conta no portal do cliente.");
		}
		
		modalContato.getElement().setAttribute("tabindex", "-1");

		formDtNasc.setLanguage(DateTimePickerLanguage.PT_BR);
		formDtNasc.setFormat("dd/mm/yyyy");
		formDtNasc.setMinView(DateTimePickerView.MONTH);
		formDtNasc.setAutoClose(true);

		buscaContatos();
	}

	private void buscaContatos() {

		botaoModalNovo.setEnabled(false);
		botaoAtivar.setEnabled(false);
		botaoInativar.setEnabled(false);
		botaoModalEditar.setEnabled(false);
		
		XMLServiceProxy service = new XMLServiceProxy(this);

		service.call("PORCLI@BuscaContatosParceiroService", null, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {

				EntityParser.buildFromXml(response, new FormItemBuilder() {

					@Override
					public void buildItem(GenericData genericData) {

						boolean isAtivoW = "S".equals(genericData.getValueAsString("IS_ATIVOW"));
						boolean isAtivoPlace = "S".equals(genericData.getValueAsString("IS_ATIVOPLACE"));
						String codUsu = genericData.getValueAsString("CODUSU");
						if(codUsu == null){
							codUsu = "";
						}
						
						String nome = FormatterUtils.capitalize(genericData.getValueAsString("NOMECONTATO"));
						String cargo = FormatterUtils.capitalize(genericData.getValueAsString("CARGO"));
						String email = !StringUtils.isEmpty(genericData.getValueAsString("EMAIL")) ? genericData.getValueAsString("EMAIL") : "Sem e-mail cadastrado";
						String codContato = genericData.getValueAsString("CODCONTATO");
						String telefone = genericData.getValueAsString("TELEFONE");
						
						ContatoCard contato = null;
						
						if(isAtivoPlace){
							contato = new ContatoCard(codContato, codUsu, nome, cargo, email, isAtivoW);
						}else if(!"".equals(codUsu)){ // não está ativo no place, mas tem conta
							contato = new ContatoCard(codContato, codUsu, nome, cargo, email + " *", isAtivoW);
						}else{ // não tem conta no place
							contato = new ContatoCard(codContato, nome, cargo, email + " **", isAtivoW);
						}
					
						String style = contato.isContatoAtivo() ? "success" : "danger";

						SelectableCard card = new SelectableCard(contato.getCodContato(), "card-contato " + style, contato, new ContatoCardBack(telefone), "XS_12,SM_6,MD_6,LG_4", true);
						card.addHandler(new SelectableCardEventHandler() {

							@Override
							public void onSelection(SelectionEvent event) {

								if (event.isSelected()) {

									SelectableCard card = (SelectableCard) event.getSource();
									
									ContatoCard contato = (ContatoCard) card.getFrontWidget();
									
									botaoModalEditar.setEnabled(true);

									if (contato.isContatoAtivo()) {
										botaoAtivar.setEnabled(false);
										if(AppAuth.getInstance().getUsername().equals(contato.getEmail())){
											botaoInativar.setEnabled(false);
										}else{
											botaoInativar.setEnabled(true);
										}
									} else {
										botaoInativar.setEnabled(false);
										botaoAtivar.setEnabled(true);
									}

									for (String id : mapCardWidgets.keySet()) {
										if (!card.getId().equals(id)) {
											mapCardWidgets.get(id).removeSelection();
										}
									}

								} else {
									botaoAtivar.setEnabled(false);
									botaoInativar.setEnabled(false);
									botaoModalEditar.setEnabled(false);
								}

							}

						});
						mapCardWidgets.put(contato.getCodContato(), card);
						dataProvider.put(contato.getCodContato(), genericData);
						rowThumbnails.add(card);

						botaoModalNovo.setEnabled(true);
						
					}
					
				});

			}

			@Override
			public boolean onError(ServiceProxyException e) {
				// TODO Auto-generated method stub
				return false;
			}

		});

	}

	@UiHandler("botaoAtivar")
	void ativar(ClickEvent event) {

		XMLServiceProxy service = new XMLServiceProxy(this);

		for (String widgetId : mapCardWidgets.keySet()) {
			SelectableCard widget = mapCardWidgets.get(widgetId);
			ContatoCard contato = (ContatoCard) widget.getFrontWidget();
			if (widget.isSelected() && !contato.isContatoAtivo()) {

				Document doc = XMLParser.createDocument();

				Element parametros = doc.createElement("parametros");

				Element tipoSolicElem = doc.createElement("TIPOSOLIC");
				tipoSolicElem.appendChild(doc.createTextNode("ativar"));
				parametros.appendChild(tipoSolicElem);

				Element codContatoElem = doc.createElement("CODCONTATO");
				codContatoElem.appendChild(doc.createTextNode(widgetId));
				parametros.appendChild(codContatoElem);

				doc.appendChild(parametros);

				service.call("PORCLI@SolicitacaoContatosAutorizadosService", doc, new XMLCallback() {

					@Override
					public void onResponseReceived(Element response) {
						Alert.showSuccess("Solicitação Completada", "Contato ativado.");
						refreshPage();
					}

					@Override
					public boolean onError(ServiceProxyException e) {
						Alert.showError("Erro ao ativar contato");
						return false;
					}

				});
			}
		}
	}

	@UiHandler("botaoInativar")
	void inativar(ClickEvent event) {

		XMLServiceProxy service = new XMLServiceProxy(this);

		for (String widgetId : mapCardWidgets.keySet()) {
			SelectableCard widget = mapCardWidgets.get(widgetId);
			ContatoCard contato = (ContatoCard) widget.getFrontWidget();
			if (widget.isSelected() && contato.isContatoAtivo()) {
				Document doc = XMLParser.createDocument();

				Element parametros = doc.createElement("parametros");

				Element tipoSolicElem = doc.createElement("TIPOSOLIC");
				tipoSolicElem.appendChild(doc.createTextNode("inativar"));
				parametros.appendChild(tipoSolicElem);

				Element codContatoElem = doc.createElement("CODCONTATO");
				codContatoElem.appendChild(doc.createTextNode(widgetId));
				parametros.appendChild(codContatoElem);

				doc.appendChild(parametros);

				service.call("PORCLI@SolicitacaoContatosAutorizadosService", doc, new XMLCallback() {

					@Override
					public void onResponseReceived(Element response) {
						refreshPage();
						Alert.showSuccess("Solicitação Completada", "Contato inativado.");
					}

					@Override
					public boolean onError(ServiceProxyException e) {
						Alert.showError("Erro ao inativar contato");
						return false;
					}

				});

			}
		}
	}

	@UiHandler("botaoModalNovo")
	void showModalNovo(ClickEvent event) {

		selectedContato = null;

		formNome.setValue("");
		formDtNasc.setValue(null);
		formCargo.setValue("");
		formTelefone.setValue("");
		formRamal.setValue("");
		formEmail.setValue("");
		formAbreOS.setValue(false);
		formChat.setValue(false);
		formDownload.setValue(false);
		formAcessoBT.setValue(false);
		formBoletoEmail.setValue(false);
		formNotaEmail.setValue(false);
		formRecAviCob.setValue(false);
		formAlteraContatos.setValue(false);
		formAlteraInfoParceiro.setValue(false);
		formLiberaCNPJ.setValue(false);

		modalContato.setTitle("Cadastro de Contato");
		modalContato.show();
	}

	@UiHandler("botaoModalEditar")
	void showModalEditar(ClickEvent event) {
		for (String widgetId : mapCardWidgets.keySet()) {
			SelectableCard widget = mapCardWidgets.get(widgetId);
			if (widget.isSelected()) {

				GenericData genericData = dataProvider.get(widgetId);

				selectedContato = widgetId;

				formNome.setValue(genericData.getValueAsString("NOMECONTATO"));

				DateTimeFormat inputFormat = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
				String dataNasc = genericData.getValueAsString("DTNASC");
				if(!StringUtils.isEmpty(dataNasc) && !"-".equals(dataNasc)){
					Date dtNasc = inputFormat.parse(dataNasc);
					DateTimeFormat outputFormat = DateTimeFormat.getFormat("dd/MM/yyyy");
					outputFormat.format(dtNasc);
					formDtNasc.setValue(dtNasc);
				}else{
					formDtNasc.setValue(null);
				}

				formCargo.setValue(genericData.getValueAsString("CARGO"));
				formTelefone.setValue(genericData.getValueAsString("TELEFONE"));
				formRamal.setValue(genericData.getValueAsString("RAMAL"));
				formEmail.setValue(genericData.getValueAsString("EMAIL"));
				formAbreOS.setValue("S".equals(genericData.getValueAsString("ABREOS")));
				formChat.setValue("S".equals(genericData.getValueAsString("CHAT")));
				formDownload.setValue("S".equals(genericData.getValueAsString("DOWNLOAD")));
				formAcessoBT.setValue("S".equals(genericData.getValueAsString("POSSUIACESSOBT")));
				formBoletoEmail.setValue("S".equals(genericData.getValueAsString("RECEBEBOLETOEMAIL")));
				formNotaEmail.setValue("S".equals(genericData.getValueAsString("RECEBENOTAEMAIL")));
				formRecAviCob.setValue("S".equals(genericData.getValueAsString("RECAVICOB")));
				formAlteraContatos.setValue("S".equals(genericData.getValueAsString("ALTERACONTATOS")));
				formAlteraInfoParceiro.setValue("S".equals(genericData.getValueAsString("ALTERAINFORPARCEIRO")));
				formLiberaCNPJ.setValue("S".equals(genericData.getValueAsString("LIBERACNPJ")));
				
				formLiberaCNPJ.setValue(false);

				modalContato.setTitle("Editar Contato");
				modalContato.show();

			}
		}
	}

	@UiHandler("botaoSalvarContato")
	void salvar(ClickEvent event) {

		XMLServiceProxy service = new XMLServiceProxy(this);

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");

		String tipoSolic;

		if (!"".equals(selectedContato) && selectedContato != null) {
			tipoSolic = "editar";

			Element codContatoElem = doc.createElement("CODCONTATO");
			codContatoElem.appendChild(doc.createTextNode(selectedContato));
			parametros.appendChild(codContatoElem);
		} else {
			tipoSolic = "novo";
		}

		Element tipoSolicElem = doc.createElement("TIPOSOLIC");
		tipoSolicElem.appendChild(doc.createTextNode(tipoSolic));
		parametros.appendChild(tipoSolicElem);

		Element nomeContatoElem = doc.createElement("NOMECONTATO");
		nomeContatoElem.appendChild(doc.createTextNode(formNome.getValueAsString()));
		parametros.appendChild(nomeContatoElem);

		Element dtNascElem = doc.createElement("DTNASC");
		DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy");
		dtNascElem.appendChild(doc.createTextNode(dtf.format(formDtNasc.getValue())));
		parametros.appendChild(dtNascElem);

		Element cargoElem = doc.createElement("CARGO");
		cargoElem.appendChild(doc.createTextNode(formCargo.getValueAsString()));
		parametros.appendChild(cargoElem);

		Element telefoneElem = doc.createElement("TELEFONE");
		telefoneElem.appendChild(doc.createTextNode(formTelefone.getValueAsString()));
		parametros.appendChild(telefoneElem);

		Element ramalElem = doc.createElement("RAMAL");
		ramalElem.appendChild(doc.createTextNode(formRamal.getValueAsString()));
		parametros.appendChild(ramalElem);

		Element emailElem = doc.createElement("EMAIL");
		emailElem.appendChild(doc.createTextNode(formEmail.getValueAsString()));
		parametros.appendChild(emailElem);

		Element abreOsElem = doc.createElement("ABREOS");
		abreOsElem.appendChild(doc.createTextNode(formAbreOS.getValue() ? "S" : "N"));
		parametros.appendChild(abreOsElem);

		Element chatElem = doc.createElement("CHAT");
		chatElem.appendChild(doc.createTextNode(formChat.getValue() ? "S" : "N"));
		parametros.appendChild(chatElem);

		Element downloadElem = doc.createElement("DOWNLOAD");
		downloadElem.appendChild(doc.createTextNode(formDownload.getValue() ? "S" : "N"));
		parametros.appendChild(downloadElem);

		Element possuiAcessoBTElem = doc.createElement("POSSUIACESSOBT");
		possuiAcessoBTElem.appendChild(doc.createTextNode(formAcessoBT.getValue() ? "S" : "N"));
		parametros.appendChild(possuiAcessoBTElem);

		Element boletoEmailElem = doc.createElement("RECEBEBOLETOEMAIL");
		boletoEmailElem.appendChild(doc.createTextNode(formBoletoEmail.getValue() ? "S" : "N"));
		parametros.appendChild(boletoEmailElem);

		Element notaEmailElem = doc.createElement("RECEBENOTAEMAIL");
		notaEmailElem.appendChild(doc.createTextNode(formNotaEmail.getValue() ? "S" : "N"));
		parametros.appendChild(notaEmailElem);

		Element recAviCobElem = doc.createElement("RECAVICOB");
		recAviCobElem.appendChild(doc.createTextNode(formRecAviCob.getValue() ? "S" : "N"));
		parametros.appendChild(recAviCobElem);
		
		Element alteraContatosElem = doc.createElement("ALTERACONTATOS");
		alteraContatosElem.appendChild(doc.createTextNode(formAlteraContatos.getValue() ? "S" : "N"));
		parametros.appendChild(alteraContatosElem);
		
		Element alteraInfoParceiroElem = doc.createElement("ALTERAINFOPARCEIRO");
		alteraInfoParceiroElem.appendChild(doc.createTextNode(formAlteraInfoParceiro.getValue() ? "S" : "N"));
		parametros.appendChild(alteraInfoParceiroElem);
		
		Element liberaCNPJElem = doc.createElement("LIBERACNPJ");
		liberaCNPJElem.appendChild(doc.createTextNode(formLiberaCNPJ.getValue() ? "S" : "N"));
		parametros.appendChild(liberaCNPJElem);

		doc.appendChild(parametros);

		service.call("PORCLI@SolicitacaoContatosAutorizadosService", doc, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				modalContato.hide();
				botaoSalvarContato.state().reset();
				Alert.showSuccess("Solicitação realizada", "Por favor, aguarde a confirmação.");
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				// TODO Auto-generated method stub
				return false;
			}

		});

	}

	private void refreshPage() {

		botaoAtivar.setEnabled(false);
		botaoInativar.setEnabled(false);
		botaoModalEditar.setEnabled(false);

		int index = rowThumbnails.getWidgetCount();
		for (int i = 0; i < index; i++) {
			rowThumbnails.remove(rowThumbnails.getWidget(0));
		}
		buscaContatos();
	}

}
