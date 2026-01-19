# 📋 ANÁLISE COMPLETA - ARQUITETURA HEXAGONAL E EVOLUÇÕES

## ✅ VALIDAÇÃO DA ARQUITETURA HEXAGONAL - ETAPA 01

### 1. Estrutura Atual Verificada

```
consultas-medicas/
├── core/
│   ├── domain/              ✅ NÚCLEO ISOLADO
│   │   ├── model/           (Consulta, Prontuario, Paciente, Medico, etc.)
│   │   └── exception/       (DomainException)
│   └── service/             ✅ CASOS DE USO
│       ├── CadastrarPacienteService
│       ├── ConsultarHistoricoProntuarioService
│       ├── ListarConsultasDoDiaService
│       └── RegistrarProntuarioService
├── ports/
│   ├── in/                  ✅ PORTAS DE ENTRADA
│   │   ├── CadastrarPacienteUseCase
│   │   ├── ConsultarHistoricoProntuarioUseCase
│   │   ├── ListarConsultasDoDiaUseCase
│   │   └── RegistrarProntuarioUseCase
│   └── out/                 ✅ PORTAS DE SAÍDA
│       ├── PacienteRepository
│       ├── ConsultaRepository
│       ├── ProntuarioRepository
│       ├── MedicamentoRepository
│       ├── ExameRepository
│       ├── PlanoSaudeRepository
│       └── IdGenerator
└── adapters/
    ├── in/                  ✅ ADAPTADORES DE ENTRADA
    │   ├── controller/      (ProntuarioController)
    │   ├── dto/             (DTOs para mapeamento)
    │   └── rest/            (REST Controllers)
    └── out/                 ✅ ADAPTADORES DE SAÍDA
        └── repository/      (Implementações JPA)
```

---

## ✅ CONFIRMAÇÃO: DOMÍNIO ESTÁ CORRETO

### 📌 1. Desacoplamento do Domínio

**Estado Verificado: ✅ CORRETO**

```
✓ core/domain NÃO depende de adapters
✓ core/domain NÃO depende de ports
✓ core/domain NÃO depende de frameworks (JPA, Spring Web, etc.)
✓ core/domain NÃO tem conhecimento de HTTP, BD, ou UI
```

**Evidências:**

- Modelos de domínio (`Consulta`, `Prontuario`, `Paciente`) contêm apenas lógica de negócio
- Validações via `ProntuarioValidator` encapsuladas no domínio
- Agregados com invariantes bem definidos (peso, altura, IMC)
- Enums de domínio (`ConsultaStatus`) isolados no core

---

### 📌 2. Estabilidade do Domínio

**Estado Verificado: ✅ CORRETO**

```
✓ Mudanças externas NÃO afetam o core
✓ Mudanças de persistência NÃO afetam modelos
✓ Mudanças de framework NÃO afetam casos de uso
✓ Casos de uso dependem APENAS do domínio
```

**Fluxo de Dependência Confirmado:**

```
adapters → ports → services → domain
         (uso)    (implementação)  (nenhuma dependência de saída)
```

---

### 📌 3. Preparação para Extensão (OCP - Open/Closed Principle)

**Estado Verificado: ✅ EXCELENTE**

```
✓ Portas de saída via interfaces (Repository, IdGenerator)
✓ Portas de entrada via interfaces (UseCases)
✓ Fácil adicionar novas implementações SEM modificar domain
✓ Strategy Pattern implícito em IdGenerator
✓ Adapter Pattern em adaptadores de entrada/saída
```

**Pontos Fortes OCP:**

- `PacienteRepository` interface → múltiplas implementações possíveis (JPA, MongoDB, API)
- `IdGenerator` interface → Sequência, UUID, Snowflake sem alterar domínio
- `ConsultaRepository` permite diferentes estratégias de busca

---

## 🚀 EVOLUÇÕES ARQUITETURAIS PROPOSTAS

---

# I. ATENDIMENTO ONLINE

## 📊 Análise de Impacto

### Novo Fluxo

```
Cliente Web
    ↓
Porta: AgendarConsultaOnlineUseCase
    ↓
    Service: AgendarConsultaOnlineService
        ├─ Valida disponibilidade médico
        ├─ Cria Consulta com tipo="ONLINE"
        ├─ Gera link de videoconferência
        └─ Notifica paciente (evento)
    ↓
Porta: ConsultaRepository
Porta: VideoConferenciaProvider (NOVA)
    ↓
BD + Serviço Externo (Zoom, Teams, etc.)
```

### 1.1 Novas Portas de Entrada (Adapters IN)

```java
// ✅ NOVA PORTA DE ENTRADA
public interface AgendarConsultaOnlineUseCase {
    Integer agendar(AgendarConsultaOnlineCommand command);
}

// Command contém:
// - pacienteId
// - medicoId
// - dataHoraUTC
// - tipoVideoconferencia
```

