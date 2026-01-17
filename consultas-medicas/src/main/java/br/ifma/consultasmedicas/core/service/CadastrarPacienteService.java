package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.*;
import br.ifma.consultasmedicas.ports.in.CadastrarPacienteCommand;
import br.ifma.consultasmedicas.ports.in.CadastrarPacienteUseCase;
import br.ifma.consultasmedicas.ports.out.IdGenerator;
import br.ifma.consultasmedicas.ports.out.PacienteRepository;
import br.ifma.consultasmedicas.ports.out.PlanoSaudeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Caso de uso: Cadastro de Novo Paciente.
 * 
 * Responsabilidades:
 * - Validar dados do paciente
 * - Gerar ID único
 * - Criar agregado Paciente
 * - Persistir no repositório
 * - Registrar peso/altura inicial (se informados)
 */
public class CadastrarPacienteService implements CadastrarPacienteUseCase {

    private final PacienteRepository pacienteRepository;
    private final PlanoSaudeRepository planoSaudeRepository;
    private final IdGenerator idGenerator;
    private final SimpleLogger logger = new SimpleLogger(CadastrarPacienteService.class);

    public CadastrarPacienteService(PacienteRepository pacienteRepository,
            PlanoSaudeRepository planoSaudeRepository,
            IdGenerator idGenerator) {
        this.pacienteRepository = Objects.requireNonNull(pacienteRepository);
        this.planoSaudeRepository = Objects.requireNonNull(planoSaudeRepository);
        this.idGenerator = Objects.requireNonNull(idGenerator);
    }

    @Override
    public Integer cadastrar(CadastrarPacienteCommand command) {
        logger.info("Iniciando cadastro de novo paciente: %s", command.getNomeCrianca());

        // Validar entrada
        validarCommand(command);

        // Mapear comando para entidades de domínio
        Endereco endereco = new Endereco(
                command.getEndereco().getLogradouro(),
                command.getEndereco().getNumero(),
                command.getEndereco().getComplemento(),
                command.getEndereco().getBairro(),
                command.getEndereco().getCidade(),
                command.getEndereco().getEstadoUf(),
                command.getEndereco().getCep());

        // Mapear telefones
        List<Telefone> telefones = new ArrayList<>();
        if (command.getTelefones() != null) {
            for (CadastrarPacienteCommand.TelefoneCommand telCmd : command.getTelefones()) {
                TelefoneTipo tipo = telCmd.getTipo();
                Telefone telefone = new Telefone(telCmd.getNumero(), tipo, telCmd.getResponsavel());
                telefones.add(telefone);
                logger.debug("Telefone adicionado: %s (%s)", telCmd.getNumero(), tipo);
            }
        }

        // Buscar plano de saúde (se informado)
        PlanoSaude planoSaude = null;
        if (command.getPlanoSaudeId() != null) {
            planoSaude = planoSaudeRepository.buscarPorId(command.getPlanoSaudeId())
                    .orElseThrow(() -> {
                        logger.error("Plano de saúde não encontrado: %d", command.getPlanoSaudeId());
                        return new DomainException("Plano de saúde não encontrado: " + command.getPlanoSaudeId());
                    });
            logger.debug("Plano de saúde associado: %s", planoSaude.getNomePlano());
        } else {
            logger.debug("Paciente cadastrado como particular (sem plano de saúde)");
        }

        // Criar agregado Paciente
        Integer pacienteId = idGenerator.gerarId();
        Paciente paciente = new Paciente(
                pacienteId,
                command.getNomeCrianca(),
                command.getNomeResponsavel(),
                command.getDataNascimento(),
                command.getSexo(),
                endereco,
                telefones,
                planoSaude);

        // Persistir
        pacienteRepository.salvar(paciente);
        logger.info("Paciente cadastrado com sucesso. ID: %d, Nome: %s, Tipo: %s",
                pacienteId,
                command.getNomeCrianca(),
                planoSaude != null ? "Convênio" : "Particular");

        return pacienteId;
    }

    private void validarCommand(CadastrarPacienteCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo");
        }
        if (command.getNomeCrianca() == null || command.getNomeCrianca().isBlank()) {
            throw new IllegalArgumentException("Nome da criança é obrigatório");
        }
        if (command.getNomeResponsavel() == null || command.getNomeResponsavel().isBlank()) {
            throw new IllegalArgumentException("Nome do responsável é obrigatório");
        }
        if (command.getDataNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }
        if (command.getDataNascimento().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento não pode ser no futuro");
        }
        if (command.getSexo() == null || command.getSexo().isBlank()) {
            throw new IllegalArgumentException("Sexo é obrigatório");
        }
        if (!command.getSexo().matches("[MF]")) {
            throw new IllegalArgumentException("Sexo deve ser 'M' ou 'F'");
        }
        if (command.getEndereco() == null) {
            throw new IllegalArgumentException("Endereço é obrigatório");
        }

        // Validar endereço
        CadastrarPacienteCommand.EnderecoCommand endereco = command.getEndereco();
        if (endereco.getLogradouro() == null || endereco.getLogradouro().isBlank()) {
            throw new IllegalArgumentException("Logradouro é obrigatório");
        }
        if (endereco.getNumero() == null || endereco.getNumero().isBlank()) {
            throw new IllegalArgumentException("Número é obrigatório");
        }
        if (endereco.getBairro() == null || endereco.getBairro().isBlank()) {
            throw new IllegalArgumentException("Bairro é obrigatório");
        }
        if (endereco.getCidade() == null || endereco.getCidade().isBlank()) {
            throw new IllegalArgumentException("Cidade é obrigatória");
        }
        if (endereco.getEstadoUf() == null || endereco.getEstadoUf().isBlank()) {
            throw new IllegalArgumentException("Estado (UF) é obrigatório");
        }
        if (endereco.getCep() == null || endereco.getCep().isBlank()) {
            throw new IllegalArgumentException("CEP é obrigatório");
        }

        // Validar telefones
        if (command.getTelefones() != null && !command.getTelefones().isEmpty()) {
            for (CadastrarPacienteCommand.TelefoneCommand tel : command.getTelefones()) {
                if (tel.getNumero() == null || tel.getNumero().isBlank()) {
                    throw new IllegalArgumentException("Número de telefone é obrigatório");
                }
                if (tel.getTipo() == null) {
                    throw new IllegalArgumentException("Tipo de telefone é obrigatório");
                }
            }
        }
    }
}
