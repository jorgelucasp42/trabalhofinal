package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.Medico;
import br.ifma.consultasmedicas.ports.in.CadastrarMedicoCommand;
import br.ifma.consultasmedicas.ports.in.CadastrarMedicoUseCase;
import br.ifma.consultasmedicas.ports.out.IdGenerator;
import br.ifma.consultasmedicas.ports.out.MedicoRepository;

import java.util.Objects;

/**
 * Caso de uso: Cadastro de Novo Médico.
 * 
 * Responsabilidades:
 * - Validar dados do médico
 * - Verificar se CRM já existe
 * - Gerar ID único
 * - Criar agregado Medico
 * - Persistir no repositório
 */
public class CadastrarMedicoService implements CadastrarMedicoUseCase {

    private final MedicoRepository medicoRepository;
    private final IdGenerator idGenerator;
    private final SimpleLogger logger = new SimpleLogger(CadastrarMedicoService.class);

    public CadastrarMedicoService(MedicoRepository medicoRepository, IdGenerator idGenerator) {
        this.medicoRepository = Objects.requireNonNull(medicoRepository);
        this.idGenerator = Objects.requireNonNull(idGenerator);
    }

    @Override
    public Integer cadastrar(CadastrarMedicoCommand command) {
        logger.info("Iniciando cadastro de novo médico: %s", command.getNome());

        // Validar entrada
        validarCommand(command);

        // Verificar se CRM já existe
        verificarCrmUnico(command.getCrm());

        // Criar agregado Medico
        Integer medicoId = idGenerator.gerarId();
        Medico medico = new Medico(
                medicoId,
                command.getNome(),
                command.getEspecialidade(),
                command.getCrm());

        // Persistir
        medicoRepository.salvar(medico);
        logger.info("Médico cadastrado com sucesso. ID: %d, Nome: %s, CRM: %s",
                medicoId,
                command.getNome(),
                command.getCrm());

        return medicoId;
    }

    private void validarCommand(CadastrarMedicoCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo");
        }
        if (command.getNome() == null || command.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (command.getEspecialidade() == null || command.getEspecialidade().isBlank()) {
            throw new IllegalArgumentException("Especialidade é obrigatória");
        }
        if (command.getCrm() == null || command.getCrm().isBlank()) {
            throw new IllegalArgumentException("CRM é obrigatório");
        }
    }

    private void verificarCrmUnico(String crm) {
        // Verifica se já existe médico com o mesmo CRM
        var medicos = medicoRepository.listarTodos();
        boolean crmJaExiste = medicos.stream()
                .anyMatch(m -> m.getCrm().equalsIgnoreCase(crm));

        if (crmJaExiste) {
            throw new DomainException("Já existe um médico cadastrado com o CRM: " + crm);
        }
    }
}