```java
// ✅ NOVA PORTA DE ENTRADA
public interface VisualizarHistoricoUseCase {
    List<ConsultaDTO> obterHistorico(Integer pacienteId);
}

// Retorna histórico com prontuários associados
```

```java
// ✅ NOVA PORTA DE ENTRADA
public interface ProcessarPagamentoUseCase {
    PagamentoResponse processar(PagamentoCommand command);
}

// Integra com gateway de pagamento
```

### 1.2 Novos Adaptadores de Entrada (REST/Web)

```
adapters/in/
├── controller/
│   ├── ConsultaOnlineController (NOVO)
│   ├── HistoricoController (NOVO)
│   └── PagamentoController (NOVO)
├── dto/
│   ├── AgendarConsultaOnlineRequest (NOVO)
│   ├── PagamentoRequest (NOVO)
│   └── ConsultaHistoricoResponse (NOVO)
└── rest/
    ├── ConsultaOnlineRestController (NOVO)
    ├── HistoricoRestController (NOVO)
    └── PagamentoRestController (NOVO)
```

### 1.3 Novas Portas de Saída (Adapters OUT)

```java
// ✅ NOVA PORTA DE SAÍDA
public interface VideoConferenciaProvider {
    String gerarLinkMeeting(ConsultaOnline consulta);
    void cancelarMeeting(String meetingId);
}

// Implementações: ZoomAdapter, TeamsAdapter, GoogleMeetAdapter
```

```java
// ✅ NOVA PORTA DE SAÍDA
public interface PagamentoGateway {
    TransacaoResponse processar(Pagamento pagamento);
    StatusPagamento consultar(String idTransacao);
}

// Implementações: StripeAdapter, PayPalAdapter, MercadoPagoAdapter
```

### 1.4 Impacto no Domínio

**Domínio SERÁ ESTENDIDO (NÃO MODIFICADO):**

```java
// ✅ Novo tipo de Consulta
public enum TipoConsulta {
    PRESENCIAL,      // Existente
    ONLINE           // NOVO
}

// ✅ Objeto de Valor para videoconferência
public class VideoConferencia {
    private final String linkMeeting;
    private final String idMeeting;
    private final LocalDateTime inicioPermitido;

    // Validações de horário, link válido, etc.
}

// ✅ Agregado Pagamento (NOVO)
public class Pagamento {
    private final Integer id;
    private final Consulta consulta;
    private final BigDecimal valor;
    private final StatusPagamento status;
    private final LocalDateTime dataPagamento;

    // Invariantes:
    // - valor > 0
    // - status transição válida: PENDENTE → PROCESSANDO → CONCLUÍDO/FALHOU
}
```

### 1.5 Diagrama de Dependências Arquiteturais

```
┌─────────────────────────────────────────────────────────┐
│                    ATENDIMENTO ONLINE                    │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  REST Controllers (adapters/in/controller)              │
│      ↓                                                    │
│  UseCase Interfaces (ports/in)                          │
│  • AgendarConsultaOnlineUseCase                          │
│  • VisualizarHistoricoUseCase                            │
│  • ProcessarPagamentoUseCase                             │
│      ↓                                                    │
│  Services (core/service) - LÓGICA DE NEGÓCIO            │
│  • AgendarConsultaOnlineService                          │
│  • VisualizarHistoricoService                            │
│  • ProcessarPagamentoService                             │
│      ↓                                                    │
│  DOMÍNIO (NOVO, NÃO ALTERADO)                           │
│  • TipoConsulta.ONLINE                                   │
│  • VideoConferencia (Value Object)                       │
│  • Pagamento (Agregado)                                  │
│      ↓                                                    │
│  Portas de Saída (ports/out)                            │
│  • VideoConferenciaProvider (NOVA)                       │
│  • PagamentoGateway (NOVA)                               │
│  • ConsultaRepository (EXISTENTE)                        │
│      ↓                                                    │
│  Adaptadores de Saída (adapters/out)                    │
│  • ZoomAdapter, TeamsAdapter, etc.                       │
│  • StripeAdapter, PayPalAdapter, etc.                    │
│  • JPA Repositories (EXISTENTE)                          │
│                                                           │
└─────────────────────────────────────────────────────────┘
```

### 1.6 Relação com Princípios SOLID

#### 🔷 SRP (Single Responsibility Principle)

| Classe                         | Responsabilidade              |
| ------------------------------ | ----------------------------- |
| `AgendarConsultaOnlineService` | Orquestrar agendamento online |
| `VideoConferenciaProvider`     | Gerar links de vídeo          |
| `PagamentoGateway`             | Processar transações          |
| `ConsultaOnlineRestController` | Mapear HTTP ↔ Domain          |

#### 🔷 OCP (Open/Closed Principle)

