package br.ifma.consultasmedicas.adapters.out.repository.inmemory;

import br.ifma.consultasmedicas.core.domain.model.Consulta;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ImplementaÃ§Ã£o em memÃ³ria de ConsultaRepository para testes.
 */
public class InMemoryConsultaRepository implements ConsultaRepository {
    private final Map<Integer, Consulta> consultas = new HashMap<>();

    @Override
    public Optional<Consulta> buscarPorId(Integer id) {
        Objects.requireNonNull(id, "ID nÃ£o pode ser nulo");
        return Optional.ofNullable(consultas.get(id));
    }

    @Override
    public List<Consulta> buscarPorData(LocalDate data) {
        Objects.requireNonNull(data, "Data nÃ£o pode ser nula");
        return consultas.values().stream()
                .filter(c -> c.getDataHora().toLocalDate().equals(data))
                .collect(Collectors.toList());
    }

    @Override
    public void salvar(Consulta consulta) {
        Objects.requireNonNull(consulta, "Consulta nÃ£o pode ser nula");
        consultas.put(consulta.getId(), consulta);
    }

    @Override
    public Consulta obter(Integer id) {
        Objects.requireNonNull(id, "ID nÃ£o pode ser nulo");
        return consultas.get(id);
    }

    @Override
    public List<Consulta> obterPorPaciente(Integer pacienteId) {
        Objects.requireNonNull(pacienteId, "ID do paciente nÃ£o pode ser nulo");
        return consultas.values().stream()
                .filter(c -> c.getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Consulta> obterPorData(LocalDate data) {
        Objects.requireNonNull(data, "Data nÃ£o pode ser nula");
        return consultas.values().stream()
                .filter(c -> c.getDataHora().toLocalDate().equals(data))
                .collect(Collectors.toList());
    }

    public void limpar() {
        consultas.clear();
    }
}

