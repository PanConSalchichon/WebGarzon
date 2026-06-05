package nomina.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa un departamento de la empresa.
 * Un departamento puede tener muchos empleados asignados.
 *
 * @Entity  indica a JPA que esta clase se mapea a una tabla.
 * @Table   especifica el nombre exacto de la tabla en MySQL.
 */
@Entity
@Table(name = "departamento")
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * @Id             marca este campo como clave primaria.
     * @GeneratedValue indica que MySQL genera el valor automaticamente (AUTO_INCREMENT).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column name="nombre" mapea al campo "nombre" en la tabla.
     * nullable=false → NOT NULL en la base de datos.
     * length=100     → VARCHAR(100).
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    /**
     * Relacion uno a muchos con Empleado.
     * mappedBy="departamento" indica que Empleado es el dueno de la relacion
     * (Empleado tiene la clave foranea departamento_id).
     * cascade=PERSIST,MERGE → las operaciones de guardar y actualizar
     * se propagan a los empleados del departamento.
     * fetch=LAZY → los empleados NO se cargan de la BD hasta que se necesiten.
     */
    @OneToMany(mappedBy = "departamento",
               cascade = {jakarta.persistence.CascadeType.PERSIST,
                          jakarta.persistence.CascadeType.MERGE},
               fetch = jakarta.persistence.FetchType.LAZY)
    private List<Empleado> empleados = new ArrayList<>();

    // Constructor por defecto requerido por JPA
    public Departamento() {}

    public Departamento(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Empleado> getEmpleados() { return empleados; }
    public void setEmpleados(List<Empleado> empleados) { this.empleados = empleados; }

    @Override
    public String toString() { return nombre; }
}