```
✓ Novo tipo de consulta adicionado SEM alterar Consulta existente
✓ Novos gateways (VideoConferência, Pagamento) podem ser adicionados
✓ Domínio ABERTO para extensão, FECHADO para modificação
```

#### 🔷 DIP (Dependency Inversion Principle)

```
Serviços dependem de ABSTRAÇÕES (interfaces):

  AgendarConsultaOnlineService
      ↓
  IVideoConferenciaProvider  ← Abstração
      ↓
  ZoomAdapter, TeamsAdapter  ← Implementações
```

#### 🔷 ISP (Interface Segregation Principle)

```
✓ VideoConferenciaProvider interface (gerarLink, cancelar)
✓ PagamentoGateway interface (processar, consultar)
✓ Nenhum cliente obrigado a depender de métodos não usados
```

### 1.7 Padrões de Projeto Aplicáveis

| Padrão       | Uso                                                                      |
| ------------ | ------------------------------------------------------------------------ |
| **Strategy** | Diferentes provedores de videoconferência implementam mesma interface    |
| **Factory**  | `VideoConferenciaProviderFactory.criar(tipo)` → Zoom, Teams, Google Meet |
| **Adapter**  | `ZoomAdapter` adapta Zoom API para `VideoConferenciaProvider`            |
| **Observer** | Evento `ConsultaAgendada` notifica sistemas externos                     |
| **Builder**  | `PagamentoBuilder` para construir objetos de pagamento complexos         |

### 1.8 Justificativa da Hexagonal para Atendimento Online

```
POR QUE HEXAGONAL É IDEAL?

1. Isolamento de Tecnologia
   ✓ VideoConferência pode trocar de Zoom para Teams
   ✓ Gateway pode trocar de Stripe para PayPal
   ✓ Banco de dados pode trocar de PostgreSQL para MongoDB
   → SEM alterar regras de negócio

2. Testabilidade
   ✓ Mock de VideoConferenciaProvider em testes
   ✓ Mock de PagamentoGateway sem chamar API real
   ✓ Lógica de negócio testada isoladamente

3. Escalabilidade
   ✓ Adicionar novo tipo de consulta (síncrona, assíncrona, por IA)
   ✓ Adicionar novo meio de pagamento
   ✓ Adicionar notificação (SMS, WhatsApp, Email)
   → Apenas nova porta + novo adaptador + possível extensão de domínio
```

---

# II. NOTIFICAÇÕES E LEMBRETES

## 📊 Análise de Impacto

### Novo Fluxo (Assíncrono com Eventos)

```
Domínio gera evento ConsultaAgendada
    ↓
EventPublisher distribui evento
    ↓
NotificacaoService recebe evento
    ├─ Agenda lembrete (se habilitado)
    ├─ Envia notificação (SMS/Email/Push)
    └─ Registra tentativa
    ↓
Notificação vai para:
├─ Email
├─ SMS
└─ Push Notification
```

### 2.1 Arquitetura Event-Driven

```java
// ✅ EVENTO DE DOMÍNIO
public class ConsultaAgendadaEvent {
    private final Integer consultaId;
    private final Integer pacienteId;
    private final LocalDateTime dataConsulta;
    private final String nomeResponsavel;
    private final LocalDateTime momentoGerado;
}

// ✅ EVENTO: Consulta próxima de começar
public class ConsultaProximaComendarEvent {
    private final Integer consultaId;
    private final Integer pacienteId;
    private final Integer minutosFaltam;
}
```

### 2.2 Novas Portas de Entrada

```java
// ✅ NOVA PORTA DE ENTRADA
public interface ConfigurarNotificacaoUseCase {
    void configurar(ConfigurarNotificacaoCommand command);
}

// Command contém:
// - pacienteId
// - tipoNotificacao (EMAIL, SMS, PUSH)
// - habilitado
// - minutosAntes (quanto tempo antes notificar)
```

```java
// ✅ NOVA PORTA DE ENTRADA - para testes/admin
public interface ConsultarLembreteUseCase {
    List<LembreteDTO> listar(Integer pacienteId);
}
```

### 2.3 Novas Portas de Saída

```java
// ✅ NOVA PORTA DE SAÍDA
public interface NotificadorEmail {
    void enviar(Notificacao notificacao);
}

// Implementações: SmtpAdapter, SendGridAdapter, MailChimpAdapter
```

```java
// ✅ NOVA PORTA DE SAÍDA
public interface NotificadorSMS {
    void enviar(Notificacao notificacao);
}

// Implementações: TwilioAdapter, AWSAdapter, VonageAdapter
```

```java
// ✅ NOVA PORTA DE SAÍDA
public interface NotificadorPush {
    void enviar(Notificacao notificacao);
}

// Implementações: FirebaseAdapter, OneSignalAdapter
```

