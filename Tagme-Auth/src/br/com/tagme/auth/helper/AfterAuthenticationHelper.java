package br.com.tagme.auth.helper;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.commons.utils.SWServiceHelper;
import br.com.tagme.commons.utils.SWServiceHelper.PresentationField;
import br.com.tagme.auth.dao.UsuarioDao;
import br.com.tagme.auth.model.Usuario;

@Service
public class AfterAuthenticationHelper {

	@Autowired
	private HttpContextSession contextSession;

	@Autowired
	private UsuarioDao usuarioDao;
	
	private Usuario usuario;
	
	public AfterAuthenticationHelper(){
		
	}
	
	public void initContextTo(String username){
		
		getSKPlaceUserInfo(username);
		getSKWUserInfo(username);
		getDtAtualSKW();
		setLoggedIn();
	}

	private void getSKWUserInfo(String username) {

		final List<PresentationField> presentationFields = new ArrayList<PresentationField>();
		
		presentationFields.add(new PresentationField("CODPARC", "C�digo do Parceiro"));
		presentationFields.add(new PresentationField("Parceiro_NOMEPARC", "NOMEPARC", "Nome do Parceiro"));
		presentationFields.add(new PresentationField("CODCONTATO", "C�digo do Contato"));
		presentationFields.add(new PresentationField("Parceiro_CODPARCMATRIZ", "CODPARCMATRIZ", "C�digo Parceiro Matriz"));
		
		String[] fieldsToFind = { "CODPARC", "CODCONTATO" };

		SWServiceHelper serviceHelper = new SWServiceHelper();

		serviceHelper.setEntity("Contato", false);
		serviceHelper.setCriterio("EMAIL", username);
		serviceHelper.findFields(fieldsToFind);
		serviceHelper.referenceFetch("Parceiro", new String[] { "NOMEPARC", "CODPARCMATRIZ" });

		List<Element> listEntidades = serviceHelper.callCrudFind();
		if(listEntidades.size() > 0){
			serviceHelper.prepareResponseForObject(presentationFields, listEntidades.get(0));
			
			Element entidades = serviceHelper.getResponse();
			Element entidade = entidades.getChild("entidade");
			
			contextSession.setSKW_CODPARC(entidade.getChildText("CODPARC"));
			contextSession.setSKW_NOMEPARC(entidade.getChildText("NOMEPARC"));
			contextSession.setSKW_CODPARCMATRIZ(entidade.getChildText("CODPARCMATRIZ"));
			contextSession.setSKW_CODCONTATO(entidade.getChildText("CODCONTATO"));
		}
		
	}
	
	private void getSKPlaceUserInfo(String username){
		
		usuario = usuarioDao.getUsuarioByUsername(username);
		
		contextSession.setSKPLACE_CODUSU(usuario.getCodUsu());
		contextSession.setSKPLACE_USERNAME(username);
		contextSession.setSKPLACE_NOME(usuario.getNome());
		contextSession.setSKPLACE_LASTLOGIN(usuario.getLastLogin());
	}
	
	private void setLoggedIn(){
		usuarioDao.setLoggedIn(usuario);
	}
	
	private void getDtAtualSKW(){
		String fieldToFind = "DTHOJE";
		
		SWServiceHelper serviceHelper = new SWServiceHelper();
		
		serviceHelper.setEntity("TSPDTP");
		serviceHelper.findField(fieldToFind);
		Element entidade = serviceHelper.callCrudFind().get(0);
		
		contextSession.setSKW_DTATUAL(entidade.getChildText(fieldToFind));
	}

}