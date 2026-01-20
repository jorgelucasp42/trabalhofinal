package br.ifma.consultasmedicas.adapters.in.controller;

import br.ifma.consultasmedicas.core.domain.model.Medico;
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

    public MedicoController(ListarMedicosUseCase listarMedicosUseCase) {
        this.listarMedicosUseCase = Objects.requireNonNull(listarMedicosUseCase);
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
}
