package br.com.senai.service;

// CLASSE USADA QUANDO O RESULTADO DA BUSCA PELO CEP NÃO EXISTE
// A API ViaCEP RESPONDE NORMALMENTE MAS COM O CAMPO "erro: true" NO JSON
public class CepNaoEncontradoException extends Exception {

    public CepNaoEncontradoException(String cep) {
        super("CEP não encontrado: " + cep);
    }
}