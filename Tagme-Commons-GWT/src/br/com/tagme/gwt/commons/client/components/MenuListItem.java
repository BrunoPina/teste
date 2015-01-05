package br.com.tagme.gwt.commons.client.components;

import org.gwtbootstrap3.client.ui.AnchorListItem;

import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceHistoryMapperWithFactory;

import com.google.gwt.place.impl.AbstractPlaceHistoryMapper.PrefixAndToken;

public class MenuListItem extends AnchorListItem{
	
	private AbstractPlace targetPlace;
	
	public MenuListItem(String text){
		this(text, null);
	}
	
	public MenuListItem(String text, AbstractPlace place){
		super(text);
		this.targetPlace = place;
		addStyleName("menu-link");
		
		if(place != null){
			PrefixAndToken prefixAndToken = AppPlaceHistoryMapperWithFactory.buildPrefixAndToken(targetPlace);
			super.setHref(prefixAndToken == null ? "#" : "#"+prefixAndToken.toString());
			/*addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
				}
			});*/
		}
	}
	
	public AbstractPlace getTartetPlace(){
		return targetPlace;
	}
	
}
