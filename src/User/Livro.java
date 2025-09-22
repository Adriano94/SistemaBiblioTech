public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private boolean reservado;

    public Livro(int id, String titulo, String autor, boolean reservado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.reservado = reservado;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public boolean isReservado() { return reservado; }

    public void setReservado(boolean reservado) {
        this.reservado = reservado;
    }
}
