package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Consulta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository {
    Optional<Consulta> buscarPorId(Integer id);

    Consulta obter(Integer id);

    List<Consulta> buscarPorData(LocalDate data);

    List<Consulta> obterPorData(LocalDate data);

    List<Consulta> obterPorPaciente(Integer pacienteId);

    void salvar(Consulta consulta);
}