```java
// ✅ NOVA PORTA DE SAÍDA
public interface EventPublisher {
    void publicar(DomainEvent evento);
}

// Implementações: InMemoryPublisher, RabbitMQPublisher, KafkaPublisher
```

```java
// ✅ NOVA PORTA DE SAÍDA
public interface ConfiguracaoNotificacaoRepository {
    void salvar(ConfiguracaoNotificacao config);
    ConfiguracaoNotificacao obter(Integer pacienteId);
}
```

### 2.4 Impacto no Domínio

**EXTENSÃO (NÃO MODIFICAÇÃO):**

```java
// ✅ Novos agregados (NOVOS, não alteram Consulta/Prontuario)

public class ConfiguracaoNotificacao {
    private final Integer pacienteId;
    private final Map<TipoNotificacao, Boolean> habilitadas;
    private final Integer minutosAntes; // Notificar X minutos antes

    // Invariantes:
    // - minutosAntes > 0 e <= 1440 (24h)
}

public enum TipoNotificacao {
    EMAIL, SMS, PUSH_NOTIFICATION
}

public class Notificacao {
    private final Integer id;
    private final Integer pacienteId;
    private final String destinatario;
    private final String mensagem;
    private final TipoNotificacao tipo;
    private final LocalDateTime dataEnvio;
    private final StatusNotificacao status;

    // Invariantes:
    // - mensagem não vazia
    // - destinatario válido (email/telefone)
}

public enum StatusNotificacao {
    PENDENTE, ENVIADA, FALHOU, LIDA
}
```

### 2.5 Desacoplamento entre Regra e Envio

```
ANTES (Acoplado ❌):
    AgendarConsultaService
        ├─ Cria Consulta
        ├─ Salva em BD
        └─ EnviaEmailDiretamente ← PROBLEMA: Sincronização, falhas

DEPOIS (Desacoplado ✅):
    AgendarConsultaService
        ├─ Cria Consulta
        ├─ Salva em BD
        └─ Publica Evento ConsultaAgendada
             ↓
          [Fila de Eventos]
             ↓
          NotificacaoService (Assíncrono)
             └─ Escolhe canal (Email, SMS, Push)
                └─ Envia com retry automático
```

### 2.6 Padrões de Projeto Aplicáveis

| Padrão            | Uso                                                         |
| ----------------- | ----------------------------------------------------------- |
| **Observer**      | Listeners de eventos escutam ConsultaAgendadaEvent          |
| **Pub/Sub**       | EventPublisher distribui eventos para múltiplos handlers    |
| **Strategy**      | Diferentes estratégias de notificação (Email, SMS, Push)    |
| **Factory**       | `NotificadorFactory.criar(tipo)` → EmailAdapter, SmsAdapter |
| **Decorator**     | `LoggingNotificadorDecorator` para auditar tentativas       |
| **Retry Pattern** | Tentar 3x com backoff exponencial                           |

### 2.7 Diagrama de Fluxo Event-Driven

```
┌─────────────────────────────────────────────────────────────┐
│                  NOTIFICAÇÕES EVENT-DRIVEN                   │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ConsultaAgendadaEvent ← Gerado por AgendarConsultaService  │
│         ↓                                                     │
│  EventPublisher (porta)                                      │
│         ↓                                                     │
│  [Fila de Eventos - Assíncrona]                              │
│         ↓                                                     │
│  NotificacaoService (Listener)                               │
│  ├─ Lê ConfiguracaoNotificacao                              │
│  ├─ Determina canais habilitados                            │
│  └─ Publica para múltiplos notificadores                    │
│         ↓                                                     │
│  NotificadorStrategy (porta)                                │
│  ├─ NotificadorEmail     → SmtpAdapter/SendGridAdapter      │
│  ├─ NotificadorSMS       → TwilioAdapter/VonageAdapter      │
│  └─ NotificadorPush      → FirebaseAdapter/OneSignalAdapter │
│         ↓                                                     │
│  Externos (BD, APIs, Filas)                                  │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

### 2.8 Relação com Princípios SOLID

#### 🔷 SRP

| Classe                  | Responsabilidade                      |
| ----------------------- | ------------------------------------- |
| `ConsultaAgendadaEvent` | Representar que consulta foi agendada |
| `NotificacaoService`    | Orquestrar quais notificadores usar   |
| `SmtpAdapter`           | Enviar email via SMTP                 |
| `TwilioAdapter`         | Enviar SMS via Twilio                 |

#### 🔷 OCP

```
✓ Novo tipo de notificação? Crie NotificadorWhatsApp
✓ Novo evento? Crie seu DomainEvent
✓ Domínio FECHADO para modificação, ABERTO para extensão
```

#### 🔷 DIP

```
NotificacaoService
    ↓
INotificador (interface)
    ↓
