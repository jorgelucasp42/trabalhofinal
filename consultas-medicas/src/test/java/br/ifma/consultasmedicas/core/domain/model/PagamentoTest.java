package br.ifma.consultasmedicas.core.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para Pagamento (Agregado).
 * 
 * Valida:
 * - CriaÃ§Ã£o com validaÃ§Ãµes
 * - TransiÃ§Ãµes de status
 * - Invariantes do agregado
 */
class PagamentoTest {

    @Test
    void deveCriarPagamentoValido() {
        // Act
        Pagamento pagamento = new Pagamento(1, 100, new BigDecimal("150.00"));

        // Assert
        assertNotNull(pagamento);
        assertEquals(1, (int) pagamento.getId());
        assertEquals(100, (int) pagamento.getConsultaId());
        assertEquals(new BigDecimal("150.00"), pagamento.getValor());
        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
    }

    @Test
    void deveRejeitarValorNegativo() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Pagamento(1, 100, new BigDecimal("-50.00")));
    }

    @Test
    void deveRejeitarValorZero() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Pagamento(1, 100, new BigDecimal("0.00")));
    }

    @Test
    void deveTransicionarParaProcessando() {
        // Arrange
        Pagamento pagamento = new Pagamento(1, 100, new BigDecimal("150.00"));

        // Act
        pagamento.iniciarProcessamento("stripe-123");

        // Assert
        assertEquals(StatusPagamento.PROCESSANDO, pagamento.getStatus());
        assertEquals("stripe-123", pagamento.getIdTransacao());
    }

    @Test
    void deveTransicionarParaConcluido() {
        // Arrange
        Pagamento pagamento = new Pagamento(1, 100, new BigDecimal("150.00"));
        pagamento.iniciarProcessamento("stripe-123");

        // Act
        pagamento.marcarComoConcluido();

        // Assert
        assertEquals(StatusPagamento.CONCLUIDO, pagamento.getStatus());
    }

    @Test
    void deveTransicionarParaFalho() {
        // Arrange
        Pagamento pagamento = new Pagamento(1, 100, new BigDecimal("150.00"));
        pagamento.iniciarProcessamento("stripe-123");

        // Act
        pagamento.marcarComoFalho("CartÃ£o recusado");

        // Assert
        assertEquals(StatusPagamento.FALHOU, pagamento.getStatus());
        assertEquals("CartÃ£o recusado", pagamento.getMotivo());
    }

    @Test
    void deveRejeitarTransicaoInvalida() {
        // Arrange
        Pagamento pagamento = new Pagamento(1, 100, new BigDecimal("150.00"));

        // Act & Assert - NÃ£o pode marcar como concluÃ­do direto de PENDENTE
        assertThrows(IllegalStateException.class, pagamento::marcarComoConcluido);
    }

    @Test
    void deveRejeitarDuploProcessamento() {
        // Arrange
        Pagamento pagamento = new Pagamento(1, 100, new BigDecimal("150.00"));
        pagamento.iniciarProcessamento("stripe-123");

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> pagamento.iniciarProcessamento("stripe-456"));
    }
}

