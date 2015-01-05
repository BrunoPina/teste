package br.com.tagme.gwt.core.client.ui;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Placement;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.components.MenuListItem;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.ActivityController;
import br.com.tagme.gwt.mvp.client.base.AppPlaceHistoryMapperWithFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.place.impl.AbstractPlaceHistoryMapper.PrefixAndToken;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MenuItem extends Composite {

	private static MenuItemUiBinder	uiBinder	= GWT.create(MenuItemUiBinder.class);

	private static int counter = 0;
	
	@UiField NavPills navPills;
	@UiField PanelCollapse pannelCollapse;
	@UiField Anchor anchorMenuItem;
	@UiField PanelHeader panelHeader;
	
	private ArrayList<MenuListItem> menuChildren = new ArrayList<MenuListItem>(); 
	
	interface MenuItemUiBinder extends UiBinder<Widget, MenuItem> {
	}

	public MenuItem(String label, IconType icon) {
		initWidget(uiBinder.createAndBindUi(this));
		
		counter++;
		
		anchorMenuItem.setText(label);
		anchorMenuItem.setIcon(icon);
		anchorMenuItem.setDataTarget("#collapseMenu"+counter);
		anchorMenuItem.setDataParent("#accordionMenu");
		
		anchorMenuItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			}
		});
		
		pannelCollapse.setId("collapseMenu"+counter);
		
	}
	
	public void addMenuChild(String label, AbstractPlace targetPlace){
		addMenuChild(label, targetPlace, null);
	}
	
	public void addMenuChild(String label, AbstractPlace targetPlace, ClickHandler handler){
		if(targetPlace != null && ActivityController.AUTHZ_TYPE_AUTHORIZED != AppAuth.getInstance().checkAuthorization(targetPlace)){
			return;
		}
		
		if(targetPlace != null){
			MenuListItem menuChild = new MenuListItem(label, targetPlace);
			
			menuChildren.add(menuChild);
			navPills.add(menuChild);
		}else{
			MenuListItem menuChild = new MenuListItem(label, targetPlace);
			menuChild.setEnabled(false);
			
			if(handler != null){
				menuChild.addClickHandler(handler);
				menuChild.setEnabled(true);
			}
			
			Tooltip tooltip = new Tooltip(menuChild);
			tooltip.setTitle("Disponível em breve");
			tooltip.setPlacement(Placement.LEFT);
			tooltip.setShowDelayMs(500);
			
			menuChildren.add(menuChild);
			navPills.add(tooltip);
		}
	}
	
	public boolean isEmpty(){
		return menuChildren.isEmpty();
	}

	public boolean setChildSelectedIfTarget(AbstractPlace currentPlace){
		boolean selected = false;
		for(MenuListItem child : menuChildren){
			
			PrefixAndToken menuPlaceToken = AppPlaceHistoryMapperWithFactory.buildPrefixAndToken(child.getTartetPlace());
			PrefixAndToken currentPlaceToken = AppPlaceHistoryMapperWithFactory.buildPrefixAndToken(currentPlace);
			
			if(menuPlaceToken != null && currentPlaceToken != null && menuPlaceToken.toString().equals(currentPlaceToken.toString())){
				child.addStyleName("menu-active");
				selected = true;
			}else{
				child.removeStyleName("menu-active");
			}
		}
		
		return selected;
	}
	
	
	public void openMenuChildren(){
		SchedulerImpl.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				String styleName = anchorMenuItem.getStyleName();
				if(!StringUtils.isEmpty(styleName) && styleName.contains("collapsed")){
					click(anchorMenuItem.getElement());
				}
			}
		});
	}
	
	public static native void click(Element element)/*-{
		element.click();
	}-*/;
	
	public void setLast(){
		pannelCollapse.addStyleName("last-menu-item");
		panelHeader.addStyleName("last-menu-item");
	}
	
}
