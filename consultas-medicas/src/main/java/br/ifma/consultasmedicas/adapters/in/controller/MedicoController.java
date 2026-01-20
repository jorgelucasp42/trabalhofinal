package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.Medico;
import br.ifma.consultasmedicas.ports.in.CadastrarMedicoCommand;
import br.ifma.consultasmedicas.ports.in.CadastrarMedicoUseCase;
import br.ifma.consultasmedicas.ports.in.ListarMedicosUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Adaptador de entrada (Controller) para Médicos.
 * Responsabilidade: orquestrar chamadas aos casos de uso e mapear respostas
 * para DTO.
 */
@Service
public class MedicoController {
    private final ListarMedicosUseCase listarMedicosUseCase;
    private final CadastrarMedicoUseCase cadastrarMedicoUseCase;

    public MedicoController(
            ListarMedicosUseCase listarMedicosUseCase,
            CadastrarMedicoUseCase cadastrarMedicoUseCase) {
        this.listarMedicosUseCase = Objects.requireNonNull(listarMedicosUseCase);
        this.cadastrarMedicoUseCase = Objects.requireNonNull(cadastrarMedicoUseCase);
    }

    /**
     * Lista todos os médicos cadastrados.
     * 
     * @return lista de médicos
     */
    public List<MedicoListagemResponse> listarTodos() {
        List<Medico> medicos = listarMedicosUseCase.listarTodos();
        
        return medicos.stream()
                .map(MedicoListagemResponse::fromDomain)
                .collect(Collectors.toList());
    }

    /**
     * Cadastra um novo médico.
     * 
     * @param command dados do médico a cadastrar
     * @return resposta com ID do médico criado
     * @throws DomainException          se houver erro de regra de negócio
     * @throws IllegalArgumentException se houver erro de validação
     */
    public MedicoCadastroResponse cadastrarMedico(CadastrarMedicoCommand command) {
        try {
            validarCommand(command);
            Integer medicoId = cadastrarMedicoUseCase.cadastrar(command);
            return new MedicoCadastroResponse(medicoId, "Médico cadastrado com sucesso");
        } catch (DomainException e) {
            throw e; // Relança para tratamento em nível superior
        } catch (IllegalArgumentException e) {
            throw e; // Relança para tratamento em nível superior
        }
    }

    /**
     * Valida os dados de entrada antes de processar.
     */
    private void validarCommand(CadastrarMedicoCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Comando não pode ser nulo");
        }
        // Validações adicionais podem ser feitas aqui se necessário
        // O UseCase já faz validações completas
    }

    /**
     * DTO de resposta para listagem de médicos.
     */
    public static class MedicoListagemResponse {
        private final Integer id;
        private final String nome;
        private final String especialidade;
        private final String crm;

        public MedicoListagemResponse(Integer id, String nome, String especialidade, String crm) {
            this.id = id;
            this.nome = nome;
            this.especialidade = especialidade;
            this.crm = crm;
        }

        public static MedicoListagemResponse fromDomain(Medico medico) {
            return new MedicoListagemResponse(
                    medico.getId(),
                    medico.getNome(),
                    medico.getEspecialidade(),
                    medico.getCrm());
        }

        public Integer getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getEspecialidade() {
            return especialidade;
        }

        public String getCrm() {
            return crm;
        }
    }

    /**
     * DTO de resposta para cadastro de médico.
     */
    public static class MedicoCadastroResponse {
        private final Integer medicoId;
        private final String mensagem;

        public MedicoCadastroResponse(Integer medicoId, String mensagem) {
            this.medicoId = medicoId;
            this.mensagem = mensagem;
        }

        public Integer getMedicoId() {
            return medicoId;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}
