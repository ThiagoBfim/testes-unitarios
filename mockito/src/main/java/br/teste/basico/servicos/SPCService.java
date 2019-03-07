package br.teste.basico.servicos;

import br.teste.basico.entidades.Usuario;

import java.math.BigDecimal;

public class SPCService {

    public boolean possuiNomeLimpo(Usuario usuario) {
        return usuario.getSaldo().compareTo(BigDecimal.ZERO) > 0;
    }
}
