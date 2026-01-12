package br.ifma.consultasmedicas.ports.in;

import br.ifma.consultasmedicas.core.domain.model.Consulta;

import java.time.LocalDate;
import java.util.List;

public interface ListarConsultasDoDiaUseCase {
    List<Consulta> listar(LocalDate data);
}
