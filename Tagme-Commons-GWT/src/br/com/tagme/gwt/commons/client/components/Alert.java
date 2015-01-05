package br.com.tagme.gwt.commons.client.components;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Lead;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.html.Paragraph;
import org.gwtbootstrap3.extras.growl.client.ui.Growl;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlHelper;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlOptions;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlPosition;
import org.gwtbootstrap3.extras.growl.client.ui.GrowlTemplate;

import br.com.tagme.gwt.commons.client.components.formitem.FormItem;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

public class Alert {
	
	public static PopupPanel waitMessage;
	public static int waitMessageCount = 0;
	
	public static void showError(String title, String message){
		GrowlOptions opt = GrowlHelper.getNewOptions();
		opt.setDangerType();
		opt.setPauseOnMouseOver(true);
		opt.setDelay(0);
		opt.setZIndex(9990);
		opt.setOffset(70);
		opt.setFadeIn(700);
		GrowlPosition position = GrowlHelper.getNewPosition();
		position.setTop(true);
		position.setCenter();
		opt.setGrowlPosition(position);
		GrowlTemplate gt = GrowlHelper.getNewTemplate();
		gt.setMessage("<span/>");
		opt.setTemplateObject(gt);
		
		Growl.growl(title, message, Styles.FONT_AWESOME_BASE + " " + IconType.EXCLAMATION_CIRCLE.getCssName(), opt);
	}
	
	public static void showInfo(String title, String message){
		GrowlOptions opt = GrowlHelper.getNewOptions();
		opt.setInfoType();
		opt.setPauseOnMouseOver(true);
		opt.setDelay(0);
		opt.setZIndex(9990);
		opt.setOffset(70);
		opt.setFadeIn(700);
		GrowlPosition position = GrowlHelper.getNewPosition();
		position.setTop(true);
		position.setCenter();
		opt.setGrowlPosition(position);
		GrowlTemplate gt = GrowlHelper.getNewTemplate();
		gt.setMessage("<span/>");
		opt.setTemplateObject(gt);
		
		Growl.growl(title, message, Styles.FONT_AWESOME_BASE + " " + IconType.INFO_CIRCLE.getCssName(), opt);
	}
	
	public static void showError(String message){
		showError("Atenção", message);
	}
	
	public static void showSuccess(String title, String message){
		GrowlOptions opt = GrowlHelper.getNewOptions();
		opt.setSuccessType();
		opt.setPauseOnMouseOver(true);
		opt.setDelay(0);
		opt.setZIndex(9990);
		opt.setOffset(70);
		opt.setFadeIn(700);
		GrowlPosition position = GrowlHelper.getNewPosition();
		position.setTop(true);
		position.setCenter();
		opt.setGrowlPosition(position);
		
		Growl.growl(title, message, Styles.FONT_AWESOME_BASE + " " + IconType.CHECK.getCssName(), opt);
	}
	
	/*public static void showWarning(String title, String message){
		Growl.growl("Atenção", message, Styles.FONT_AWESOME_BASE + " " + IconType.EXCLAMATION_TRIANGLE.getCssName(), opt);
	}
	
	public static void showSuccess(String title, String message){
		Growl.growl("Sucesso", message, Styles.FONT_AWESOME_BASE + " " + IconType.CHECK.getCssName(), opt);
	}
	
	public static void showInfo(String title, String message){
		Growl.growl(title, message, Styles.FONT_AWESOME_BASE + " " + IconType.INFO.getCssName(), opt);
	}*/
	
	private static PopupPanel buildWaitMessage(){
		final PopupPanel simplePopup = new PopupPanel(false, false);
		simplePopup.addStyleName("wait-message");
		simplePopup.setAnimationEnabled(false);
		simplePopup.getElement().getStyle().setZIndex(9999);
		
		HorizontalPanel panel = new HorizontalPanel();
		
		HTML textPanel = new HTML("Carregando...");
		
		panel.add(textPanel);
		panel.setCellHorizontalAlignment(textPanel, HasHorizontalAlignment.ALIGN_CENTER);
		
		simplePopup.setWidget(panel);
		
		return simplePopup;
	}
	
