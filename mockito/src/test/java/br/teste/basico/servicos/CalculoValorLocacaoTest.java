package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.entidades.Usuario;
import br.teste.basico.repository.LocacaoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

    //Utilizando a tenica Data Driven Test

    public List<Filme> filmes;

    public Double valorDesconto;

    private LocacaoService locacaoService;

    public CalculoValorLocacaoTest(List<Filme> filmes, Double valorDesconto) {
        LocacaoRepository locacaoRepository = Mockito.mock(LocacaoRepository.class);
        ISPCService spcService = Mockito.mock(ISPCService.class);
        Mockito.when(spcService.possuiNomeLimpo(Mockito.any())).thenReturn(true);
        IEmailService emailService =  Mockito.mock(IEmailService.class);
        Mockito.when(emailService.enviarEmail(Mockito.contains("@"))).thenReturn(true);
        locacaoService = new LocacaoService(locacaoRepository, spcService,emailService );
        this.filmes = filmes;
        this.valorDesconto = valorDesconto;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(createFilmeLigaJustica(), createFilmeLigaJustica(), createFilmeMulherMaravilha()), 0.25},
                {Arrays.asList(createFilmeLigaJustica(), createFilmeLigaJustica(), createFilmeMulherMaravilha(), createFilmeAquaman()), 0.50},
                {Arrays.asList(createFilmeLigaJustica(), createFilmeLigaJustica(), createFilmeMulherMaravilha(), createFilmeAquaman(),
                        createFilmeFlash()), 0.75},
                {Arrays.asList(createFilmeLigaJustica(), createFilmeLigaJustica(), createFilmeMulherMaravilha(), createFilmeAquaman(),
                        createFilmeFlash(), createFilmeSuperman()), 1.0},
                {Arrays.asList(createFilmeLigaJustica(), createFilmeLigaJustica(), createFilmeMulherMaravilha(), createFilmeAquaman(),
                        createFilmeFlash(), createFilmeSuperman(), createFilmeSuperman()), 0.0}
        });
    }

    @Test
    public void deveHaverDescontoAoAlugarMuitosFilmes() {
        Locacao locacao = locacaoService.alugarFilmes(new Usuario("jose"), filmes, 1);
        double desconto = filmes.get(filmes.size() - 1).getPrecoLocacao() * valorDesconto;
        assertEquals(locacao.getDesconto(), desconto, 0.1);
        assertThat(locacao.getValor(), is(filmes.stream().mapToDouble(Filme::getPrecoLocacao).sum() - desconto));
    }


    private static Filme createFilmeLigaJustica() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Liga da Justica");
        filme.setPrecoLocacao(30.0);
        return filme;
    }

    private static Filme createFilmeSuperman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Superman");
        filme.setPrecoLocacao(10.50);
        return filme;
    }

    private static Filme createFilmeFlash() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Flash");
        filme.setPrecoLocacao(15.00);
        return filme;
    }

    private static Filme createFilmeAquaman() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Aquaman");
        filme.setPrecoLocacao(5.00);
        return filme;
    }

    private static Filme createFilmeMulherMaravilha() {
        Filme filme = new Filme();
        filme.setEstoque(2);
        filme.setNome("Mulher Maravilha");
        filme.setPrecoLocacao(10.00);
        return filme;
    }

}
