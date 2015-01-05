package br.com.tagme.gwt.commons.client.integracao;

import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

public class SkwHelper {

	public void logoutSWProxy(String sessionId) {

		XMLServiceProxy service = new XMLServiceProxy(this);

		Document doc = XMLParser.createDocument();

		Element parametros = doc.createElement("parametros");

		Element jsessionidElem = doc.createElement("jsessionid");
		jsessionidElem.appendChild(doc.createTextNode(sessionId));
		parametros.appendChild(jsessionidElem);

		doc.appendChild(parametros);

		service.call("commons@SWProxyLogoutService", doc, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				return false;
			}

		});
	}
	
	
	
}
