package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.PlanoSaude;

import java.util.List;
import java.util.Optional;

/**
 * Porta de saída para persistência de Plano de Saúde.
 */
public interface PlanoSaudeRepository {
    Optional<PlanoSaude> buscarPorId(Integer id);
    List<PlanoSaude> listarTodos();
    void salvar(PlanoSaude planoSaude);
}
