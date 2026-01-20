package br.ifma.consultasmedicas.adapters.in.rest;

import br.ifma.consultasmedicas.adapters.in.controller.ConsultaController;
import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.ports.in.AgendarConsultaOnlineCommand;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * REST Controller para Consultas.
 * Adaptador de entrada REST baseado no Controller existente.
 */
@RestController
@RequestMapping("/v1/consultas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ConsultaRestController {

    private final ConsultaController controller;

    public ConsultaRestController(ConsultaController controller) {
        this.controller = Objects.requireNonNull(controller);
    }

    /**
     * Lista todas as consultas agendadas para uma data específica.
     *
     * @param data data para listar consultas (formato: yyyy-MM-dd)
     * @return lista de consultas do dia
     */
    @GetMapping
    public ResponseEntity<?> listarConsultasDoDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            if (data == null) {
                data = LocalDate.now(); // Se não informado, usa data atual
            }
            
            List<ConsultaController.ConsultaListagemResponse> consultas = 
                    controller.listarConsultasDoDia(data);
            
            return ResponseEntity.ok(new ListagemConsultasResponse(
                    consultas,
                    data.toString(),
                    LocalDateTime.now()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ErrorResponse(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
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
     * Agenda uma nova consulta online.
     *
     * @param command dados da consulta
     * @return resposta com ID da consulta criada
     */
    @PostMapping("/online")
    public ResponseEntity<?> agendarConsultaOnline(@RequestBody AgendarConsultaOnlineCommand command) {
        try {
            var response = controller.agendarConsultaOnline(command);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AgendamentoConsultaResponse(
                            response.getConsultaId(),
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
     * DTO de resposta para listagem de consultas.
     */
    public static class ListagemConsultasResponse {
        private final List<ConsultaController.ConsultaListagemResponse> consultas;
        private final String data;
        private final LocalDateTime timestamp;

        public ListagemConsultasResponse(
                List<ConsultaController.ConsultaListagemResponse> consultas,
                String data,
                LocalDateTime timestamp) {
            this.consultas = consultas;
            this.data = data;
            this.timestamp = timestamp;
        }

        public List<ConsultaController.ConsultaListagemResponse> getConsultas() {
            return consultas;
        }

        public String getData() {
            return data;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    /**
     * DTO de resposta para agendamento bem-sucedido.
     */
    public static class AgendamentoConsultaResponse {
        private final Integer consultaId;
        private final String mensagem;
        private final LocalDateTime timestamp;

        public AgendamentoConsultaResponse(Integer consultaId, String mensagem, LocalDateTime timestamp) {
            this.consultaId = consultaId;
            this.mensagem = mensagem;
            this.timestamp = timestamp;
        }

        public Integer getConsultaId() {
            return consultaId;
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
