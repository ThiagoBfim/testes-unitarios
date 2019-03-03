package br.teste.basico.servicos;

import br.teste.basico.entidades.Filme;
import br.teste.basico.entidades.Locacao;
import br.teste.basico.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoServiceTest {

    /**
     * O teste precisa ser FIRST
     * Fast				(Menos de 1 segundo)
     * Independent		(Não depender de outros testes)
     * Repeatable		(Retornar o mesmo resultado indepdentemente de quantas vezes for executado)
     * Self-verifying	(O teste precisa se auto avaliar, e informar se houver erro ou não)
     * Timely			(Deve ser criada no momento certo.)
     */
    @Test
    public void testarAlugarFilme() {
        LocacaoService locacaoService = new LocacaoService();
        Filme filme = new Filme();
        filme.setPrecoLocacao(10.50);
        Locacao locacao = locacaoService.alugarFilme(null, filme);
        Assert.assertEquals(locacao.getValor(), (filme.getPrecoLocacao()));
        Assert.assertTrue(DataUtils.isMesmaDataSimples(locacao.getDataRetorno(),DataUtils.adicionarDias(new Date(), 1)));
        Assert.assertTrue(DataUtils.isMesmaDataSimples(locacao.getDataLocacao(),new Date()));
    }
}
