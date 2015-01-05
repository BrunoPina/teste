package br.com.tagme.gwt.mvp.client.base;

import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;

public interface IActivityFactory {
	public DefaultActivity createActivity(AbstractPlace targetPlace);
}