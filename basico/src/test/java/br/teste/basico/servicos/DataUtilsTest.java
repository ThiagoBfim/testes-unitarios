package br.teste.basico.servicos;

import br.teste.basico.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataUtilsTest {

    @Test
    public void deveRetonarTrueCasoSejaDomingo(){
        Date dateSunday = new GregorianCalendar(2014, Calendar.FEBRUARY, 9).getTime();
        Assert.assertTrue(DataUtils.isDomingo(dateSunday));
    }

    @Test
    public void deveRetonarDataSemanaPorExtenso(){
        Assert.assertEquals("Domingo", DataUtils.getDiaSemanaExtenso(Calendar.SUNDAY));
    }
}
