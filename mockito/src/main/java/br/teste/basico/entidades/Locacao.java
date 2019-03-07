package br.teste.basico.entidades;

import br.teste.basico.utils.DataUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Locacao {

    private Usuario usuario;
    private List<Filme> filmes = new ArrayList<Filme>();
    private Date dataLocacao;
    private Date dataRetorno;
    private Double valor;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataLocacao() {
        return dataLocacao;
    }

    public void setDataLocacao(Date dataLocacao) {
        this.dataLocacao = dataLocacao;
    }

    public Date getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(Date dataRetorno) {
        if(DataUtils.isDomingo(dataRetorno)) {
            this.dataRetorno = DataUtils.adicionarDias(dataRetorno, 1);
        } else {
            this.dataRetorno = dataRetorno;
        }
    }

    public Double getValor() {
        return valor - getDesconto();
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public List<Filme> getFilmes() {
        return filmes;
    }

    public void setFilmes(List<Filme> filmes) {
        this.filmes = filmes;
    }

    public double getDesconto() {
        int desconto = getPercentOfDesconto();
        return filmes.get(filmes.size() - 1).getPrecoLocacao() * desconto / 100;
    }

    private int getPercentOfDesconto() {
        switch (filmes.size()) {
            case 3:
                return 25;
            case 4:
                return 50;
            case 5:
                return 75;
            case 6:
                return 100;
            default:
                return 0;
        }
    }
}