package br.com.tagme.gwt.http.client;

import java.util.HashMap;

import br.com.tagme.gwt.commons.client.components.Alert;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public abstract class ServiceProxyBase {

	private String		requestContext;
	
	public ServiceProxyBase(){
	}

	protected final <T> void call(String service, Object requestData, final Callback<T> callback, final int config, HashMap<String, String> serviceAttributes){

		final ServiceProxyRequestBuilder builder = buildRequestBuilder(service, serviceAttributes);
		String textRequestData = buildRequestEntity(requestData, builder);

		try {
			if (containsConfig(config, ServiceProxyConfig.SHOW_WAITING)) {
				Alert.showWaitMessage();
			}

			builder.sendRequest(textRequestData, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					// Couldn't connect to server (could be timeout, SOP violation, etc.)
					if (containsConfig(config, ServiceProxyConfig.SHOW_WAITING)) {
						Alert.hideWaitMessage();
					}

					Alert.showError("Erro de comunicação", "Requisição cancelada e não completada.");
				}

				public void onResponseReceived(Request request, Response response) {
					if (containsConfig(config, ServiceProxyConfig.SHOW_WAITING)) {
						Alert.hideWaitMessage();
					}
					
					int statusCode = response.getStatusCode();
					
					if (statusCode == 200) {
						try {
							parseResponseEntity(response, callback);
						} catch (ServiceProxyException e) {
							Alert.showError(e.getMessage());
						}
					} else {
						callback.onError(new ServiceProxyException(response.getStatusText(), statusCode));
					}
				}

			});
		} catch (Exception e) {
			Alert.showError("Erro Interno", "Requisição não enviada.");			
			e.printStackTrace();
		}
	}

	
	protected boolean containsConfig(int config, int atribute) {
		if ((config & atribute) == atribute) {
			return true;
		}

		return false;
	}
	
	protected String getRequestContext(){
		return requestContext;
	}
	
	protected void setRequestContext(String requestContext){
		this.requestContext = requestContext;
	}

	protected abstract ServiceProxyRequestBuilder buildRequestBuilder(String service, HashMap<String, String> serviceAttributes);

	protected abstract String buildRequestEntity(Object requestData, ServiceProxyRequestBuilder builder);

	protected abstract <T> void parseResponseEntity(Response response, Callback<T> callback) throws ServiceProxyException;

	public interface Callback<T> {
		void onResponseReceived(T response);

		boolean onError(ServiceProxyException e);
	}
}
