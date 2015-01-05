package br.com.tagme.gwt.core.client;

import br.com.tagme.gwt.commons.client.DefaultEntryPoint;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.auth.AppAuthEvent;
import br.com.tagme.gwt.commons.client.auth.AppAuthEventHandler;
import br.com.tagme.gwt.commons.utils.client.XMLUtils;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.MVPEntryPoint;
import br.com.tagme.gwt.core.client.activity.factory.CoreActivityFactory;
import br.com.tagme.gwt.core.client.place.AtendimentoOnlinePlace;
import br.com.tagme.gwt.core.client.place.BoletoPlace;
import br.com.tagme.gwt.core.client.place.CNPJLicenciadosPlace;
import br.com.tagme.gwt.core.client.place.ConsultaOSPlace;
import br.com.tagme.gwt.core.client.place.ContatosAutorizadosPlace;
import br.com.tagme.gwt.core.client.place.IndexPlace;
import br.com.tagme.gwt.core.client.place.InformacoesCadastraisPlace;
import br.com.tagme.gwt.core.client.place.InserirOSPlace;
import br.com.tagme.gwt.core.client.ui.Footer;
import br.com.tagme.gwt.core.client.ui.Menu;
import br.com.tagme.gwt.core.client.ui.TopNavbar;
import br.com.tagme.gwt.core.client.ui.UIContentWrapper;
import br.com.tagme.gwt.core.client.ui.pages.DefaultInnerPage;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Element;

public class CoreEntryPoint extends DefaultEntryPoint {

	private static boolean firstHistoryHandleDispatched = false;
	
	@Override
	public void onModuleLoad() {
		if(DefaultEntryPoint.isJiva()){
			br.com.tagme.gwt.theme.jiva.client.CommonStyles.INSTANCE.css().ensureInjected();
		}else{
			br.com.tagme.gwt.theme.sankhya.client.CommonStyles.INSTANCE.css().ensureInjected();
		}
		
		addApplicationActivityFactory(new CoreActivityFactory());
		
		addApplicationActivityFactory(new AtendimentoOnlinePlace.Initializer());
		addApplicationTokenizer(AtendimentoOnlinePlace.PREFIX, new AtendimentoOnlinePlace.Initializer());
		
		addApplicationActivityFactory(new BoletoPlace.Initializer());
		addApplicationTokenizer(BoletoPlace.PREFIX, new BoletoPlace.Initializer());
		
		addApplicationActivityFactory(new ConsultaOSPlace.Initializer());
		addApplicationTokenizer(ConsultaOSPlace.PREFIX, new ConsultaOSPlace.Initializer());
		
		addApplicationActivityFactory(new InserirOSPlace.Initializer());
		addApplicationTokenizer(InserirOSPlace.PREFIX, new InserirOSPlace.Initializer());
		
		addApplicationActivityFactory(new InformacoesCadastraisPlace.Initializer());
		addApplicationTokenizer(InformacoesCadastraisPlace.PREFIX, new InformacoesCadastraisPlace.Initializer());
		
		addApplicationActivityFactory(new CNPJLicenciadosPlace.Initializer());
		addApplicationTokenizer(CNPJLicenciadosPlace.PREFIX, new CNPJLicenciadosPlace.Initializer());
		
		addApplicationActivityFactory(new ContatosAutorizadosPlace.Initializer());
		addApplicationTokenizer(ContatosAutorizadosPlace.PREFIX, new ContatosAutorizadosPlace.Initializer());
		
		addApplicationActivityFactory(new IndexPlace.Initializer());
		addApplicationTokenizer(IndexPlace.PREFIX, new IndexPlace.Initializer());
		
		MVPEntryPoint.startMVP();
		
		final TopNavbar topNavbar = new TopNavbar();
		final UIContentWrapper contentWrapper = new UIContentWrapper();
		
		setMainApplicationContainer(contentWrapper.getMainContainer());
		setApplicationOuterContainer(contentWrapper.getOuterContainer());
		
		DefaultInnerPage.setMenu(new Menu());
		
		AppAuth.getInstance().addAppAuthHandler(new AppAuthEventHandler() {
			
			@Override
			public void onAppAuthChange(AppAuthEvent event) {
				AppAuth.getInstance().removeAppAuthHandler(this);
				if(event.typeLoginVerified() || event.typeLoginNotVerified()){
					if(!firstHistoryHandleDispatched){
						firstHistoryHandleDispatched = true;
						RootPanel.get().add(topNavbar);
						RootPanel.get().add(contentWrapper);
						RootPanel.get().add(new Footer());
						MVPEntryPoint.handleCurrentHistory();
					}
				}
			}
		});
		
		XMLServiceProxy serviceProxy = new XMLServiceProxy(this);
		serviceProxy.call("auth@VerifyAuthzService", null, new XMLCallback() {
			
			@Override
			public void onResponseReceived(Element response) {
				Element authElem = XMLUtils.getFirstChild(response, "auth");
				
				String authString = "";
				
				if(authElem != null){
					authString = XMLUtils.getNodeValue(authElem);
				}
				
				AppAuth.getInstance().tryAuth(authString);
			}
			
			@Override
			public boolean onError(ServiceProxyException e) {
				AppAuth.getInstance().setLoginNotVerified(e.getMessage());
				return false;
			}
		});
		
	}
	
}
