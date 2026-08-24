package br.com.senai.model;

// CLASSE CLIENTE
// REPRESENTA UM CLIENTE DO SISTEMA, OS CAMPOS DE ENDEREÇO COMEÇAM VAZIOS
// E SÃO PREENCHIDOS AUTOMATICAMENTE POR UM SERVIÇO QUE CONSULTA A API VIACEP A PARTIR DO CEP INFORMADO.
public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;

    // CONSTRUTOR USADO QUANDO O CLIENTE AINDA NÃO TEM ID (NÃO VEM DO BANCO)
    public Cliente(String nome, String cpf, String cep) {
        this.setNome(nome);
        this.setCpf(cpf);
        this.setCep(cep);
    }

    // CONSTRUTOR USADO QUANDO O CLIENTE JÁ VEM DO BANCO (JÁ TEM ID E ENDEREÇO PREENCHIDO)
    public Cliente(int id, String nome, String cpf, String cep,
                   String logradouro, String bairro, String cidade, String uf) {
        this(nome, cpf, cep);
        this.id = id;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
    }

    // GETTERS E SETTERS (ENCAPSULAMENTO)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do Cliente não pode ser vazio.");
        }
        this.nome = nome;
    }

    // VALIDAÇÃO DO CPF: REMOVE PONTOS E TRAÇO, VERIFICA SE TEM 11 DÍGITOS
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }
        String cpfLimpo = cpf.replaceAll("[^0-9]", ""); // remove pontos e traço, se vierem
        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos. Recebido: " + cpf);
        }
        this.cpf = cpfLimpo;
    }

    // VALIDAÇÃO DO CEP: REMOVE PONTOS E TRAÇO, VERIFICA SE TEM 8 DÍGITOS
    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("CEP não pode ser nulo.");
        }
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("CEP deve conter 8 dígitos. Recebido: " + cep);
        }
        this.cep = cepLimpo;
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

    // INDICA SE O ENDEREÇO JÁ FOI PREENCHIDO (OU SE A CONSULTA AO VIACEP JÁ FOI FEITA COM SUCESSO)
    public boolean enderecoPreenchido() {
        return logradouro != null && cidade != null && uf != null;
    }

    // RETORNA UMA STRING COM OS DADOS DO CLIENTE (USADO PARA DEBUG)
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", uf='" + uf + '\'' +
                '}';
    }
}