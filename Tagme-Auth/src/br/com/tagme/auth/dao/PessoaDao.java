package br.com.tagme.auth.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;

import br.com.tagme.auth.db.ConnectionTemplate;
import br.com.tagme.auth.model.Pessoa;
import br.com.tagme.auth.model.Usuario;
import br.com.tagme.commons.daointerfaces.GenericObject;
import br.com.tagme.commons.daointerfaces.IDao;
import br.com.tagme.commons.spring.HttpContextSession;

@Repository
public class PessoaDao implements IDao {

	@Autowired
	private HttpContextSession	contextSession;
	
	@Autowired
	private UsuarioDao			usuarioDao;


	public PessoaDao() {
	}

	public Pessoa getPessoaById(final String id) {

		String sql = "SELECT cod_sujeito as codUsu, usuario as email, senha, foto, dhalter_perfil, dhalter_foto, nome, last_login, ativo " + "FROM sec_sujeito " + "WHERE cod_sujeito = ?";

		try{
			Pessoa pessoa = ConnectionTemplate.getTemplate().queryForObject(sql, new Object[] { id }, new RowMapper<Pessoa>() {
	
				@Override
				public Pessoa mapRow(ResultSet rs, int rowNum) throws SQLException {
	
					Pessoa pessoa = new Pessoa();
					pessoa.setNomeCompleto(rs.getString("nomeCompleto"));
					pessoa.setProfissao(rs.getString("profissao"));
					pessoa.setDtNasc(rs.getTimestamp("dtnasc"));
					pessoa.setCpf(rs.getString("cpf"));
					pessoa.setEndereco(rs.getString("endereco"));
					pessoa.setBairro(rs.getString("bairro"));
					pessoa.setCep(rs.getString("cep"));
					pessoa.setCidade(rs.getString("cidade"));
					pessoa.setEstado(rs.getString("estado"));
					pessoa.setNacionalidade(rs.getString("nacionalidade"));
					pessoa.setTelefone(rs.getString("telefone"));
					pessoa.setCelular(rs.getString("celular"));
					pessoa.setSexo(rs.getString("sexo"));
					pessoa.setFumante(rs.getString("fumante"));
					pessoa.setEmail(rs.getString("email"));
					pessoa.setFoto(rs.getBytes("foto"));
					
	
					return pessoa;
				}
	
			});
			return pessoa;
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}

	
	public void insertPessoa(final Pessoa pessoa, boolean uploadFoto) {
		
		//TODO Com Foto
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nomeCompleto"  , pessoa.getNomeCompleto());
		parametros.put("profissao"     , pessoa.getProfissao());
		parametros.put("dtNasc"        , pessoa.getDtNasc());
		parametros.put("cpf"           , pessoa.getCpf());
		parametros.put("endereco"      , pessoa.getEndereco());
		parametros.put("bairro"        , pessoa.getBairro());
		parametros.put("cep"           , pessoa.getCep());
		parametros.put("cidade"        , pessoa.getCidade());
		parametros.put("estado"        , pessoa.getEstado());
		parametros.put("nacionalidade" , pessoa.getNacionalidade());
		parametros.put("telefone"      , pessoa.getTelefone());
		parametros.put("celular"       , pessoa.getCelular());
		parametros.put("sexo"          , pessoa.getSexo());
		parametros.put("fumante"       , pessoa.getFumante());
		parametros.put("email"         , pessoa.getEmail());
		
		long codPes = new SimpleJdbcInsert(ConnectionTemplate.getTemplate())
		.withTableName("TAGPES")
		.usingGeneratedKeyColumns("codpes")
		.executeAndReturnKey(parametros).longValue();
		
		vincularUsuario(codPes);		
		
	}
	
	private void vincularUsuario(long codPes){
		Usuario usuario = usuarioDao.getUsuarioById(contextSession.getSKPLACE_CODUSU());
		usuario.setCodPes(new BigDecimal(codPes));
		usuarioDao.updateUsuario(usuario, false);
	}
	
	
	
	

	@Override
	public GenericObject findByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GenericObject> findByKeys(Collection<String> key) {
		// TODO Auto-generated method stub
		return null;
	}

}
