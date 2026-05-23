// Autor: Kevin Garzon
// Asignatura: Programacion Avanzada - 301
// Universidad Distrital Francisco Jose de Caldas - 2026
package asp.modelo;

import java.util.ArrayList;

public class ProgAcadDAO {

    public static ArrayList<ProgAcad> lista_P = new ArrayList<ProgAcad>();

    public static void cargaDatos() {
        if (lista_P.isEmpty()) {
            lista_P.add(new ProgAcad(10, "Ingenieria de Sistemas"));
            lista_P.add(new ProgAcad(20, "Ingenieria Industrial"));
            lista_P.add(new ProgAcad(30, "Matematica"));
            lista_P.add(new ProgAcad(40, "Administracion de Empresas"));
            lista_P.add(new ProgAcad(50, "Derecho"));
            lista_P.add(new ProgAcad(60, "Ingenieria Telematica"));
        }
    }
}