package br.teste.basico.suite;

import br.teste.basico.servicos.CalculoValorLocacaoTest;
import br.teste.basico.servicos.DataUtilsTest;
import br.teste.basico.servicos.LocacaoServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CalculoValorLocacaoTest.class,
        DataUtilsTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {
    /*Não é recomendado, pois com uma ferramanta de integração como o Jenkins ou TeamCity, os testes seriam executados de forma duplicada.*/
}