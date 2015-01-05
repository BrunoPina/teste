package br.com.tagme.gwt.core.client.ui;

import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.ThumbnailLink;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.html.Paragraph;

import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceHistoryMapperWithFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.impl.AbstractPlaceHistoryMapper.PrefixAndToken;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class ClickableThumbnailWidget extends ThumbnailWidget {

	private static ClickableThumbnailWidgetUiBinder	uiBinder	= GWT.create(ClickableThumbnailWidgetUiBinder.class);

	@UiField ThumbnailLink thumbnailLink;
	@UiField Tooltip tooltip;
	final private AbstractPlace targetPlace;
	
	//private HandlerRegistration thumbnailLinkRegistration;
	
	@UiField Icon icon;
	@UiField Heading heading;
	@UiField Paragraph paragraph;
		
	interface ClickableThumbnailWidgetUiBinder extends UiBinder<Widget, ClickableThumbnailWidget> {
	}

	public ClickableThumbnailWidget(IconType iconType, String heading, String paragraph, AbstractPlace targetPlace) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.icon.setType(iconType);
		this.heading.setText(heading);
		this.paragraph.setText(paragraph);
		this.targetPlace = targetPlace;
		
		PrefixAndToken prefixAndToken = AppPlaceHistoryMapperWithFactory.buildPrefixAndToken(targetPlace);
		thumbnailLink.setHref(prefixAndToken == null ? "" : "#"+prefixAndToken.toString());
		
	 	/*thumbnailLinkRegistration = thumbnailLink.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(finalTargetPlace != null){
					AppPlaceController.goTo(finalTargetPlace);
				}
			}
		});*/
	 	
	}
	
	public AbstractPlace getTargetPlace(){
		return targetPlace;
	}
	
	public void removeLink(){
		//thumbnailLinkRegistration.removeHandler();
		thumbnailLink.setHref("#");
		thumbnailLink.addStyleName("disabled");
		tooltip.setTitle("Não Autorizado");
	}
	
}
