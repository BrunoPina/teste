package br.com.tagme.auth.model;

import java.sql.Timestamp;

public class Pessoa {

	private String	codPes;
	private String nomeCompleto;
	private String profissao; 
	private Timestamp dtNasc; 
	private String cpf;
	private String endereco; 
	private String bairro; 
	private String cep; 
	private String cidade; 
	private String estado;
	private String nacionalidade; 
	private String telefone; 
	private String celular;
	private String sexo;
	private String fumante; 
	private String email;
	private byte[]		foto;
	private Timestamp	dhalter_perfil;
	private Timestamp	dhalter_foto;
	private Timestamp	last_login;
	public String getCodPes() {
		return codPes;
	}
	public void setCodPes(String codPes) {
		this.codPes = codPes;
	}
	public String getNomeCompleto() {
		return nomeCompleto;
	}
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	public String getProfissao() {
		return profissao;
	}
	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}
	public Timestamp getDtNasc() {
		return dtNasc;
	}
	public void setDtNasc(Timestamp dtNasc) {
		this.dtNasc = dtNasc;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNacionalidade() {
		return nacionalidade;
	}
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getFumante() {
		return fumante;
	}
	public void setFumante(String fumante) {
		this.fumante = fumante;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte[] getFoto() {
		return foto;
	}
	public void setFoto(byte[] foto) {
		this.foto = foto;
	}
	public Timestamp getDhalter_perfil() {
		return dhalter_perfil;
	}
	public void setDhalter_perfil(Timestamp dhalter_perfil) {
		this.dhalter_perfil = dhalter_perfil;
	}
	public Timestamp getDhalter_foto() {
		return dhalter_foto;
	}
	public void setDhalter_foto(Timestamp dhalter_foto) {
		this.dhalter_foto = dhalter_foto;
	}
	public Timestamp getLast_login() {
		return last_login;
	}
	public void setLast_login(Timestamp last_login) {
		this.last_login = last_login;
	}
	
}
