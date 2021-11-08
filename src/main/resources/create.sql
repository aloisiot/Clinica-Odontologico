CREATE TABLE IF NOT EXISTS pacientes (
    idPaciente INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    rg VARCHAR(8) NOT NULL,
    dataCadastro DATE
);

CREATE TABLE IF NOT EXISTS enderecos (
    idMorador INT NOT NULL,
    idEndereco INT AUTO_INCREMENT PRIMARY KEY,
    classMorador VARCHAR(100) NOT NULL,
    rua VARCHAR(255) NOT NULL,
    bairro VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    numero INT NOT NULL
);

CREATE TABLE IF NOT EXISTS dentistas (
    matricula INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    dataCadastro DATE
);