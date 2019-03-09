package br.teste.basico.servicos;

import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.SPCIntegrationException;

public class SPCServiecImpl implements SPCService {

    @Override
    public boolean possuiNomeLimpo(Usuario usuario) throws SPCIntegrationException{
        return usuario.temSaldo();
    }
}
