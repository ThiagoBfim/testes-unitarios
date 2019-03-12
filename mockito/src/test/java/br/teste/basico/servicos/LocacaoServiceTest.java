package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.FaltaEstoqueException;
import br.teste.basico.exceptions.LocadoraException;
import br.teste.basico.matchers.SomaDiasMatcher;
import br.teste.basico.repository.LocacaoRepository;
import br.teste.basico.utils.DataUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static br.teste.basico.matchers.MatchersService.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class, SomaDiasMatcher.class})
public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private SPCService spcService;
    @Mock
    private IEmailService emailService;
    @Mock
    private LocacaoRepository locacaoRepository;

    private Filme filmeBatman;

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
        MockitoAnnotations.initMocks(this);
        Mockito.when(spcService.possuiNomeLimpo(Mockito.any())).thenReturn(true);
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
    public void naoDeveEnviarNotificacaoLocacoesNaoAtrasadas() {
        Mockito.when(emailService.enviarEmail(Mockito.any())).thenReturn(true);
        locacaoService.notificarAtrasos();
        List<Locacao> locacaoList = new ArrayList<>();
        Locacao locacao = new Locacao();
        locacao.setUsuario(new Usuario());
        locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));
        locacaoList.add(locacao);
        Mockito.when(locacaoRepository.obterLocacoesPendentes()).thenReturn(locacaoList);
        locacaoService.notificarAtrasos();
        Mockito.verify(emailService, Mockito.never()).enviarEmail(Mockito.any());
    }

    @Test
    public void naoDeveEnviarNotificacaoLocacoesDiaRetornoHoje() {
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
    public void deveAlugarPorUmDia() throws Exception {
//        Assume.assumeTrue(!DataUtils.isSabado(new Date()));
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(11, 03, 2019));
        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman);
        assertThat(locacao.getDataRetorno(), ehHojeMaisDias(1));
        assertTrue(DataUtils.isMesmaDataSimples(locacao.getDataRetorno(), DataUtils.obterData(12, 03, 2019)));
        assertTrue(DataUtils.isMesmaDataSimples(locacao.getDataLocacao(), DataUtils.obterData(11, 03, 2019)));
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
    public void deveDevolverFilmeEmSegundaQuandoCairEmDomingo() throws Exception {
//        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.WEDNESDAY));

        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(26,4,2017));

        Locacao locacao = locacaoService.alugarFilme(new Usuario("jose"), filmeBatman, 4);
        assertThat(locacao.getDataRetorno(), caiEmUmaSegunda());
    }

    @Test
    public void deveProrrogarLocacao() throws Exception {
        Locacao locacao = criandoLocacao();
        Date dataRetornoBeforeProrrogacao = locacao.getDataRetorno();
        Double valorAntesProrrogacao = locacao.getValor();
        int diasAdicioandos = 7;

        Mockito.when(locacaoRepository.findByCod(Mockito.eq(1L))).thenReturn(locacao);
        locacaoService.prorrogarLocacao(1L, diasAdicioandos);

        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoRepository).salvar(argumentCaptor.capture());
        Locacao locacaoRetornada = argumentCaptor.getValue();


        if(DataUtils.isDomingo(new Date())){
            diasAdicioandos++;
        }
        assertEquals(valorAntesProrrogacao *diasAdicioandos , locacaoRetornada.getValor(), 0.1);
        assertTrue(DataUtils.isMesmaDataSimples(locacaoRetornada.getDataRetorno(), DataUtils.adicionarDias(dataRetornoBeforeProrrogacao, diasAdicioandos)));

    }

    private Locacao criandoLocacao() {
        Locacao locacao = new Locacao();
        locacao.setValor(10.0);
        locacao.setDataRetorno(new Date());
        locacao.setDataLocacao(new Date());
        locacao.setCodLocacao(1L);
        locacao.setFilmes(Collections.singletonList(filmeBatman));
        return locacao;
    }

    private Filme createFilmeBatman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Batman");
        filme.setPrecoLocacao(10.50);
        return filme;
    }

}
