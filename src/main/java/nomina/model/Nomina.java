package nomina.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Entidad JPA que representa un recibo de nomina.
 * Cada vez que se calcula la nomina de un empleado
 * se genera y persiste un registro en esta tabla.
 */
@Entity
@Table(name = "nomina")
public class Nomina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacion muchos a uno con Empleado.
     * Muchas nominas pueden pertenecer a un empleado (historial).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(name = "salario_calculado", nullable = false)
    private double salarioCalculado;

    @Column(name = "auxilio_transporte")
    private double auxilioTransporte;

    @Column(name = "descuento_salud", nullable = false)
    private double descuentoSalud;

    @Column(name = "descuento_pension", nullable = false)
    private double descuentoPension;

    @Column(name = "neto_pagar", nullable = false)
    private double netoPagar;

    /**
     * Fecha en que se genero la nomina.
     * @Temporal(DATE) almacena solo la fecha sin hora en MySQL.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    public Nomina() {
        this.fecha = new Date(); // fecha actual al crear
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}