package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Divider;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.Placement;

import br.com.tagme.gwt.commons.client.DefaultComposite;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.auth.AppAuthEvent;
import br.com.tagme.gwt.commons.client.auth.AppAuthEventHandler;
import br.com.tagme.gwt.commons.client.components.MenuListItem;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.ActivityController;
import br.com.tagme.gwt.core.client.CoreEntryPoint;
import br.com.tagme.gwt.core.client.place.AtendimentoOnlinePlace;
import br.com.tagme.gwt.core.client.place.BoletoPlace;
import br.com.tagme.gwt.core.client.place.CNPJLicenciadosPlace;
import br.com.tagme.gwt.core.client.place.ConsultaOSPlace;
import br.com.tagme.gwt.core.client.place.ContatosAutorizadosPlace;
import br.com.tagme.gwt.core.client.place.InformacoesCadastraisPlace;
import br.com.tagme.gwt.core.client.place.InserirOSPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class TopNavbar extends DefaultComposite {

	private static TopNavbarUiBinder uiBinder = GWT.create(TopNavbarUiBinder.class);

	interface TopNavbarUiBinder extends UiBinder<Widget, TopNavbar> {
	}
	
	@UiField NavbarCollapse navbarContainer;
	@UiField DropDownMenu dropDownMenu;
	@UiField Anchor logoAnchor;
	
	private Composite currUserWidget;
	private LoggedInUserWidget loggedInUser;
	private LoggedOutUserWidget loggedOutUser = new LoggedOutUserWidget();
	
	public TopNavbar() {
		initWidget(uiBinder.createAndBindUi(this));
		
		if(CoreEntryPoint.isJiva()){
			logoAnchor.setHref("#index");
		}
		
		AppAuth.getInstance().addAppAuthHandler(new AppAuthEventHandler() {
			
			@Override
			public void onAppAuthChange(AppAuthEvent event) {
				if(event.typeInfosChanged()){
					loggedInUser.refresh();
					return;
				}
				dropDownMenu.clear();
				
				boolean anyAdded = false;
				if(addDropDownChild("Dados Cadastrais", new InformacoesCadastraisPlace("home"))){ 
					anyAdded = true;
				}
				if(addDropDownChild("CNPJs Licenciados", new CNPJLicenciadosPlace("home"))){
					anyAdded = true;
				}
				if(addDropDownChild("Contatos Autorizados", new ContatosAutorizadosPlace("home"))){
					anyAdded = true;
				}
				if(anyAdded){
					addDivider();
				}
				
				anyAdded = false;
				if(addDropDownChild("Atendimento Online", new AtendimentoOnlinePlace("home"))){
					anyAdded = true;
				}
				if(addDropDownChild("Emitir Autorização ECF", null)){
					anyAdded = true;
				}
				if(anyAdded){
					addDivider();
				}
				
				anyAdded = false;
				if(addDropDownChild("Consultar Ordens de Serviço", new ConsultaOSPlace("home"))){
					anyAdded = true;
				}
				if(addDropDownChild("Incluir Solicitação", new InserirOSPlace("home"))){
					anyAdded = true;
				}
				if(addDropDownChild("Histórico de Atendimentos", null)){
					anyAdded = true;
				}
				if(anyAdded){
					addDivider();
				}
				
				anyAdded = false;
				if(addDropDownChild("2ª Via de Boleto", new BoletoPlace("home"))){
					anyAdded = true;
				}
				if(addDropDownChild("Extrato Financeiro", null)){
					anyAdded = true;
				}
				if(anyAdded){
					addDivider();
				}
				
				anyAdded = false;
				if(addDropDownChild("Visualizar Soluções Adquiridas", null)){
					anyAdded = true;
				}
				if(addDropDownChild("Consultar Projetos", null)){
					anyAdded = true;
				}
				if(anyAdded){
					addDivider();
				}
				
				addDropDownChild("Enviar Depoimento", null);
				
				if(currUserWidget != null){
					navbarContainer.remove(navbarContainer.getWidgetIndex(currUserWidget));
				}
				
				if(event.typeLoginSuccess() || event.typeLoginVerified()){
					loggedInUser =  new LoggedInUserWidget();
					currUserWidget = loggedInUser;
					loggedInUser.refresh();
				}else if(event.typeLoginFail() || event.typeLoginNotVerified()){
					currUserWidget = loggedOutUser;
				}
				
				navbarContainer.add(currUserWidget);
				
			}
		});
	}
	
	public void addDivider(){
		dropDownMenu.add(new Divider());
	}
	
	public boolean addDropDownChild(String label, AbstractPlace targetPlace){
		if(AppAuth.getInstance().isAuthenticated() && targetPlace != null && ActivityController.AUTHZ_TYPE_AUTHORIZED != AppAuth.getInstance().checkAuthorization(targetPlace)){
			return false;
		}
		
		if(targetPlace != null){
			MenuListItem menuChild = new MenuListItem(label, targetPlace);
			
			dropDownMenu.add(menuChild);
		}else{
			MenuListItem menuChild = new MenuListItem(label, targetPlace);
			menuChild.setEnabled(false);
			
			Tooltip tooltip = new Tooltip(menuChild);
			tooltip.setTitle("Disponível em breve");
			tooltip.setPlacement(Placement.LEFT);
			
			dropDownMenu.add(menuChild);
		}
		return true;
	}
	
	public void hideTopnavbar(){
		String styleName = navbarContainer.getStyleName();
		if(!StringUtils.isEmpty(styleName) && styleName.contains("in")){
			navbarContainer.hide();
		}
	}
	
}
