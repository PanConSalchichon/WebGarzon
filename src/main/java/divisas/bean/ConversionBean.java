package divisas.bean;

import divisas.model.Conversion;
import divisas.service.ConversionService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Backing Bean principal para la vista de conversion de divisas.
 *
 * @Named       - hace el bean accesible desde JSF/EL como #{conversionBean}
 * @SessionScoped - mantiene el historial durante la sesion del usuario
 */
@Named("conversionBean")
@SessionScoped
public class ConversionBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final int MAX_HISTORIAL = 5;

    // ---- Estado del formulario ----
    private double monto;
    private String divisaOrigen = "COP";   // valor por defecto
    private Conversion ultimaConversion;

    // ---- Historial de la sesion ----
    private final LinkedList<Conversion> historial = new LinkedList<>();

    // ---- Inyeccion de dependencias CDI ----
    @Inject
    private ConversionService conversionService;

    // ============================================================
    // Metodo de accion invocado por el boton "Convertir" (AJAX)
    // ============================================================
    public void convertir() {
        if (monto <= 0) return;

        ultimaConversion = conversionService.convertir(monto, divisaOrigen);

        // Agrega al historial; mantiene un maximo de 5 entradas
        historial.addFirst(ultimaConversion);
        if (historial.size() > MAX_HISTORIAL) {
            historial.removeLast();
        }
    }

    /** Opciones para el SelectOneMenu de divisa de origen */
    public List<String> getDivisasDisponibles() {
        List<String> opciones = new ArrayList<>();
        opciones.add("COP");
        opciones.add("USD");
        return opciones;
    }

    /** Etiqueta descriptiva de la divisa de destino para mostrar en la UI */
    public String getDivisaDestinoLabel() {
        return "COP".equals(divisaOrigen) ? "USD (Dolares)" : "COP (Pesos)";
    }

    /** Tasa de cambio actual leida desde .properties */
    public double getTasaActual() {
        return conversionService.getTasaActual();
    }

    // ---- Getters y Setters ----

    public double getMonto()              { return monto; }
    public void setMonto(double monto)    { this.monto = monto; }

    public String getDivisaOrigen()       { return divisaOrigen; }
    public void setDivisaOrigen(String d) { this.divisaOrigen = d; }

    public Conversion getUltimaConversion()             { return ultimaConversion; }
    public void setUltimaConversion(Conversion conv)    { this.ultimaConversion = conv; }

    public List<Conversion> getHistorial()              { return historial; }
}