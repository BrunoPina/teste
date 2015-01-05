package br.com.tagme.gwt.commons.client;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public class OneWidgetColumn extends Column implements AcceptsOneWidget{

	public OneWidgetColumn() {
		super(ColumnSize.XS_12);
	}

	@Override
	public void setWidget(IsWidget w) {
		if(w != null){
			int widgetCount = super.getWidgetCount();
			
			for(int i=0; i < widgetCount; i++){
				super.remove(0);
			}
			
			super.add(w);
		}
	}

}
