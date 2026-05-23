package divisas.service;

import divisas.model.Conversion;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio de conversion de divisas.
 * Lee la tasa de cambio desde divisas.properties y realiza el calculo.
 * Alcance ApplicationScoped: una sola instancia para toda la aplicacion.
 */
@ApplicationScoped
public class ConversionService {

    private static final Logger LOG = Logger.getLogger(ConversionService.class.getName());

    private static final String PROP_FILE   = "/divisas.properties";
    private static final String KEY_TASA    = "tasa.usd.a.cop";
    private static final String DIVISA_COP  = "COP";
    private static final String DIVISA_USD  = "USD";

    /** Carga la tasa de cambio desde el archivo .properties */
    private double cargarTasa() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream(PROP_FILE)) {
            if (is == null) {
                LOG.warning("No se encontro divisas.properties. Usando tasa por defecto.");
                return 4250.50;
            }
            props.load(is);
            return Double.parseDouble(props.getProperty(KEY_TASA, "4250.50"));
        } catch (IOException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "Error al leer divisas.properties", e);
            return 4250.50;
        }
    }

    /**
     * Realiza la conversion segun las divisas indicadas.
     *
     * @param monto        Monto a convertir
     * @param divisaOrigen "COP" o "USD"
     * @return Objeto Conversion con el resultado y la tasa aplicada
     */
    public Conversion convertir(double monto, String divisaOrigen) {
        double tasa = cargarTasa();
        double resultado;
        String divisaDestino;

        if (DIVISA_COP.equals(divisaOrigen)) {
            // COP -> USD: dividimos entre la tasa
            resultado     = monto / tasa;
            divisaDestino = DIVISA_USD;
        } else {
            // USD -> COP: multiplicamos por la tasa
            resultado     = monto * tasa;
            divisaDestino = DIVISA_COP;
        }

        return new Conversion(monto, resultado, divisaOrigen, divisaDestino, tasa);
    }

    /** Devuelve la tasa actual configurada en el archivo .properties */
    public double getTasaActual() {
        return cargarTasa();
    }
}