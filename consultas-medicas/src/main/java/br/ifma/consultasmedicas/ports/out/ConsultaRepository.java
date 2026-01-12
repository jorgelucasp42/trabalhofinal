package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.Consulta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository {
    Optional<Consulta> buscarPorId(Integer id);
    List<Consulta> buscarPorData(LocalDate data);
    void salvar(Consulta consulta);
}
