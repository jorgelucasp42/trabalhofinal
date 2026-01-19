package br.ifma.consultasmedicas.adapters.out.provider;

import br.ifma.consultasmedicas.core.domain.model.VideoConferencia;
import br.ifma.consultasmedicas.core.service.SimpleLogger;
import br.ifma.consultasmedicas.ports.out.VideoConferenciaProvider;

import java.time.LocalDateTime;

/**
 * Adapter para Microsoft Teams (implementação da interface
 * VideoConferenciaProvider).
 * 
 * Responsabilidade: Integrar com API do Teams para criar e cancelar reuniões.
 * 
 * Nota: Esta é uma implementação mock. Em produção, faria chamadas HTTP reais
 * para a API do Teams.
 */
public class TeamsAdapter implements VideoConferenciaProvider {

    private final String teamsGraphToken;
    private final SimpleLogger logger = new SimpleLogger(TeamsAdapter.class);

    public TeamsAdapter(String teamsGraphToken) {
        this.teamsGraphToken = teamsGraphToken;
    }

    @Override
    public VideoConferencia gerarLinkMeeting(Integer consultaId, String titulo, String descricao) {
        logger.info("Gerando link de reunião Teams para consulta: " + consultaId);

        try {
            // Em produção: Chamar Microsoft Graph API
            // POST https://graph.microsoft.com/v1.0/me/onlineMeetings
            // Com dados: { "subject": titulo, ... }

            // Mock: Simular resposta
            String idMeeting = "teams-meeting-" + consultaId + "-" + System.currentTimeMillis();
            String linkMeeting = "https://teams.microsoft.com/l/meetup-join/" + idMeeting;

            LocalDateTime agora = LocalDateTime.now();
            LocalDateTime inicio = agora;
            LocalDateTime fim = agora.plusHours(1);

            logger.info("Link Teams gerado: " + linkMeeting);

            return new VideoConferencia(linkMeeting, idMeeting, "Teams", inicio, fim);

        } catch (Exception e) {
            logger.error("Erro ao gerar link Teams: " + e.getMessage());
            throw new RuntimeException("Erro ao integrar com Teams: " + e.getMessage());
        }
    }

    @Override
    public void cancelarMeeting(String idMeeting) {
        logger.info("Cancelando reunião Teams: " + idMeeting);

        try {
            // Em produção: Chamar Microsoft Graph API
            // DELETE https://graph.microsoft.com/v1.0/me/onlineMeetings/{meetingId}

            logger.info("Reunião Teams cancelada com sucesso");

        } catch (Exception e) {
            logger.error("Erro ao cancelar reunião Teams: " + e.getMessage());
            throw new RuntimeException("Erro ao cancelar reunião: " + e.getMessage());
        }
    }

    @Override
    public String getNomeProvedor() {
        return "Teams";
    }
}
