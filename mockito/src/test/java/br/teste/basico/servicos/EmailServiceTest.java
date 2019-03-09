package br.teste.basico.servicos;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class EmailServiceTest {

    @Test
    public void deveEnviarEmail(){
        IEmailService emailService =  Mockito.mock(IEmailService.class);
        Mockito.when(emailService.enviarEmail(Mockito.contains("@"))).thenReturn(true);
        Assert.assertTrue(emailService.enviarEmail("email@mail.com"));
    }
    @Test
    public void naoDeveEnviarEmail(){
        IEmailService emailService =  Mockito.mock(IEmailService.class);
        Mockito.when(emailService.enviarEmail(Mockito.isNull(String.class))).thenReturn(false);
        Assert.assertFalse(emailService.enviarEmail(null));
    }
}
