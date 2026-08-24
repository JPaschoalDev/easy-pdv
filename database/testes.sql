-- ============================================
-- SCRIPT DE POPULAÇÃO (DADOS DE TESTE)
-- SISTEMA DE VENDAS (EDUCACIONAL)
-- ============================================
-- OBJETIVO: gerar dados fictícios para testar telas, DAOs e regras
-- de negócio (login, estoque, cadastro de Cliente, vendas com
-- múltiplos itens). Rode DEPOIS do script de criação das tabelas.
-- ============================================

USE sistema_vendas;

-- DESLIGA O "SAFE UPDATES" DO WORKBENCH APENAS PARA ESTA SESSÃO
SET SQL_SAFE_UPDATES = 0;

-- LIMPA DADOS ANTERIORES (ORDEM RESPEITA AS FOREIGN KEYS)
DELETE FROM item_venda;
DELETE FROM venda;
DELETE FROM cliente;
DELETE FROM produto;
DELETE FROM usuario;

-- REINICIA OS AUTO_INCREMENT PARA MANTER OS IDs PREVISÍVEIS
ALTER TABLE usuario AUTO_INCREMENT = 1;
ALTER TABLE produto AUTO_INCREMENT = 1;
ALTER TABLE cliente AUTO_INCREMENT = 1;
ALTER TABLE venda AUTO_INCREMENT = 1;
ALTER TABLE item_venda AUTO_INCREMENT = 1;

-- ============================================
-- USUARIO
-- Hashes REAIS em BCrypt (60 caracteres, prefixo $2a$, compatível
-- com jBCrypt). Use estas senhas em texto puro para testar o login:
--   admin@sistemavendas.com          -> admin123
--   ana.souza@sistemavendas.com      -> admin123
--   carlos.lima@sistemavendas.com    -> vendedor123
--   beatriz.alves@sistemavendas.com  -> vendedor123
--   diego.ramos@sistemavendas.com    -> vendedor123
-- ============================================
INSERT INTO usuario (nome, email, senha_hash, perfil) VALUES
                                                          ('Admin Master', 'admin@sistemavendas.com', '$2a$10$vI8o2RPv7knoC4E40uAS4eDA6IAKaf/HoRic1rK2uRWI69okINjKe', 'ADMIN'),
                                                          ('Ana Souza', 'ana.souza@sistemavendas.com', '$2a$10$vI8o2RPv7knoC4E40uAS4eDA6IAKaf/HoRic1rK2uRWI69okINjKe', 'ADMIN'),
                                                          ('Carlos Lima', 'carlos.lima@sistemavendas.com', '$2a$10$QFjS4EMqSJtC6KhPibz79.f//7ZNWSQu.CaLppW5K8/XuZ1Djcknq', 'VENDEDOR'),
                                                          ('Beatriz Alves', 'beatriz.alves@sistemavendas.com', '$2a$10$QFjS4EMqSJtC6KhPibz79.f//7ZNWSQu.CaLppW5K8/XuZ1Djcknq', 'VENDEDOR'),
                                                          ('Diego Ramos', 'diego.ramos@sistemavendas.com', '$2a$10$QFjS4EMqSJtC6KhPibz79.f//7ZNWSQu.CaLppW5K8/XuZ1Djcknq', 'VENDEDOR');

-- ============================================
-- PRODUTO
-- ============================================
INSERT INTO produto (nome, descricao, preco, quantidade_estoque) VALUES
                                                                     ('Mouse Óptico USB', 'Mouse com fio, 1600 DPI', 39.90, 80),
                                                                     ('Teclado ABNT2 USB', 'Teclado padrão brasileiro, com fio', 69.90, 60),
                                                                     ('Monitor 21" Full HD', 'Monitor LED 21 polegadas, HDMI/VGA', 799.00, 25),
                                                                     ('Notebook 15" i5 8GB', 'Notebook i5, 8GB RAM, SSD 256GB', 3299.00, 10),
                                                                     ('Fone de Ouvido Bluetooth', 'Fone sem fio, estojo de recarga', 129.90, 50),
                                                                     ('Cadeira de Escritório', 'Cadeira giratória com apoio lombar', 459.00, 15),
                                                                     ('Mesa para Computador', 'Mesa em MDF, 1,20m', 389.00, 12),
                                                                     ('Impressora Multifuncional', 'Imprime, copia e digitaliza, com Wi-Fi', 549.00, 8),
                                                                     ('Pen Drive 64GB', 'Pen drive USB 3.0', 34.90, 100),
                                                                     ('HD Externo 1TB', 'HD externo portátil USB 3.0', 279.90, 30),
                                                                     ('Webcam Full HD', 'Webcam 1080p com microfone', 159.90, 40),
                                                                     ('Carregador Portátil 10000mAh', 'Power bank com 2 saídas USB', 89.90, 45);

