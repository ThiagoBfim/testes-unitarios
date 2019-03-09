package br.teste.basico.servicos;

import br.teste.basico.entidades.Usuario;
import br.teste.basico.exceptions.SPCIntegrationException;

public interface ISPCService {

    boolean possuiNomeLimpo(Usuario usuario) throws SPCIntegrationException;
}
