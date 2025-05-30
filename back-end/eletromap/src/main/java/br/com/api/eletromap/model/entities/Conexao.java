package br.com.api.eletromap.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public abstract class Conexao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idOrigem;
    private Long idDestino;

    public Conexao() {
        // Construtor padrao para a JPA
    }

    public Conexao(Long idOrigem, Long idDestino) {
        this.idOrigem = idOrigem;
        this.idDestino = idDestino;
    }

    public Long getId() {
        return this.id;
    }

    public Long getIdOrigem() {
        return this.idOrigem;
    }

    public void setIdOrigem(Long idOrigem) {
        this.idOrigem = idOrigem;
    }

    public Long getIdDestino() {
        return this.idDestino;
    }

    public void setIdDestino(Long idDestino) {
        this.idDestino = idDestino;
    }
}
