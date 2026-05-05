// Autor: Kevin Garzon y Thomas Diaz
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package pf.modelo;

import java.io.Serializable;

public class PfDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titulo;
    private int paginas;
    private String genero;
    private boolean disponible;
    private String sinopsis;

    public PfDto() {}

    public PfDto(String titulo, int paginas, String genero, boolean disponible, String sinopsis) {
        this.titulo = titulo;
        this.paginas = paginas;
        this.genero = genero;
        this.disponible = disponible;
        this.sinopsis = sinopsis;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getPaginas() { return paginas; }
    public void setPaginas(int paginas) { this.paginas = paginas; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }
}