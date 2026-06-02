package nomina.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import nomina.model.Departamento;
import nomina.service.NominaService;

/**
 * Bean para el CRUD de departamentos.
 * Maneja el formulario de creacion, edicion y eliminacion
 * de departamentos desde la vista departamentos.xhtml.
 */
@Named("departamentoBean")
@ViewScoped
public class DepartamentoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Departamento del formulario de creacion */
    private Departamento departamento;

    /** Departamento seleccionado para editar */
    private Departamento departamentoEditando;

    @Inject
    private NominaService nominaService;

    @PostConstruct
    public void init() {
        departamento        = new Departamento();
        departamentoEditando = null;
    }

    // -----------------------------------------------------------------------
    // Acciones
    // -----------------------------------------------------------------------

    /**
     * Guarda un nuevo departamento desde el formulario.
     */
    public void guardar() {
        try {
            nominaService.guardarDepartamento(departamento);
            departamento = new Departamento();
            addInfo("Departamento guardado correctamente.");
        } catch (Exception e) {
            addError(e.getMessage());
        }
    }

    /**
     * Prepara el bean para editar el departamento seleccionado.
     */
    public void prepararEdicion(Departamento dep) {
        departamentoEditando = dep;
    }

    /**
     * Guarda los cambios del departamento en edicion.
     */
    public void guardarEdicion() {
        try {
            nominaService.actualizarDepartamento(departamentoEditando);
            departamentoEditando = null;
            addInfo("Departamento actualizado correctamente.");
        } catch (Exception e) {
            addError(e.getMessage());
        }
    }

    /**
     * Elimina un departamento si no tiene empleados asignados.
     */
    public void eliminar(Departamento dep) {
        try {
            nominaService.eliminarDepartamento(dep.getId());
            addInfo("Departamento eliminado correctamente.");
        } catch (Exception e) {
            addError(e.getMessage());
        }
    }

    /**
     * Cancela la edicion en curso.
     */
    public void cancelarEdicion() {
        departamentoEditando = null;
    }

    // -----------------------------------------------------------------------
    // Lista expuesta a la vista
    // -----------------------------------------------------------------------

    public List<Departamento> getListaDepartamentos() {
        return nominaService.listarDepartamentos();
    }

    // -----------------------------------------------------------------------
    // Getters y Setters
    // -----------------------------------------------------------------------

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento d) { this.departamento = d; }

    public Departamento getDepartamentoEditando() { return departamentoEditando; }
    public void setDepartamentoEditando(Departamento d) { this.departamentoEditando = d; }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private void addError(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }

    private void addInfo(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", mensaje));
    }
}