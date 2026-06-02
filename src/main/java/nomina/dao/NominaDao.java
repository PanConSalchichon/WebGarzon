package nomina.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import nomina.model.Nomina;
import nomina.util.JPAUtil;

/**
 * DAO para operaciones sobre la entidad Nomina.
 * El CRUD completo aqui es: guardar, listar todas,
 * listar por empleado y eliminar.
 * No se editan nominas existentes — cada calculo genera
 * un registro nuevo en el historial.
 */
@ApplicationScoped
public class NominaDAO {

    // -----------------------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------------------

    /**
     * Persiste una nomina calculada en la base de datos.
     * El empleado asociado debe existir previamente.
     */
    public void guardar(Nomina nomina) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(nomina);
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
     * Retorna todas las nominas con su empleado y departamento cargados.
     * Doble JOIN FETCH para evitar LazyInitializationException al acceder
     * a nomina.getEmpleado().getDepartamento() fuera del EntityManager.
     */
    public List<Nomina> listarTodas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Nomina> query = em.createQuery(
                "SELECT n FROM Nomina n " +
                "JOIN FETCH n.empleado e " +
                "JOIN FETCH e.departamento " +
                "ORDER BY n.fecha DESC",
                Nomina.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Retorna el historial de nominas de un empleado especifico,
     * ordenado de mas reciente a mas antiguo.
     *
     * @param empleadoId Id del empleado
     * @return Lista de nominas del empleado
     */
    public List<Nomina> listarPorEmpleado(Long empleadoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Nomina> query = em.createQuery(
                "SELECT n FROM Nomina n " +
                "JOIN FETCH n.empleado e " +
                "WHERE e.id = :empId " +
                "ORDER BY n.fecha DESC",
                Nomina.class
            );
            query.setParameter("empId", empleadoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // -----------------------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------------------

    /**
     * Elimina una nomina por su id.
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Nomina nomina = em.find(Nomina.class, id);
            if (nomina != null) {
                em.remove(nomina);
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

    /**
     * Elimina todas las nominas de un empleado.
     * Se llama antes de eliminar el empleado para respetar
     * la integridad referencial de la base de datos.
     *
     * @param empleadoId Id del empleado
     */
    public void eliminarPorEmpleado(Long empleadoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery(
                "DELETE FROM Nomina n WHERE n.empleado.id = :empId"
            ).setParameter("empId", empleadoId)
             .executeUpdate();
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
}