-- ============================================
-- CLIENTE (endereço já preenchido, simulando retorno da API ViaCEP)
-- ============================================
INSERT INTO cliente (nome, cpf, cep, logradouro, bairro, cidade, uf) VALUES
                                                                         ('João Pedro Martins', '11122233344', '30130010', 'Rua da Bahia', 'Centro', 'Belo Horizonte', 'MG'),
                                                                         ('Maria Fernanda Costa', '22233344455', '01310100', 'Avenida Paulista', 'Bela Vista', 'São Paulo', 'SP'),
                                                                         ('Pedro Henrique Silva', '33344455566', '20040020', 'Avenida Rio Branco', 'Centro', 'Rio de Janeiro', 'RJ'),
                                                                         ('Juliana Santos Oliveira', '44455566677', '80010000', 'Rua XV de Novembro', 'Centro', 'Curitiba', 'PR'),
                                                                         ('Lucas Gabriel Pereira', '55566677788', '90010150', 'Rua dos Andradas', 'Centro', 'Porto Alegre', 'RS'),
                                                                         ('Camila Rodrigues', '66677788899', '40020000', 'Avenida Sete de Setembro', 'Centro', 'Salvador', 'BA'),
                                                                         ('Rafael Almeida Souza', '77788899900', '60010000', 'Rua Barão do Rio Branco', 'Centro', 'Fortaleza', 'CE'),
                                                                         ('Beatriz Lima Ferreira', '88899900011', '70040010', 'SBS Quadra 2', 'Asa Sul', 'Brasília', 'DF');

-- ============================================
-- VENDA
-- ============================================
INSERT INTO venda (cliente_id, usuario_id, forma_pagamento, valor_total) VALUES
                                                                             (1, 3, 'CARTAO', 0),
                                                                             (2, 4, 'DINHEIRO', 0),
                                                                             (3, 5, 'CARTAO', 0),
                                                                             (4, 3, 'CARTAO', 0),
                                                                             (5, 4, 'DINHEIRO', 0),
                                                                             (6, 5, 'CARTAO', 0),
                                                                             (7, 3, 'DINHEIRO', 0),
                                                                             (8, 4, 'CARTAO', 0),
                                                                             (1, 5, 'CARTAO', 0),
                                                                             (3, 1, 'DINHEIRO', 0);

-- ============================================
-- ITEM_VENDA (preco_unitario copiado do preço do produto no
-- momento da venda, como aconteceria no sistema real)
-- ============================================
INSERT INTO item_venda (venda_id, produto_id, quantidade, preco_unitario) VALUES
-- Venda 1
(1, 4, 1, 3299.00),
(1, 11, 1, 159.90),
-- Venda 2
(2, 1, 2, 39.90),
(2, 2, 1, 69.90),
-- Venda 3
(3, 3, 1, 799.00),
-- Venda 4
(4, 6, 1, 459.00),
(4, 7, 1, 389.00),
-- Venda 5
(5, 9, 3, 34.90),
(5, 5, 1, 129.90),
-- Venda 6
(6, 8, 1, 549.00),
-- Venda 7
(7, 10, 1, 279.90),
(7, 12, 2, 89.90),
-- Venda 8
(8, 4, 1, 3299.00),
-- Venda 9
(9, 2, 2, 69.90),
(9, 1, 3, 39.90),
-- Venda 10
(10, 5, 2, 129.90),
(10, 11, 1, 159.90);

-- ============================================
-- RECALCULA valor_total DE CADA VENDA A PARTIR DOS ITENS
-- ============================================
UPDATE venda v
    JOIN (
    SELECT venda_id, SUM(quantidade * preco_unitario) AS total
    FROM item_venda
    GROUP BY venda_id
    ) t ON t.venda_id = v.id
    SET v.valor_total = t.total;

-- RELIGA O "SAFE UPDATES"
SET SQL_SAFE_UPDATES = 1;