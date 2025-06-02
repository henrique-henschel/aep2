package br.com.api.eletromap.model.entities;

import br.com.api.eletromap.model.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endereco;
    private Status status;
    @OneToMany(mappedBy = "origem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Conexao> conexoes;

    public Unidade() {
        // Construtor padrao para a JPA
    }

    public Unidade(String endereco, List<Conexao> conexoes) {
        this.endereco = endereco;
        this.status = Status.NORMAL;
        this.conexoes = conexoes;
    }

    public Long getId() {
        return this.id;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Conexao> getConexoes() {
        return this.conexoes;
    }

    public void setConexoes(List<Conexao> conexoes) {
        this.conexoes = conexoes;
    }
}
