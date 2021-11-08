package model;

public class Endereco {
    private String rua;
    String bairro;
    String cidade;
    String classMorador;
    private Integer idEndereco;
    private Integer idMorador;
    private Integer numero;

    public Endereco(Integer idEndereco, Integer idMorador, String rua, String bairro, String cidade, String classMorador, Integer numero) {
        this.idEndereco = idEndereco;
        this.idMorador = idMorador;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.classMorador = classMorador;
        this.numero = numero;
    }

    public Endereco(String rua, String bairro, String cidade, String classMorador, Integer idMorador, Integer numero) {
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.classMorador = classMorador;
        this.idMorador = idMorador;
        this.numero = numero;
    }

    public Endereco(String rua, String bairro, String cidade, Integer numero) {
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.numero = numero;
    }

    public void setIdMorador(Integer idMorador) {
        this.idMorador = idMorador;
    }

    public void setClassMorador(String classMorador) {
        this.classMorador = classMorador;
    }

    public String getClassMorador() {
        return classMorador;
    }

    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public Integer getIdMorador() {
        return idMorador;
    }

    public Integer getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "{" +
                "\"idEndereco\" : " + idEndereco +
                ", \"idMorador\" : " + idMorador +
                ", \"rua\" : \"" + rua + '\"' +
                ", \"bairro\" : \"" + bairro + '\"' +
                ", \"cidade\" : \"" + cidade + '\"' +
                ", \"classMorador\" : \"" + classMorador + '\"' +
                ", \"numero\" : " + numero +
                "}";
    }

}
