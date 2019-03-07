package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.FaltaEstoqueException;
import br.teste.basico.exceptions.LocadoraException;
import br.teste.basico.repository.LocacaoRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static br.teste.basico.utils.DataUtils.adicionarDias;

public class LocacaoService {

    private LocacaoRepository locacaoRepository;
    private SPCService spcService;

    public LocacaoService(LocacaoRepository locacaoRepository, SPCService spcService) {
        this.locacaoRepository = locacaoRepository;
        this.spcService = spcService;
    }

    public Locacao alugarFilme(Usuario usuario, Filme filme) {
        return alugarFilmes(usuario, Collections.singletonList(filme), 1);
    }

    public Locacao alugarFilme(Usuario usuario, Filme filme, int diasAluguel) {
        return alugarFilmes(usuario, Collections.singletonList(filme), diasAluguel);
    }

    public Locacao alugarFilmes(Usuario usuario, List<Filme> filmes, int diasAluguel) {

        validarUsuario(usuario);
        filmes.forEach(this::validarFilme);

        if (!spcService.possuiNomeLimpo(usuario)) {
            throw new LocadoraException("Usuario Negativado");
        }

        Locacao locacao = new Locacao();
        locacao.setFilmes(filmes);
        locacao.setUsuario(usuario);
        locacao.setDataLocacao(new Date());

        //Entrega no dia seguinte
        Date dataEntrega = new Date();
        dataEntrega = adicionarDias(dataEntrega, diasAluguel);
        locacao.setDataRetorno(dataEntrega);
        locacao.setValor(filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum() * diasAluguel);

        //Salvando a locacao...

        locacaoRepository.salvar(locacao);

        return locacao;
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new LocadoraException("Usuário vazio");
        }
    }

    private void validarFilme(Filme filme) {
        if (filme == null) {
            throw new LocadoraException("Filme vazio");
        }
        validarEstoque(filme);
    }

    private void validarEstoque(Filme filme) {
        if (filme.getEstoque() == 0) {
            throw new FaltaEstoqueException(String.format("Filme %s sem estoque", filme.getNome()));
        }
    }
}