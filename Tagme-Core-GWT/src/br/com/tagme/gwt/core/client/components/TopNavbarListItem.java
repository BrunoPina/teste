package br.com.tagme.gwt.core.client.components;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.ListItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;

public class TopNavbarListItem extends AnchorListItem{
	
	private AbstractPlace targetPlace;
	
	public TopNavbarListItem(String text){
		this(text, null);
	}
	
	public TopNavbarListItem(String text, AbstractPlace place){
		super(text);
		this.targetPlace = place;
		
		if(place != null){
			addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					if(targetPlace != null){
						AppPlaceController.goTo(targetPlace);
					}
				}
			});
		}
	}
	
	public AbstractPlace getTartetPlace(){
		return targetPlace;
	}
	
}
