package nomina.model;

/**
 * Clase que representa a un Empleado dentro del sistema de nómina.
 * Hereda de la clase Persona, agregando atributos específicos laborales
 * como el salario básico y los días trabajados.
 * 
 */
public class Empleado extends Persona {
    
    private double salarioBasico;
    private int diasTrabajados;

    /**
     * Constructor por defecto requerido por los frameworks (JSF/JPA).
     */
    public Empleado() {
    }

    /**
     * Constructor parametrizado para crear un empleado con todos sus datos.
     * @param id_p Identificación del empleado
     * @param nombres Nombres del empleado
     * @param apellidos Apellidos del empleado
     * @param telefono Teléfono de contacto
     * @param correo Correo electrónico
     * @param salarioBasico Salario básico mensual pactado
     * @param diasTrabajados Días laborados en el periodo a liquidar
     */
    public Empleado(long id_p, String nombres, String apellidos, String telefono, String correo, double salarioBasico, int diasTrabajados) {
        super(id_p, nombres, apellidos, telefono, correo);
        this.salarioBasico = salarioBasico;
        this.diasTrabajados = diasTrabajados;
    }

    // Getters y Setters
    
    public double getSalarioBasico() {
        return salarioBasico;
    }

    public void setSalarioBasico(double salarioBasico) {
        this.salarioBasico = salarioBasico;
    }

    public int getDiasTrabajados() {
        return diasTrabajados;
    }

    public void setDiasTrabajados(int diasTrabajados) {
        this.diasTrabajados = diasTrabajados;
    }
}