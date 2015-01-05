package br.com.tagme.gwt.core.client.ui.pages;

import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.components.Alert.ConfirmCallback;
import br.com.tagme.gwt.commons.client.components.GenericData;
import br.com.tagme.gwt.commons.client.components.card.SelectableCardEventHandler;
import br.com.tagme.gwt.commons.client.components.card.SelectionEvent;
import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.commons.client.xml.EntityParser;
import br.com.tagme.gwt.commons.client.xml.EntityParser.FormItemBuilder;
import br.com.tagme.gwt.commons.utils.client.FormatterUtils;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.core.client.ui.BoletoCard;
import br.com.tagme.gwt.core.client.ui.BoletoCardBack;
import br.com.tagme.gwt.core.client.ui.SelectableCard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class BoletoPage extends DefaultCompositeWithPresenter {

	private static BoletoPageUiBinder	uiBinder		= GWT.create(BoletoPageUiBinder.class);

	private String						currSkwSessionid;
	private Map<String, SelectableCard>	mapCardWidgets	= new HashMap<String, SelectableCard>();
	private Map<String, GenericData>	dataProvider	= new HashMap<String, GenericData>();
	private static Map<String, IconType> mapNaturezaIcon = new HashMap<String, IconType>();
	
	static{
		mapNaturezaIcon.put("Licença de Uso", IconType.STAR);
		mapNaturezaIcon.put("Mensalidade", IconType.CALENDAR);
		mapNaturezaIcon.put("Customização", IconType.PUZZLE_PIECE);
		mapNaturezaIcon.put("Implantação", IconType.GEARS);
		mapNaturezaIcon.put("Serviços", IconType.GROUP);
		mapNaturezaIcon.put("Treinamento/Ead", IconType.GRADUATION_CAP);
		mapNaturezaIcon.put("Comissões Parcerias", IconType.ARROW_RIGHT);
		mapNaturezaIcon.put("Horas Cruzadas", IconType.CLOCK_O);
		mapNaturezaIcon.put("Viagem", IconType.PLANE);
		mapNaturezaIcon.put("Outros", IconType.ASTERISK);
	}

	interface BoletoPageUiBinder extends UiBinder<Widget, BoletoPage> {
	}

	@UiField Column			boletoPageContainer;
	@UiField FormPanel		loginBoletoForm;
	@UiField Row			cards;
	@UiField Button			botaoImpr;
	@UiField Button			botaoReneg;
	@UiField Row 			legenda;

	private boolean	isRenegociacao;
	private String	currNuFin;
	private int qtdTitulos = 0;

	public BoletoPage(DefaultActivity presenter) {
		super(presenter);
		
		initWidget(uiBinder.createAndBindUi(this));

		loginBoletoForm.setMethod(FormPanel.METHOD_POST);
		loginBoletoForm.setAction(SchemeReference.SKW_URL + "/mge/MGEIntegracao?servico=guestacess&codUsuConvidado=" + SchemeReference.SKW_CODUSU + "&senhaUsuConvidado=" + SchemeReference.SKW_ENCODEDPASS + "&redirect=" + SchemeReference.SKP_URL + "/views/jsessionid.jsp");
		
		buscaTitulos();
		
		loginBoletoForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {

				currSkwSessionid = event.getResults().trim();

				if (currSkwSessionid != null) {
					showBoleto(currSkwSessionid, isRenegociacao);
				}
			}
		});

	}
	
	private void showBoleto(String currSkwSessionid, boolean isRenegocicao){
		XMLServiceProxy service = new XMLServiceProxy(this);

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");

		Element jsessionidElem = doc.createElement("jsessionid");
		jsessionidElem.appendChild(doc.createTextNode(currSkwSessionid));
		parametros.appendChild(jsessionidElem);

		Element gerarNumeroBoleto = doc.createElement("gerarNumeroBoleto");
		gerarNumeroBoleto.appendChild(doc.createTextNode(Boolean.toString(isRenegocicao)));
		parametros.appendChild(gerarNumeroBoleto);

		Element nuFinElem = doc.createElement("nuFin");
		nuFinElem.appendChild(doc.createTextNode(currNuFin));
		parametros.appendChild(nuFinElem);

		doc.appendChild(parametros);

		service.call("PORCLI@ChaveBoletoService", doc, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				Node boletoNode = response.getElementsByTagName("boleto").item(0);
				String chaveBoleto = ((Element) boletoNode).getAttribute("valor");
				Window.open(SchemeReference.SKW_URL + "/mge/visualizadorArquivos.mge?chaveArquivo=" + chaveBoleto, "_blank", "");
				if(isRenegociacao){
					Window.alert("renegociacao");
					int index = cards.getWidgetCount();
					for (int i = 0; i < index; i++) {
						cards.remove(cards.getWidget(0));
					}
					buscaTitulos();
				}
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}

		});
	}

	private void buscaTitulos() {
		botaoImpr.setEnabled(false);
		botaoReneg.setEnabled(false);
		
		qtdTitulos = 0;
		XMLServiceProxy service = new XMLServiceProxy(this);
		
		service.call("PORCLI@BuscaTitulosParceiroService", null, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {

				EntityParser.buildFromXml(response, new FormItemBuilder() {

					@Override
					public void buildItem(GenericData genericData) {

						String status;
						String styleName;
						LabelType labelType;
						boolean allowSelection = true;

						if(Boolean.parseBoolean(genericData.getValueAsString("IS_RENEGOCIAVEL"))){
							if (Boolean.parseBoolean(genericData.getValueAsString("IS_VENCIDO"))) {
								if ("S".equals(genericData.getValueAsString("IS_RENEGOCIADO"))) {
									labelType = LabelType.DANGER;
									status = "Não Renegociável";
									styleName = "danger";
									allowSelection = false;
								} else {
									labelType = LabelType.WARNING;
									status = "Vencido";
									styleName = "warning";
								}
							} else {
								labelType = LabelType.SUCCESS;
								status = "À vencer";
								styleName = "success";
							}
						}else{
							labelType = LabelType.DANGER;
							status = "Não Renegociável";
							styleName = "danger";
							allowSelection = false;
						}

						String nuFin = genericData.getValueAsString("NUFIN");
						String natureza = genericData.getValueAsString("NATUREZA");
						String dtVenc = genericData.getValueAsString("DTVENC");
						String vlrTotal = FormatterUtils.formatCurrency(genericData.getValueAsString("VLRTOTAL"));
						String descrNatureza = genericData.getValueAsString("DESCR_NATUREZA");
						String dtVencAnt = genericData.getValueAsString("DTVENCANT");
						String nomeContatoReneg = genericData.getValueAsString("NOMECONTATORENEG");
						
						BoletoCard boleto = new BoletoCard(mapNaturezaIcon.get(natureza), dtVenc, FormatterUtils.formatOverflowText(natureza, 20), vlrTotal, status, labelType);
						SelectableCard card = new SelectableCard(nuFin, "card-boleto " + styleName, boleto,  new BoletoCardBack(descrNatureza, StringUtils.isEmpty(dtVencAnt) ? dtVenc : dtVencAnt, nomeContatoReneg), "XS_12,SM_4,MD_4,LG_3", allowSelection);
						card.addHandler(new SelectableCardEventHandler() {

							@Override
							public void onSelection(SelectionEvent event) {
								if (event.isSelected()) {

									SelectableCard card = (SelectableCard) event.getSource();

									GenericData genericData = dataProvider.get(card.getId());
							
									if(Boolean.parseBoolean(genericData.getValueAsString("IS_RENEGOCIAVEL"))){
										if (Boolean.parseBoolean(genericData.getValueAsString("IS_VENCIDO"))) {
											if("S".equals(genericData.getValueAsString("IS_RENEGOCIADO"))){
												botaoImpr.setEnabled(false);
												botaoReneg.setEnabled(false);
											}else{
												botaoImpr.setEnabled(false);
												botaoReneg.setEnabled(true);
											}
										} else {
											botaoImpr.setEnabled(true);
											botaoReneg.setEnabled(false);
										}
									}else{
										botaoImpr.setEnabled(false);
										botaoReneg.setEnabled(false);
									}
									

									for (String id : mapCardWidgets.keySet()) {
										if (!card.getId().equals(id)) {
											mapCardWidgets.get(id).removeSelection();
										}
									}

								} else {
									botaoImpr.setEnabled(false);
									botaoReneg.setEnabled(false);
								}
							}
						});

						mapCardWidgets.put(nuFin, card);
						cards.add(card);
						legenda.setVisible(true);
						qtdTitulos++;
						dataProvider.put(nuFin, genericData);
					}
				});
				
				if(qtdTitulos == 0){
					legenda.setVisible(false);
					Column container = new Column("XS_12");
					container.add(br.com.tagme.gwt.commons.client.components.Alert.buildLeadMessage("Nenhum título para ser exibido."));
					cards.add(container);
				}

			}

			@Override
			public boolean onError(ServiceProxyException e) {
				Alert.showError("Ocorreu um erro ao buscar seus títulos. Por favor, entre em contato com a central.");
				return true;
			}

		});

	}

	@UiHandler("botaoImpr")
	void imprimir(ClickEvent e) {
		for (String widgetId : mapCardWidgets.keySet()) {
			SelectableCard widget = mapCardWidgets.get(widgetId);
			if (widget.isSelected()) {
				GenericData genericData = dataProvider.get(widgetId);
				geraChaveBoleto(genericData.getValueAsString("NUFIN"), false);
				break;
			}
		}
	}

	@UiHandler("botaoReneg")
	void renegociar(ClickEvent e) {
		for (String widgetId : mapCardWidgets.keySet()) {
			SelectableCard widget = mapCardWidgets.get(widgetId);
			if (widget.isSelected()) {
				final GenericData genericData = dataProvider.get(widgetId);

				Alert.confirm("Este título será renegociado com data de vencimento para o próximo dia útil, com juros de 0,08% ao dia e multa de 2%.", new ConfirmCallback(){

					@Override
					public void onConfirm() {
						renegociaBoletoVencido(genericData.getValueAsString("NUFIN"));
					}

					@Override
					public void onCancel() {						
					}
					
				});
			}
		}
	}

	private void renegociaBoletoVencido(final String nuFin) {

		XMLServiceProxy service = new XMLServiceProxy(this);

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");
		doc.appendChild(parametros);

		Element financeiro = doc.createElement("nuFin");
		financeiro.appendChild(doc.createTextNode(nuFin));
		parametros.appendChild(financeiro);

		service.call("PORCLI@RenegociaBoletoVencidoService", doc, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
				geraChaveBoleto(nuFin, true);
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}

		});

	}

	private void geraChaveBoleto(String nuFin, boolean isRenegociacao) {
		currNuFin = nuFin;
		this.isRenegociacao = isRenegociacao;

		if(currSkwSessionid == null){
			loginBoletoForm.submit();
		}else{
			showBoleto(currSkwSessionid, isRenegociacao);
		}
	}
	
	public String getCurrSkwSessionId(){
		return currSkwSessionid;
	}

}