EmailAdapter, SmsAdapter, PushAdapter (implementações)
```

---

# III. COMPARTILHAMENTO E INTEGRAÇÃO

## 📊 Análise de Impacto

### Novo Fluxo Integração

```
Sistema Externo (Hospital X)
    ↓ [HTTP/API/HL7]
AnticorruptionLayer
    ├─ Traduz ExternalPatient → Paciente interno
    ├─ Valida contra domínio local
    └─ Integra dados
    ↓
SistemaIntegracao (NOVO)
    ├─ Sincroniza pacientes
    ├─ Sincroniza consultas
    └─ Registra auditoria
    ↓
Repositórios (EXISTENTES)
```

### 3.1 Anticorruption Layer (ACL)

```java
// ✅ PORTA DE SAÍDA - Integração Externa
public interface SistemaExternoConnector {
    ExternalPatient buscarPacienteExterno(String id);
    void enviarConsultaExterna(ConsultaDTO consulta);
}

// Implementações: HL7Adapter, HL7v3Adapter, RESTExternalAdapter, SOAPAdapter
```

```java
// ✅ ANTICORRUPTION LAYER - Traduz modelo externo para domínio
public class PacienteExternoTranslator {
    public Paciente traduzir(ExternalPatient external) {
        // Normaliza dados
        // Valida contra regras de domínio
        // Converte formato externo → formato interno
        // Trata exceções de incompatibilidade
        return new Paciente(/* ... */);
    }
}

public class ConsultaExternaTranslator {
    public Consulta traduzir(ExternalConsulta externa) {
        // Similar: converte e valida
        return new Consulta(/* ... */);
    }
}
```

### 3.2 Novas Portas de Entrada

```java
// ✅ NOVA PORTA - Receber dados de sistema externo
public interface ImportarPacienteExternoUseCase {
    Integer importar(ImportarPacienteCommand command);
}

// ✅ NOVA PORTA - Sincronizar consultas
public interface SincronizarConsultasUseCase {
    void sincronizar(SincronizacaoRequest request);
}

// ✅ NOVA PORTA - Compartilhar prontuário
public interface CompartilharProntuarioUseCase {
    void compartilhar(CompartilhamentoProntuarioCommand command);
}
```

### 3.3 Adaptadores de Integração

```
adapters/out/integration/
├── hl7/
│   ├── HL7PatientAdapter (Implementa SistemaExternoConnector)
│   └── HL7ConsultaAdapter
├── rest/
│   ├── RESTExternalSystemAdapter
│   └── APIExternalAdapter
├── soap/
│   └── SOAPExternalAdapter
└── files/
    ├── CSVImportAdapter
    └── XMLImportAdapter
```

### 3.4 Impacto no Domínio

**NENHUMA MODIFICAÇÃO NECESSÁRIA:**

```
Domínio continua puro, SEM conhecimento de:
✓ Sistemas externos
✓ HL7, APIs, SOAP
✓ Formatos de integração
✓ Protocolos de comunicação
```

**Novos agregados (para auditoria/rastreamento):**

```java
// ✅ Agregado de integração (isolado do domínio clínico)
public class IntegracaoExterna {
    private final Integer id;
    private final Integer pacienteLocalId;
    private final String pacienteExternoId;
    private final String sistemaProcedencia;
    private final LocalDateTime dataSincronizacao;
    private final StatusIntegracao status;

    // Invariantes:
    // - pacienteLocalId ou pacienteExternoId sempre válidos
    // - sistemaProcedencia identificado
}

public enum StatusIntegracao {
    SINCRONIZADO, DIVERGENTE, PENDENTE_VALIDACAO, FALHOU
}
```

### 3.5 Proteção do Domínio (ACL)

```
┌────────────────────────────────────────┐
│     SISTEMA EXTERNO (HL7, REST, ...)   │
├────────────────────────────────────────┤
│                                        │
│  ExternalPatient                       │
│  {                                     │
│    externalId: "EXT123"               │
│    firstName: "João"                   │
│    birthDate: "1990-05-15"            │
│    ssn: "123.456.789-00"              │
│  }                                     │
│                                        │
└─────────────────────────────────────────┤
                  ↓
        ┌─────────────────────────┐
        │ ANTICORRUPTION LAYER    │
        ├─────────────────────────┤
        │                         │
        │ PacienteExternoTrans    │
        │ lator.traduzir(ext)    │
        │                         │
        │ Validações:            │
        │ • Nome não vazio       │
        │ • Data valida          │
        │ • Regex de documento   │
        │ • Normaliza campos     │
        │                         │
        └─────────────────────────┤
                ↓
        ┌──────────────────────────┐
        │   DOMÍNIO LIMPO          │
        ├──────────────────────────┤
        │                          │
        │  Paciente {             │
        │    id: 456              │
        │    nomeCrianca: "João"  │
        │    sexo: "M"            │
        │    dataNascimento: ...  │
        │    ...                  │
        │  }                       │
        │                          │
        │  IntegracaoExterna {    │
        │    pacienteLocalId: 456 │
        │    pacienteExternoId:   │
        │      "EXT123"           │
        │    sistemaProcedencia   │
        │      : "Hospital_X"     │
        │  }                       │
        └──────────────────────────┘
