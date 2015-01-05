package br.com.tagme.gwt.mvp.client;

import br.com.tagme.gwt.commons.client.DefaultEntryPoint;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.ActivityController;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;
import br.com.tagme.gwt.mvp.client.base.AppPlaceHistoryMapperWithFactory;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.place.ErrorPlace;
import br.com.tagme.gwt.mvp.client.place.RedirectPlace;
import br.com.tagme.gwt.mvp.client.tokenizer.TokenizerFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class MVPEntryPoint implements EntryPoint {
	
	private static final EventBus 				eventBus 			= new SimpleEventBus();
	private static final PlaceController 		placeController 	= new PlaceController(eventBus);
	private static final TokenizerFactory 		tokenizerFactory 	= new TokenizerFactory();
	private static PlaceHistoryHandler 			historyHandler;
	
	/**
	 * Inicia o MVP e regitra quatro places por padrao: 
	 * {@link IndexPlace} que gera um history do tipo "#index:locator" 
	 * {@link AbstractPlace} que gera o history "#locator".
	 * {@link LoginPlace} que gera o history "#login:locator"
	 * {@link PartialLoginPlace} que nao altera o history do browser 
	 * Quem quizer escutar estes places deve implementar uma {@link IActivityFactory}.
	 * 
	 * Exemplo de utilizacao no modulo core, classe CoreActivityFactory:
	 * 
	 * if(targetPlace instanceof IndexPlace){
	 * 		return new MyCustomActivity((IndexPlace) targetPlace);
	 * }
	 */
	
	@Override
	public void onModuleLoad() {
		DefaultEntryPoint.defineSankhyaJiva();
		
		AppPlaceHistoryMapperWithFactory historyMapper = new AppPlaceHistoryMapperWithFactory();
		historyMapper.setFactory(tokenizerFactory);
		
		ActivityController.setApplicationPlaceEventBus(eventBus);
		AppPlaceController.setApplicationPlaceController(placeController);
		DefaultEntryPoint.setApplicationTokenizerFactory(tokenizerFactory);
		
		DefaultEntryPoint.addApplicationTokenizer(RedirectPlace.PREFIX, new RedirectPlace.Initializer());
		DefaultEntryPoint.addApplicationTokenizer(ErrorPlace.PREFIX, new ErrorPlace.Initializer());
		
		historyHandler = new PlaceHistoryHandler(historyMapper);
	}
	
	public static void startMVP(){
		historyHandler.register(placeController, eventBus, (AbstractPlace) TokenizerFactory.getTokenizer("index").getPlace("home"));
	}
	
	public static void handleCurrentHistory(){
		historyHandler.handleCurrentHistory();
	}
	
}
