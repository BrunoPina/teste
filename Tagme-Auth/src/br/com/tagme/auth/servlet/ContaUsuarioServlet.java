package br.com.tagme.auth.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.com.tagme.commons.http.SchemeReference;
import br.com.tagme.commons.spring.HttpContextSession;
import br.com.tagme.commons.utils.SWServiceHelper;
import br.com.tagme.commons.utils.SWServiceHelper.Campo;
import br.com.tagme.commons.utils.StringUtils;
import br.com.tagme.auth.dao.UsuarioDao;
import br.com.tagme.auth.model.Usuario;

@Controller
public class ContaUsuarioServlet {

	@Autowired
	private HttpContextSession	contextSession;
	
	@Autowired
	private UsuarioDao			usuarioDao;

	private static final int	STATUS_SUCCESS	= 1;
	private static final int	STATUS_ERROR	= 2;
	
	@RequestMapping(value = "/ajustesConta", headers = "content-type=multipart/*", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.POST)
	public @ResponseBody void ajustesConta(HttpServletRequest request, HttpServletResponse response, @RequestParam("foto") MultipartFile file, @RequestParam Map<String, String> params) throws IOException {

		StringBuffer responseString = new StringBuffer("<html><head></head><body>");

		String nome = params.get("nome");
		String alteraSenha = params.get("alteraSenha");
		String senhaAtual = params.get("senhaAtual");
		String novaSenha = params.get("novaSenha");
		String confSenha = params.get("confSenha");

		PasswordEncoder passwordEncode = new BCryptPasswordEncoder();

		Usuario usuario = usuarioDao.getUsuarioById(contextSession.getSKPLACE_CODUSU());
		usuario.setNome(nome);

		int status = STATUS_SUCCESS;
		boolean updateFoto = false;
		String motivoErro = "";

		if ("on".equals(alteraSenha)) {

			if (passwordEncode.matches(senhaAtual, usuario.getSenha())) {
				if (confSenha.equals(novaSenha)) {
					usuario.setSenha(passwordEncode.encode(novaSenha));
				} else {
					status = STATUS_ERROR;
					motivoErro = "Nova senha e confirma��o de senha n�o conferem.";
				}

			} else {
				status = STATUS_ERROR;
				motivoErro = "Senha atual informada n�o confere com a do banco de dados";
			}
		}

		if (status == STATUS_SUCCESS) {

			if (!file.isEmpty()) {
				byte[] bytes = file.getBytes();

				/*ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				BufferedImage img = ImageIO.read(in);
				int size = img.getHeight() > img.getWidth() ? img.getHeight() : img.getWidth(); 
				Image scaledImage = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				BufferedImage imageBuff = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
				imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				ImageIO.write(imageBuff, "jpg", buffer);

				usuario.setFoto(buffer.toByteArray());*/
				usuario.setFoto(bytes);
				updateFoto = true;
			}

			boolean updated = usuarioDao.updateUsuario(usuario, updateFoto);
			if (!updated) {
				status = STATUS_ERROR;
				motivoErro = "Erro ao atualizar banco de dados.";
			}
		}

		responseString.append(String.valueOf(status)).append(";");

		if (status == STATUS_SUCCESS) {
			responseString.append(nome).append(";");
			responseString.append(usuarioDao.getDataHoraAlteracao(usuario).toString());
		} else {
			responseString.append(motivoErro);
		}

		responseString.append("</body></html>");

		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
	}
	
	@RequestMapping(value = "/criarConta", headers = "content-type=multipart/*", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.POST)
	public @ResponseBody void criarConta(HttpServletRequest request, HttpServletResponse response, @RequestParam("foto") MultipartFile file, @RequestParam Map<String, String> params) throws IOException {
		
		SWServiceHelper serviceHelper = new SWServiceHelper();
		
		PasswordEncoder passwordEncode = new BCryptPasswordEncoder();
		
		StringBuffer responseString = new StringBuffer("<html><head></head><body>");
		int status = STATUS_ERROR;
		String motivoErro = "";
		
		String email = params.get("email");
		String nome = params.get("nome");
		String novaSenha = params.get("novaSenha");
		String confSenha = params.get("confSenha");
		boolean uploadFoto = false;
		
		if(StringUtils.isNotEmpty(email)){
		
			Usuario foundUsuario = usuarioDao.getUsuarioByUsername(email);
			
			if(foundUsuario != null){
				responseString.append(STATUS_ERROR);
				motivoErro = "J� existe uma conta com este e-mail.";
			}else if(StringUtils.isNotEmpty(nome) && StringUtils.isNotEmpty(novaSenha) && StringUtils.isNotEmpty(confSenha)){
			
				if(confSenha.equals(novaSenha)){
				
					serviceHelper.setEntity("ValidaContaPlace");
					
					List<Campo> listCampos = new ArrayList<Campo>();
					Campo campoEmail = new Campo("EMAIL", email);
					listCampos.add(campoEmail);
					Campo campoReenviarEmail = new Campo("REENVIAREMAIL", "N");
					listCampos.add(campoReenviarEmail);
					Element respElem = serviceHelper.callCrudSave(listCampos);
					Element entidades = respElem.getChild("entidades");
					Element entidade = entidades.getChild("entidade");
					
					String link = entidade.getChild("LINK").getValue();
					
					String chave = link.substring(link.lastIndexOf("/")+1);
					
					Usuario usuario = new Usuario();
					usuario.setAtivo(false);
					usuario.setEmail(email);
					usuario.setNome(nome);
					usuario.setSenha(passwordEncode.encode(novaSenha));
					usuario.setChave(chave);
					
					if (!file.isEmpty()) {
						byte[] bytes = file.getBytes();
						usuario.setFoto(bytes);
						uploadFoto = true;
					}
					
					usuarioDao.insertUsuario(usuario, uploadFoto);
					
					responseString.append(STATUS_SUCCESS);
				
				}else{
					responseString.append(STATUS_ERROR);
					motivoErro = "Senhas n�o conferem";
				}
				
			}else{
				responseString.append(STATUS_ERROR);
				motivoErro = "Todos os campos devem ser informados.";
			}
		
		}
		
		if(status == STATUS_ERROR){
			responseString.append(";").append(motivoErro);
		}
		
		responseString.append("</body></html>");

		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
		
	}
	
