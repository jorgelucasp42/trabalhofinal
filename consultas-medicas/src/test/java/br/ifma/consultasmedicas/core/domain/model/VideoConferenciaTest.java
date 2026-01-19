package br.ifma.consultasmedicas.core.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para VideoConferencia (Value Object).
 */
class VideoConferenciaTest {

    @Test
    void deveCriarVideoConferenciaValida() {
        // Arrange
        String link = "https://zoom.us/j/123456";
        String id = "zoom-123";
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.plusHours(1);

        // Act
        VideoConferencia vc = new VideoConferencia(link, id, "Zoom", inicio, fim);

        // Assert
        assertNotNull(vc);
        assertEquals(link, vc.getLinkMeeting());
        assertEquals(id, vc.getIdMeeting());
        assertEquals("Zoom", vc.getProvedor());
    }

    @Test
    void deveRejeitarLinkVazio() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new VideoConferencia("", "id", "Zoom", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    }

    @Test
    void deveRejeitarIdVazio() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new VideoConferencia("https://zoom.us/j/123", "", "Zoom",
                LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
    }

    @Test
    void deveRejeitarFimAntesDeinicio() {
        // Act & Assert
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = inicio.minusHours(1);

        assertThrows(IllegalArgumentException.class,
                () -> new VideoConferencia("https://zoom.us/j/123", "id", "Zoom", inicio, fim));
    }

    @Test
    void deveConhecerOHorarioPassado() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.now().minusMinutes(30);
        LocalDateTime fim = LocalDateTime.now().plusMinutes(30);
        VideoConferencia vc = new VideoConferencia("https://zoom.us/j/123", "id", "Zoom", inicio, fim);

        // Act & Assert
        assertTrue(vc.estaPermitidoAgora());
    }
}

