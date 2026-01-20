package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.model.Medico;
import br.ifma.consultasmedicas.ports.in.ListarMedicosUseCase;
import br.ifma.consultasmedicas.ports.out.MedicoRepository;

import java.util.List;
import java.util.Objects;

/**
 * Caso de uso: Listar todos os médicos cadastrados.
 */
public class ListarMedicosService implements ListarMedicosUseCase {
    private final MedicoRepository medicoRepository;
    private final SimpleLogger logger = new SimpleLogger(ListarMedicosService.class);

    public ListarMedicosService(MedicoRepository medicoRepository) {
        this.medicoRepository = Objects.requireNonNull(medicoRepository);
    }

    @Override
    public List<Medico> listarTodos() {
        logger.info("Listando todos os médicos cadastrados");
        List<Medico> medicos = medicoRepository.listarTodos();
        logger.info("Total de %d médicos encontrados", medicos.size());
        return medicos;
    }
}
