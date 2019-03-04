package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.FaltaEstoqueException;

import java.util.Date;

import static br.teste.basico.utils.DataUtils.adicionarDias;

public class LocacaoService {

    public Locacao alugarFilme(Usuario usuario, Filme filme) {

        validarEstoque(filme);
        Locacao locacao = new Locacao();
        locacao.setFilme(filme);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        int diasAluguel = 1;
        dataEntrega = adicionarDias(dataEntrega, diasAluguel);
        locacao.setDataRetorno(dataEntrega);
        locacao.setValor(filme.getPrecoLocacao() * diasAluguel);

        //Salvando a locacao...
        //TODO adicionar método para salvar

        return locacao;
    }

    private void validarEstoque(Filme filme) {
        if (filme.getEstoque() == 0) {
            throw new FaltaEstoqueException(String.format("Filme %s sem estoque", filme.getNome()));
        }
    }
}