package br.com.tagme.gwt.core.client.activity;

import java.util.HashMap;

import br.com.tagme.gwt.commons.client.auth.AppAuth;
import br.com.tagme.gwt.commons.client.integracao.SchemeReference;
import br.com.tagme.gwt.commons.utils.client.XMLUtils;
import br.com.tagme.gwt.http.client.ServiceProxyException;
import br.com.tagme.gwt.http.client.XMLCallback;
import br.com.tagme.gwt.http.client.XMLServiceProxy;
import br.com.tagme.gwt.mvp.client.activity.DefaultActivity;
import br.com.tagme.gwt.mvp.client.base.AbstractPlace;
import br.com.tagme.gwt.mvp.client.base.IBreadcumbPlace;
import br.com.tagme.gwt.core.client.place.ConsultaOSPlace;
import br.com.tagme.gwt.core.client.place.InserirOSPlace;
import br.com.tagme.gwt.core.client.ui.pages.DefaultInnerPage;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.xml.client.Element;

public class IFrameActivity extends DefaultActivity {

	public IFrameActivity(IBreadcumbPlace place) {
		super((AbstractPlace) place);
	}

	@Override
	public void start(final AcceptsOneWidget panel, EventBus eventBus) {
		DefaultInnerPage.getInstance().setPresenter(this);

		XMLServiceProxy service = new XMLServiceProxy(this);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("requireMgeSession", "true");
		
		service.call("PORCLI@BuscaInfosContatoService", null, params, new XMLCallback() {

			@Override
			public void onResponseReceived(Element response) {

				String codParc = XMLUtils.getNodeValue(response.getElementsByTagName("CODPARC").item(0));
				String codContato = XMLUtils.getNodeValue(response.getElementsByTagName("CODCONTATO").item(0));

				Frame frame = new Frame();
				frame.addStyleName("mashup-iframe");

				if (getContextPlace() instanceof ConsultaOSPlace) {
					frame.setUrl(SchemeReference.SKW_URL + "/mge/MGEIntegracao?servico=guestacess&parceiro=" + codParc + "&email=" + AppAuth.getInstance().getUsername() + "&senha=ebdcf817c51f265309cb5dd74edc976b&redirect=/mgeos/OSPortal.mgeos");
				} else if (getContextPlace() instanceof InserirOSPlace) {
					String mgeSession = XMLUtils.getNodeValue(response.getElementsByTagName("MGESESSION").item(0));
					frame.setUrl(SchemeReference.SKW_URL + "/mge/MGEIntegracao?servico=guestacess&parceiro=" + codParc + "&email=" + AppAuth.getInstance().getUsername() + "&senha=ebdcf817c51f265309cb5dd74edc976b&redirect=/mgeos/OSPortal.mgeos?acao=novaOS&mgeSession=" + mgeSession + "&codParc=" + codParc + "&contato=" + codContato);
				}
				DefaultInnerPage.getInstance().setView(frame);
			}

			@Override
			public boolean onError(ServiceProxyException e) {
				// TODO Auto-generated method stub
				return false;
			}

		});

		
		DefaultInnerPage.getInstance().setHeight(Window.getClientHeight() - 220 + "px");
		panel.setWidget(DefaultInnerPage.getInstance());

	}

	@Override
	public String mayStop() {
		DefaultInnerPage.getInstance().setHeight("100%");
		return super.mayStop();
	}
}
