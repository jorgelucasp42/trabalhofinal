package br.ifma.consultasmedicas.adapters.in.rest;

import br.ifma.consultasmedicas.adapters.in.controller.MedicoController;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.CadastrarMedicoCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * REST Controller para Médicos.
 * Adaptador de entrada REST baseado no Controller existente.
 */
@RestController
@RequestMapping("/v1/medicos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MedicoRestController {

    private final MedicoController controller;

    public MedicoRestController(MedicoController controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    /**
     * Lista todos os médicos cadastrados.
     *
     * @return lista de médicos
     */
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<MedicoController.MedicoListagemResponse> medicos = controller.listarTodos();
            
            return ResponseEntity.ok(new ListagemMedicosResponse(
                    medicos,
                    LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Erro interno do servidor: " + e.getMessage(),
                            LocalDateTime.now()));
        }
    }

    /**
     * Cadastra um novo médico.
     *
     * @param command dados do médico
     * @return resposta com ID do médico criado
     */
    @PostMapping
    public ResponseEntity<?> cadastrarMedico(@RequestBody CadastrarMedicoCommand command) {
        try {
            var response = controller.cadastrarMedico(command);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CadastroMedicoResponse(
                            response.getMedicoId(),
                            response.getMensagem(),
                            LocalDateTime.now()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            e.getMessage(),
                            LocalDateTime.now()));
        } catch (DomainException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            e.getMessage(),
                            LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Erro interno do servidor: " + e.getMessage(),
                            LocalDateTime.now()));
        }
    }

    /**
     * DTO de resposta para listagem de médicos.
     */
    public static class ListagemMedicosResponse {
        private final List<MedicoController.MedicoListagemResponse> medicos;
        private final LocalDateTime timestamp;

        public ListagemMedicosResponse(
                List<MedicoController.MedicoListagemResponse> medicos,
                LocalDateTime timestamp) {
            this.medicos = medicos;
            this.timestamp = timestamp;
        }

        public List<MedicoController.MedicoListagemResponse> getMedicos() {
            return medicos;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    /**
     * DTO de resposta para cadastro bem-sucedido.
     */
    public static class CadastroMedicoResponse {
        private final Integer medicoId;
        private final String mensagem;
        private final LocalDateTime timestamp;

        public CadastroMedicoResponse(Integer medicoId, String mensagem, LocalDateTime timestamp) {
            this.medicoId = medicoId;
            this.mensagem = mensagem;
            this.timestamp = timestamp;
        }

        public Integer getMedicoId() {
            return medicoId;
        }

        public String getMensagem() {
            return mensagem;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    /**
     * DTO de resposta para erros.
     */
    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final LocalDateTime timestamp;

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}
