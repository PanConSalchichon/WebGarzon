package nomina.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un empleado del sistema de nomina.
 * Hereda los campos personales de Persona via @MappedSuperclass.
 */
@Entity
@Table(name = "empleado")
public class Empleado extends Persona {

    /**
     * Clave primaria generada automaticamente por MySQL (AUTO_INCREMENT).
     * Reemplaza al id_p anterior que era ingresado manualmente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "salario_basico", nullable = false)
    private double salarioBasico;

    @Column(name = "dias_trabajados", nullable = false)
    private int diasTrabajados;

    /**
     * Relacion muchos a uno con Departamento.
     * Muchos empleados pueden pertenecer a un departamento.
     *
     * @JoinColumn name="departamento_id" → nombre de la clave foranea en la tabla empleado.
     * fetch=LAZY → el departamento NO se carga de BD hasta que se acceda a el.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    /**
     * Historial de nominas del empleado.
     * Un empleado puede tener multiples nominas (una por periodo).
     */
    @OneToMany(mappedBy = "empleado",
               cascade = {jakarta.persistence.CascadeType.PERSIST,
                          jakarta.persistence.CascadeType.MERGE},
               fetch = FetchType.LAZY)
    private List<Nomina> nominas = new ArrayList<>();

    public Empleado() {}

    public Empleado(String nombres, String apellidos, String telefono,
                    String correo, double salarioBasico, int diasTrabajados,
                    Departamento departamento) {
        super(nombres, apellidos, telefono, correo);
        this.salarioBasico = salarioBasico;
        this.diasTrabajados = diasTrabajados;
        this.departamento = departamento;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getSalarioBasico() { return salarioBasico; }
    public void setSalarioBasico(double salarioBasico) { this.salarioBasico = salarioBasico; }

    public int getDiasTrabajados() { return diasTrabajados; }
    public void setDiasTrabajados(int diasTrabajados) { this.diasTrabajados = diasTrabajados; }

    public Departamento getDepartamento() { return departamento; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }

    public List<Nomina> getNominas() { return nominas; }
    public void setNominas(List<Nomina> nominas) { this.nominas = nominas; }
}