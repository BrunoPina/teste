package br.com.tagme.gwt.http.client;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import br.com.tagme.gwt.commons.client.DefaultCompositeWithPresenter;
import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.components.Alert;
import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.commons.utils.client.Base64Utils;
import br.com.tagme.gwt.commons.utils.client.StringUtils;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.AppPlaceController;
import br.com.tagme.gwt.mvp.client.tokenizer.TokenizerFactory;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class XMLServiceProxy extends ServiceProxyBase implements HasHandlers{

	private static final int	STATUS_ERROR			 = 0;
	private static final int	STATUS_OK				 = 1;
	private static final int	STATUS_NOT_AUTHORIZED	 = 2;
	private static final int	STATUS_NOT_AUTHENTICATED = 3;

	private static final String DEFAULT_MODULE			= "core";
	private static final String	DEFAULT_CONTEXT			= "NOCONTEXT";
	private static final String	BROKER_ROOTPATH			= "broker";
	private static final String DEFAULT_ACTION			= "E"; 
	
	private HandlerManager handlerManager;
	
	public XMLServiceProxy(Object source){
		this(source, null);
	}
	
	public XMLServiceProxy(Object source, String requiredAction){
		handlerManager = new HandlerManager(source);
		String requestContextId = source.getClass().getSimpleName();
		
		if(source instanceof DefaultCompositeWithPresenter){
			DefaultActivity presenter = ((DefaultCompositeWithPresenter) source).getPresenter();
			String prefix = presenter.getContextPrefix();
			String locator = presenter.getContextLocator();
			if(prefix == null || "".equals(prefix) || prefix.equals(locator)){
				requestContextId = locator;
				if(requiredAction != null && !"".equals(requiredAction)){
					requestContextId = requestContextId+","+requiredAction; 
				}else{
					requestContextId = requestContextId+","+DEFAULT_ACTION;
				}
			}else if(locator == null || "".equals(locator) || "home".equals(locator)){
				requestContextId = prefix;
				if(requiredAction != null && !"".equals(requiredAction)){
					requestContextId = requestContextId+","+requiredAction; 
				}else{
					requestContextId = requestContextId+","+DEFAULT_ACTION;
				}
			}else{
				if(requiredAction != null && !"".equals(requiredAction)){
					requestContextId = prefix+","+requiredAction; 
				}else{
					requestContextId = prefix+","+locator;
				}
			}
		}else if(requiredAction != null){
			requestContextId = requestContextId+","+requiredAction;
		}
		
		super.setRequestContext(requestContextId);
	}
	
	@Override
	protected ServiceProxyRequestBuilder buildRequestBuilder(String service, HashMap<String, String> serviceAttributes) {
		String module = DEFAULT_MODULE;
		
		if(StringUtils.getEmptyAsNull(super.getRequestContext()) == null){
			super.setRequestContext(DEFAULT_CONTEXT);
		}
		
		String serviceName = service;

		if (service.indexOf("@") > -1) {
			String[] moduleAndService = service.split("@");
			module = moduleAndService[0];
			serviceName = moduleAndService[1];
		}
		
		String url = SchemeReference.SKP_RELATIVEURL + BROKER_ROOTPATH + "/" + module + "/" + serviceName;
		url+= ";requestContext=" + super.getRequestContext();
		
		if(serviceAttributes != null){
			for (String key : serviceAttributes.keySet()) {
				url += ";" + key + "=" + serviceAttributes.get(key);
			}
		}

		ServiceProxyRequestBuilder builder = new ServiceProxyRequestBuilder(RequestBuilder.POST, URL.encode(url));
		builder.setHeader("Content-Type", "text/xml; charset=utf-8");
		builder.setHeader("Accept", "text/xml");
		builder.setServiceName(serviceName);
		builder.setModule(module);

		return builder;
	}

	@Override
	protected String buildRequestEntity(Object requestData, ServiceProxyRequestBuilder builder) {
		Document doc = XMLParser.createDocument();
		
		Element serviceRequest = doc.createElement("serviceRequest");
		serviceRequest.setAttribute("serviceName", builder.getModule()+"@"+builder.getServiceName());

		Element requestBody = doc.createElement("requestBody");
		if (requestData != null) {
			if (requestData instanceof Document) {
				Node nodeData = ((Document) requestData).getDocumentElement();
				requestBody.appendChild(nodeData);
			} else if (requestData instanceof Node) {
				requestBody.appendChild((Node) requestData);
			} else if (requestData instanceof NodeList) {
				NodeList nodes = (NodeList) requestData;

				while (nodes.getLength() > 0) {
					requestBody.appendChild(nodes.item(0));
				}
			} else {
				throw new IllegalArgumentException("Não foi possível criar conteúdo para a requisição.");
			}
		}

		serviceRequest.appendChild(requestBody);
		doc.appendChild(serviceRequest);

		return doc.toString();
	}

	@Override
	protected <T> void parseResponseEntity(Response response, Callback<T> callback) throws ServiceProxyException {
		if (response.getStatusCode() == 200) {
			Document doc = XMLParser.parse(response.getText());

			Element responseElem = doc.getDocumentElement();
			Element responseBody = (Element) responseElem.getElementsByTagName("responseBody").item(0);

			int status = Integer.parseInt(responseElem.getAttribute("status"));

			String statusMessage = null;

			NodeList c = responseElem.getElementsByTagName("statusMessage");

			if (c != null && c.getLength() > 0) {
				statusMessage = c.item(0).getFirstChild().getNodeValue();
				try {
					statusMessage = new String(Base64Utils.decode(statusMessage), "ISO-8859-1");
				} catch (UnsupportedEncodingException e) {
				}
			}

			switch (status) {
				
				case STATUS_ERROR:
					ServiceProxyException statusError = new ServiceProxyException(statusMessage, 500);

					if (!callback.onError(statusError)) {
						throw new ServiceProxyException(statusMessage == null ? "Execução da operação falhou" : statusMessage);
					}

					statusError.printStackTrace();

					break;
				
				case STATUS_OK:
					callback.onResponseReceived((T) responseBody);
					break;
					
				case STATUS_NOT_AUTHORIZED:
					throw new ServiceProxyException("Você não possui acessos para esta operação.");
				
				case STATUS_NOT_AUTHENTICATED:
					Alert.showError("Sessão Expirada", "Por favor, faça login novamente.");
					AppAuth.getInstance().setLoginNotVerified("Sessão finalizada");
					AppPlaceController.goTo((AbstractPlace) TokenizerFactory.getTokenizer("login").getPlace("home"));
					break;
					
			}

		}
	}

	public void call(String service, Node requestData, HashMap<String, String> uriParams, XMLCallback callback) {
		super.call(service, requestData, callback, ServiceProxyConfig.DEFAULT, uriParams);
	}
	
	public void call(String service, Node requestData, XMLCallback callback) {
		super.call(service, requestData, callback, ServiceProxyConfig.DEFAULT, null);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

}
