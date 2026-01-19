package br.ifma.consultasmedicas.adapters.out.repository.inmemory;

import br.ifma.consultasmedicas.core.domain.model.Medico;
import br.ifma.consultasmedicas.ports.out.MedicoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ImplementaÃ§Ã£o em memÃ³ria de MedicoRepository para testes.
 */
public class InMemoryMedicoRepository implements MedicoRepository {
    private final Map<Integer, Medico> medicos = new HashMap<>();

    @Override
    public void salvar(Medico medico) {
        Objects.requireNonNull(medico, "MÃ©dico nÃ£o pode ser nulo");
        medicos.put(medico.getId(), medico);
    }

    @Override
    public Medico obter(Integer id) {
        Objects.requireNonNull(id, "ID nÃ£o pode ser nulo");
        return medicos.get(id);
    }

    @Override
    public List<Medico> listarTodos() {
        return List.copyOf(medicos.values());
    }

    public void limpar() {
        medicos.clear();
    }
}

