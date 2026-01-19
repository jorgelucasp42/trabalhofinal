package br.ifma.consultasmedicas.adapters.out.provider;

import br.ifma.consultasmedicas.core.domain.model.VideoConferencia;
import br.ifma.consultasmedicas.core.service.SimpleLogger;
import br.ifma.consultasmedicas.ports.out.VideoConferenciaProvider;

import java.time.LocalDateTime;

/**
 * Adapter para Zoom (implementação da interface VideoConferenciaProvider).
 * 
 * Responsabilidade: Integrar com API do Zoom para criar e cancelar reuniões.
 * 
 * Nota: Esta é uma implementação mock. Em produção, faria chamadas HTTP reais
 * para a API do Zoom.
 */
public class ZoomAdapter implements VideoConferenciaProvider {

    private final String zoomApiKey;
    private final String zoomApiSecret;
    private final SimpleLogger logger = new SimpleLogger(ZoomAdapter.class);

    public ZoomAdapter(String zoomApiKey, String zoomApiSecret) {
        this.zoomApiKey = zoomApiKey;
        this.zoomApiSecret = zoomApiSecret;
    }

    @Override
    public VideoConferencia gerarLinkMeeting(Integer consultaId, String titulo, String descricao) {
        logger.info("Gerando link de reunião Zoom para consulta: " + consultaId);

        try {
            // Em produção: Chamar API do Zoom
            // POST https://api.zoom.us/v2/users/me/meetings
            // Com dados: { "topic": titulo, "description": descricao, ... }

            // Mock: Simular resposta
            String idMeeting = "zoom-meeting-" + consultaId + "-" + System.currentTimeMillis();
            String linkMeeting = "https://zoom.us/j/" + idMeeting;

            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime inicio = agora;
            LocalDateTime fim = agora.plusHours(1);

            logger.info("Link Zoom gerado: " + linkMeeting);

            return new VideoConferencia(linkMeeting, idMeeting, "Zoom", inicio, fim);

        } catch (Exception e) {
            logger.error("Erro ao gerar link Zoom: " + e.getMessage());
            throw new RuntimeException("Erro ao integrar com Zoom: " + e.getMessage());
        }
    }

    @Override
    public void cancelarMeeting(String idMeeting) {
        logger.info("Cancelando reunião Zoom: " + idMeeting);

        try {
            // Em produção: Chamar API do Zoom
            // DELETE https://api.zoom.us/v2/meetings/{meetingId}

            logger.info("Reunião Zoom cancelada com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao cancelar reunião Zoom: " + e.getMessage());
            throw new RuntimeException("Erro ao cancelar reunião: " + e.getMessage());
        }
    }

    @Override
    public String getNomeProvedor() {
        return "Zoom";
    }
}
