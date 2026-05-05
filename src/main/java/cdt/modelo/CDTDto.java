// Autor: Kevin Garzon
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package cdt.modelo;

import java.io.Serializable;

/**
 * DTO (Data Transfer Object) que representa los datos de un CDT.
 * Contiene los datos de entrada del usuario y los resultados del calculo.
 */
public class CDTDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // Datos de entrada
    private double inversion;
    private double interes;   // tasa E.A. en decimal, ej: 0.12 para 12%
    private int plazo;        // en dias

    // Resultados calculados
    private double ganancia;
    private double valorFuturo;
    private double impuesto;
    private double gananciaNetа;
    private double valorFuturoNeto;

    public CDTDto() {}

    public CDTDto(double inversion, double interes, int plazo) {
        this.inversion = inversion;
        this.interes = interes;
        this.plazo = plazo;
    }

    // --- Getters y Setters de entrada ---

    public double getInversion() { return inversion; }
    public void setInversion(double inversion) { this.inversion = inversion; }

    public double getInteres() { return interes; }
    public void setInteres(double interes) { this.interes = interes; }

    public int getPlazo() { return plazo; }
    public void setPlazo(int plazo) { this.plazo = plazo; }

    // --- Getters y Setters de resultados ---

    public double getGanancia() { return ganancia; }
    public void setGanancia(double ganancia) { this.ganancia = ganancia; }

    public double getValorFuturo() { return valorFuturo; }
    public void setValorFuturo(double valorFuturo) { this.valorFuturo = valorFuturo; }

    public double getImpuesto() { return impuesto; }
    public void setImpuesto(double impuesto) { this.impuesto = impuesto; }

    public double getGananciaNeta() { return gananciaNetа; }
    public void setGananciaNeta(double gananciaNeta) { this.gananciaNetа = gananciaNeta; }

    public double getValorFuturoNeto() { return valorFuturoNeto; }
    public void setValorFuturoNeto(double valorFuturoNeto) { this.valorFuturoNeto = valorFuturoNeto; }

    @Override
    public String toString() {
        return "CDTDto [inversion=" + inversion + ", interes=" + interes
                + ", plazo=" + plazo + ", ganancia=" + ganancia + "]";
    }
}