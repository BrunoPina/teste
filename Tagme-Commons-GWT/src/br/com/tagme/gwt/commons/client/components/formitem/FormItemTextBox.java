package br.com.tagme.gwt.commons.client.components.formitem;

import org.gwtbootstrap3.client.ui.TextBox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class FormItemTextBox extends TextBox implements FormItemWidget<String>{

	@Override
	public void addOnValueCommitCommand(final br.com.tagme.gwt.commons.client.components.formitem.FormItemWidget.ValueCommitCommand command) {
		
		if(command != null){
			final HandlerRegistration reg = super.addBlurHandler(new BlurHandler() {
				
				@Override
				public void onBlur(BlurEvent event) {
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
		return super.getValue();
	}

}
