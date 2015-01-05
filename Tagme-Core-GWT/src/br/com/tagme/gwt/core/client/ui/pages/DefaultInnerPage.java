package br.com.tagme.gwt.core.client.ui.pages;

import java.util.HashMap;

import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Breadcrumbs;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.ListGroupItem;
import org.gwtbootstrap3.client.ui.constants.AlertType;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceHistoryMapperWithFactory;
import br.com.tagme.gwt.mvp.client.base.IBreadcumbPlace;
import br.com.tagme.gwt.core.client.CoreEntryPoint;
import br.com.tagme.gwt.core.client.ui.Menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.impl.AbstractPlaceHistoryMapper.PrefixAndToken;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class DefaultInnerPage extends DefaultCompositeWithPresenter {

	private static DefaultInnerPageUiBinder	uiBinder	= GWT.create(DefaultInnerPageUiBinder.class);
	
	interface DefaultInnerPageUiBinder extends UiBinder<Widget, DefaultInnerPage> {
	}
	
	private static HashMap<String, String> 		MESSAGES = new HashMap<String, String>();
	private static String 						DEFAULT_MESSAGE = "Ocorreu um erro ao carregar a página. Por favor, entre em contato com a central.";
	private static DefaultInnerPage 			INSTANCE;
	private static boolean 						initialized = false;
	
	@UiField Breadcrumbs breadcrumbs;
	@UiField Column contentContainer;
	@UiField Column menuContainer;
	private Menu menu;
	
	static{
		MESSAGES.put("naoautorizado", "Você não está autorizado a executar esta operação. Solicite acesso ao responsável pelo ERP Sankhya em sua organização.");
		MESSAGES.put("naoencontrado", "Não foi possível encontrar o local especificado. Por favor, verifique o link da página.");
	}
	
	private DefaultInnerPage(Menu menu) {
		super();
		this.menu = menu;
	}
	
	public static DefaultInnerPage getInstance(){
		if(!initialized){
			INSTANCE.initWidget(uiBinder.createAndBindUi(INSTANCE));
			INSTANCE.addMenuOnContainer();
			initialized = true;
		}
		
		return INSTANCE;
	}
	
	public static void setMenu(Menu menu){
		if(INSTANCE == null){
			INSTANCE = new DefaultInnerPage(menu);
		}
	}
	
	public void addMenuOnContainer(){
		menuContainer.add(menu);
	}
	
	private void buildView(Widget w, String locator){
		clearContainer();
		clearBreadcumb();
		
		if(getPresenter().getContextPlace() instanceof IBreadcumbPlace){
			IBreadcumbPlace breadcumbPlace = (IBreadcumbPlace) getPresenter().getContextPlace();
			
			for(int i=0; i<breadcumbPlace.getPathSize(); i++){
				
				final AbstractPlace targetPlace = breadcumbPlace.getPathItem(i);
				
				if(targetPlace != null && targetPlace.getDescriptor() != null){
					AnchorListItem pathItem = new AnchorListItem(targetPlace.getDescriptor());
					PrefixAndToken prefixAndToken = AppPlaceHistoryMapperWithFactory.buildPrefixAndToken(targetPlace);
					pathItem.setHref(prefixAndToken == null ? "#" : "#"+prefixAndToken.toString());
					
					/*pathItem.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							AppPlaceController.goTo(targetPlace);
						}
					});*/
					breadcrumbs.add(pathItem);
				}
				
			}
			
			ListGroupItem finalPathItem = new ListGroupItem();
			finalPathItem.setText(breadcumbPlace.getDescriptor());
			finalPathItem.setStyleName("active");
			breadcrumbs.add(finalPathItem);
		}
		
		menu.setMenuItemChildSelected(getPresenter().getContextPlace());
		
		if(w == null){
			setError(locator);
		}else{
			contentContainer.add(w);
		}
	}
	
	public void setView(Widget w){
		buildView(w, null);
	}
	
	public void setView(String messageLocator){
		buildView(null, messageLocator);
	}
	
	private void setError(String messageLocator){
		String mensagem = MESSAGES.get(messageLocator) == null ? DEFAULT_MESSAGE : MESSAGES.get(messageLocator);
		if(CoreEntryPoint.isJiva() && mensagem.contains("Sankhya")){
			mensagem = mensagem.replaceAll("Sankhya", "Jiva");	
		}
		Alert notAuthorizedAlert = new Alert(mensagem);
		notAuthorizedAlert.setType(AlertType.DANGER);
		
		contentContainer.insert(notAuthorizedAlert, 0);
	}
	
	private void clearContainer(){
		int widgetCount = contentContainer.getWidgetCount();
		
		for(int i=0; i < widgetCount; i++){
			contentContainer.remove(0);
		}
	}
	
	private void clearBreadcumb(){
		int widgetCount = breadcrumbs.getWidgetCount();
		
		for(int i=0; i < widgetCount; i++){
			breadcrumbs.remove(0);
		}
	}
	
}
