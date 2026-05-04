// Autor: Kevin Garzon
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package asp.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Aspirante extends Persona implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LocalDate fecha_reg;
	private ProgAcad pro_acad;

	public Aspirante() {
		super();
		pro_acad = new ProgAcad();
	}

	public Aspirante(long id_p, String nombres, String apellidos, String telefono, String correo, LocalDate fecha_reg,
			ProgAcad pro_acad) {
		super(id_p, nombres, apellidos, telefono, correo);
		this.fecha_reg = fecha_reg;
		this.pro_acad = pro_acad;
	}

	public LocalDate getFecha_reg() {
		return fecha_reg;
	}

	public void setFecha_reg(LocalDate fecha_reg) {
		this.fecha_reg = fecha_reg;
	}

	public ProgAcad getPro_acad() {
		return pro_acad;
	}

	public void setPro_acad(ProgAcad pro_acad) {
		this.pro_acad = pro_acad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(fecha_reg, pro_acad);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aspirante other = (Aspirante) obj;
		return Objects.equals(fecha_reg, other.fecha_reg) && Objects.equals(pro_acad, other.pro_acad);
	}

	@Override
	public String toString() {
		return "Aspirante [nombre=" + getNombres() + "fecha_reg=" + fecha_reg + ", pro_acad=" + pro_acad + "]";
	}

}
