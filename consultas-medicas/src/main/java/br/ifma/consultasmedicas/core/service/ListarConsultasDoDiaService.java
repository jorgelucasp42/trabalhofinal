package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.model.Consulta;
import br.ifma.consultasmedicas.ports.in.ListarConsultasDoDiaUseCase;
import br.ifma.consultasmedicas.ports.out.ConsultaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ListarConsultasDoDiaService implements ListarConsultasDoDiaUseCase {
    private final ConsultaRepository consultaRepository;

    public ListarConsultasDoDiaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = Objects.requireNonNull(consultaRepository);
    }

    @Override
    public List<Consulta> listar(LocalDate data) {
        return consultaRepository.buscarPorData(data);
    }
}
