package nomina.bean;

import java.io.Serializable;
import java.util.List;

import nomina.model.Empleado;
import nomina.model.Nomina;
import nomina.service.NominaService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing Bean (Controlador) que gestiona la interacción entre la vista (XHTML)
 * y la capa de servicios (NominaService).
 * 
 * @Named permite acceder al bean desde la vista XHTML con el nombre "nominaBean".
 * @ViewScoped mantiene el bean vivo mientras el usuario permanezca en la misma página.
 * 
 */
@Named("nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empleado empleado;         // Objeto para enlazar los datos del formulario
    private Nomina nominaResultado;    // Objeto para mostrar los resultados en pantalla

    // Inyección de dependencias (CDI). El contenedor se encarga de instanciar el servicio.
    @Inject
    private NominaService nominaService;

    /**
     * Método que se ejecuta automáticamente después de que el Bean es creado.
     * Se usa para inicializar variables.
     */
    @PostConstruct
    public void init() {
        empleado = new Empleado();
        nominaResultado = null; // Al inicio no hay resultados que mostrar
    }

    /**
     * Acción del botón "Calcular". 
     * Intenta guardar el empleado (validando duplicados) y calcula la nómina.
     * Maneja las excepciones para mostrar mensajes amigables en la vista.
     */
    public void calcular() {
        try {
            // Paso 1: Guardar validando reglas de negocio
            nominaService.guardarEmpleado(empleado);

            // Paso 2: Calcular la nómina si no hubo errores
            nominaResultado = nominaService.calcularNomina(empleado);

            // Paso 3: Enviar mensaje de éxito a la vista (p:growl)
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Éxito",
                    "Nómina calculada y empleado guardado."
                )
            );

        } catch (Exception e) {

            // Si falla la validación en el Service, se muestra el error y se limpia el resultado
            nominaResultado = null;

            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error de validación",
                    e.getMessage()
                )
            );
        }
    }

    /**
     * Acción del botón "Limpiar".
     * Reinicia los objetos para limpiar el formulario y la tabla de resultados.
     * immediate="true" en la vista evita validaciones al usar este botón.
     */
    public void limpiar() {
        empleado = new Empleado();
        nominaResultado = null;
    }

    // Getters y Setters necesarios para el binding con JSF

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Nomina getNominaResultado() {
        return nominaResultado;
    }

    public void setNominaResultado(Nomina nominaResultado) {
        this.nominaResultado = nominaResultado;
    }

    public List<Empleado> getListaEmpleados() {
        return nominaService.getEmpleadosRegistrados();
    }
}