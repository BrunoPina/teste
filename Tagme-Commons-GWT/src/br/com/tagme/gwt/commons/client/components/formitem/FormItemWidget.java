package br.com.tagme.gwt.commons.client.components.formitem;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

public interface FormItemWidget<T>{
	public void setText(String text);
	public void setEnabled(boolean setEnabled);
	
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler);
	public void addOnValueCommitCommand(ValueCommitCommand command);
	
	public interface ValueCommitCommand{
		public void validate();
	}
	
	public String getValueAsString();
	
	public void setName(String inputName);
}
