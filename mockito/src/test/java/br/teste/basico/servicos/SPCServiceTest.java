package br.teste.basico.servicos;

import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.SPCIntegrationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class SPCServiceTest {

    /*Com SPY é realizado a chamada real do método, caso não haja um captura do metodo para tratar de uma forma diferente.
     *
     * O spy funciona de forma semalhante ao seguinte codigo: Mockito.when(method).thenCallRealMethod()
     *
     * NOTE: É preciso tomar cuidado ao utilizar o spy com when, pois ele chamará o metodo antes de passar para o Mockito gerenciar.
     * Exemplo:
     *   Mockito.when(spcService.possuiNomeLimpo(usuario)).thenThrow(new SPCIntegrationException("Falha de comunicação com SPC")); //WRONG
     *   Mockito.doThrow(new SPCIntegrationException("Falha de comunicação com SPC")).when(spcService).possuiNomeLimpo(usuario); //NICE
     * */
    @Spy
    private SPCServiecImpl spcService;

    @Test
    public void deveTerNomeLimpoQuandoSaldoForSuperiorZero() {
        Usuario usuario = new Usuario("Joaquim", BigDecimal.TEN);
        Assert.assertTrue(usuario.temSaldo());
        Assert.assertTrue(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void naoDeveTerNomeLimpoQuandoSaldoForZero() {
        Usuario usuario = new Usuario("Joaquim", BigDecimal.ZERO);
        Assert.assertFalse(usuario.temSaldo());
        Assert.assertFalse(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void naoDeveTerNomeLimpoQuandoSaldoNegativo() {
        Usuario usuario = new Usuario("Joaquim", new BigDecimal("-100"));
        Assert.assertFalse(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void deveTratarErroSPC() {
        Usuario usuario = new Usuario("Joaquim", new BigDecimal("-100"));
        try {
            Mockito.doThrow(new SPCIntegrationException("Falha de comunicação com SPC")).when(spcService).possuiNomeLimpo(usuario);
            spcService.possuiNomeLimpo(usuario);
            Assert.fail("Deveria ter lançado exception.");
        } catch (SPCIntegrationException e) {
            Assert.assertEquals(e.getMessage(), ("Falha de comunicação com SPC"));
        }
    }
}
