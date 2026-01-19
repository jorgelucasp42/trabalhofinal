package br.ifma.consultasmedicas.core.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object que representa uma videoconferência para consulta online.
 * 
 * Responsabilidades:
 * - Armazenar informações do link de reunião
 * - Validar horários de acesso
 * - Garantir imutabilidade
 * 
 * Invariantes:
 * - linkMeeting não pode ser vazio
 * - idMeeting não pode ser vazio
 * - inicioPermitido deve ser antes ou igual ao término
 */
public class VideoConferencia {
    private final String linkMeeting;
    private final String idMeeting;
    private final String provedor; // Zoom, Teams, Google Meet, etc.
    private final LocalDateTime inicioPermitido;
    private final LocalDateTime fimPermitido;

    public VideoConferencia(String linkMeeting, String idMeeting, String provedor,
            LocalDateTime inicioPermitido, LocalDateTime fimPermitido) {
        this.linkMeeting = Objects.requireNonNull(linkMeeting, "Link de reunião não pode ser nulo");
        if (linkMeeting.isBlank()) {
            throw new IllegalArgumentException("Link de reunião não pode estar vazio");
        }

        this.idMeeting = Objects.requireNonNull(idMeeting, "ID da reunião não pode ser nulo");
        if (idMeeting.isBlank()) {
            throw new IllegalArgumentException("ID da reunião não pode estar vazio");
        }

        this.provedor = Objects.requireNonNull(provedor, "Provedor não pode ser nulo");
        this.inicioPermitido = Objects.requireNonNull(inicioPermitido, "Início permitido não pode ser nulo");
        this.fimPermitido = Objects.requireNonNull(fimPermitido, "Fim permitido não pode ser nulo");

        if (inicioPermitido.isAfter(fimPermitido)) {
            throw new IllegalArgumentException("Início permitido não pode ser depois do fim permitido");
        }
    }

    public String getLinkMeeting() {
        return linkMeeting;
    }

    public String getIdMeeting() {
        return idMeeting;
    }

    public String getProvedor() {
        return provedor;
    }

    public LocalDateTime getInicioPermitido() {
        return inicioPermitido;
    }

    public LocalDateTime getFimPermitido() {
        return fimPermitido;
    }

    public boolean estaPermitidoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return !agora.isBefore(inicioPermitido) && !agora.isAfter(fimPermitido);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VideoConferencia that = (VideoConferencia) o;
        return Objects.equals(idMeeting, that.idMeeting) &&
                Objects.equals(provedor, that.provedor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMeeting, provedor);
    }

    @Override
    public String toString() {
        return "VideoConferencia{" +
                "provedor='" + provedor + '\'' +
                ", idMeeting='" + idMeeting + '\'' +
                '}';
    }
}
