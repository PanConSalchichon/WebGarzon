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
import nomina.model.Empleado;
import nomina.model.Nomina;
import nomina.service.NominaService;

/**
 * Bean principal del modulo de nomina.
 * Gestiona el formulario de registro, el calculo de nomina
 * y expone los datos a las vistas XHTML.
 *
 * @ViewScoped → el bean vive mientras el usuario este en la misma vista.
 */
@Named("nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // -----------------------------------------------------------------------
    // Atributos enlazados a la vista
    // -----------------------------------------------------------------------

    /** Empleado del formulario de registro */
    private Empleado empleado;

    /** Id del departamento seleccionado en el formulario */
    private Long departamentoSeleccionadoId;

    /** Resultado del ultimo calculo de nomina */
    private Nomina nominaResultado;

    /** Empleado seleccionado para editar en la tabla */
    private Empleado empleadoEditando;

    // -----------------------------------------------------------------------
    // Dependencias
    // -----------------------------------------------------------------------

    @Inject
    private NominaService nominaService;

    // -----------------------------------------------------------------------
    // Inicializacion
    // -----------------------------------------------------------------------

    @PostConstruct
    public void init() {
        empleado        = new Empleado();
        nominaResultado = null;
        empleadoEditando = null;
    }

    // -----------------------------------------------------------------------
    // Acciones — formulario de registro y calculo
    // -----------------------------------------------------------------------

    /**
     * Accion del boton Calcular.
     * Flujo: asignar departamento → validar formato → guardar empleado
     *        → calcular y persistir nomina.
     */
    public void calcular() {
        // Asignar el departamento seleccionado al empleado
        if (departamentoSeleccionadoId == null) {
            addError(null, "Seleccione un departamento.");
            return;
        }
        Departamento dep = nominaService.buscarDepartamento(departamentoSeleccionadoId);
        empleado.setDepartamento(dep);

        try {
            nominaService.guardarEmpleado(empleado);
            nominaResultado = nominaService.calcularYGuardarNomina(empleado);
            addInfo(null, "Nomina calculada y guardada correctamente.");
        } catch (Exception e) {
            nominaResultado = null;
            addError(null, e.getMessage());
        }
    }

    /**
     * Accion del boton Limpiar.
     * Reinicia el formulario sin validaciones (immediate=true en la vista).
     */
    public void limpiar() {
        empleado                  = new Empleado();
        departamentoSeleccionadoId = null;
        nominaResultado           = null;
    }

    // -----------------------------------------------------------------------
    // Acciones — CRUD Empleado
    // -----------------------------------------------------------------------

    /**
     * Prepara el bean para editar un empleado seleccionado de la tabla.
     * Carga el empleado en empleadoEditando y su departamento en el selector.
     *
     * @param emp Empleado seleccionado desde la vista
     */
    public void prepararEdicion(Empleado emp) {
        empleadoEditando          = emp;
        departamentoSeleccionadoId = emp.getDepartamento().getId();
    }

    /**
     * Guarda los cambios del empleado que se esta editando.
     */
    public void guardarEdicion() {
        Departamento dep = nominaService.buscarDepartamento(departamentoSeleccionadoId);
        empleadoEditando.setDepartamento(dep);
        try {
            nominaService.actualizarEmpleado(empleadoEditando);
            empleadoEditando          = null;
            departamentoSeleccionadoId = null;
            addInfo(null, "Empleado actualizado correctamente.");
        } catch (Exception e) {
            addError(null, e.getMessage());
        }
    }

    /**
     * Elimina un empleado y su historial de nominas.
     *
     * @param emp Empleado a eliminar
     */
    public void eliminarEmpleado(Empleado emp) {
        nominaService.eliminarEmpleado(emp.getId());
        addInfo(null, "Empleado eliminado correctamente.");
    }

    /**
     * Cancela la edicion en curso.
     */
    public void cancelarEdicion() {
        empleadoEditando          = null;
        departamentoSeleccionadoId = null;
    }

    // -----------------------------------------------------------------------
    // Listas expuestas a la vista
    // -----------------------------------------------------------------------

    /** Lista de todos los empleados para la tabla de gestion */
    public List<Empleado> getListaEmpleados() {
        return nominaService.listarEmpleados();
    }

    /** Lista de departamentos para el selector del formulario */
    public List<Departamento> getListaDepartamentos() {
        return nominaService.listarDepartamentos();
    }

    /** Historial completo de nominas */
    public List<Nomina> getListaNominas() {
        return nominaService.listarNominas();
    }

    // -----------------------------------------------------------------------
    // Getters y Setters
    // -----------------------------------------------------------------------

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public Long getDepartamentoSeleccionadoId() { return departamentoSeleccionadoId; }
    public void setDepartamentoSeleccionadoId(Long id) { this.departamentoSeleccionadoId = id; }

    public Nomina getNominaResultado() { return nominaResultado; }
    public void setNominaResultado(Nomina nominaResultado) { this.nominaResultado = nominaResultado; }

    public Empleado getEmpleadoEditando() { return empleadoEditando; }
    public void setEmpleadoEditando(Empleado empleadoEditando) { this.empleadoEditando = empleadoEditando; }

    // -----------------------------------------------------------------------
    // Helpers privados para mensajes
    // -----------------------------------------------------------------------

    private void addError(String clientId, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(clientId,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, null));
    }

    private void addInfo(String clientId, String mensaje) {
        FacesContext.getCurrentInstance().addMessage(clientId,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", mensaje));
    }
}