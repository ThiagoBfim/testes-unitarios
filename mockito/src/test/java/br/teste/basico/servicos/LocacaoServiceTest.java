package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.FaltaEstoqueException;
import br.teste.basico.exceptions.LocadoraException;
import br.teste.basico.repository.LocacaoRepository;
import br.teste.basico.utils.DataUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.teste.basico.matchers.MatchersService.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocacaoServiceTest {


    private LocacaoService locacaoService;
    private Filme filmeBatman;
    private ISPCService spcService;
    private IEmailService emailService;
    private LocacaoRepository locacaoRepository;

    /**
     * O teste precisa ser FIRST
     * Fast				        (Menos de 1 segundo)
     * Independent/Isolate		(Não depender de outros testes) (Cada teste deve testar uma unica coisa de forma isolada.)
     * Repeatable		        (Retornar o mesmo resultado indepdentemente de quantas vezes for executado)
     * Self-verifying	        (O teste precisa se auto avaliar, e informar se houver erro ou não)
     * Timely			        (Deve ser criada no momento certo.)
     * <p>
     * Note: Boas praticas: Utilizar should ou shouldNot nos nomes dos metodos
     */
    @Before
    public void setUp() {
        locacaoRepository = Mockito.mock(LocacaoRepository.class);
        spcService = Mockito.mock(ISPCService.class);
        emailService = Mockito.mock(IEmailService.class);
        Mockito.when(spcService.possuiNomeLimpo(Mockito.any())).thenReturn(true);
        locacaoService = new LocacaoService(locacaoRepository, spcService, emailService);
        filmeBatman = createFilmeBatman();
    }

    @Test
    public void deveEnviarNotificacaoLocacoesAtrasadas() {
        Mockito.when(emailService.enviarEmail(Mockito.any())).thenReturn(true);
        locacaoService.notificarAtrasos();
        List<Locacao> locacaoList = new ArrayList<>();
        Locacao locacao = new Locacao();
        locacao.setUsuario(new Usuario());
        locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), -1));
        locacaoList.add(locacao);
        Mockito.when(locacaoRepository.obterLocacoesPendentes()).thenReturn(locacaoList);
        locacaoService.notificarAtrasos();
        Mockito.verify(emailService, Mockito.times(1)).enviarEmail(Mockito.any());
    }

    @Test
    public void naoDeveEnviarNotificacaoLocacoesAtrasadas() {
        Mockito.when(emailService.enviarEmail(Mockito.any())).thenReturn(true);
        locacaoService.notificarAtrasos();
        List<Locacao> locacaoList = new ArrayList<>();
        Locacao locacao = new Locacao();
        locacao.setUsuario(new Usuario());
        locacao.setDataRetorno(new Date());
        locacaoList.add(locacao);
        Mockito.when(locacaoRepository.obterLocacoesPendentes()).thenReturn(locacaoList);
        locacaoService.notificarAtrasos();
        Mockito.verify(emailService, Mockito.never()).enviarEmail(Mockito.any());
    }

    @Test
    public void deveAlugarPorUmDia() {
        Assume.assumeTrue(!DataUtils.isSabado(new Date()));
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
        assertThat(locacao.getDataRetorno(), ehHojeMaisDias(1));
    }

    @Test
    public void deveRealizarLocacaoNoMesmoDia() {
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
        assertThat(locacao.getDataLocacao(), ehHoje());
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
            Assert.fail("É esperado exception quando não houver filmeB.");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Filme vazio"));
        }
    }

    @Test
    public void naoDeveAlugarFilmeCasoNomeEstejaSujo() {
        Mockito.when(spcService.possuiNomeLimpo(Mockito.any())).thenReturn(false);
        try {
            locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
            Assert.fail("É esperado exception quando usuário estiver com nome sujo.");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario Negativado"));
        }
        Mockito.verify(spcService, Mockito.times(1)).possuiNomeLimpo(Mockito.any());
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
    public void deveDevolverFilmeEmSegundaQuandoCairEmDomingo() {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.WEDNESDAY));
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman, 4);
        assertThat(locacao.getDataRetorno(), caiEmUmaSegunda());
    }

    private Filme createFilmeBatman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Batman");
        filme.setPrecoLocacao(10.50);
        return filme;
    }

}