```

### 3.6 Padrões de Projeto Aplicáveis

| Padrão                      | Uso                                                                 |
| --------------------------- | ------------------------------------------------------------------- |
| **Adapter**                 | `HL7Adapter` adapta HL7 API para `SistemaExternoConnector`          |
| **Translator**              | `PacienteExternoTranslator` converte ExternalPatient → Paciente     |
| **Facade**                  | `IntegracaoFacade` expõe interface simplificada para síncronização  |
| **Chain of Responsibility** | Validações encadeadas: formato → domínio → negócio                  |
| **Factory**                 | `SistemaExternoConnectorFactory` cria adaptador correto (HL7, REST) |

### 3.7 Relação com Princípios SOLID

#### 🔷 DIP

```
SincronizarConsultasService
    ↓
ISistemaExternoConnector (interface)
    ↓
HL7Adapter, RESTAdapter, SOAPAdapter (implementações)
```

#### 🔷 SRP

| Classe                        | Responsabilidade                 |
| ----------------------------- | -------------------------------- |
| `HL7Adapter`                  | Chamar API HL7                   |
| `PacienteExternoTranslator`   | Traduzir modelo externo          |
| `SincronizarConsultasService` | Orquestrar sincronização         |
| `IntegracaoExternaRepository` | Persistir registro de integração |

---

# IV. SUPORTE A MÚLTIPLAS CLÍNICAS E MÉDICOS

## 📊 Análise de Impacto

### Novo Fluxo com Isolamento de Contextos

```
Cliente Web
    ↓ [Clinica_ID, Medico_ID]
ClinicaContext
    ├─ Valida acesso à clínica
    └─ Filtra dados por clínica
    ↓
UseCase (mesmo, mas com contexto)
    ├─ AgendarConsultaService
    │   └─ Valida se médico pertence à clínica
    ├─ RegistrarProntuarioService
    │   └─ Valida se consulta é da clínica
    └─ ...
    ↓
Repositórios (com predicados de clínica)
```

### 4.1 Bounded Contexts Identificados

```
1. CLÍNICA CONTEXT
   ├─ Agregados: Clinica, ClinicaConfig
   └─ Responsabilidade: Dados e configurações da clínica

2. MÉDICO CONTEXT
   ├─ Agregados: Medico, HorarioAtendimento
   └─ Responsabilidade: Médicos e disponibilidades

3. PACIENTE CONTEXT (EXISTENTE)
   ├─ Agregados: Paciente, HistoricoMedico
   └─ Responsabilidade: Dados de pacientes

4. CONSULTA CONTEXT (EXISTENTE)
   ├─ Agregados: Consulta, Prontuario
   └─ Responsabilidade: Agendamento e registros clínicos

5. FATURAMENTO CONTEXT (NOVO)
   ├─ Agregados: Fatura, ItemFatura
   └─ Responsabilidade: Cobranças por clínica
```

### 4.2 Novos Agregados no Domínio

```java
// ✅ Agregado Clínica (NOVO)
public class Clinica {
    private final Integer id;
    private final String nome;
    private final Endereco endereco;
    private final List<Telefone> telefones;
    private final String cnpj;
    private final StatusClinica status;
    private final List<Integer> medicoIds; // IDs dos médicos

    // Invariantes:
    // - nome não vazio
    // - CNPJ válido
    // - apenas médicos da clínica podem fazer consultas
}

public enum StatusClinica {
    ATIVA, INATIVA, SUSPENSA
}

// ✅ Agregado Médico EXPANDIDO
public class Medico {
    private final Integer id;
    private final Integer clinicaId; // NOVO: associação com clínica
    private final String nome;
    private final String especialidade;
    private final String crm;
    private final List<HorarioAtendimento> horariosAtendimento;

    // Invariantes:
    // - CRM válido
    // - clinicaId sempre válido
    // - não pode atender em duas clínicas simultaneamente
}

// ✅ Value Object novo
public class HorarioAtendimento {
    private final DiaSemana dia;
    private final LocalTime inicio;
    private final LocalTime fim;
    private final Integer intervaloMinutos;

    // Validações de horário
}

public enum DiaSemana {
    SEGUNDA, TERÇA, QUARTA, QUINTA, SEXTA, SÁBADO, DOMINGO
}
```

### 4.3 Impacto em Agregados Existentes

```java
// ❌ NÃO MODIFICAR diretamente, mas adicionar contexto
// ANTES:
public class Consulta {
    private final Integer id;
    private final Paciente paciente;
    private final Medico medico;
    private final LocalDateTime dataHora;
    // ...
}

