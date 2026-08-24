package br.com.senai.model;

import org.mindrot.jbcrypt.BCrypt;

// CLASSE USUÁRIO
// REPRESENTA UM USUÁRIO DO SISTEMA (VENDEDOR OU ADMIN), RESPONSÁVEL PELO LOGIN.
// AQUI, O SISTEMA DE CRIPTOGRAFIA DE SENHA É IMPLEMENTADO COM BCRYPT, QUE É UM HASH DE MÃO ÚNICA
// A SENHA NUNCA É GUARDADA EM TEXTO PURO, APENAS O HASH GERADO PELO BCRYPT.
public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senhaHash;
    private Perfil perfil;

    // CONSTRUTOR USADO AO CADASTRAR UM USUÁRIO NOVO
    // RECEBE A SENHA EM TEXTO PURO (SÓ NESSE MOMENTO, DE PASSAGEM)
    public Usuario(String nome, String email, String senhaEmTextoPuro, Perfil perfil) {
        this.setNome(nome);
        this.setEmail(email);
        this.setSenha(senhaEmTextoPuro);
        this.perfil = perfil;
    }

    // CONSTRUTOR USADO QUANDO O USUÁRIO JÁ VEM DO BANCO (JÁ TEM ID E HASH DE SENHA)
    public Usuario(int id, String nome, String email, String senhaHash, Perfil perfil) {
        this.id = id;
        this.setNome(nome);
        this.setEmail(email);
        this.senhaHash = senhaHash;
        this.perfil = perfil;
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
    // VALIDAÇÃO SIMPLES DE NOME: NÃO PODE SER VAZIO OU NULO
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do usuário não pode ser vazio.");
        }
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    // VALIDAÇÃO SIMPLES DE E-MAIL: VERIFICA SE CONTÉM "@" (NÃO É UMA VALIDAÇÃO COMPLETA, MAS JÁ AJUDA)
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
        this.email = email;
    }

    // RETORNA O HASH DA SENHA (NUNCA A SENHA ORIGINAL — ELA NÃO EXISTE MAIS DEPOIS DE CONVERTIDA)
    // USADO PRINCIPALMENTE PARA PERSISTIR NO BANCO.
    public String getSenhaHash() {
        return senhaHash;
    }

    // RECEBE A SENHA EM TEXTO PURO E TRANSFORMA EM HASH USANDO BCRYPT ANTES DE GUARDAR.
    public void setSenha(String senhaEmTextoPuro) {
        if (senhaEmTextoPuro == null || senhaEmTextoPuro.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 6 caracteres.");
        }
        this.senhaHash = BCrypt.hashpw(senhaEmTextoPuro, BCrypt.gensalt());
    }

    // VALIDA A SENHA DIGITADA PELO USUÁRIO (TEXTO PURO) COM O HASH GUARDADO (SENHA CRIPTOGRAFADA)
    public boolean verificarSenha(String senhaDigitada) {
        return BCrypt.checkpw(senhaDigitada, this.senhaHash);
    }

    public Perfil getPerfil() {
        return perfil;
    }
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    // RETORNA TRUE SE O USUÁRIO FOR ADMINISTRADOR, FALSE CASO CONTRÁRIO
    public boolean isAdmin() {
        return this.perfil == Perfil.ADMIN;
    }

    @Override
    public String toString() {
        // NUNCA É INCLUIDO A SENHA/HASH NO "toString", POR SEGURANÇA (EVITA VAZAR EM LOGS DE DEPURAÇÃO, POR EXEMPLO).
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", perfil=" + perfil +
                '}';
    }
}