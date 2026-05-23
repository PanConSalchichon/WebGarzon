package nomina.bean;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import nomina.model.Empleado;
import nomina.model.Nomina;
import nomina.service.NominaService;

/**
 * Bean de respaldo (Backing Bean) del modulo de nomina.
 *
 * Responsabilidades:
 *   - Enlazar los datos del formulario con el modelo (Empleado).
 *   - Validar formato de telefono y correo antes de persistir.
 *   - Validar duplicados de ID, correo y telefono mostrando el error
 *     en el campo especifico que lo provoca.
 *   - Delegar el calculo y guardado al NominaService.
 *   - Exponer el resultado de la nomina a la vista.
 *
 * @Named     permite referenciar este bean desde XHTML como #{nominaBean}.
 * @ViewScoped mantiene el bean vivo mientras el usuario este en la misma vista.
 */
@Named("nominaBean")
@ViewScoped
public class NominaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // -----------------------------------------------------------------------
    // Atributos enlazados a la vista
    // -----------------------------------------------------------------------

    /** Objeto que recibe los datos del formulario via binding JSF. */
    private Empleado empleado;

    /** Resultado del calculo de nomina; null mientras no se haya calculado. */
    private Nomina nominaResultado;

    // -----------------------------------------------------------------------
    // Dependencias inyectadas
    // -----------------------------------------------------------------------

    /**
     * Servicio con la logica de negocio.
     * CDI lo instancia e inyecta automaticamente.
     */
    @Inject
    private NominaService nominaService;

    // -----------------------------------------------------------------------
    // Inicializacion
    // -----------------------------------------------------------------------

    /**
     * Se ejecuta automaticamente despues de que CDI construye el bean.
     * Inicializa el empleado vacio para que el formulario tenga un objeto
     * al que enlazarse desde el primer render.
     */
    @PostConstruct
    public void init() {
        empleado = new Empleado();
        nominaResultado = null;
    }

    // -----------------------------------------------------------------------
    // Acciones de los botones
    // -----------------------------------------------------------------------

    /**
     * Accion del boton "Calcular".
     *
     * Flujo de validacion en tres pasos:
     *   1. Validar formato de telefono y correo (regex).
     *   2. Validar duplicados de ID, correo y telefono contra la lista
     *      en memoria; cada error se asocia al campo correspondiente.
     *   3. Si todo es valido: guardar el empleado y calcular la nomina.
     */
    public void calcular() {

        // Paso 1: validaciones de formato
        if (!validarFormato()) return;

        // Paso 2: validar duplicados campo por campo
        if (!validarDuplicados()) return;

        // Paso 3: guardar y calcular
        try {
            nominaService.guardarEmpleado(empleado);
            nominaResultado = nominaService.calcularNomina(empleado);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Exito", "Nomina calculada correctamente."));

        } catch (Exception e) {
            // Captura cualquier error inesperado del servicio
            nominaResultado = null;
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error inesperado", e.getMessage()));
        }
    }

    /**
     * Accion del boton "Limpiar".
     * Reinicia el formulario y oculta el panel de resultado.
     * La vista usa immediate="true" para saltarse las validaciones JSF
     * al presionar este boton.
     */
    public void limpiar() {
        empleado = new Empleado();
        nominaResultado = null;
    }

    // -----------------------------------------------------------------------
    // Validaciones
    // -----------------------------------------------------------------------

    /**
     * Valida el formato del telefono y del correo usando expresiones regulares.
     * Los mensajes se asocian al ID del componente en la vista para que
     * aparezcan junto al campo correspondiente.
     *
     * @return true si los formatos son correctos, false si alguno falla.
     */
    private boolean validarFormato() {
        boolean valido = true;
        FacesContext ctx = FacesContext.getCurrentInstance();

        // Validar telefono: solo digitos, entre 7 y 10 caracteres
        String tel = empleado.getTelefono();
        if (tel != null && !tel.trim().isEmpty() && !tel.matches("\\d{7,10}")) {
            ctx.addMessage("formNomina:telefono",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Telefono invalido",
                    "Solo digitos, entre 7 y 10 caracteres."));
            valido = false;
        }

        // Validar correo: formato estandar usuario@dominio.ext
        String correo = empleado.getCorreo();
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (correo != null && !correo.trim().isEmpty()
                && !Pattern.matches(emailRegex, correo)) {
            ctx.addMessage("formNomina:correo",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Correo invalido",
                    "Formato esperado: usuario@dominio.com"));
            valido = false;
        }

        return valido;
    }

    /**
     * Consulta al servicio si algun campo unico ya existe en la lista.
     * El servicio retorna una lista con los nombres de los campos duplicados
     * ("id", "correo", "telefono") para que cada error se muestre
     * en el campo exacto de la vista.
     *
     * @return true si no hay duplicados, false si se encontro al menos uno.
     */
    private boolean validarDuplicados() {
        List<String> duplicados = nominaService.validarDuplicados(empleado);

        if (duplicados.isEmpty()) return true;

        FacesContext ctx = FacesContext.getCurrentInstance();

        // Mostrar error en el campo de identificacion
        if (duplicados.contains("id")) {
            ctx.addMessage("formNomina:identificacion",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "ID duplicado",
                    "La identificacion " + empleado.getId_p() + " ya esta registrada."));
        }

        // Mostrar error en el campo de correo
        if (duplicados.contains("correo")) {
            ctx.addMessage("formNomina:correo",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Correo duplicado",
                    "El correo " + empleado.getCorreo() + " ya esta registrado."));
        }

        // Mostrar error en el campo de telefono
        if (duplicados.contains("telefono")) {
            ctx.addMessage("formNomina:telefono",
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Telefono duplicado",
                    "El telefono " + empleado.getTelefono() + " ya esta registrado."));
        }

        return false;
    }

    // -----------------------------------------------------------------------
    // Getters y Setters
    // -----------------------------------------------------------------------

    /** Retorna el empleado actual enlazado al formulario. */
    public Empleado getEmpleado() { return empleado; }

    /** Permite que JSF actualice el empleado desde la vista. */
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    /** Retorna el resultado del ultimo calculo, o null si no se ha calculado. */
    public Nomina getNominaResultado() { return nominaResultado; }

    /** Permite inyectar un resultado desde tests o logica externa. */
    public void setNominaResultado(Nomina nominaResultado) {
        this.nominaResultado = nominaResultado;
    }

    /**
     * Retorna la lista actualizada de empleados registrados.
     * Se llama cada vez que la vista renderiza la tabla de gestion.
     */
    public List<Empleado> getListaEmpleados() {
        return nominaService.getEmpleadosRegistrados();
    }
}