-- ============================================
-- SISTEMA DE VENDAS (EDUCACIONAL)
-- ============================================

-- CRIA O SCHEMA CASO NÃO EXISTA
CREATE DATABASE IF NOT EXISTS sistema_vendas;
USE sistema_vendas;

-- ============================================
-- TABELA: usuario
-- TABELA RESPONSÁVEL POR CONTROLAR O LOGIN DE ACESSO (VENDEDOR E ADMINISTRADOR)
-- ============================================
CREATE TABLE usuario (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL UNIQUE,
    -- "senha_hash" = VARCHAR COM 60 CARECTERES PARA CUMPRIR O TAMANHO DA CRIPTOGRAFIA VIA "jbcrypt"
                         senha_hash VARCHAR(60) NOT NULL,
                         perfil ENUM('ADMIN', 'VENDEDOR') NOT NULL DEFAULT 'VENDEDOR',
    -- CASO NÃO PREENCHIDO A DATA E HORA SERÁ PREENCHIDA NO INSTANTE QUE O INSERT ACONTECER
                         data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABELA: produto
-- TABELA RESPONSÁVEL POR CONTROLAR O ITENS E QUANTIDADES DELES
-- ============================================
CREATE TABLE produto (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         descricao VARCHAR(500),
                         preco DECIMAL(10,2) NOT NULL,
                         quantidade_estoque INT NOT NULL DEFAULT 0,
                         data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- "CHECK" = CUMPRINDO A REGRA DE NEGÓCIO QUE O ESTOQUE NUNCA PODE SER NEGATIVO
                         CONSTRAINT chk_estoque_nao_negativo CHECK (quantidade_estoque >= 0)
);

-- ============================================
-- TABELA: Cliente
-- TABELA RESPONSÁVEL POR CONTROLAR OS CLIENTES E SUAS INFORMAÇÕES
-- ============================================
CREATE TABLE cliente (
-- DADOS DE ENDEREÇO DO CLIENTE SERÁ PREENCHIDO ATRAVÉS DA API VIA-CEP
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(150) NOT NULL,
                         cpf VARCHAR(11) NOT NULL UNIQUE,
                         cep VARCHAR(8) NOT NULL,
                         logradouro VARCHAR(200),
                         bairro VARCHAR(100),
                         cidade VARCHAR(100),
                         uf CHAR(2),
                         data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABELA: venda
-- TABELA RESPONSÁVEL PELO CONTROLE DA VENDA E SUAS INFORMAÇÕES
-- ============================================
CREATE TABLE venda (
                       id INT AUTO_INCREMENT PRIMARY KEY,
    -- QUEM COMPROU
                       cliente_id INT NOT NULL,
    -- QUEM VENDEU
                       usuario_id INT NOT NULL,
    -- QUANDO COMPROU
                       data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- QUANTO PAGOU
                       valor_total DECIMAL(10,2) NOT NULL DEFAULT 0,
    -- COMO PAGOU
                       forma_pagamento ENUM('DINHEIRO', 'CARTAO') NOT NULL,
                       FOREIGN KEY (cliente_id) REFERENCES cliente(id), -- REFERENCIA QUEM COMPROU
                       FOREIGN KEY (usuario_id) REFERENCES usuario(id) -- REFERENCIA QUEM VENDEU
);

-- ============================================
-- TABELA: item_venda
-- TABELA DE LIGAÇÃO QUE LIGA (N:N) A VENDA AO PRODUTO
-- ============================================
CREATE TABLE item_venda (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            venda_id INT NOT NULL,
                            produto_id INT NOT NULL,
                            quantidade INT NOT NULL,
                            preco_unitario DECIMAL(10,2) NOT NULL,
                            FOREIGN KEY (venda_id) REFERENCES venda(id),
                            FOREIGN KEY (produto_id) REFERENCES produto(id),
                            CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0)
);