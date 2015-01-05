package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.extras.card.client.ui.Back;
import org.gwtbootstrap3.extras.card.client.ui.Card;
import org.gwtbootstrap3.extras.card.client.ui.Front;

import br.com.tagme.gwt.commons.client.components.card.SelectableCardEventHandler;
import br.com.tagme.gwt.commons.client.components.card.SelectionEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SelectableCard extends Composite implements HasHandlers {

	private static SelectableCardUiBinder	uiBinder	= GWT.create(SelectableCardUiBinder.class);

	private HandlerManager	handlerManager;

	interface SelectableCardUiBinder extends UiBinder<Widget, SelectableCard> {
	}

	@UiField Front cardFront;
	@UiField Back cardBack;
	@UiField Card card;
	
	@UiField Icon selectedIconFront;
	@UiField Icon selectedIconBack;
	
	@UiField Column column;

	private boolean	changeFaceClick	= false;
	private boolean	isSelected		= false;
	private SelectableCard instance;
	
	private Widget frontWidget;
	
	private String id;
	private boolean allowSelection = true;

	public SelectableCard(String id, String styleName, Widget frontWidget, Widget backWidget, String columnSizes, boolean selectable) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.instance = this;
		this.id = id;
		this.allowSelection = selectable;
		this.frontWidget = frontWidget;
		
		column.setSize(columnSizes);
		
		card.addStyleName(styleName);
		
		handlerManager = new HandlerManager(this);
		
		if(frontWidget != null){
			cardFront.add(frontWidget);
		}
		
		if(backWidget != null){
			cardBack.add(backWidget);
		}
		
		card.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!changeFaceClick) {
					if (isSelected) {
						removeSelection();
						instance.fireEvent(new SelectionEvent(false));
					} else {
						if(allowSelection){
							setSelected();
							instance.fireEvent(new SelectionEvent(true));
						}
					}
				}
				changeFaceClick = false;
			}

		}, ClickEvent.getType());

		
		cardFront.getWidget(0).addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeFaceClick = true;
			}

		}, ClickEvent.getType());

		cardBack.getWidget(0).addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				changeFaceClick = true;
			}

		}, ClickEvent.getType());
	}
	
	public Widget getFrontWidget(){
		return this.frontWidget;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	private void setSelected() {

		isSelected = true;

		cardFront.addStyleName("card-selected");
		cardBack.addStyleName("card-selected");
		selectedIconFront.addStyleName("card-selected-icon");
		selectedIconBack.addStyleName("card-selected-icon");
		selectedIconFront.setVisible(true);
		selectedIconBack.setVisible(true);
	}

	public void removeSelection() {

		isSelected = false;

		cardFront.removeStyleName("card-selected");
		cardBack.removeStyleName("card-selected");
		selectedIconFront.setVisible(false);
		selectedIconBack.setVisible(false);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addHandler(SelectableCardEventHandler handler) {
		return handlerManager.addHandler(SelectionEvent.TYPE, handler);
	}
	
	public String getId(){
		return id;
	}
}
