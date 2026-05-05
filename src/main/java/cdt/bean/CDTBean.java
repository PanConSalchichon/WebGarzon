// Autor: Kevin Garzon
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package cdt.bean;

import java.io.Serializable;

import cdt.modelo.CDTDto;
import cdt.servicio.CDTServicio;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 * Bean controlador JSF del simulador CDT.
 * Actua como capa Controlador en el patron MVC:
 *   - Recibe datos del formulario (Vista)
 *   - Delega la logica a CDTServicio (Modelo/Servicio)
 *   - Expone los resultados de vuelta a la Vista
 */
@Named("cdt")
@ViewScoped
public class CDTBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private CDTDto dto = new CDTDto();
    private CDTServicio servicio = new CDTServicio();
    private boolean calculado = false;
    private String mensajeError = "";

    public CDTBean() {}

    /**
     * Accion del boton Calcular.
     * Valida primero con el servicio; si hay error lo muestra con FacesMessage.
     * Si pasa validacion, delega el calculo al servicio.
     */
    public void calcular() {
        calculado = false;
        mensajeError = "";

        String error = servicio.validar(dto);
        if (error != null) {
            mensajeError = error;
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Datos invalidos", error));
            return;
        }

        servicio.calcular(dto);
        calculado = true;
    }

    /** Reinicia el formulario para una nueva simulacion. */
    public void limpiar() {
        dto = new CDTDto();
        calculado = false;
        mensajeError = "";
    }

    // --- Getters y Setters ---

    public CDTDto getDto() { return dto; }
    public void setDto(CDTDto dto) { this.dto = dto; }

    public boolean isCalculado() { return calculado; }

    public String getMensajeError() { return mensajeError; }
}