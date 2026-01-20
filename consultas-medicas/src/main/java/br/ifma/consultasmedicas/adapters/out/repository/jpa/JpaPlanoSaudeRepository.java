package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.PlanoSaudeCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.PlanoSaudeJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.PlanoSaude;
import br.ifma.consultasmedicas.ports.out.PlanoSaudeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JpaPlanoSaudeRepository implements PlanoSaudeRepository {

    private final PlanoSaudeCrudRepository crudRepository;

    public JpaPlanoSaudeRepository(PlanoSaudeCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<PlanoSaude> buscarPorId(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<PlanoSaude> listarTodos() {
        return crudRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void salvar(PlanoSaude planoSaude) {
        PlanoSaudeJpaEntity entity = mapToEntity(planoSaude);
        crudRepository.save(entity);
    }

    /* =========================
       MAPPERS
       ========================= */

    private PlanoSaude mapToDomain(PlanoSaudeJpaEntity entity) {
        return new PlanoSaude(
                entity.getId(),
                entity.getNomePlano()
        );
    }

    private PlanoSaudeJpaEntity mapToEntity(PlanoSaude planoSaude) {
        return new PlanoSaudeJpaEntity(
                planoSaude.getId(),
                planoSaude.getNomePlano()
        );
    }
}
