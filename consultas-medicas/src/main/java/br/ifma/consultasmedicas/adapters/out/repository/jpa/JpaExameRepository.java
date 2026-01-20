package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.ExameCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.ExameJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.Exame;
import br.ifma.consultasmedicas.ports.out.ExameRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação JPA de ExameRepository.
 * Responsável por mapear domínio ↔ persistência.
 */
@Repository
public class JpaExameRepository implements ExameRepository {

    private final ExameCrudRepository crudRepository;

    public JpaExameRepository(ExameCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<Exame> buscarPorId(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Exame> listarTodos() {
        return crudRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void salvar(Exame exame) {
        ExameJpaEntity entity = mapToEntity(exame);
        crudRepository.save(entity);
    }

    /* =========================
       MAPPERS
       ========================= */

    private Exame mapToDomain(ExameJpaEntity entity) {
        return new Exame(
                entity.getId(),
                entity.getNome()
        );
    }

    private ExameJpaEntity mapToEntity(Exame exame) {
        return new ExameJpaEntity(
                exame.getId(),
                exame.getNome()
        );
    }
}

