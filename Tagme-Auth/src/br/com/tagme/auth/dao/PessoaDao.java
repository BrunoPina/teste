package br.com.tagme.auth.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import br.com.tagme.auth.db.ConnectionTemplate;
import br.com.tagme.auth.model.Pessoa;
import br.com.tagme.commons.daointerfaces.GenericObject;
import br.com.tagme.commons.daointerfaces.IDao;
import br.com.tagme.commons.spring.HttpContextSession;

@Repository
public class PessoaDao implements IDao {

	@Autowired
	private HttpContextSession	contextSession;

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

	
	public void insertPessoa(Pessoa pessoa, boolean uploadFoto) {
		
		if(!uploadFoto){
			String sql = "INSERT INTO TAGPES (nomeCompleto, profissao,dtnasc, cpf, endereco, bairro, cep, cidade, estado, nacionalidade, telefone, celular, sexo, fumante ,email)"
				       + "VALUES(?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?);";
			ConnectionTemplate.getTemplate().update(sql, new Object[] {pessoa.getNomeCompleto(),
					pessoa.getProfissao(),
					pessoa.getDtNasc(),
					pessoa.getCpf(),
					pessoa.getEndereco(),
					pessoa.getBairro(),
					pessoa.getCep(),
					pessoa.getCidade(),
					pessoa.getEstado(),
					pessoa.getNacionalidade(),
					pessoa.getTelefone(),
					pessoa.getCelular(),
					pessoa.getSexo(),
					pessoa.getFumante(),
					pessoa.getEmail()});
		}else{
			// TODO  com foto
		}
		
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
