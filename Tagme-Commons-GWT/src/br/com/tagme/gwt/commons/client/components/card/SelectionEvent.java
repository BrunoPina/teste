package br.com.tagme.gwt.commons.client.components.card;

import com.google.gwt.event.shared.GwtEvent;

public class SelectionEvent extends GwtEvent<SelectableCardEventHandler> {

	public static Type<SelectableCardEventHandler>	TYPE			= new Type<SelectableCardEventHandler>();

	private boolean isSelected;
	
	public SelectionEvent(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public Type<SelectableCardEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelectableCardEventHandler handler) {
		handler.onSelection(this);
	}

	public boolean isSelected() {
		return isSelected;
	}
	
}
