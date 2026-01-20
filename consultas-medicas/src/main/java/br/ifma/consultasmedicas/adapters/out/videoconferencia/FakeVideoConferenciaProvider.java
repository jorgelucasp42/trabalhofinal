package br.ifma.consultasmedicas.adapters.out.videoconferencia;

import br.ifma.consultasmedicas.core.domain.model.VideoConferencia;
import br.ifma.consultasmedicas.ports.out.VideoConferenciaProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FakeVideoConferenciaProvider implements VideoConferenciaProvider {

    @Override
    public VideoConferencia gerarLinkMeeting(
            Integer consultaId,
            String titulo,
            String descricao
    ) {
        String idMeeting = UUID.randomUUID().toString();
        String link = "https://fake-meet.com/" + idMeeting;

        LocalDateTime inicio = LocalDateTime.now().minusMinutes(5);
        LocalDateTime fim = inicio.plusHours(1);

        return new VideoConferencia(
                link,
                idMeeting,
                getNomeProvedor(),
                inicio,
                fim
        );
    }

    @Override
    public void cancelarMeeting(String idMeeting) {
        // Fake → não faz nada
    }

    @Override
    public String getNomeProvedor() {
        return "FakeMeet";
    }
}
