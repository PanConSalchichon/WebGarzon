// Autores: Kevin Garzon y Thomas Diaz
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package pf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import pf.model.PfDto;

@Named("pf")
@SessionScoped
public class PfBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PfDto dto = new PfDto();
    private List<PfDto> listaLibros = new ArrayList<>();
    private List<String> generosLiterarios;
    private String mensaje = "";

    public PfBean() {
        generosLiterarios = new ArrayList<>();
        generosLiterarios.add("Novela");
        generosLiterarios.add("Ciencia ficcion");
        generosLiterarios.add("Historia");
        generosLiterarios.add("Tecnologia");
        generosLiterarios.add("Filosofia");
        generosLiterarios.add("Derecho");
        generosLiterarios.add("Matematica");
    }

    public void registrar() {
        mensaje = "";
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            mensaje = "El titulo es obligatorio.";
            return;
        }
        if (dto.getPaginas() <= 0) {
            mensaje = "Ingrese un numero de paginas valido.";
            return;
        }
        if (dto.getGenero() == null || dto.getGenero().isEmpty()) {
            mensaje = "Seleccione un genero literario.";
            return;
        }
        listaLibros.add(dto);
        mensaje = "Libro registrado correctamente.";
        dto = new PfDto();
    }

    public void limpiar() {
        dto = new PfDto();
        mensaje = "";
    }

    public PfDto getDto() { return dto; }
    public void setDto(PfDto dto) { this.dto = dto; }

    public List<PfDto> getListaLibros() { return listaLibros; }

    public List<String> getGenerosLiterarios() { return generosLiterarios; }

    public String getMensaje() { return mensaje; }
}