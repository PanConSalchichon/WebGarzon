package nomina.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import nomina.model.Departamento;
import nomina.util.JPAUtil;

/**
 * DAO para operaciones CRUD sobre la entidad Departamento.
 *
 * @ApplicationScoped → CDI crea una sola instancia para toda la aplicacion.
 * Cada metodo abre su propio EntityManager, ejecuta la operacion y lo cierra.
 * Esto evita problemas de concurrencia con EntityManagers compartidos.
 */
@ApplicationScoped
public class DepartamentoDAO {

    // -----------------------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------------------

    /**
     * Persiste un nuevo departamento en la base de datos.
     * La transaccion se abre y cierra dentro del metodo.
     *
     * @param departamento Objeto a guardar
     */
    public void guardar(Departamento departamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(departamento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // -----------------------------------------------------------------------
    // READ
    // -----------------------------------------------------------------------

    /**
     * Busca un departamento por su clave primaria.
     *
     * @param id Identificador del departamento
     * @return Departamento encontrado o null si no existe
     */
    public Departamento buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Retorna todos los departamentos ordenados por nombre.
     * Se usa JPQL — lenguaje de consulta de JPA, similar a SQL
     * pero trabaja con clases y atributos, no con tablas y columnas.
     *
     * @return Lista de todos los departamentos
     */
    public List<Departamento> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Departamento> query = em.createQuery(
                "SELECT d FROM Departamento d ORDER BY d.nombre ASC",
                Departamento.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // -----------------------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------------------

    /**
     * Actualiza un departamento existente en la base de datos.
     * merge() sincroniza el estado del objeto con el registro en BD.
     *
     * @param departamento Objeto con los datos actualizados
     */
    public void actualizar(Departamento departamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(departamento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // -----------------------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------------------

    /**
     * Elimina un departamento por su id.
     * Primero lo busca con find() para obtener una instancia gestionada
     * por JPA, luego la elimina con remove().
     *
     * @param id Identificador del departamento a eliminar
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Departamento departamento = em.find(Departamento.class, id);
            if (departamento != null) {
                em.remove(departamento);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // -----------------------------------------------------------------------
    // CONSULTAS ADICIONALES
    // -----------------------------------------------------------------------

    /**
     * Verifica si ya existe un departamento con ese nombre.
     * Util para evitar duplicados antes de guardar.
     *
     * @param nombre Nombre a verificar
     * @return true si ya existe, false si no
     */
    public boolean existeNombre(String nombre) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Departamento d WHERE LOWER(d.nombre) = LOWER(:nombre)",
                Long.class
            );
            query.setParameter("nombre", nombre);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}