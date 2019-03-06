package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.FaltaEstoqueException;
import br.teste.basico.exceptions.LocadoraException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static br.teste.basico.utils.DataUtils.adicionarDias;
import static br.teste.basico.utils.DataUtils.isMesmaDataSimples;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class LocacaoServiceTest {

    private LocacaoService locacaoService;
    private Filme filmeBatman;

    /**
     * O teste precisa ser FIRST
     * Fast				        (Menos de 1 segundo)
     * Independent/Isolate		(Não depender de outros testes) (Cada teste deve testar uma unica coisa de forma isolada.)
     * Repeatable		        (Retornar o mesmo resultado indepdentemente de quantas vezes for executado)
     * Self-verifying	        (O teste precisa se auto avaliar, e informar se houver erro ou não)
     * Timely			        (Deve ser criada no momento certo.)
     *
     * Note: Boas praticas: Utilizar should ou shouldNot nos nomes dos metodos
     */
    @Before
    public void setUp() {
        locacaoService = new LocacaoService();
        filmeBatman = createFilmeBatman();
    }

    @Test
    public void deveAlugarPorUmDia() {
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
        assertTrue(isMesmaDataSimples(locacao.getDataRetorno(), adicionarDias(new Date(), 1)));
        assertTrue(isMesmaDataSimples(locacao.getDataLocacao(), new Date()));
    }
    @Test
    public void deveRealizarLocacaoNoMesmoDia() {
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
        assertTrue(isMesmaDataSimples(locacao.getDataLocacao(), new Date()));
    }

    @Test(expected = FaltaEstoqueException.class)
    public void naoDeveAlugarCasoNaoTenhaEstoque() {
        filmeBatman.setEstoque(0);
        locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() {
        try {
            locacaoService.alugarFilme(new Usuario("jose"), null);
            Assert.fail("É esperado exception quando não houver filmeBatman.");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Filme vazio"));
        }
    }

    @Test
    public void naoDeveAlugarQuandoNaoHouverUsuario() {
        try {
            locacaoService.alugarFilme(null, filmeBatman);
            Assert.fail("É esperado exception quando não houver autor.");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    @Test
    public void deveConterValorLocacaoIgualValorFilme() {
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
        assertEquals(locacao.getValor(), filmeBatman.getPrecoLocacao());
        assertThat(locacao.getValor(), org.hamcrest.CoreMatchers.is(10.50));
        assertThat(locacao.getValor(), is(not(10.501)));
    }

    @Test
    public void deveHaverDesconto25PctNo3Filme() {
        Filme filmeMulherMaravilha = createFilmeMulherMaravilha();
        Filme filmeSuperman = createFilmeSuperman();
        Locacao locacao = locacaoService.alugarFilmes(new Usuario("jose"), Arrays.asList(filmeBatman, filmeMulherMaravilha, filmeSuperman));
        double desconto = filmeSuperman.getPrecoLocacao() * 25 /100;
        assertEquals(locacao.getDesconto(), desconto, 0.1);
        assertThat(locacao.getValor(), is(filmeBatman.getPrecoLocacao() + filmeMulherMaravilha.getPrecoLocacao() + filmeSuperman.getPrecoLocacao() - desconto));
    }

    @Test
    public void deveHaverDesconto50PctNo4Filme() {
        Filme filmeFlash = createFilmeFlash();
        Filme filmeMulherMaravilha = createFilmeMulherMaravilha();
        Filme filmeSuperman = createFilmeSuperman();
        List<Filme> filmes = Arrays.asList(filmeBatman, filmeMulherMaravilha, filmeSuperman, filmeFlash);
        Locacao locacao = locacaoService.alugarFilmes(new Usuario("jose"), filmes);
        double desconto = filmeFlash.getPrecoLocacao() * 50 /100;
        assertEquals(locacao.getDesconto(), desconto, 0.1);
        assertThat(locacao.getValor(), is(filmeBatman.getPrecoLocacao() + filmeMulherMaravilha.getPrecoLocacao() + filmeSuperman.getPrecoLocacao() + filmeFlash.getPrecoLocacao() - desconto));
    }

    @Test
    public void deveHaverDesconto75PctNo5Filme() {
        Filme filmeAquaman = createFilmeAquaman();
        Filme filmeFlash = createFilmeFlash();
        Filme filmeMulherMaravilha = createFilmeMulherMaravilha();
        Filme filmeSuperman = createFilmeSuperman();
        List<Filme> filmes = Arrays.asList(filmeBatman, filmeMulherMaravilha, filmeSuperman, filmeFlash, filmeAquaman);
        Locacao locacao = locacaoService.alugarFilmes(new Usuario("jose"), filmes);
        double desconto = filmeAquaman.getPrecoLocacao() * 75 /100;
        assertEquals(locacao.getDesconto(), desconto, 0.1);
        assertThat(locacao.getValor(), is(filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum() - desconto));
    }

    @Test
    public void deveHaverDesconto100PctNo6Filme() {
        Filme filmeAquaman = createFilmeAquaman();
        Filme filmeFlash = createFilmeFlash();
        Filme filmeMulherMaravilha = createFilmeMulherMaravilha();
        Filme filmeSuperman = createFilmeSuperman();
        Filme filmeLigaJustica = createFilmeLigaJustica();
        List<Filme> filmes = Arrays.asList(filmeBatman, filmeMulherMaravilha, filmeSuperman, filmeFlash, filmeAquaman, filmeLigaJustica);
        Locacao locacao = locacaoService.alugarFilmes(new Usuario("jose"), filmes);
        double desconto = filmeLigaJustica.getPrecoLocacao() * 100/100;
        assertEquals(locacao.getDesconto(), desconto, 0.1);
        assertThat(locacao.getValor(), is(filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum() - desconto));
    }

    private Filme createFilmeLigaJustica() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Liga da Justica");
        filme.setPrecoLocacao(30.0);
        return filme;
    }

    private Filme createFilmeBatman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Batman");
        filme.setPrecoLocacao(10.50);
        return filme;
    }

    private Filme createFilmeSuperman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Superman");
        filme.setPrecoLocacao(10.50);
        return filme;
    }

    private Filme createFilmeFlash() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Flash");
        filme.setPrecoLocacao(15.00);
        return filme;
    }

    private Filme createFilmeAquaman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Aquaman");
        filme.setPrecoLocacao(5.00);
        return filme;
    }

    private Filme createFilmeMulherMaravilha() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Mulher Maravilha");
        filme.setPrecoLocacao(10.00);
        return filme;
    }
}
