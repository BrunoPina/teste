package br.com.tagme.gwt.mvp.client.base;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.mvp.client.MVPEntryPoint;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.place.ErrorPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.TokenizerFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public class ActivityController implements ActivityMapper {

	public static final int	AUTHZ_TYPE_NOT_AUTHORIZED			= 0;
	public static final int	AUTHZ_TYPE_AUTHORIZED				= 1;
	public static final int	AUTHZ_TYPE_PARTIALLOGIN_REQUIRED 	= 2;
	public static final int	AUTHZ_TYPE_LOGIN_REQUIRED			= 3;
	public static final int	AUTHZ_TYPE_ALREADY_LOGGED_IN		= 4;
	public static final int AUTHZ_TYPE_REDIRECT					= 5;
	
	private static EventBus appPlaceEventBus;
	private static AbstractPlace lastTriedPlace;
	private static HashMap<String, ActivityController> singletons = new HashMap<String, ActivityController>();
	
	private ArrayList<IActivityFactory> factories = new ArrayList<IActivityFactory>();
	
	public static ActivityController get(AcceptsOneWidget contextWidget){
		ActivityController controller = singletons.get(String.valueOf(contextWidget.hashCode()));
		if(controller == null){
			controller = new ActivityController(contextWidget);
			singletons.put(String.valueOf(contextWidget.hashCode()), controller);
		}
		
		return controller;
	}
	
	private ActivityController(AcceptsOneWidget contextWidget) {
		super();
		
		if(appPlaceEventBus != null && contextWidget != null){
			ActivityManager activityManager = new ActivityManager(this, appPlaceEventBus);
			activityManager.setDisplay(contextWidget);
		}
	}
	
	@Override
	public Activity getActivity(Place place) {
		
		if(place instanceof AbstractPlace){
			final AbstractPlace abstractPlace = ((AbstractPlace) place);
			
			DefaultActivity targetActivity = null;
			for(IActivityFactory factory : factories){
				if(targetActivity != null){
					break;
				}
				//Ao identificar um novo place, cada ActivityController (ou seja, para cada container), busca uma atividade relacionado a ele em suas factories. A primeira factory que retornar uma atividade ganha o controle.
				targetActivity = factory.createActivity(abstractPlace);
			}
			
			int authorizationType = AppAuth.getInstance().checkAuthorization(abstractPlace);
			
			if(authorizationType != AUTHZ_TYPE_REDIRECT && targetActivity == null){
				AppPlaceController.goTo(new ErrorPlace("naoencontrado"));
				return null;
			}
			
			switch (authorizationType) {
				case AUTHZ_TYPE_AUTHORIZED:
					return targetActivity;
					
				case AUTHZ_TYPE_NOT_AUTHORIZED:
					AppPlaceController.goTo(new ErrorPlace("naoautorizado"));
					break;
				
				case AUTHZ_TYPE_PARTIALLOGIN_REQUIRED:
					/*Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						
						@Override
						public void execute() {
							AppAuth.getInstance().goToPartialLoginPlace(defaultPlace);
						}
					});*/
					break;
				
				case AUTHZ_TYPE_LOGIN_REQUIRED:
					lastTriedPlace = abstractPlace;
					AppPlaceController.goTo((AbstractPlace) TokenizerFactory.getTokenizer("login").getPlace("home"));
					break;
					
				case AUTHZ_TYPE_ALREADY_LOGGED_IN:
					AppPlaceController.goTo((AbstractPlace) TokenizerFactory.getTokenizer("index").getPlace("home"));
					break;
				case AUTHZ_TYPE_REDIRECT:
					if(lastTriedPlace == null){
						AppPlaceController.goTo((AbstractPlace) TokenizerFactory.getTokenizer("index").getPlace("home"));
					}else{
						AbstractPlace tempLastTriedPlace = lastTriedPlace;
						lastTriedPlace = null;
						AppPlaceController.goTo(tempLastTriedPlace);
					}
					break;
			}
			
		}
		
		return null;
	}
	
	public void addActivityFactory(IActivityFactory factory){
		factories.add(factory);
	}
	
	/** 
	 * Mesmo {@link EventBus} utilizado pelo {@link PlaceHistoryHandler} e {@link PlaceController}, no {@link MVPEntryPoint}.
	 */
	public static void setApplicationPlaceEventBus(EventBus eventBus){
		appPlaceEventBus = eventBus;
	}

}