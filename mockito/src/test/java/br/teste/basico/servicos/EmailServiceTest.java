package br.teste.basico.servicos;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    IEmailService emailService;

    @Test
    public void deveEnviarEmail(){
        Mockito.when(emailService.enviarEmail(Mockito.contains("@"))).thenReturn(true);
        Assert.assertTrue(emailService.enviarEmail("email@mail.com"));
    }
    @Test
    public void naoDeveEnviarEmail(){
        Mockito.when(emailService.enviarEmail(Mockito.isNull(String.class))).thenReturn(false);
        Assert.assertFalse(emailService.enviarEmail(null));
    }
}
