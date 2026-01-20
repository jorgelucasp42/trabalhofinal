package br.ifma.consultasmedicas.adapters.in.rest;

import br.ifma.consultasmedicas.adapters.in.controller.PacienteController;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.CadastrarPacienteCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * REST Controller para Pacientes.
 * Adaptador de entrada REST baseado no Controller existente.
 */
@RestController
@RequestMapping("/v1/pacientes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PacienteRestController {

    private final PacienteController controller;

    public PacienteRestController(PacienteController controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    /**
     * Cadastra um novo paciente.
     *
     * @param command dados do paciente
     * @return resposta com ID do paciente criado
     */
    @PostMapping
    public ResponseEntity<?> cadastrarPaciente(@RequestBody CadastrarPacienteCommand command) {
        try {
            var response = controller.cadastrarPaciente(command);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CadastroPacienteResponse(
                            response.getPacienteId(),
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
     * DTO de resposta para cadastro bem-sucedido.
     */
    public static class CadastroPacienteResponse {
        private final Integer pacienteId;
        private final String mensagem;
        private final LocalDateTime timestamp;

        public CadastroPacienteResponse(Integer pacienteId, String mensagem, LocalDateTime timestamp) {
            this.pacienteId = pacienteId;
            this.mensagem = mensagem;
            this.timestamp = timestamp;
        }

        public Integer getPacienteId() {
            return pacienteId;
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