// DEPOIS (com isolamento de contexto):
public class Consulta {
    private final Integer id;
    private final Integer clinicaId;      // ✅ NOVO campo
    private final Paciente paciente;
    private final Medico medico;          // Medico já tem clinicaId
    private final LocalDateTime dataHora;

    // Invariante NOVO:
    // - Médico deve pertencer à clínica
}
```

### 4.4 Novas Portas de Entrada

```java
// ✅ NOVA PORTA - Gerenciar clínicas
public interface GerenciarClinicaUseCase {
    Integer criar(CriarClinicaCommand command);
    void atualizar(AtualizarClinicaCommand command);
    void desativar(Integer clinicaId);
}

// ✅ NOVA PORTA - Gerenciar médicos por clínica
public interface GerenciarMedicoUseCase {
    Integer criar(CriarMedicoCommand command);
    void desativar(Integer medicoId);
}

// ✅ NOVA PORTA - Consultas isoladas por clínica
public interface ListarConsultasPorClinicaUseCase {
    List<ConsultaDTO> listar(Integer clinicaId, LocalDate data);
}

// ✅ NOVA PORTA - Validar acesso a recurso
public interface ValidarAcessoClinicaUseCase {
    void validar(Integer usuarioId, Integer clinicaId);
}
```

### 4.5 Novas Portas de Saída

```java
// ✅ NOVA PORTA
public interface ClinicaRepository {
    void salvar(Clinica clinica);
    Clinica obter(Integer id);
    List<Clinica> listarTodas();
}

// ✅ NOVA PORTA - Modificação da existente
public interface MedicoRepository {
    void salvar(Medico medico);
    Medico obter(Integer id);
    List<Medico> listarPorClinica(Integer clinicaId);  // NOVO
}

// ✅ NOVA PORTA
public interface ClinicaAcessoRepository {
    void salvar(ClinicaAcesso acesso);
    boolean temAcesso(Integer usuarioId, Integer clinicaId);
}
```

### 4.6 Estratégia de Isolamento: Tenant Context

```java
// ✅ Context para isolamento de dados por clínica
public class ClinicaContext {
    private static final ThreadLocal<Integer> clinicaId = new ThreadLocal<>();

    public static void definirClinica(Integer id) {
        clinicaId.set(id);
    }

    public static Integer obterClinica() {
        Integer id = clinicaId.get();
        if (id == null) {
            throw new DomainException("Contexto de clínica não definido");
        }
        return id;
    }

    public static void limpar() {
        clinicaId.remove();
    }
}

// Uso:
// ClinicaContext.definirClinica(123);
// List<Consultas> = consultaRepository.listar();  // Filtra por clínica
// ClinicaContext.limpar();
```

### 4.7 Estrutura de Pacotes com Bounded Contexts

```
consultas-medicas/
├── core/
│   ├── domain/
│   │   ├── clinica/           (NOVO BOUNDED CONTEXT)
│   │   │   ├── Clinica
│   │   │   └── ClinicaConfig
│   │   ├── medico/            (EXPANDIDO)
│   │   │   ├── Medico
│   │   │   └── HorarioAtendimento
│   │   ├── paciente/          (EXISTENTE)
│   │   ├── consulta/          (EXISTENTE, expande)
│   │   └── shared/            (Modelos compartilhados)
│   │
│   └── service/
│       ├── clinica/           (NOVO)
│       ├── medico/            (NOVO)
│       ├── consulta/          (EXISTENTE, modifica)
│       └── shared/            (Serviços comuns)
│
└── adapters/
    ├── in/
    │   ├── controller/clinica/    (NOVO)
    │   ├── controller/medico/     (NOVO)
    │   └── controller/consulta/   (EXISTENTE)
    │
    └── out/
        └── repository/
            ├── clinica/           (NOVO)
            ├── medico/            (EXPANDIDO)
            └── consulta/          (EXISTENTE, modifica)
```

### 4.8 Integração Entre Bounded Contexts

```
CLINICA CONTEXT
    ├─ Publica evento: ClinicaDescativada
    │
MÉDICO CONTEXT
    ├─ Publica evento: MedicoAssociadoAClinica
    │   ↓
    CONSULTA CONTEXT
    ├─ Escuta: MedicoAssociadoAClinica
    ├─ Valida: Médico pode agendar em sua clínica
    │
PACIENTE CONTEXT
    ├─ Isolado por clínica? Não (pacientes são globais)
    ├─ Mas consultas filtradas por clínica
