package nomina.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import nomina.model.Empleado;
import nomina.util.JPAUtil;

/**
 * DAO para operaciones CRUD sobre la entidad Empleado.
 */
@ApplicationScoped
public class EmpleadoDAO {

    // -----------------------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------------------

    /**
     * Persiste un nuevo empleado en la base de datos.
     * Como Empleado tiene relacion ManyToOne con Departamento,
     * el departamento debe existir previamente en BD.
     */
    public void guardar(Empleado empleado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(empleado);
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
     * Busca un empleado por su clave primaria.
     *
     * @param id Identificador del empleado
     * @return Empleado encontrado o null si no existe
     */
    public Empleado buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Retorna todos los empleados con su departamento cargado (JOIN FETCH).
     * Sin JOIN FETCH, acceder a empleado.getDepartamento() fuera del
     * EntityManager lanzaria LazyInitializationException porque
     * el departamento se carga de forma LAZY.
     */
    public List<Empleado> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                "SELECT e FROM Empleado e JOIN FETCH e.departamento " +
                "ORDER BY e.apellidos ASC",
                Empleado.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Lista los empleados de un departamento especifico.
     *
     * @param departamentoId Id del departamento a filtrar
     * @return Lista de empleados del departamento
     */
    public List<Empleado> listarPorDepartamento(Long departamentoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Empleado> query = em.createQuery(
                "SELECT e FROM Empleado e JOIN FETCH e.departamento d " +
                "WHERE d.id = :depId ORDER BY e.apellidos ASC",
                Empleado.class
            );
            query.setParameter("depId", departamentoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // -----------------------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------------------

    /**
     * Actualiza los datos de un empleado existente.
     */
    public void actualizar(Empleado empleado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(empleado);
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
     * Elimina un empleado por su id.
     * Las nominas asociadas se eliminan en cascada si el
     * CascadeType.REMOVE esta configurado; si no, se eliminan
     * manualmente desde NominaDAO antes de llamar este metodo.
     */
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Empleado empleado = em.find(Empleado.class, id);
            if (empleado != null) {
                em.remove(empleado);
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
    // CONSULTAS DE VALIDACION
    // -----------------------------------------------------------------------

    public boolean existeCc(long cc, Long idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Empleado e " +
                "WHERE e.cc = :cc AND e.id <> :id",
                Long.class
            );
            query.setParameter("cc", cc);
            query.setParameter("id", idExcluir != null ? idExcluir : -1L);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
    
    /**
     * Verifica si ya existe un empleado con ese correo.
     * Se usa antes de guardar para evitar duplicados.
     *
     * @param correo Correo a verificar
     * @param idExcluir Id a excluir de la busqueda (util al editar)
     * @return true si ya existe otro empleado con ese correo
     */
    public boolean existeCorreo(String correo, Long idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Empleado e " +
                "WHERE LOWER(e.correo) = LOWER(:correo) AND e.id <> :id",
                Long.class
            );
            query.setParameter("correo", correo);
            query.setParameter("id", idExcluir != null ? idExcluir : -1L);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si ya existe un empleado con ese telefono.
     *
     * @param telefono  Telefono a verificar
     * @param idExcluir Id a excluir de la busqueda (util al editar)
     * @return true si ya existe otro empleado con ese telefono
     */
    public boolean existeTelefono(String telefono, Long idExcluir) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Empleado e " +
                "WHERE e.telefono = :telefono AND e.id <> :id",
                Long.class
            );
            query.setParameter("telefono", telefono);
            query.setParameter("id", idExcluir != null ? idExcluir : -1L);
            return query.getSingleResult() > 0;
        } finally {
            em.close();
        }
    }
}