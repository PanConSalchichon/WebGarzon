package nomina.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Listener del ciclo de vida de la aplicacion.
 * Cierra el EntityManagerFactory cuando Tomcat se apaga,
 * liberando las conexiones a la base de datos correctamente.
 */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}