package br.ifma.consultasmedicas.adapters.in.rest;

import br.ifma.consultasmedicas.adapters.in.controller.ProntuarioController;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.RegistrarProntuarioCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * REST Controller para Prontuários.
 * Adaptador de entrada REST baseado no Controller existente.
 */
@RestController
@RequestMapping("/v1/prontuarios")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProntuarioRestController {

    private final ProntuarioController controller;

    public ProntuarioRestController(ProntuarioController controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    /**
     * Registra um novo prontuário.
     *
     * @param command dados do prontuário
     * @return resposta com ID do prontuário criado
     */
    @PostMapping
    public ResponseEntity<?> registrarProntuario(@RequestBody RegistrarProntuarioCommand command) {
        try {
            var response = controller.registrarProntuario(command);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegistroProntuarioResponse(
                            response.getProntuarioId(),
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
     * DTO de resposta para registro bem-sucedido.
     */
    public static class RegistroProntuarioResponse {
        private final Integer prontuarioId;
        private final String mensagem;
        private final LocalDateTime timestamp;

        public RegistroProntuarioResponse(Integer prontuarioId, String mensagem, LocalDateTime timestamp) {
            this.prontuarioId = prontuarioId;
            this.mensagem = mensagem;
            this.timestamp = timestamp;
        }

        public Integer getProntuarioId() {
            return prontuarioId;
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
