package br.ifma.consultasmedicas.ports.in;

import br.ifma.consultasmedicas.core.domain.model.Medico;

import java.util.List;

/**
 * Porta de entrada para listagem de médicos.
 */
public interface ListarMedicosUseCase {
    List<Medico> listarTodos();
}
