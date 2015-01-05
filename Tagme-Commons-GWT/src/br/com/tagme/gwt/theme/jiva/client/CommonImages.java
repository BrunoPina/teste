package br.com.tagme.gwt.theme.jiva.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.RetinaImageResource;

public interface CommonImages extends br.com.tagme.gwt.theme.sankhya.client.CommonImages{
	
	ImageResource logo();
	ImageResource logosankhya();
	ImageResource logomarcasankhya();
	
	ImageResource oscar();
	ImageResource rosalina();
	
	public interface Retina extends CommonImages {
		
		RetinaImageResource logo();
		RetinaImageResource logosankhya();
		RetinaImageResource logomarcasankhya();
		
		RetinaImageResource oscar();
		RetinaImageResource rosalina();
		
	}
	
}
