@Entity
public class Conexao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Unidade origem;

    @ManyToOne
    private Unidade destino;

    public Conexao() {}

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
