package br.ifma.consultasmedicas.adapters.out.repository;

import br.ifma.consultasmedicas.core.domain.model.PlanoSaude;
import br.ifma.consultasmedicas.ports.out.PlanoSaudeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementação em memória do PlanoSaudeRepository.
 */
public class InMemoryPlanoSaudeRepository implements PlanoSaudeRepository {
    private final Map<Integer, PlanoSaude> store = new HashMap<>();

    @Override
    public Optional<PlanoSaude> buscarPorId(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<PlanoSaude> listarTodos() {
        return List.copyOf(store.values());
    }

    @Override
    public void salvar(PlanoSaude planoSaude) {
        store.put(planoSaude.getId(), planoSaude);
    }
}
