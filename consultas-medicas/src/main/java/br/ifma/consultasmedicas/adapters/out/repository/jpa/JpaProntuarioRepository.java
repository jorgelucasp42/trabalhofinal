package br.ifma.consultasmedicas.adapters.out.repository.jpa;

import br.ifma.consultasmedicas.adapters.out.repository.jpa.crud.ProntuarioCrudRepository;
import br.ifma.consultasmedicas.adapters.out.repository.jpa.entity.ProntuarioJpaEntity;
import br.ifma.consultasmedicas.core.domain.model.Prontuario;
import br.ifma.consultasmedicas.ports.out.ProntuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação JPA de ProntuarioRepository.
 * Responsável por mapear domínio ↔ banco de dados.
 */
@Repository
public class JpaProntuarioRepository implements ProntuarioRepository {

    private final ProntuarioCrudRepository crudRepository;

    public JpaProntuarioRepository(ProntuarioCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public void salvar(Prontuario prontuario) {
        double imc = prontuario.getPeso() / (prontuario.getAltura() * prontuario.getAltura());
        ProntuarioJpaEntity entity = new ProntuarioJpaEntity(
                prontuario.getId(),
                prontuario.getConsulta().getId(),
                prontuario.getPeso(),
                prontuario.getAltura(),
                imc,
                prontuario.getSintomas(),
                prontuario.getObservacaoClinica());
        crudRepository.save(entity);
    }

    @Override
    public Optional<Prontuario> buscarPorId(Integer id) {
        return crudRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Prontuario> buscarPorPaciente(Integer pacienteId) {
        // Nota: Esta implementação simplificada não carrega relacionamentos
        // Em produção, seria necessário JOIN com consultas
        return List.of();
    }

    @Override
    public boolean existeParaConsulta(Integer consultaId) {
        List<ProntuarioJpaEntity> entities = crudRepository.findByConsultaId(consultaId);
        return !entities.isEmpty();
    }

    private Prontuario mapToDomain(ProntuarioJpaEntity entity) {
        // Nota: Esta implementação simplificada
        // Em produção, seria necessário recuperar relacionamentos
        return new Prontuario(
                entity.getId(),
                null, // Seria necessário buscar Consulta do repository
                entity.getPeso(),
                entity.getAltura(),
                entity.getSintomas(),
                entity.getObservacaoClinica(),
                List.of(),
                List.of());
    }
}
