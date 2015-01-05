package br.com.tagme.gwt.commons.client.components.formitem;

import org.gwtbootstrap3.client.ui.CheckBox;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class FormItemCheckbox extends CheckBox implements FormItemWidget<Boolean>{

	@Override
	public void addOnValueCommitCommand(final br.com.tagme.gwt.commons.client.components.formitem.FormItemWidget.ValueCommitCommand command) {
		
		if(command != null){
			final HandlerRegistration reg = super.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					command.validate();
				}
			});
			
			addAttachHandler(new Handler() {
				
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					if(!event.isAttached()){
						reg.removeHandler();
					}
				}
			});
		}
		
	}

	@Override
	public String getValueAsString() {
		return super.getValue() == true ? "S" : "N";
	}
	
	



}
