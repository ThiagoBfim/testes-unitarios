package br.teste.basico.entidades;

import java.math.BigDecimal;

public class Usuario {

	private String nome;
	private String email;
	private BigDecimal saldo = BigDecimal.ZERO;
	
	public Usuario() {}
	
	public Usuario(String nome, BigDecimal saldo) {
		this.nome = nome;
		this.saldo = saldo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Usuario(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public boolean temSaldo() {
		return getSaldo().compareTo(BigDecimal.ZERO) > 0;
	}
}