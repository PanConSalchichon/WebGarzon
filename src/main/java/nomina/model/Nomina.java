package nomina.model;

/**
 * Clase que representa el recibo de nómina o resultado de la liquidación
 * de un empleado en un periodo específico.
 * Aplica el principio de Responsabilidad Única: solo almacena datos, no calcula.
 * 
 */
public class Nomina {
    
    private Empleado empleado;
    private double salarioCalculado; // Salario proporcional a los días trabajados
    private double auxilioTransporte;
    private double descuentoSalud;
    private double descuentoPension;
    private double netoPagar;

    // Getters y Setters
    
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public double getSalarioCalculado() { return salarioCalculado; }
    public void setSalarioCalculado(double salarioCalculado) { this.salarioCalculado = salarioCalculado; }

    public double getAuxilioTransporte() { return auxilioTransporte; }
    public void setAuxilioTransporte(double auxilioTransporte) { this.auxilioTransporte = auxilioTransporte; }

    public double getDescuentoSalud() { return descuentoSalud; }
    public void setDescuentoSalud(double descuentoSalud) { this.descuentoSalud = descuentoSalud; }

    public double getDescuentoPension() { return descuentoPension; }
    public void setDescuentoPension(double descuentoPension) { this.descuentoPension = descuentoPension; }

    public double getNetoPagar() { return netoPagar; }
    public void setNetoPagar(double netoPagar) { this.netoPagar = netoPagar; }
}