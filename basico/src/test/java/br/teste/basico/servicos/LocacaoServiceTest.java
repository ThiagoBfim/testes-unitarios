package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.exceptions.FaltaEstoqueException;
import org.junit.Test;

import java.util.Date;

import static br.teste.basico.utils.DataUtils.adicionarDias;
import static br.teste.basico.utils.DataUtils.isMesmaDataSimples;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    /**
     * O teste precisa ser FIRST
     * Fast				        (Menos de 1 segundo)
     * Independent/Isolate		(Não depender de outros testes) (Cada teste deve testar uma unica coisa de forma isolada.)
     * Repeatable		        (Retornar o mesmo resultado indepdentemente de quantas vezes for executado)
     * Self-verifying	        (O teste precisa se auto avaliar, e informar se houver erro ou não)
     * Timely			        (Deve ser criada no momento certo.)
     */
    @Test
    public void testarDataLocacao() {
        LocacaoService locacaoService = new LocacaoService();
        Filme filme = createFilmeBatman();
        Locacao locacao = locacaoService.alugarFilme(null, filme);
        assertTrue(isMesmaDataSimples(locacao.getDataRetorno(), adicionarDias(new Date(), 1)));
        assertTrue(isMesmaDataSimples(locacao.getDataLocacao(), new Date()));
    }

    @Test(expected = FaltaEstoqueException.class)
    public void testarSemEstoqueLocacao() {
        LocacaoService locacaoService = new LocacaoService();
        Filme filme = createFilmeBatman();
        filme.setEstoque(0);
        locacaoService.alugarFilme(null, filme);
    }

    @Test
    public void testarValorLocacao() {
        LocacaoService locacaoService = new LocacaoService();
        Filme filme = createFilmeBatman();
        Locacao locacao = locacaoService.alugarFilme(null, filme);
        assertEquals(locacao.getValor(), filme.getPrecoLocacao());
        assertThat(locacao.getValor(), org.hamcrest.CoreMatchers.is(10.50));
        assertThat(locacao.getValor(), is(not(10.501)));
    }

    private Filme createFilmeBatman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Batman");
        filme.setPrecoLocacao(10.50);
        return filme;
    }
}
