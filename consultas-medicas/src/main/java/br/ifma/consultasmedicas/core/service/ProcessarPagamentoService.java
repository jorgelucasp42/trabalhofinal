package br.ifma.consultasmedicas.core.service;

import br.ifma.consultasmedicas.core.domain.exception.DomainException;
import br.ifma.consultasmedicas.core.domain.model.Pagamento;
import br.ifma.consultasmedicas.core.domain.model.StatusPagamento;
import br.ifma.consultasmedicas.ports.in.PagamentoResponse;
import br.ifma.consultasmedicas.ports.in.ProcessarPagamentoCommand;
import br.ifma.consultasmedicas.ports.in.ProcessarPagamentoUseCase;
import br.ifma.consultasmedicas.ports.out.*;

import java.util.Objects;

/**
 * Caso de uso: Processar Pagamento.
 * 
 * Responsabilidades:
 * - Validar dados do pagamento
 * - Criar agregado Pagamento
 * - Enviar para gateway de pagamento
 * - Persistir resultado
 * - Retornar resposta ao cliente
 * 
 * Invariantes:
 * - Consulta deve existir
 * - Valor deve ser positivo
 * - Dados de cartão devem passar por validação
 */
public class ProcessarPagamentoService implements ProcessarPagamentoUseCase {

    private final ConsultaRepository consultaRepository;
    private final PagamentoRepository pagamentoRepository;
    private final PagamentoGateway pagamentoGateway;
    private final IdGenerator idGenerator;
    private final SimpleLogger logger = new SimpleLogger(ProcessarPagamentoService.class);

    public ProcessarPagamentoService(ConsultaRepository consultaRepository,
            PagamentoRepository pagamentoRepository,
            PagamentoGateway pagamentoGateway,
            IdGenerator idGenerator) {
        this.consultaRepository = Objects.requireNonNull(consultaRepository);
        this.pagamentoRepository = Objects.requireNonNull(pagamentoRepository);
        this.pagamentoGateway = Objects.requireNonNull(pagamentoGateway);
        this.idGenerator = Objects.requireNonNull(idGenerator);
    }

    @Override
    public PagamentoResponse processar(ProcessarPagamentoCommand command) {
        logger.info("Iniciando processamento de pagamento para consulta: " + command.getConsultaId());

        try {
            // 1. Valida existência da consulta
            if (consultaRepository.obter(command.getConsultaId()) == null) {
                throw new DomainException("Consulta não encontrada: " + command.getConsultaId());
            }
            logger.info("Consulta validada");

            // 2. Valida dados de cartão (validação básica)
            validarDadosCartao(command);
            logger.info("Dados de cartão validados");

            // 3. Cria agregado Pagamento
            Integer pagamentoId = idGenerator.gerarId();
            Pagamento pagamento = new Pagamento(pagamentoId, command.getConsultaId(), command.getValor());

            logger.info("Agregado Pagamento criado. ID: " + pagamentoId);

            // 4. Inicia processamento (status = PROCESSANDO)
            pagamento.iniciarProcessamento("temp-id-" + pagamentoId);
            pagamentoRepository.salvar(pagamento);
            logger.info("Pagamento marcado como PROCESSANDO");

            // 5. Envia para gateway
            ProcessarTransacaoRequest requisicao = new ProcessarTransacaoRequest(
                    command.getNumeroCartao(),
                    command.getNomeTitular(),
                    command.getValidade(),
                    command.getCvv(),
                    command.getValor(),
                    "Consulta Online - ID: " + command.getConsultaId());

            TransacaoResponse resposta = pagamentoGateway.processar(requisicao);
            logger.info("Resposta do gateway recebida. Sucesso: " + resposta.isSucesso());

            // 6. Atualiza status do pagamento conforme resposta
            if (resposta.isSucesso() && resposta.getStatus() == StatusTransacao.CAPTURADA) {
                pagamento.marcarComoConcluido();
                logger.info("Pagamento marcado como CONCLUÍDO");

                pagamentoRepository.salvar(pagamento);

                return new PagamentoResponse(
                        pagamentoId,
                        "CONCLUÍDO",
                        "Pagamento processado com sucesso",
                        resposta.getIdTransacao());
            } else {
                String motivo = "Gateway retornou: " + resposta.getMensagem();
                pagamento.marcarComoFalho(motivo);
                logger.info("Pagamento marcado como FALHOU: " + motivo);

                pagamentoRepository.salvar(pagamento);

                return new PagamentoResponse(
                        pagamentoId,
                        "FALHOU",
                        resposta.getMensagem(),
                        null);
            }

        } catch (DomainException e) {
            logger.error("Erro de domínio ao processar pagamento: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao processar pagamento: " + e.getMessage());
            throw new DomainException("Erro ao processar pagamento: " + e.getMessage());
        }
    }

    /**
     * Valida dados de cartão com regras básicas.
     * Uma implementação real usaria bibliotecas de validação PCI-compliant.
     */
    private void validarDadosCartao(ProcessarPagamentoCommand command) {
        if (command.getNumeroCartao() == null || command.getNumeroCartao().length() < 13) {
            throw new DomainException("Número de cartão inválido");
        }
        if (command.getNomeTitular() == null || command.getNomeTitular().isBlank()) {
            throw new DomainException("Nome do titular inválido");
        }
        if (command.getValidade() == null || !command.getValidade().matches("\\d{2}/\\d{2}")) {
            throw new DomainException("Validade inválida (formato MM/AA)");
        }
        if (command.getCvv() == null || command.getCvv().length() < 3) {
            throw new DomainException("CVV inválido");
        }
    }
}
