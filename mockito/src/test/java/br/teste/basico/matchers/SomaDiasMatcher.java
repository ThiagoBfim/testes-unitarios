package br.teste.basico.matchers;

import br.teste.basico.utils.DataUtils;
import org.hamcrest.Description;
import org.junit.matchers.TypeSafeMatcher;

import java.util.Date;

public class SomaDiasMatcher extends TypeSafeMatcher<Date> {

    private Integer qtdDias;

    public SomaDiasMatcher(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }

    @Override
    public boolean matchesSafely(Date item) {
        return DataUtils.isMesmaDataSimples(item, sumDaysInDate());
    }

    private Date sumDaysInDate() {
        return DataUtils.adicionarDias(new Date(), qtdDias);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Esperado que a data fosse: " + sumDaysInDate());
    }
}
