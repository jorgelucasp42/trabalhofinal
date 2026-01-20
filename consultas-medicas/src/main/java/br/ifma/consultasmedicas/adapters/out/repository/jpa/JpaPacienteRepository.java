package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.PacienteCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.EnderecoEmbeddable;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.PacienteJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.ports.out.PacienteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaPacienteRepository implements PacienteRepository {

    private final PacienteCrudRepository crudRepository;

    public JpaPacienteRepository(PacienteCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public Paciente obter(Integer id) {
        return buscarPorId(id).orElse(null);
    }

    @Override
    public void salvar(Paciente paciente) {
        Endereco endereco = paciente.getEndereco();

        EnderecoEmbeddable enderecoEmbeddable = new EnderecoEmbeddable(
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstadoUf(),
                endereco.getCep()
        );

        PacienteJpaEntity entity = new PacienteJpaEntity(
                paciente.getId(),
                paciente.getNomeCrianca(),
                paciente.getNomeResponsavel(),
                paciente.getDataNascimento(),
                paciente.getSexo(),
                enderecoEmbeddable,
                paciente.getPlanoSaude() != null
                        ? paciente.getPlanoSaude().toString()
                        : null
        );

        crudRepository.save(entity);
    }

    private Paciente mapToDomain(PacienteJpaEntity entity) {
        EnderecoEmbeddable e = entity.getEndereco();

        Endereco endereco = new Endereco(
                e.getLogradouro(),
                e.getNumero(),
                e.getComplemento(),
                e.getBairro(),
                e.getCidade(),
                e.getEstadoUf(),
                e.getCep()
        );

        return new Paciente(
                entity.getId(),
                entity.getNomeCrianca(),
                entity.getNomeResponsavel(),
                entity.getDataNascimento(),
                entity.getSexo(),
                endereco,
                null,
                null
        );
    }
}
