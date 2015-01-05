package br.com.tagme.gwt.commons.client.components.formitem;

import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.InputType;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.Trigger;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import br.com.tagme.gwt.commons.client.components.formitem.FormItemWidget.ValueCommitCommand;
import br.com.tagme.gwt.commons.utils.client.StringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class FormItem<T> extends Composite {

	private static FormItemUiBinder	uiBinder	= GWT.create(FormItemUiBinder.class);

	@SuppressWarnings("rawtypes")
	interface FormItemUiBinder extends UiBinder<Widget, FormItem> {
	}

	public static final int TYPE_LABEL = 0;
	public static final int TYPE_TEXTBOX = 1;
	public static final int TYPE_INPUT_PASSWORD = 2;
	public static final int TYPE_CHECKBOX = 3;
	
	private int widgetType = TYPE_LABEL;
	
	private FormItemWidget<T> widget;
	
	@UiField FormGroup formGroup;
	@UiField Column leftColumn;
	@UiField Column rightColumn;
	private Tooltip tooltip;
	private String dataField;
	
	private String rightColumnDefaultStyle;
	private String leftColumnDefaultStyle;
	private String initialValue;
	
	@SuppressWarnings("unchecked")
	public @UiConstructor FormItem(String label, int type) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.widgetType = type;
		
		if(TYPE_CHECKBOX != type){
			FormLabel formLabel = new FormLabel();
			formLabel.setText(label);
			formLabel.addStyleName("form-label");
			leftColumn.add(formLabel);
		}
		
		switch (type) {
			case TYPE_LABEL:
				widget = (FormItemWidget<T>) new FormItemStaticLabel();
				break;
			case TYPE_INPUT_PASSWORD:
				widget = (FormItemWidget<T>) new FormItemInput();
				((Input) widget).setType(InputType.PASSWORD);
				break;
			case TYPE_TEXTBOX:
				widget = (FormItemWidget<T>) new FormItemTextBox();
				break;
			case TYPE_CHECKBOX:
				widget = (FormItemWidget<T>) new FormItemCheckbox();
				widget.setText(label);
				break;
			default:
				break;
		}
		
		tooltip = new Tooltip((Widget) widget);
		tooltip.setPlacement(Placement.TOP);
		tooltip.setTrigger(Trigger.MANUAL);
		
		rightColumn.add(tooltip);
	}
	
	public void setLeftLabel(String label){
		FormLabel formLabel = new FormLabel();
		formLabel.setText(label);
		formLabel.addStyleName("form-label");
		leftColumn.clear();
		leftColumn.add(formLabel);
	}

	public void setValue(String value){
		initialValue = value;
		switch (widgetType) {
			case TYPE_LABEL:
				if(value != null){
					widget.setText(value);
				}
				break;
			case TYPE_INPUT_PASSWORD:
				if(value != null){
					((Input) widget).setText(value);
				}
				break;
			case TYPE_TEXTBOX:
				if(value != null){
					((TextBox) widget).setText(value);
				}
				break;
			case TYPE_CHECKBOX:
				if(value != null){
					((CheckBox) widget).setActive("S".equals(value));
				}
				break;
			default:
				break;
		}
	}
	
	public boolean hasChanged(){
		String oldValue = StringUtils.getNullAsEmpty(initialValue);
		String newValue = StringUtils.getNullAsEmpty(getValueAsString());
		
		return !oldValue.equals(newValue);
	}
	
	public void setPlaceHolder(String placeHolder){
		switch (widgetType) {
			case TYPE_INPUT_PASSWORD:
				if(!StringUtils.isEmpty(placeHolder)){
					((Input) widget).setPlaceholder(placeHolder);
				}
				break;
			case TYPE_TEXTBOX:
				if(!StringUtils.isEmpty(placeHolder)){
					((TextBox) widget).setPlaceholder(placeHolder);
				}
				break;
			default:
				break;
		}
	}
	
	public String getValueAsString(){
		return widget.getValueAsString();
	}
	
	public void setDataField(String dataField){
		this.dataField = dataField;
	}
	
	public String getDataField(){
		return dataField;
	}
	
	public void setLeftColumnSize(ColumnSize leftColumnSize){
		leftColumn.addSize(leftColumnSize);
	}
	
	public void setRightColumnSize(ColumnSize rightColumnSize){
		rightColumn.addSize(rightColumnSize);
	}
	
	public void setEnabled(boolean enabled){
		widget.setEnabled(enabled);
	}
	
	public void setAddStyleNames(String styleNames){
		formGroup.addStyleName(styleNames);
	}
	
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> valueChangeHandler){
		if(valueChangeHandler != null){
			return widget.addValueChangeHandler(valueChangeHandler);
		}
		return null;
	}
	
	public void addOnValueCommitCommand(ValueCommitCommand command){
		widget.addOnValueCommitCommand(command);
	}
	
	public void createAndShowTooltip(String message, ValidationState state){
		createAndShowTooltip(message, state, null);
	}
	
	public void createAndShowTooltip(final String message, final ValidationState state, final Placement customPlacement){
		hideTooltip();
		SchedulerImpl.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				String styleSufix = "-tooltip";
				
				if(customPlacement != null){
					tooltip.setPlacement(customPlacement);
					
					if(!customPlacement.equals(Placement.TOP)){
						styleSufix = styleSufix+"-"+customPlacement.getCssName();
					}
				}else{
					tooltip.setPlacement(Placement.TOP);
				}
				
				if(rightColumnDefaultStyle == null){
					rightColumnDefaultStyle = rightColumn.getStyleName();
				}
				rightColumn.setStyleName(rightColumnDefaultStyle);
				if(leftColumnDefaultStyle == null){
					leftColumnDefaultStyle = leftColumn.getStyleName();
				}
				leftColumn.setStyleName(leftColumnDefaultStyle);
				
				rightColumn.addStyleName(state.getCssName()+styleSufix);
				
				tooltip.setTitle(message);
				tooltip.reconfigure();
				tooltip.show();
				
				formGroup.setValidationState(state);
			}
		});
	}
	
	public void hideTooltip(){
		tooltip.hide();
		formGroup.setValidationState(ValidationState.NONE);
	}

	public void setName(String inputName){
		widget.setName(inputName);
	}
	
	public void setAutoComplete(boolean autoComplete){
		if(widget instanceof TextBox){
			((TextBox) widget).setAutoComplete(autoComplete);
		}
	}
	
	public FormItemWidget<T> getFormItemWidget(){
		return this.widget;
	}

}
