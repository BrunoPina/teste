package br.com.tagme.gwt.http.client;

import com.google.gwt.http.client.RequestBuilder;

public class ServiceProxyRequestBuilder extends RequestBuilder {

	private String serviceName;
	private String module;
	
	public ServiceProxyRequestBuilder(Method httpMethod, String url) {
		super(httpMethod, url);
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
}