```

### 4.9 Padrões de Projeto Aplicáveis

| Padrão                    | Uso                                                 |
| ------------------------- | --------------------------------------------------- |
| **Bounded Context**       | Separação de Clínica, Médico, Consulta em contextos |
| **Multi-Tenancy**         | `ClinicaContext.definirClinica()` para isolamento   |
| **Repository Pattern**    | Queries com filtro automático de clínica            |
| **Factory**               | `ConsultaFactory.criar()` valida clínica/médico     |
| **Specification Pattern** | `MedicoEhDaClinicaSpec` valida invariantes          |

### 4.10 Relação com Princípios SOLID

#### 🔷 SRP

| Classe                    | Responsabilidade                          |
| ------------------------- | ----------------------------------------- |
| `GerenciarClinicaService` | Criar/atualizar clínicas                  |
| `GerenciarMedicoService`  | Associar médicos a clínicas               |
| `AgendarConsultaService`  | Agendar COM validação de clínica          |
| `ValidarAcessoService`    | Verificar se usuário pode acessar clínica |

#### 🔷 OCP

```
✓ Novo tipo de clínica (privada, pública, ONG)? Estenda Clinica
✓ Novo tipo de médico? Estenda com novo agregado
✓ Domínio FECHADO, adaptadores ABERTOS
```

#### 🔷 DIP

```
GerenciarClinicaService
    ↓
IClinicaRepository (interface)
    ↓
JpaClinicaRepository, MongoClinicaRepository (implementações)
```

---

## 📌 RESUMO COMPARATIVO DAS EVOLUÇÕES

| Aspecto                        | I. Online                            | II. Notificações                         | III. Integração                | IV. Múltiplas Clínicas                     |
| ------------------------------ | ------------------------------------ | ---------------------------------------- | ------------------------------ | ------------------------------------------ |
| **Portas Entrada**             | 3 (Agend., Histórico, Pagto.)        | 2 (Config., Consultar)                   | 3 (Import, Sinc., Compartilh.) | 4 (Gerenc. Clín., Méd., Consultas, Acesso) |
| **Portas Saída**               | 2 (Video, Pagto.)                    | 5 (Email, SMS, Push, Publisher, Config.) | 1 (Conector Externo)           | 3 (Clínica, Médico, Acesso)                |
| **Modificação Domínio**        | ✓ Extensão (TipoConsulta, Pagamento) | ✗ Nenhuma (só novos agregados)           | ✗ Nenhuma (ACL isola)          | ✓ Extensão (Clinica, contexto em Consulta) |
| **Novos Agregados**            | Pagamento, VideoConferencia          | ConfiguracaoNotificacao, Notificacao     | IntegracaoExterna              | Clinica, ClinicaAcesso                     |
| **Padrão Principal**           | Strategy, Factory, Observer          | Observer, Pub/Sub                        | Adapter, Translator            | Bounded Context, Multi-Tenancy             |
| **Impacto Banco Dados**        | +2 tabelas                           | +2 tabelas                               | +1 tabela                      | +2 tabelas, +FK em Consulta                |
| **Complexidade Testabilidade** | Média                                | Baixa (eventos mocados)                  | Média (tradutores isolados)    | Alta (contextos isolados)                  |

---

## 🎯 CONCLUSÃO

### ✅ Arquitetura Hexagonal Validada

Seu projeto **ESTÁ CORRETO** em relação à Hexagonal Architecture:

1. **Desacoplamento Total**: Domínio independente de frameworks, BD, UI
2. **Estabilidade**: Mudanças externas não afetam lógica de negócio
3. **Extensibilidade (OCP)**: Novas funcionalidades sem modificar core
4. **Testabilidade**: Mocks de portas permitem testes isolados

### 🚀 Recomendação de Próximos Passos

Para implementação real, sugiro a **Etapa 02** focar em:

**FUNCIONALIDADE OBRIGATÓRIA (Escolha 1 para implementar):**

□ **I.1 - Agendamento Online** (Menor escopo, rápido ROI)

- Adicionar TipoConsulta.ONLINE
- Criar VideoConferenciaProvider
- Implementar ZoomAdapter como primeiro provider

□ **II - Notificações** (Média complexidade, alto impacto)

- Implementar EventPublisher simples (InMemory)
- Criar NotificadorEmail com SMTP
- Registrar configurações de notificação

□ **IV.1 - Múltiplas Clínicas** (Maior escopo, crítico para crescimento)

- Criar Clinica agregado
- Adicionar clinicaId a Consulta
- Implementar ClinicaContext para isolamento

### 📚 Próxima Sessão

Quando prosseguir à **Etapa 02**, você precisará:

1. Escolher 1 funcionalidade para implementar
2. Detalhar estrutura de pacotes
3. Implementar adaptadores específicos
4. Gerar testes unitários e integração
5. Documentar migrations de BD (se necessário)

---

**FIM DA ANÁLISE**

_Arquivo gerado: 2026-01-18_
_Arquitetura validada: ✅ CORRETA_
_Pronto para Etapa 02 (Escolher 1 funcionalidade)_
