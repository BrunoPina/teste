package br.com.tagme.auth.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.tagme.auth.dao.UsuarioDao;
import br.com.tagme.auth.model.Usuario;
import br.com.tagme.commons.spring.HttpContextSession;

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
		setLoggedIn();
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
	


}
