package br.com.tagme.gwt.commons.client.components;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Widget;

public class ButtonContainer extends Column{

	private int maxButtonSize = 0;
	
	public ButtonContainer(){
		super("XS_12");
		setContainerType();
	}
	
	public ButtonContainer(String size) {
		super(size);
		setContainerType();
	}
	
	public ButtonContainer(ColumnSize size, Widget firstWidget, Widget[] otherWidgets) {
		super(size, firstWidget, otherWidgets);
		setContainerType();
	}

	public ButtonContainer(ColumnSize firstSize, ColumnSize[] otherSizes) {
		super(firstSize, otherSizes);
		setContainerType();
	}
	
	private void setContainerType(){
		super.addStyleName("right-text xs-center-text");
		super.addAttachHandler(new AttachEvent.Handler() {
			
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					ButtonContainer currInstance = (ButtonContainer) event.getSource(); 
					
					for(Widget w : currInstance.getChildren()){
						if(w.getOffsetWidth()+24 > maxButtonSize){
							maxButtonSize = w.getOffsetWidth()+24;
						}
					}
					
					for(Widget w : currInstance.getChildren()){
						w.setWidth(maxButtonSize+"px");
					}
				}
			}
		});
	} 
	
	@Override
	public void add(Widget child) {
		super.add(child);
		
		if(!(child instanceof Button)){
			throw new RuntimeException("Conteúdo deve ser do tipo Button");
		}else{
			child.addStyleName("action-button");
		}
	}

}
