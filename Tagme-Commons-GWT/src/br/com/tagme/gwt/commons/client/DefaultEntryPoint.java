package br.com.tagme.gwt.commons.client;

import java.util.ArrayList;

import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;

import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.ActivityController;
import br.com.tagme.gwt.mvp.client.base.AppPlaceHistoryMapperWithFactory;
import br.com.tagme.gwt.mvp.client.base.IActivityFactory;
import br.com.tagme.gwt.mvp.client.tokenizer.AbstractTokenizer;
import br.com.tagme.gwt.mvp.client.tokenizer.ChangeHistoryTokenizer;
import br.com.tagme.gwt.mvp.client.tokenizer.KeepHistoryTokenizer;
import br.com.tagme.gwt.mvp.client.tokenizer.TokenizerFactory;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;

public abstract class DefaultEntryPoint implements EntryPoint{
	
	private static TokenizerFactory tokenizerFactory;
	
	private static Container outerContainer;
	
	private static AcceptsOneWidget mainApplicationContainer;
	private static AcceptsOneWidget contextLoginApplicationContainer;
	
	private static ArrayList<IActivityFactory> pendingRegistrationFactorys = new ArrayList<IActivityFactory>();
	private static ArrayList<IActivityFactory> pendingLoginRegistrationFactorys = new ArrayList<IActivityFactory>();
	private static Composite partialLoginWidget;
	
	private static String sankhyaJiva = "";
	
	/**
	 * Possui duas implementacoes abstratas padroes, dos tipos {@link ChangeHistoryTokenizer#ChangeHistoryTokenizer(String) prefix} e {@link KeepHistoryTokenizer#KeepHistoryTokenizer(String) prefix}
	 * Ambas devem receber um prefixo como argumento, que deve ser unico mesmo entre diferentes modulos.
	 * Ambas ja sobrescrevem {@link AbstractTokenizer#getToken(com.google.gwt.place.shared.Place)}, para modificar ou nao a url do browser, correspondentemente
	 * O tokenizer concreto que extender alguma destas implementacoes deve definir o prefixo e apenas sobrescrever o metodo {@link AbstractTokenizer#getPlace(String)}, que retorna uma nova instancia de um {@link AbstractPlace} correspondente
	 * 
	 * A classe {@link IndexPlace} possui um exemplo de implementacao.
	 * 
	 * @param tokenizer Classe concreta que implementa {@link ChangeHistoryTokenizer} ou {@link KeepHistoryTokenizer}
	 */
	public static void addApplicationTokenizer(String prefix, AbstractTokenizer tokenizer){
		if(tokenizerFactory != null){
			tokenizerFactory.addTokenizer(prefix, tokenizer);
		}
	}
	
	/** 
	 * @param factory Factory que retorna uma {@link AbstractTokenizer} para gerar tokens e places, utilizada pelo {@link AppPlaceHistoryMapperWithFactory} 
	 */
	public static void setApplicationTokenizerFactory(TokenizerFactory factory){
		tokenizerFactory = factory;
	}
	
	/**
	 * Utilizado para registrar uma atividade como um listener no EventBus de places. Ou seja, quando navegar a um novo place esta atividade sera disparada com o place mapeado na factory. 
	 * 
	 * @param activityContextWidget - Widget que sera modificado pela activity
	 * @param activityFactory
	 */
	public static void addApplicationActivityFactory(AcceptsOneWidget activityContextWidget, IActivityFactory activityFactory){
		ActivityController activityController = ActivityController.get(activityContextWidget);
		activityController.addActivityFactory(activityFactory);
	}
	
	/**
	 * Sobrescreve o metodo {@link DefaultEntryPoint#addApplicationActivityFactory(AcceptsOneWidget, IActivityFactory)} passando como widget o que eh definido como 'principal'
	 * da aplicacao, normalmente o widget que contem o conteudo principal das telas
	 * 
	 * @param activityFactory
	 */
	public static void addApplicationActivityFactory(IActivityFactory activityFactory){
		if(mainApplicationContainer != null){
			addApplicationActivityFactory(mainApplicationContainer, activityFactory);
		}else{
			pendingRegistrationFactorys.add(activityFactory);
		}
	}
	
	public static void addLoginApplicationActiviyFactory(IActivityFactory activityFactory){
		if(contextLoginApplicationContainer != null){
			addApplicationActivityFactory(contextLoginApplicationContainer, activityFactory);
		}else{
			pendingLoginRegistrationFactorys.add(activityFactory);
		}
	}
	
	public static void setMainApplicationContainer(AcceptsOneWidget widget){
		mainApplicationContainer = widget;
		
		for(IActivityFactory lazyLoadedFactory : pendingRegistrationFactorys){
			addApplicationActivityFactory(lazyLoadedFactory);
		}
		pendingRegistrationFactorys.clear();
	}
	
	public static void setLoginApplicationContainer(AcceptsOneWidget widget){
		contextLoginApplicationContainer = widget;
		contextLoginApplicationContainer.setWidget(partialLoginWidget);
		
		for(IActivityFactory lazyLoadedFactory : pendingLoginRegistrationFactorys){
			addLoginApplicationActiviyFactory(lazyLoadedFactory);
		}
		pendingLoginRegistrationFactorys.clear();
	}

	public static void setPartialLoginWidget(Composite widget) {
		partialLoginWidget = widget;
	}
	
	public static Composite getPartialLoginWidget(){
		return partialLoginWidget;
	}
	
	public static void addMainContainerStyleName(String className){
		((Column) mainApplicationContainer).addStyleName(className);
	}
	
	public static void removeMainContainerStyleName(String className){
		((Column) mainApplicationContainer).removeStyleName(className);
	}
	
	public static void removeCenteredContentPage(){
		outerContainer.removeStyleName("centered-content-page");
	}
	
	public static void setCenteredContentPage(){
		outerContainer.addStyleName("centered-content-page");
	}
	
	public static void setApplicationOuterContainer(Container container){
		outerContainer = container;
	}
	
	public static native String getNativeVariableType(String jsVar)/*-{
	    return  eval('$wnd.' + jsVar);
	}-*/;
	
	public static boolean isJiva(){
		return "jiva".equals(sankhyaJiva);
	}
	
	public static void defineSankhyaJiva(){
		sankhyaJiva = getNativeVariableType("appProfile");
	}
}