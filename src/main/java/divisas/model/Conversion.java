package divisas.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modelo que representa una conversion de divisa realizada.
 * Almacena el monto origen, destino, tasa y fecha/hora.
 */
public class Conversion implements Serializable {

    private static final long serialVersionUID = 1L;

    private double montoOrigen;
    private double montoDestino;
    private String divisaOrigen;
    private String divisaDestino;
    private double tasaAplicada;
    private LocalDateTime fechaHora;

    public Conversion() {
        this.fechaHora = LocalDateTime.now();
    }

    public Conversion(double montoOrigen, double montoDestino,
                      String divisaOrigen, String divisaDestino,
                      double tasaAplicada) {
        this.montoOrigen  = montoOrigen;
        this.montoDestino = montoDestino;
        this.divisaOrigen  = divisaOrigen;
        this.divisaDestino = divisaDestino;
        this.tasaAplicada  = tasaAplicada;
        this.fechaHora = LocalDateTime.now();
    }

    /** Representacion legible de la conversion para mostrar en historial */
    public String getDescripcion() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] %.2f %s → %.2f %s",
                fechaHora.format(fmt),
                montoOrigen, divisaOrigen,
                montoDestino, divisaDestino);
    }

    // ---- Getters y Setters ----

    public double getMontoOrigen()         { return montoOrigen; }
    public void setMontoOrigen(double v)   { this.montoOrigen = v; }

    public double getMontoDestino()        { return montoDestino; }
    public void setMontoDestino(double v)  { this.montoDestino = v; }

    public String getDivisaOrigen()        { return divisaOrigen; }
    public void setDivisaOrigen(String v)  { this.divisaOrigen = v; }

    public String getDivisaDestino()       { return divisaDestino; }
    public void setDivisaDestino(String v) { this.divisaDestino = v; }

    public double getTasaAplicada()        { return tasaAplicada; }
    public void setTasaAplicada(double v)  { this.tasaAplicada = v; }

    public LocalDateTime getFechaHora()    { return fechaHora; }
    public void setFechaHora(LocalDateTime v) { this.fechaHora = v; }
}