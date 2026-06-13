package mapa;

public class Decoracao {
    private final String nomeArquivo;
    private final int id;

    public Decoracao(int id, String nomeArquivo) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
    }

    public int getId() {
        return id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public static final Decoracao[] TIPOS = {
            null,
            new Decoracao(1, "troncocomfolha.png"),
            new Decoracao(2, "troncoseco.png"),
            new Decoracao(3, "arvorecomfruta.png"),
            new Decoracao(4, "palmeira1.png"),
            new Decoracao(5, "palmeira2.png"),
            new Decoracao(6, "arvore.png")
    };
}