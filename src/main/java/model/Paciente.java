package model;

import java.util.Date;
import java.util.Optional;

public class Paciente {
    private Integer id;
    private String nome, sobrenome, rg;
    private Date dataCadastro;
    private Endereco endereco;

    public Paciente(int id, String nome, String sobrenome, String rg, Date dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.rg = rg;
        this.dataCadastro = dataCadastro;
    }

    public Paciente(String nome, String sobrenome, String rg) {
        this.id = null;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.rg = rg;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getRg() {
        return rg;
    }

    @Override
    public String toString() {
        Endereco endereco = null;
        if(this.getEndereco() != null) endereco = this.getEndereco();
        return "{" +
                "\"id\" : " + this.id +
                ", \"nome\" : \"" + this.nome + '\"' +
                ", \"sobrenome\" : \"" + this.sobrenome + '\"' +
                ", \"rg\" : \"" + this.rg + '\"' +
                ", \"dataCadastro\" : \"" + this.dataCadastro + '\"' +
                ", \"endereco\" : " + endereco +
                '}';
    }
}
