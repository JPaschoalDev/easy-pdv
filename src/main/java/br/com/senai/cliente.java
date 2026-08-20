package br.com.senai;

import com.mysql.cj.xdevapi.Client;

public class cliente {
    private int id;
    private String cpf;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
    private String nome;


    public cliente(String nome, String cpf, String cep) {
        this.SetNome(nome);
        this.SetCpf(cpf);
        this.SetCep(cep);
    }

    public cliente(String nome, String cpf, String cep, String logradouro, String bairro, String cidade, String uf) {
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.id = id;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do cliente não pode ser vazio");
        }

        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("cpf não pode ser nulo .");

        }
        String cpflimpo = cpf.replaceAll("[^0-9]", "");
        if (cpflimpo.length() != 11) {
            throw new IllegalArgumentException("cpf deve conter 11 digitos. recebido:" + cpf);

        }

        this.cpf = cpf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("cep não pode ser nulo");
        }
        String ceplimpo = cep.replaceAll("[^0-9]", "");
        if (ceplimpo.length() != 8) {
            throw new IllegalArgumentException("cep deve conter 8 digitos.recebido:" + cep);

        }

        this.cep = cep;
    }

    public boolean enderecoPreenchido() {
        return logradouro != null && cidade != null && uf != null;
    }


        @Override
        public String toString () {
            return "CLIENTE ["+
                    "ID" +id+
                    "NOME"+nome+
                    "CPF"+cpf+
                    "CIDADE"+cidade+
                    "UF"+uf+"\n";

        }

    }
