package nomina.service;

import java.util.ArrayList;
import java.util.List;
import nomina.model.Empleado;
import nomina.model.Nomina;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Clase de servicio que encapsula la lógica de negocio relacionada con la 
 * gestión de empleados y el cálculo de la nómina.
 * 
 * @ApplicationScoped garantiza que la lista de empleados se mantenga en memoria
 * mientras la aplicación esté en ejecución (simula una base de datos).
 * 
 */
@ApplicationScoped
public class NominaService {

    // Constantes legales colombianas (Año 2024)
    private static final double SMMLV = 1300000;
    private static final double AUXILIO_TRANSPORTE_MENSUAL = 162000;

    // Lista en memoria para almacenar los empleados registrados
    private List<Empleado> empleadosRegistrados = new ArrayList<>();

    /**
     * Guarda un nuevo empleado en la lista, previa validación de datos únicos.
     * 
     * @param emp Objeto Empleado a guardar
     * @throws Exception Si el ID, correo o teléfono ya existen en la lista
     */
    public void guardarEmpleado(Empleado emp) throws Exception {
        // Validar ID duplicado usando Streams de Java
        if (empleadosRegistrados.stream().anyMatch(e -> e.getId_p() == emp.getId_p())) {
            throw new Exception("La identificación " + emp.getId_p() + " ya está registrada.");
        }
        
        // Validar Correo duplicado (ignorando mayúsculas/minúsculas)
        if (empleadosRegistrados.stream().anyMatch(e -> e.getCorreo().equalsIgnoreCase(emp.getCorreo()))) {
            throw new Exception("El correo " + emp.getCorreo() + " ya está registrado.");
        }
        
        // Validar Teléfono duplicado
        if (empleadosRegistrados.stream().anyMatch(e -> e.getTelefono().equals(emp.getTelefono()))) {
            throw new Exception("El teléfono " + emp.getTelefono() + " ya está registrado.");
        }

        // Si pasa todas las validaciones, se agrega a la lista
        empleadosRegistrados.add(emp);
    }

    /**
     * Calcula la nómina de un empleado aplicando las reglas de negocio establecidas.
     * 1. Salario proporcional a los días trabajados.
     * 2. Descuento del 4% para Salud y Pensión.
     * 3. Auxilio de transporte si gana hasta 2 SMMLV.
     * 4. Neto a pagar = Salario - Salud - Pensión + Auxilio.
     * 
     * @param emp Empleado al que se le calculará la nómina
     * @return Objeto Nomina con los resultados detallados
     */
    public Nomina calcularNomina(Empleado emp) {
        Nomina nomina = new Nomina();
        nomina.setEmpleado(emp);

        // 1. Cálculo del salario devengado (Contablemente un mes = 30 días)
        double salarioCalculado = (emp.getSalarioBasico() / 30) * emp.getDiasTrabajados();
        nomina.setSalarioCalculado(salarioCalculado);

        // 2. Descuentos de Seguridad Social (4% c/u sobre el salario devengado)
        double descSalud = salarioCalculado * 0.04;
        double descPension = salarioCalculado * 0.04;
        nomina.setDescuentoSalud(descSalud);
        nomina.setDescuentoPension(descPension);

        // 3. Auxilio de transporte (Solo si el salario básico es <= 2 SMMLV, proporcional a días)
        double auxTransporte = 0;
        if (emp.getSalarioBasico() <= (SMMLV * 2)) {
            auxTransporte = (AUXILIO_TRANSPORTE_MENSUAL / 30) * emp.getDiasTrabajados();
        }
        nomina.setAuxilioTransporte(auxTransporte);

        // 4. Cálculo del Neto a Pagar
        double neto = salarioCalculado - descSalud - descPension + auxTransporte;
        nomina.setNetoPagar(neto);

        return nomina;
    }

    /**
     * Obtiene la lista de todos los empleados registrados en el sistema.
     * @return Lista de objetos Empleado
     */
    public List<Empleado> getEmpleadosRegistrados() {
        return empleadosRegistrados;
    }
}