package br.teste.basico.matchers;

import java.util.Calendar;

public class MatchersService {

    public static DiaSemanaMatcher caiEm(Integer diaSemana){
        return new DiaSemanaMatcher(diaSemana);
    }

    public static DiaSemanaMatcher caiEmUmaSegunda(){
        return new DiaSemanaMatcher(Calendar.MONDAY);
    }

    public static SomaDiasMatcher ehHojeMaisDias(Integer dias){
        return new SomaDiasMatcher(dias);
    }

    public static SomaDiasMatcher ehHoje(){
        return new SomaDiasMatcher(0);
    }
}
