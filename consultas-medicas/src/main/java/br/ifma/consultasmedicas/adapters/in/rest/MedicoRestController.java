package br.ifma.consultasmedicas.adapters.in.rest;

import br.ifma.consultasmedicas.adapters.in.controller.MedicoController;
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
