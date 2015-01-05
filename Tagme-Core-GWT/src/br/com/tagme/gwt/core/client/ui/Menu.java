package br.com.tagme.gwt.core.client.ui;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.constants.IconType;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.auth.AppAuthEvent;
import br.com.tagme.gwt.commons.client.auth.AppAuthEventHandler;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.core.client.CoreEntryPoint;
import br.com.tagme.gwt.core.client.place.AtendimentoOnlinePlace;
import br.com.tagme.gwt.core.client.place.BoletoPlace;
import br.com.tagme.gwt.core.client.place.CNPJLicenciadosPlace;
import br.com.tagme.gwt.core.client.place.ConsultaOSPlace;
import br.com.tagme.gwt.core.client.place.ContatosAutorizadosPlace;
import br.com.tagme.gwt.core.client.place.InformacoesCadastraisPlace;
import br.com.tagme.gwt.core.client.place.InserirOSPlace;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Menu extends Composite {

	private static MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);

	interface MenuUiBinder extends UiBinder<Widget, Menu> {
	}
	
	@UiField PanelGroup menuAccordion;
	private ArrayList<MenuItem> logicalMenuAccordion = new ArrayList<MenuItem>();
	
	public Menu() {
		initWidget(uiBinder.createAndBindUi(this));
		
		AppAuth.getInstance().addAppAuthHandler(new AppAuthEventHandler() {
			
			@Override
			public void onAppAuthChange(AppAuthEvent event) {
				menuAccordion.clear();
				logicalMenuAccordion = new ArrayList<MenuItem>();
				
				MenuItem infCadMenu = new MenuItem("Informações Cadastrais", IconType.BUILDING_O);
				infCadMenu.addMenuChild("Dados Cadastrais", new InformacoesCadastraisPlace("home"));
				infCadMenu.addMenuChild("CNPJs Licenciados", new CNPJLicenciadosPlace("home"));
				infCadMenu.addMenuChild("Contatos Autorizados", new ContatosAutorizadosPlace("home"));
				if(!infCadMenu.isEmpty()){
					menuAccordion.add(infCadMenu);
					logicalMenuAccordion.add(infCadMenu);
				}
				
				MenuItem ateOnlMenu = new MenuItem("Atendimento Online", IconType.COMMENTS);
				ateOnlMenu.addMenuChild("Iniciar Atendimento", new AtendimentoOnlinePlace("home"));
				if(!ateOnlMenu.isEmpty()){
					menuAccordion.add(ateOnlMenu);
					logicalMenuAccordion.add(ateOnlMenu);
				}
				
				MenuItem autEcfMenu = new MenuItem("Autorização ECF", IconType.PRINT);
				autEcfMenu.addMenuChild("Emitir Autorização ECF", null);
				if(!autEcfMenu.isEmpty()){
					menuAccordion.add(autEcfMenu);	
					logicalMenuAccordion.add(autEcfMenu);
				}
				
				MenuItem ordSerMenu = new MenuItem("Ordens de Serviço", IconType.FILE_TEXT);
				ordSerMenu.addStyleName("menu-item-os");
				ordSerMenu.addMenuChild("Consultar Ordens de Serviço", new ConsultaOSPlace("home"));
				ordSerMenu.addMenuChild("Incluir Solicitação", new InserirOSPlace("home"));
				ordSerMenu.addMenuChild("Histórico de Atendimentos", null);
				if(!ordSerMenu.isEmpty()){
					menuAccordion.add(ordSerMenu);
					logicalMenuAccordion.add(ordSerMenu);
				}
				
				MenuItem movFinMenu = new MenuItem("Movimentações Financeiras", IconType.USD);
				movFinMenu.addMenuChild("2ª Via de Boleto", new BoletoPlace("home"));
				movFinMenu.addMenuChild("Extrato Financeiro", null);
				if(!movFinMenu.isEmpty()){
					menuAccordion.add(movFinMenu);	
					logicalMenuAccordion.add(movFinMenu);
				}
				
				MenuItem solSnkMenu = new MenuItem(CoreEntryPoint.isJiva() ? "Soluções Jiva" : "Soluções Sankhya", IconType.STAR);
				solSnkMenu.addMenuChild("Visualizar Soluções Adquiridas", null);
				if(!solSnkMenu.isEmpty()){
					menuAccordion.add(solSnkMenu);	
					logicalMenuAccordion.add(solSnkMenu);
				}
				
				MenuItem projetMenu = new MenuItem("Projetos", IconType.ARCHIVE);
				projetMenu.addMenuChild("Consultar Projetos", null);
				if(!projetMenu.isEmpty()){
					menuAccordion.add(projetMenu);	
					logicalMenuAccordion.add(projetMenu);
				}
				
				MenuItem depoimMenu = new MenuItem("Depoimentos", IconType.QUOTE_LEFT);
				depoimMenu.addMenuChild("Enviar Depoimento", null);
				if(!depoimMenu.isEmpty()){
					menuAccordion.add(depoimMenu);	
					logicalMenuAccordion.add(depoimMenu);
				}
				
				MenuItem docsMenu = new MenuItem("Documentos", IconType.FOLDER_OPEN);
				docsMenu.addStyleName("menu-item-documentos");
				docsMenu.addMenuChild("Visualizar documentos", null, new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						if(CoreEntryPoint.isJiva()){
							Alert.showError("Disponível em breve", "Todos os documentos da área restrita atual estarão, em breve, aqui disponíveis. Por enquanto, para acessá-los, utilize o link <a href='http://restrita.jiva.com.br/' target='_blank'>http://restrita.jiva.com.br/</a>");
						}else{
							Alert.showError("Disponível em breve", "Todos os documentos da área restrita atual estarão, em breve, aqui disponíveis. Por enquanto, para acessá-los, utilize o link <a href='http://restrita.sankhya.com.br/' target='_blank'>http://restrita.sankhya.com.br/</a>");
						}
					}
				});
				if(!docsMenu.isEmpty()){
					menuAccordion.add(docsMenu);	
					logicalMenuAccordion.add(docsMenu);
				}
				
				logicalMenuAccordion.get(logicalMenuAccordion.size()-1).setLast();
			}
			
		});
		
	}
	
	public void setMenuItemChildSelected(AbstractPlace currentPlace){
		MenuItem parentMenuItem = null;
		
		for(MenuItem menuItem : logicalMenuAccordion){
			boolean anyChildSelected = menuItem.setChildSelectedIfTarget(currentPlace);
			
			if(anyChildSelected && parentMenuItem == null){
				parentMenuItem = menuItem;
			}
		}
		
		if(parentMenuItem != null){
			parentMenuItem.openMenuChildren();
		}
	}
}
