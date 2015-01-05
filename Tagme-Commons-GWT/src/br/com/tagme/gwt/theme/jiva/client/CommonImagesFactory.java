package br.com.tagme.gwt.theme.jiva.client;

import com.google.gwt.core.client.GWT;

public class CommonImagesFactory extends br.com.tagme.gwt.theme.sankhya.client.CommonImagesFactory{
	
	public CommonImages create() {
		return GWT.create(CommonImages.class);
	}
	
}
