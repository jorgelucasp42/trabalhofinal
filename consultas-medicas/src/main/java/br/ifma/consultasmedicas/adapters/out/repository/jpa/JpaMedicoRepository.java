package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.MedicoCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.MedicoJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.Medico;
import br.ifma.consultasmedicas.ports.out.MedicoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaMedicoRepository implements MedicoRepository {

    private final MedicoCrudRepository crudRepository;

    public JpaMedicoRepository(MedicoCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public void salvar(Medico medico) {
        crudRepository.save(new MedicoJpaEntity(
                medico.getId(),
                medico.getNome(),
                medico.getEspecialidade(),
                medico.getCrm()
        ));
    }

    @Override
    public Medico obter(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain)
                .orElse(null);
    }

    @Override
    public List<Medico> listarTodos() {
        return crudRepository.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Medico mapToDomain(MedicoJpaEntity entity) {
        return new Medico(
                entity.getId(),
                entity.getNome(),
                entity.getEspecialidade(),
                entity.getCrm()
        );
    }
}