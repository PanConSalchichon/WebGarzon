package nomina.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import nomina.dao.DepartamentoDAO;
import nomina.dao.EmpleadoDAO;
import nomina.dao.NominaDAO;
import nomina.model.Departamento;
import nomina.model.Empleado;
import nomina.model.Nomina;

/**
 * Servicio que encapsula la logica de negocio del sistema de nomina.
 * Ya no mantiene listas en memoria — ahora delega la persistencia
 * a los DAOs y MySQL via JPA.
 *
 * @ApplicationScoped → una sola instancia para toda la aplicacion.
 */
@ApplicationScoped
public class NominaService {

    // -----------------------------------------------------------------------
    // Constantes legales colombianas (2025)
    // -----------------------------------------------------------------------
    private static final double SMMLV                   = 1300000;
    private static final double AUXILIO_TRANSPORTE_MENSUAL = 162000;

    // -----------------------------------------------------------------------
    // DAOs inyectados por CDI
    // -----------------------------------------------------------------------

    @Inject
    private DepartamentoDAO departamentoDAO;

    @Inject
    private EmpleadoDAO empleadoDAO;

    @Inject
    private NominaDAO nominaDAO;

    // -----------------------------------------------------------------------
    // CRUD Departamento
    // -----------------------------------------------------------------------

    /**
     * Guarda un departamento nuevo validando que el nombre no exista.
     *
     * @throws Exception si el nombre ya esta registrado
     */
    public void guardarDepartamento(Departamento dep) throws Exception {
        if (departamentoDAO.existeNombre(dep.getNombre())) {
            throw new Exception("Ya existe un departamento con el nombre '"
                + dep.getNombre() + "'.");
        }
        departamentoDAO.guardar(dep);
    }

    /**
     * Actualiza un departamento existente.
     */
    public void actualizarDepartamento(Departamento dep) {
        departamentoDAO.actualizar(dep);
    }

    /**
     * Elimina un departamento por id.
     * Verifica que no tenga empleados asignados antes de eliminar.
     *
     * @throws Exception si el departamento tiene empleados
     */
    public void eliminarDepartamento(Long id) throws Exception {
        List<Empleado> empleados = empleadoDAO.listarPorDepartamento(id);
        if (!empleados.isEmpty()) {
            throw new Exception(
                "No se puede eliminar el departamento porque tiene "
                + empleados.size() + " empleado(s) asignado(s).");
        }
        departamentoDAO.eliminar(id);
    }

    /**
     * Retorna todos los departamentos ordenados por nombre.
     */
    public List<Departamento> listarDepartamentos() {
        return departamentoDAO.listarTodos();
    }

    /**
     * Busca un departamento por su id.
     */
    public Departamento buscarDepartamento(Long id) {
        return departamentoDAO.buscarPorId(id);
    }

    // -----------------------------------------------------------------------
    // CRUD Empleado
    // -----------------------------------------------------------------------

    /**
     * Guarda un empleado nuevo validando duplicados de correo y telefono.
     *
     * @throws Exception si el correo o telefono ya estan registrados
     */
    public void guardarEmpleado(Empleado emp) throws Exception {
        List<String> errores = validarDuplicadosEmpleado(emp);
        if (!errores.isEmpty()) {
            throw new Exception(String.join(", ", errores));
        }
        empleadoDAO.guardar(emp);
    }

    /**
     * Actualiza un empleado existente validando duplicados
     * pero excluyendo al propio empleado de la busqueda.
     *
     * @throws Exception si el correo o telefono ya pertenecen a otro empleado
     */
    public void actualizarEmpleado(Empleado emp) throws Exception {
        List<String> errores = validarDuplicadosEmpleado(emp);
        if (!errores.isEmpty()) {
            throw new Exception(String.join(", ", errores));
        }
        empleadoDAO.actualizar(emp);
    }