	@RequestMapping(value = "/validarConta/{chave}", produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.GET)
	public @ResponseBody void validarConta(HttpServletRequest request, HttpServletResponse response, @PathVariable("chave") String chave) throws IOException, ServletException{
		
		String motivoErro = "";
		boolean isValid = false;
		
		if(StringUtils.isNotEmpty(chave)){
		
			Usuario usuario = usuarioDao.getUsuarioByChave(chave);
			if(usuario != null){
			
				if(usuario.isAtivo()){	
					motivoErro = "Conta j� est� ativa.";
				}else if(usuario.getDhAtivacao() != null){
					motivoErro = "Conta j� foi ativada.";
				}else{
					usuario.setAtivo(true);
					if(usuarioDao.ativarUsuario(usuario)){
						isValid = true;
					}else{
						motivoErro = "Erro ao ativar conta.";
					}
				}
			}else{
				motivoErro = "Conta n�o encontrada.";
			}
		}else{
			motivoErro = "Chave de valida��o n�o foi informada.";
		}

		StringBuffer responseString = new StringBuffer("<html><head></head>");
		responseString.append("<script type='text/javascript'>function placeDelayer(){");
		
		if(isValid){
			responseString.append("document.getElementById('msg').innerHTML='Conta validada, redirecionando para p�gina de login...';");
			responseString.append("setTimeout('placeRedirect()',3000);");
		}else{
			responseString.append("document.getElementById('msg').innerHTML='").append(motivoErro).append("';");
		}
		
		responseString.append("} function placeRedirect(){ document.location = '" + SchemeReference.SKP_RELATIVEURL +"'; }</script>");
		responseString.append("<body onload=\"setTimeout('placeDelayer()',1000)\">");
		responseString.append("Validando usu�rio... <br/> <div id='msg'></div>");
		responseString.append("</body></html>");
		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
		
	}
	
	@RequestMapping(value = "/reenviarEmailValidacao", produces = MediaType.TEXT_HTML_VALUE, method = RequestMethod.POST)
	public @ResponseBody void reenviarEmailValidacao(HttpServletRequest request, HttpServletResponse response, @RequestParam("emailReenv") String emailReenv) throws IOException{
		
		SWServiceHelper serviceHelper = new SWServiceHelper();
		
		StringBuffer responseString = new StringBuffer("<html><head></head><body>");
		int status = STATUS_ERROR;
		String motivoErro = "";
		
		if(StringUtils.isNotEmpty(emailReenv)){
			
			Usuario usuario = usuarioDao.getUsuarioByUsername(emailReenv);
			if(usuario != null){
				if(!usuario.isAtivo()){
					if(usuario.getDhAtivacao() == null){
						
						serviceHelper.setEntity("ValidaContaPlace");
						serviceHelper.setCriterio("EMAIL", emailReenv);
						serviceHelper.findField("CODSOLIC");
						List<Element> listEntidades = serviceHelper.callCrudFind();
						if(listEntidades.size() == 1){
							Element entidade = listEntidades.get(0);
							String codSolic = entidade.getChildText("CODSOLIC");
							
							List<Campo> listCampos = new ArrayList<Campo>();
							Campo campoCodSolic = new Campo("CODSOLIC", codSolic);
							listCampos.add(campoCodSolic);
							Campo emailEnviadoN = new Campo("REENVIAREMAIL","N");
							listCampos.add(emailEnviadoN);
							serviceHelper.callCrudSave(listCampos);
							
							status = STATUS_SUCCESS;
							responseString.append(STATUS_SUCCESS);
						}else{
							motivoErro = "Solicita��o inexistente. Por favor, entre em contato com a central.";
						}
						
					}else{
						motivoErro = "Conta j� foi ativada.";
					}
				}else{
					motivoErro = "Conta j� est� ativa.";
				}
			}else{
				motivoErro = "Conta inexistente.";
			}
			
		}else{
			motivoErro = "E-mail deve ser informado.";
		}
		
		if(status == STATUS_ERROR){
			responseString.append(STATUS_ERROR).append(";").append(motivoErro);
		}
		
		responseString.append("</body></html>");

		response.setContentType("text/html");
		response.getWriter().println(responseString.toString());
		
	}
	
}