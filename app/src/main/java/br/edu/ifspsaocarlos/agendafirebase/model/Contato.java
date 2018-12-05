package br.edu.ifspsaocarlos.agendafirebase.model;



public class Contato {

    private String nome;
    private String fone;
    private String email;

    public Contato()
    {
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getFone() {
        return fone;
    }
    public void setFone(String fone) {
        this.fone = fone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

