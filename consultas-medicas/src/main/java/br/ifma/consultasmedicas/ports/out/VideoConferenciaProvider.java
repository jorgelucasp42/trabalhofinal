package br.ifma.consultasmedicas.ports.out;

import br.ifma.consultasmedicas.core.domain.model.VideoConferencia;

/**
 * Porta de saída para provedores de videoconferência.
 * 
 * Responsabilidade: Abstrair a integração com diferentes plataformas
 * de videoconferência (Zoom, Teams, Google Meet, etc).
 * 
 * Strategy Pattern: Diferentes implementadores desta interface
 * permitem trocar de provedor sem alterar o domínio.
 */
public interface VideoConferenciaProvider {
    /**
     * Gera um link de reunião para a consulta.
     * 
     * @param consultaId ID da consulta
     * @param titulo     Título da reunião
     * @param descricao  Descrição da reunião
     * @return VideoConferencia com link gerado
     */
    VideoConferencia gerarLinkMeeting(Integer consultaId, String titulo, String descricao);

    /**
     * Cancela uma reunião existente.
     * 
     * @param idMeeting ID da reunião no provedor
     */
    void cancelarMeeting(String idMeeting);

    /**
     * Retorna o nome do provedor (Zoom, Teams, etc).
     */
    String getNomeProvedor();
}
