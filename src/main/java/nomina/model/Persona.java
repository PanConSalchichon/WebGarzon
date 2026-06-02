package nomina.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase base con datos personales compartidos por Empleado.
 *
 * @MappedSuperclass indica que esta clase NO tiene tabla propia en la BD,
 * pero sus campos se heredan a las entidades hijas (Empleado).
 * Es el equivalente JPA de una clase abstracta padre.
 */
@MappedSuperclass
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "correo", unique = true, length = 150)
    private String correo;

    public Persona() {}

    public Persona(String nombres, String apellidos, String telefono, String correo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
    }

    // Getters y Setters
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override
    public int hashCode() {
        return Objects.hash(apellidos, correo, nombres, telefono);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Persona other = (Persona) obj;
        return Objects.equals(apellidos, other.apellidos)
            && Objects.equals(correo, other.correo)
            && Objects.equals(nombres, other.nombres)
            && Objects.equals(telefono, other.telefono);
    }
}