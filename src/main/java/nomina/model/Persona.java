// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package nomina.model;

import java.io.Serializable;
import java.util.Objects;

public class Persona implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id_p;
	private String nombres;
	private String apellidos;
	private String telefono;
	private String correo;
	
	public Persona() {

	}

	public Persona(long id_p, String nombres, String apellidos, String telefono, String correo) {
		this.id_p = id_p;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.correo = correo;
	}

	public long getId_p() {
		return id_p;
	}

	public void setId_p(long id_p) {
		this.id_p = id_p;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apellidos, correo, id_p, nombres, telefono);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Persona other = (Persona) obj;
		return Objects.equals(apellidos, other.apellidos) && Objects.equals(correo, other.correo) && id_p == other.id_p
				&& Objects.equals(nombres, other.nombres) && Objects.equals(telefono, other.telefono);
	}
	
	
	
	
}
