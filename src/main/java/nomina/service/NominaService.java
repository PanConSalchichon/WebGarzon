package nomina.service;

import java.util.ArrayList;
import java.util.List;
import nomina.model.Empleado;
import nomina.model.Nomina;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Clase de servicio que encapsula la logica de negocio relacionada con la
 * gestion de empleados y el calculo de la nomina.
 *
 * @ApplicationScoped garantiza que la lista de empleados se mantenga en memoria
 * mientras la aplicacion este en ejecucion (simula una base de datos).
 */
@ApplicationScoped
public class NominaService {

    // -----------------------------------------------------------------------
    // Constantes legales colombianas (Año 2024)
    // -----------------------------------------------------------------------
    private static final double SMMLV = 1750905;
    private static final double AUXILIO_TRANSPORTE_MENSUAL = 249095;

    // Lista en memoria para almacenar los empleados registrados
    private List<Empleado> empleadosRegistrados = new ArrayList<>();

    // -----------------------------------------------------------------------
    // Validacion de duplicados
    // -----------------------------------------------------------------------

    /**
     * Revisa TODOS los campos unicos (ID, correo, telefono) de una sola vez
     * y retorna una lista con los nombres de los campos que ya existen.
     *
     * A diferencia de guardarEmpleado(), este metodo NO para en el primer
     * duplicado: revisa los tres campos y acumula todos los errores.
     * Esto permite que el bean muestre cada error en su campo correspondiente.
     *
     * @param emp Empleado cuyos datos se van a verificar
     * @return Lista de Strings con los campos duplicados:
     *         "id", "correo", "telefono" segun corresponda.
     *         Lista vacia si no hay duplicados.
     */
    public List<String> validarDuplicados(Empleado emp) {
        List<String> errores = new ArrayList<>();

        // Verificar ID duplicado
        if (empleadosRegistrados.stream()
                .anyMatch(e -> e.getId_p() == emp.getId_p())) {
            errores.add("id");
        }

        // Verificar correo duplicado (sin distinguir mayusculas/minusculas)
        if (empleadosRegistrados.stream()
                .anyMatch(e -> e.getCorreo().equalsIgnoreCase(emp.getCorreo()))) {
            errores.add("correo");
        }

        // Verificar telefono duplicado
        if (empleadosRegistrados.stream()
                .anyMatch(e -> e.getTelefono().equals(emp.getTelefono()))) {
            errores.add("telefono");
        }

        return errores;
    }

    // -----------------------------------------------------------------------
    // Guardado de empleados
    // -----------------------------------------------------------------------

    /**
     * Guarda un nuevo empleado en la lista, previa validacion de datos unicos.
     * Usa validarDuplicados() internamente para mantener la logica centralizada.
     *
     * @param emp Objeto Empleado a guardar
     * @throws Exception si el ID, correo o telefono ya existen en la lista
     */
    public void guardarEmpleado(Empleado emp) throws Exception {
        // Reutiliza validarDuplicados para no duplicar logica
        List<String> errores = validarDuplicados(emp);

        if (!errores.isEmpty()) {
            // Lanza excepcion generica; el bean ya habra mostrado
            // los errores por campo antes de llamar a este metodo.
            throw new Exception("Existen datos duplicados en el registro.");
        }

        // Si pasa todas las validaciones, se agrega a la lista
        empleadosRegistrados.add(emp);
    }

    // -----------------------------------------------------------------------
    // Calculo de nomina
    // -----------------------------------------------------------------------

    /**
     * Calcula la nomina de un empleado aplicando las reglas de negocio:
     *   1. Salario proporcional a los dias trabajados (mes contable = 30 dias).
     *   2. Descuento del 4% por Salud sobre el salario devengado.
     *   3. Descuento del 4% por Pension sobre el salario devengado.
     *   4. Auxilio de transporte si salario basico es menor o igual a 2 SMMLV,
     *      calculado proporcional a los dias trabajados.
     *   5. Neto = salario devengado - salud - pension + auxilio transporte.
     *
     * @param emp Empleado al que se le calculara la nomina
     * @return Objeto Nomina con todos los conceptos detallados
     */
    public Nomina calcularNomina(Empleado emp) {
        Nomina nomina = new Nomina();
        nomina.setEmpleado(emp);

        // 1. Salario devengado proporcional a dias trabajados
        double salarioCalculado = (emp.getSalarioBasico() / 30) * emp.getDiasTrabajados();
        nomina.setSalarioCalculado(salarioCalculado);

        // 2. Descuentos de seguridad social (4% cada uno sobre salario devengado)
        double descSalud   = salarioCalculado * 0.04;
        double descPension = salarioCalculado * 0.04;
        nomina.setDescuentoSalud(descSalud);
        nomina.setDescuentoPension(descPension);

        // 3. Auxilio de transporte (solo si salario basico <= 2 SMMLV)
        double auxTransporte = 0;
        if (emp.getSalarioBasico() <= (SMMLV * 2)) {
            auxTransporte = (AUXILIO_TRANSPORTE_MENSUAL / 30) * emp.getDiasTrabajados();
        }
        nomina.setAuxilioTransporte(auxTransporte);

        // 4. Neto a pagar
        double neto = salarioCalculado - descSalud - descPension + auxTransporte;
        nomina.setNetoPagar(neto);

        return nomina;
    }

    // -----------------------------------------------------------------------
    // Consultas
    // -----------------------------------------------------------------------

    /**
     * Obtiene la lista completa de empleados registrados en el sistema.
     *
     * @return Lista de objetos Empleado (puede estar vacia al inicio)
     */
    public List<Empleado> getEmpleadosRegistrados() {
        return empleadosRegistrados;
    }
}