    /**
     * Elimina un empleado y su historial de nominas.
     * Primero elimina las nominas para respetar la integridad referencial.
     */
    public void eliminarEmpleado(Long id) {
        nominaDAO.eliminarPorEmpleado(id);
        empleadoDAO.eliminar(id);
    }

    /**
     * Retorna todos los empleados con su departamento cargado.
     */
    public List<Empleado> listarEmpleados() {
        return empleadoDAO.listarTodos();
    }

    /**
     * Busca un empleado por su id.
     */
    public Empleado buscarEmpleado(Long id) {
        return empleadoDAO.buscarPorId(id);
    }

    // -----------------------------------------------------------------------
    // Calculo y persistencia de Nomina
    // -----------------------------------------------------------------------

    /**
     * Calcula la nomina de un empleado, la persiste en BD y la retorna.
     *
     * Reglas de negocio:
     *   1. Salario proporcional a dias trabajados (mes = 30 dias).
     *   2. Descuento 4% salud sobre salario devengado.
     *   3. Descuento 4% pension sobre salario devengado.
     *   4. Auxilio transporte si salario basico menor o igual a 2 SMMLV,
     *      proporcional a dias trabajados.
     *   5. Neto = devengado - salud - pension + auxilio.
     *
     * @param emp Empleado al que se calcula la nomina
     * @return Nomina calculada y persistida
     */
    public Nomina calcularYGuardarNomina(Empleado emp) {
        Nomina nomina = new Nomina();
        nomina.setEmpleado(emp);

        // 1. Salario devengado proporcional a dias trabajados
        double salarioCalculado = (emp.getSalarioBasico() / 30.0)
                                  * emp.getDiasTrabajados();
        nomina.setSalarioCalculado(salarioCalculado);

        // 2. Descuentos de seguridad social
        double descSalud   = salarioCalculado * 0.04;
        double descPension = salarioCalculado * 0.04;
        nomina.setDescuentoSalud(descSalud);
        nomina.setDescuentoPension(descPension);

        // 3. Auxilio de transporte
        double auxTransporte = 0;
        if (emp.getSalarioBasico() <= (SMMLV * 2)) {
            auxTransporte = (AUXILIO_TRANSPORTE_MENSUAL / 30.0)
                            * emp.getDiasTrabajados();
        }
        nomina.setAuxilioTransporte(auxTransporte);

        // 4. Neto a pagar
        double neto = salarioCalculado - descSalud - descPension + auxTransporte;
        nomina.setNetoPagar(neto);

        // 5. Persistir en BD
        nominaDAO.guardar(nomina);

        return nomina;
    }

    /**
     * Retorna el historial completo de nominas de todos los empleados.
     */
    public List<Nomina> listarNominas() {
        return nominaDAO.listarTodas();
    }

    /**
     * Retorna el historial de nominas de un empleado especifico.
     */
    public List<Nomina> listarNominasPorEmpleado(Long empleadoId) {
        return nominaDAO.listarPorEmpleado(empleadoId);
    }

    /**
     * Elimina una nomina del historial por su id.
     */
    public void eliminarNomina(Long id) {
        nominaDAO.eliminar(id);
    }

    // -----------------------------------------------------------------------
    // Validaciones internas
    // -----------------------------------------------------------------------

    /**
     * Valida duplicados de correo y telefono para un empleado.
     * Excluye al propio empleado cuando se esta editando (id != null).
     *
     * @param emp Empleado a validar
     * @return Lista de mensajes de error; vacia si no hay duplicados
     */
    private List<String> validarDuplicadosEmpleado(Empleado emp) {
        List<String> errores = new ArrayList<>();

        if (empleadoDAO.existeCorreo(emp.getCorreo(), emp.getId())) {
            errores.add("El correo " + emp.getCorreo() + " ya esta registrado.");
        }
        if (empleadoDAO.existeTelefono(emp.getTelefono(), emp.getId())) {
            errores.add("El telefono " + emp.getTelefono() + " ya esta registrado.");
        }
        return errores;
    }
}