package br.com.tagme.gwt.core.client.activity.factory;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.BreadcumbPlace;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.place.ErrorPlace;
import br.com.tagme.gwt.core.client.activity.DefaultInnerPageActivity;

public class CoreActivityFactory implements IActivityFactory{

		@Override
		public DefaultActivity createActivity(AbstractPlace targetPlace) {
			if(targetPlace instanceof ErrorPlace){
				return new DefaultInnerPageActivity((BreadcumbPlace) targetPlace);
			}
			return null;
		}
		
}
