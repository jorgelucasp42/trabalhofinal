package br.ifma.consultasmedicas.adapters.out.repository.jpa.entity;

import br.ifma.consultasmedicas.core.domain.model.StatusPagamento;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class PagamentoJpaEntity {

    @Id
    private Integer id;

    @Column(name = "consulta_id", nullable = false)
    private Integer consultaId;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;

    @Column(name = "id_transacao")
    private String idTransacao;

    @Column
    private String motivo;

    protected PagamentoJpaEntity() {} // JPA precisa

    public PagamentoJpaEntity(Integer id, Integer consultaId, BigDecimal valor, StatusPagamento status,
                              LocalDateTime dataCriacao, LocalDateTime dataProcessamento,
                              String idTransacao, String motivo) {
        this.id = id;
        this.consultaId = consultaId;
        this.valor = valor;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataProcessamento = dataProcessamento;
        this.idTransacao = idTransacao;
        this.motivo = motivo;
    }

    // getters e setters
    public Integer getId() { return id; }
    public Integer getConsultaId() { return consultaId; }
    public BigDecimal getValor() { return valor; }
    public StatusPagamento getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataProcessamento() { return dataProcessamento; }
    public String getIdTransacao() { return idTransacao; }
    public String getMotivo() { return motivo; }

    public void setStatus(StatusPagamento status) { this.status = status; }
    public void setDataProcessamento(LocalDateTime dataProcessamento) { this.dataProcessamento = dataProcessamento; }
    public void setIdTransacao(String idTransacao) { this.idTransacao = idTransacao; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
