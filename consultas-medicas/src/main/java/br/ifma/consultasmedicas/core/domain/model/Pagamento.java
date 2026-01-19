package br.ifma.consultasmedicas.core.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Agregado de Pagamento para consultas online.
 * 
 * Responsabilidades:
 * - Manter dados da transação de pagamento
 * - Validar transições de status
 * - Registrar histórico de tentativas
 * 
 * Invariantes:
 * - consultaId sempre válido
 * - valor > 0
 * - status segue ordem: PENDENTE → PROCESSANDO → CONCLUÍDO OU FALHOU
 */
public class Pagamento {
    private final Integer id;
    private final Integer consultaId;
    private final BigDecimal valor;
    private StatusPagamento status;
    private final LocalDateTime dataCriacao;
    private LocalDateTime dataProcessamento;
    private String idTransacao; // ID retornado pelo gateway
    private String motivo; // Motivo de falha, se houver

    public Pagamento(Integer id, Integer consultaId, BigDecimal valor) {
        this.id = Objects.requireNonNull(id, "ID não pode ser nulo");
        this.consultaId = Objects.requireNonNull(consultaId, "ID da consulta não pode ser nulo");

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
        this.valor = valor;

        this.status = StatusPagamento.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Integer getConsultaId() {
        return consultaId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataProcessamento() {
        return dataProcessamento;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public String getMotivo() {
        return motivo;
    }

    /**
     * Inicia o processamento do pagamento.
     * Status: PENDENTE → PROCESSANDO
     */
    public void iniciarProcessamento(String idTransacao) {
        if (status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException(
                    "Só é possível processar pagamentos em status PENDENTE. Status atual: " + status);
        }

        Objects.requireNonNull(idTransacao, "ID da transação não pode ser nulo");

        this.status = StatusPagamento.PROCESSANDO;
        this.idTransacao = idTransacao;
        this.dataProcessamento = LocalDateTime.now();
    }

    /**
     * Marca o pagamento como concluído.
     * Status: PROCESSANDO → CONCLUÍDO
     */
    public void marcarComoConcluido() {
        if (status != StatusPagamento.PROCESSANDO) {
            throw new IllegalStateException(
                    "Só é possível concluir pagamentos em status PROCESSANDO. Status atual: " + status);
        }
        this.status = StatusPagamento.CONCLUIDO;
    }

    /**
     * Marca o pagamento como falho.
     * Status: PROCESSANDO → FALHOU
     */
    public void marcarComoFalho(String motivo) {
        if (status != StatusPagamento.PROCESSANDO) {
            throw new IllegalStateException(
                    "Só é possível falhar pagamentos em status PROCESSANDO. Status atual: " + status);
        }

        Objects.requireNonNull(motivo, "Motivo de falha não pode ser nulo");
        if (motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo de falha não pode estar vazio");
        }

        this.status = StatusPagamento.FALHOU;
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", consultaId=" + consultaId +
                ", valor=" + valor +
                ", status=" + status +
                '}';
    }
}
