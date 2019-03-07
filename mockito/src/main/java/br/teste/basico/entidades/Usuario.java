package br.teste.basico.entidades;

import java.math.BigDecimal;

public class Usuario {

	private String nome;
	private BigDecimal saldo = BigDecimal.ZERO;
	
	public Usuario() {}
	
	public Usuario(String nome, BigDecimal saldo) {
		this.nome = nome;
		this.saldo = saldo;
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
}