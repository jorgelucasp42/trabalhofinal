package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.MedicamentoCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.MedicamentoJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.Medicamento;
import br.ifma.consultasmedicas.ports.out.MedicamentoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaMedicamentoRepository implements MedicamentoRepository {

    private final MedicamentoCrudRepository crudRepository;

    public JpaMedicamentoRepository(MedicamentoCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<Medicamento> buscarPorId(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Medicamento> listarTodos() {
        return crudRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void salvar(Medicamento medicamento) {
        MedicamentoJpaEntity entity = mapToEntity(medicamento);
        crudRepository.save(entity);
    }

    /* =========================
       MAPPERS
       ========================= */

    private Medicamento mapToDomain(MedicamentoJpaEntity entity) {
        return new Medicamento(
                entity.getId(),
                entity.getNome()
        );
    }

    private MedicamentoJpaEntity mapToEntity(Medicamento medicamento) {
        return new MedicamentoJpaEntity(
                medicamento.getId(),
                medicamento.getNome()
        );
    }
}
