// Autor: Kevin Garzon
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package asp.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import asp.modelo.Aspirante;
import asp.modelo.AspiranteDAO;
import asp.modelo.ProgAcad;
import asp.modelo.ProgAcadDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("asp")
@SessionScoped
public class AspiranteBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Aspirante dto = new Aspirante();
    private ArrayList<Aspirante> listaAs = AspiranteDAO.lista_A;
    private ArrayList<ProgAcad> listaPa = ProgAcadDAO.lista_P;
    private String mensaje = "";

    public AspiranteBean() {
        ProgAcadDAO.cargaDatos();
    }

    // --- Getters y Setters ---

    public Aspirante getDto() { return dto; }           
    public void setDto(Aspirante dto) { this.dto = dto; }

    public ArrayList<Aspirante> getListaAs() { return listaAs; }
    public void setListaAs(ArrayList<Aspirante> listaAs) { this.listaAs = listaAs; }

    public ArrayList<ProgAcad> getListaPa() { return listaPa; }
    public void setListaPa(ArrayList<ProgAcad> listaPa) { this.listaPa = listaPa; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    // --- Logica ---

    public void registrar() {
        ProgAcad programaSeleccionado = null;
        for (ProgAcad p : listaPa) {
            if (p.getCod() == dto.getPro_acad().getCod()) {
                programaSeleccionado = p;
                break;
            }
        }

        if (programaSeleccionado != null) {
            dto.setFecha_reg(LocalDate.now());
            dto.setPro_acad(programaSeleccionado);
            listaAs.add(dto);
            mensaje = "Aspirante registrado exitosamente.";
            dto = new Aspirante();
        } else {
            mensaje = "Seleccione un programa academico valido.";
        }
    }
}