	public static void showWaitMessage() {
		if (waitMessage == null) {
			waitMessage = buildWaitMessage();
		}

		waitMessageCount++;

		if (!waitMessage.isShowing()) {
			waitMessage.setPopupPositionAndShow(new PositionCallback() {

				@Override
				public void setPosition(int offsetWidth, int offsetHeight) {
					int x = (Window.getClientWidth() / 2) - (offsetWidth / 2);
					int y = 40;

					waitMessage.setPopupPosition(x, y);
				}
			});
		}
	}

	public static void hideWaitMessage() {
		if (waitMessage != null) {
			if (waitMessageCount > 0) {
				waitMessageCount--;
			}

			if (waitMessageCount == 0) {
				waitMessage.hide();
			}
		}
	}
	
	public static Lead buildLeadMessage(String message){
		Lead lead = new Lead();
		lead.setText(message);
		lead.addStyleName("lead-message");
		return lead;
	}
	
	public static void confirm(String message, final ConfirmCallback confirmHandler){
		final Modal modal = new Modal();
		modal.getElement().setAttribute("tabindex", "-1");
		modal.setTitle("Confirmar");
		modal.setClosable(false);
		modal.setFade(true);
		ModalBody body = new ModalBody();
		body.add(new Paragraph(message));
		ModalFooter footer = new ModalFooter();
		modal.add(body);
		modal.add(footer);
		
		Button confirm = new Button("Ok");
		confirm.setType(ButtonType.SUCCESS);
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmHandler.onConfirm();
				modal.hide();
			}
		});
		Button cancel = new Button("Cancelar");
		cancel.setType(ButtonType.DANGER);
		cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmHandler.onCancel();
				modal.hide();
			}
		});
		footer.add(confirm);
		footer.add(cancel);
		modal.show();
	}
	
	public static void alert(String message, final ConfirmCallback confirmHandler){
		final Modal modal = new Modal();
		modal.getElement().setAttribute("tabindex", "-1");
		modal.setTitle("Atenção");
		modal.setClosable(false);
		modal.setFade(true);
		ModalBody body = new ModalBody();
		body.add(new Paragraph(message));
		ModalFooter footer = new ModalFooter();
		modal.add(body);
		modal.add(footer);
		
		Button confirm = new Button("Ok");
		confirm.setType(ButtonType.SUCCESS);
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmHandler.onConfirm();
				modal.hide();
			}
		});
		footer.add(confirm);
		modal.show();
	}
	
	public static void prompt(String title, String message, String label, final ConfirmTextCallback confirmHandler){
		final Modal modal = new Modal();
		modal.getElement().setAttribute("tabindex", "-1");
		modal.setTitle(title);
		modal.setClosable(false);
		modal.setFade(true);
		ModalBody body = new ModalBody();
		Paragraph p = new Paragraph(message);
		p.addStyleName("justify-text");
		body.add(p);
		FieldSet fs = new FieldSet();
		fs.addStyleName("form-horizontal");
		final FormItem<String> emailForm = new FormItem<String>(label, 1);
		emailForm.setLeftColumnSize(ColumnSize.SM_2);
		emailForm.setRightColumnSize(ColumnSize.SM_9);
		fs.add(emailForm);
		body.add(fs);
		ModalFooter footer = new ModalFooter();
		modal.add(body);
		modal.add(footer);
		
		Button confirm = new Button("Ok");
		confirm.setType(ButtonType.SUCCESS);
		confirm.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmHandler.onConfirm(emailForm.getValueAsString());
				modal.hide();
			}
		});
		Button cancel = new Button("Cancelar");
		cancel.setType(ButtonType.DANGER);
		cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				confirmHandler.onCancel();
				modal.hide();
			}
		});
		footer.add(confirm);
		footer.add(cancel);
		modal.show();
	}
	
	public interface ConfirmTextCallback{
		void onConfirm(String text);
		void onCancel();
	}
	
	public interface ConfirmCallback{
		void onConfirm();
		void onCancel();
	}
}
