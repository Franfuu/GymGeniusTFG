package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Clase;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Sala;
import org.hibernate.Session;

import java.util.List;

public class ClaseDAO {

    public void insert(Clase clase) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(clase);
        session.getTransaction().commit();
        session.close();
    }

    public Clase findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Clase clase = session.get(Clase.class, id);
        session.getTransaction().commit();
        session.close();
        return clase;
    }

    public List<Clase> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        // Usar join fetch para cargar las relaciones con anticipación
        List<Clase> clases = session.createQuery(
                "SELECT DISTINCT c FROM Clase c LEFT JOIN FETCH c.idEmpleado LEFT JOIN FETCH c.sala",
                Clase.class).list();

        session.getTransaction().commit();
        session.close();
        return clases;
    }

    public void update(Clase clase) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        // Usar SQL nativo para evitar problemas con el tipo SET
        String sql = "UPDATE clases SET nombre = :nombre, descripcion = :desc, " +
                "id_empleado = :emp, id_sala = :sala, " +
                "hora_inicio = :inicio, hora_fin = :fin, " +
                "dias_semana = :dias WHERE id_clase = :id";

        session.createNativeQuery(sql)
                .setParameter("nombre", clase.getNombre())
                .setParameter("desc", clase.getDescripcion())
                .setParameter("emp", clase.getIdEmpleado().getId()) // ID del empleado, no el nombre
                .setParameter("sala", clase.getSala().getId()) // ID de la sala, no el nombre
                .setParameter("inicio", clase.getHoraInicio())
                .setParameter("fin", clase.getHoraFin())
                .setParameter("dias", clase.getDiasSemana())
                .setParameter("id", clase.getId())
                .executeUpdate();

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Integer claseId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Clase clase = session.get(Clase.class, claseId);
        if (clase != null) {
            session.delete(clase);
        }
        session.getTransaction().commit();
        session.close();
    }

    public List<Clase> findByEmpleado(Empleado empleado) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Clase> clases = session.createQuery("from Clase c where c.idEmpleado = :empleado", Clase.class)
                .setParameter("empleado", empleado)
                .list();
        session.getTransaction().commit();
        session.close();
        return clases;
    }

    public List<Clase> findBySala(Sala sala) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Clase> clases = session.createQuery("from Clase c where c.idSala = :sala", Clase.class)
                .setParameter("sala", sala)
                .list();
        session.getTransaction().commit();
        session.close();
        return clases;
    }

    public List<Clase> findByNombre(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Clase> clases = session.createQuery("from Clase c where c.nombre like :nombre", Clase.class)
                .setParameter("nombre", "%" + nombre + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return clases;
    }
}