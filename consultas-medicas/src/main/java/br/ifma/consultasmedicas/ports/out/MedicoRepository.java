package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Medico;

import java.util.List;

/**
 * Porta de saída para persistência de médicos.
 */
public interface MedicoRepository {
    void salvar(Medico medico);
    
    Medico obter(Integer id);
    
    List<Medico> listarTodos();
}
