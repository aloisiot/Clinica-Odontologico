package model;

import java.util.Date;
import java.util.Optional;

public class Dentista {
    private Integer matricula;
    private String nome;
    private String sobrenome;
    private Endereco endereco;
    private Date dataCadastro;

    public Dentista(String nome, String sobrenome) {
        this.nome = nome;
        this.sobrenome = sobrenome;
    }

    public Dentista(int matricula, String nome, String sobrenome, Date dataCadastro) {
        this.matricula = matricula;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataCadastro = dataCadastro;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public Endereco getEndereco() {
        return this.endereco;
    }

    @Override
    public String toString() {
        Endereco endereco = null;
        if(this.getEndereco() != null) endereco = this.getEndereco();
        return "{" +
                "\"matricula\" : " + matricula +
                ", \"nome\" : \"" + nome + '\"' +
                ", \"sobrenome\" : \"" + sobrenome + '\"' +
                ", \"dataCadastro\" : \"" + this.dataCadastro + '\"' +
                ", \"endereco\" : " + endereco +
                '}';
    }
}
