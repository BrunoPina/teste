package br.com.tagme.gwt.mvp.client.base;


import com.google.gwt.place.shared.PlaceController;

public class AppPlaceController {

	private static PlaceController placeController;
	
	/** 
	 * Utilizado para navegar entre {@link AbstractPlace}'s
	 */
	public static void setApplicationPlaceController(PlaceController controller){
		placeController = controller;
	}
	
	public static void goTo(AbstractPlace newPlace){
		if(placeController != null){
			placeController.goTo(newPlace);
		}
	}
	
	public static AbstractPlace getWhere(){
		return placeController == null ? null : (AbstractPlace) placeController.getWhere();
	}
	
}