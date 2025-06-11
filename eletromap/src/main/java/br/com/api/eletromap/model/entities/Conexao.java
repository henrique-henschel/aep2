package br.com.api.eletromap.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonBackReference; // Importar

@Entity
public class Conexao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference("unidade-origem-conexoes") // Nome da referência DEVE corresponder
    private Unidade origem;

    @ManyToOne
    @JsonBackReference("unidade-destino-conexoes") // Nome da referência DEVE corresponder
    private Unidade destino;

    public Conexao() {
        // Construtor padrão para a JPA
    }

    public Conexao(Unidade origem, Unidade destino) {
        this.origem = origem;
        this.destino = destino;
    }

    public Long getId() {
        return id;
    }

    public Unidade getOrigem() {
        return origem;
    }

    public void setOrigem(Unidade origem) {
        this.origem = origem;
    }

    public Unidade getDestino() {
        return destino;
    }

    public void setDestino(Unidade destino) {
        this.destino = destino;
    }
}