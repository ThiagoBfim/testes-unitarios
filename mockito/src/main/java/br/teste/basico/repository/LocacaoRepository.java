package br.teste.basico.repository;

import br.teste.basico.entidades.Locacao;

import java.util.List;

public interface LocacaoRepository {

    void salvar(Locacao locacao);

    List<Locacao> obterLocacoesPendentes();

    Locacao findByCod(Long codLocacao);
}
