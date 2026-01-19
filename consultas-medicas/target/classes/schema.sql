-- Tabela de Médicos
CREATE TABLE IF NOT EXISTS medicos (
    id INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE
);

-- Tabela de Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id INT PRIMARY KEY,
    nome_crianca VARCHAR(255) NOT NULL,
    nome_responsavel VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    sexo CHAR(1) NOT NULL,
    plano_saude_id INT,
    endereco_rua VARCHAR(255),
    endereco_numero VARCHAR(10),
    endereco_complemento VARCHAR(255),
    endereco_bairro VARCHAR(100),
    endereco_cidade VARCHAR(100),
    endereco_estado CHAR(2),
    endereco_cep VARCHAR(10)
);

-- Tabela de Consultas
CREATE TABLE IF NOT EXISTS consultas (
    id INT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    paciente_novo BOOLEAN NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADA',
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (medico_id) REFERENCES medicos(id)
);

-- Tabela de Prontuários
CREATE TABLE IF NOT EXISTS prontuarios (
    id INT PRIMARY KEY,
    consulta_id INT NOT NULL,
    peso DOUBLE NOT NULL,
    altura DOUBLE NOT NULL,
    imc DOUBLE,
    sintomas TEXT,
    observacao_clinica TEXT,
    FOREIGN KEY (consulta_id) REFERENCES consultas(id)
);

-- Tabela de Medicamentos
CREATE TABLE IF NOT EXISTS medicamentos (
    id INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

-- Tabela de Exames
CREATE TABLE IF NOT EXISTS exames (
    id INT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

-- Tabela de Prescrições
CREATE TABLE IF NOT EXISTS prescricoes (
    id INT PRIMARY KEY,
    prontuario_id INT NOT NULL,
    medicamento_id INT NOT NULL,
    dosagem VARCHAR(100),
    administracao VARCHAR(100),
    tempo_uso VARCHAR(50),
    FOREIGN KEY (prontuario_id) REFERENCES prontuarios(id),
    FOREIGN KEY (medicamento_id) REFERENCES medicamentos(id)
);

-- Tabela de Exames do Prontuário
CREATE TABLE IF NOT EXISTS prontuario_exames (
    prontuario_id INT NOT NULL,
    exame_id INT NOT NULL,
    PRIMARY KEY (prontuario_id, exame_id),
    FOREIGN KEY (prontuario_id) REFERENCES prontuarios(id),
    FOREIGN KEY (exame_id) REFERENCES exames(id)
);

-- Tabela de Telefones
CREATE TABLE IF NOT EXISTS telefones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    numero VARCHAR(20) NOT NULL,
    tipo VARCHAR(20),
    contato VARCHAR(255),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

-- Tabela de Plano de Saúde
CREATE TABLE IF NOT EXISTS planos_saude (
    id INT PRIMARY KEY,
    nome_plano VARCHAR(255) NOT NULL
);
