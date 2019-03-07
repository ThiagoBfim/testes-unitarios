package br.teste.basico.matchers;

import br.teste.basico.utils.DataUtils;
import org.hamcrest.Description;
import org.junit.matchers.TypeSafeMatcher;

import java.util.Date;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

    private Integer diaSemana;

    public DiaSemanaMatcher(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    @Override
    public boolean matchesSafely(Date item) {
        return DataUtils.verificarDiaSemana(item, diaSemana);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Esperado que a data fosse: " + DataUtils.getDiaSemanaExtenso(diaSemana));
    }
}
