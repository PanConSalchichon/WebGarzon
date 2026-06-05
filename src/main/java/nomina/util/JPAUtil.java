package nomina.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria que gestiona el EntityManagerFactory como singleton.
 * El EMF es costoso de crear — se instancia una sola vez al arrancar
 * la aplicacion y se reutiliza durante toda la sesion.
 */
@ApplicationScoped
public class JPAUtil {

    private static final String PERSISTENCE_UNIT = "nominaPU";
    private static EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    /**
     * Retorna un EntityManager nuevo para cada operacion.
     * El llamador es responsable de cerrarlo despues de usarlo.
     */
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     * Cierra el EMF al apagar la aplicacion.
     * Se llama desde el ServletContextListener.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}