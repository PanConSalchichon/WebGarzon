// Autor: Kevin Garzon
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package cdt.servicio;

import cdt.modelo.CDTDto;

/**
 * Capa de servicio / logica de negocio del CDT.
 * Aplica la formula de interes simple con base 360 dias
 * y descuenta el impuesto de retencion en la fuente (4%).
 *
 * Formula:
 *   ganancia      = inversion * (tasa_EA * (plazo / 360))
 *   valorFuturo   = inversion + ganancia
 *   impuesto      = ganancia * 0.04
 *   gananciaNeta  = ganancia - impuesto
 *   VFNeto        = valorFuturo - impuesto
 */
public class CDTServicio {

    private static final double BASE_ANIO = 360.0;
    private static final double TASA_IMPUESTO = 0.04;

    /**
     * Recibe un CDTDto con los datos de entrada y llena los campos de resultado.
     * La tasa de interes llega como porcentaje (ej: 12.5) y se convierte a decimal.
     */
    public void calcular(CDTDto dto) {
        double tasaDecimal = dto.getInteres() / 100.0;
        double ganancia = dto.getInversion() * (tasaDecimal * (dto.getPlazo() / BASE_ANIO));
        double valorFuturo = dto.getInversion() + ganancia;
        double impuesto = ganancia * TASA_IMPUESTO;
        double gananciaNeta = ganancia - impuesto;
        double valorFuturoNeto = valorFuturo - impuesto;

        dto.setGanancia(ganancia);
        dto.setValorFuturo(valorFuturo);
        dto.setImpuesto(impuesto);
        dto.setGananciaNeta(gananciaNeta);
        dto.setValorFuturoNeto(valorFuturoNeto);
    }

    /**
     * Valida que los datos de entrada sean coherentes antes de calcular.
     * Retorna null si todo esta bien, o un mensaje de error.
     */
    public String validar(CDTDto dto) {
        if (dto.getInversion() < 1_000_000) {
            return "La inversion minima es de $1.000.000 COP.";
        }
        if (dto.getInteres() <= 0 || dto.getInteres() > 30) {
            return "La tasa de interes debe estar entre 0.01% y 30% E.A.";
        }
        if (dto.getPlazo() < 30 || dto.getPlazo() > 1800) {
            return "El plazo debe estar entre 30 y 1800 dias.";
        }
        return null;
    }
}