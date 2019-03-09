package br.teste.basico.servicos;

import br.teste.basico.entidades.Usuario;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ISPCServiceTest {

    private ISPCService spcService;

    @Before
    public void setUp() throws Exception {
        spcService = new SPCService();
    }

    @Test
    public void deveTerNomeLimpoQuandoSaldoForSuperiorZero(){
        Usuario usuario = new Usuario("Joaquim", BigDecimal.TEN);
        Assert.assertTrue(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void naoDeveTerNomeLimpoQuandoSaldoForZero(){
        Usuario usuario = new Usuario("Joaquim", BigDecimal.ZERO);
        Assert.assertFalse(spcService.possuiNomeLimpo(usuario));
    }

    @Test
    public void naoDeveTerNomeLimpoQuandoSaldoNegativo(){
        Usuario usuario = new Usuario("Joaquim", new BigDecimal("-100"));
        Assert.assertFalse(spcService.possuiNomeLimpo(usuario));
    }
}
