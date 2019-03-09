package br.teste.basico.servicos;

import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.SPCIntegrationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class SPCServiceTest {

    @Mock
    private ISPCService spcService;

    @Test
    public void deveTerNomeLimpoQuandoSaldoForSuperiorZero(){
        Usuario usuario = new Usuario("Joaquim", BigDecimal.TEN);
        Assert.assertTrue(usuario.temSaldo());
        Mockito.when(spcService.possuiNomeLimpo(usuario)).thenReturn(true);
        Assert.assertTrue(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void naoDeveTerNomeLimpoQuandoSaldoForZero(){
        Usuario usuario = new Usuario("Joaquim", BigDecimal.ZERO);
        Assert.assertFalse(usuario.temSaldo());
        Assert.assertFalse(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void naoDeveTerNomeLimpoQuandoSaldoNegativo(){
        Usuario usuario = new Usuario("Joaquim", new BigDecimal("-100"));
        Assert.assertFalse(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void deveTratarErroSPC() {
        Usuario usuario = new Usuario("Joaquim", new BigDecimal("-100"));
        try {
            Mockito.when(spcService.possuiNomeLimpo(usuario)).thenThrow(new SPCIntegrationException("Falha de comunicação com SPC"));
            spcService.possuiNomeLimpo(usuario);
            Assert.fail("Deveria ter lançado exception.");
        } catch (SPCIntegrationException e) {
            Assert.assertEquals(e.getMessage(), ("Falha de comunicação com SPC"));
        }
    }
}
