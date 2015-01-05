package br.com.tagme.gwt.http.client;

public class ServiceProxyException extends Exception {

	private int statusCode;
	
	public ServiceProxyException() {
		super();
		statusCode = 200;
	}

	public ServiceProxyException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode; 
	}
	
	public ServiceProxyException(String message) {
		super(message);
	}

	public ServiceProxyException(Throwable cause) {
		super(cause);
	}

	public ServiceProxyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public int getStatusCode(){
		return statusCode;
	}
}
