package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Empleado;
import org.hibernate.Session;

import java.util.List;

public class RutinaDAO {

    public void insert(Rutina rutina) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(rutina);
        session.getTransaction().commit();
        session.close();
    }

    public Rutina findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Rutina rutina = session.get(Rutina.class, id);
        session.getTransaction().commit();
        session.close();
        return rutina;
    }

    public List<Rutina> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        // Usar JOIN FETCH para cargar los objetos relacionados en la misma consulta
        List<Rutina> rutinas = session.createQuery(
                "SELECT DISTINCT r FROM Rutina r LEFT JOIN FETCH r.cliente LEFT JOIN FETCH r.empleado",
                Rutina.class
        ).list();

        session.getTransaction().commit();
        session.close();
        return rutinas;
    }

    public void update(Rutina rutina) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(rutina);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Integer rutinaId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Rutina rutina = session.get(Rutina.class, rutinaId);
        if (rutina != null) {
            session.delete(rutina);
        }
        session.getTransaction().commit();
        session.close();
    }

    public List<Rutina> findByCliente(Cliente cliente) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        // Corregir nombre de propiedad: idCliente -> cliente
        List<Rutina> rutinas = session.createQuery(
                        "FROM Rutina r WHERE r.cliente = :cliente",
                        Rutina.class
                )
                .setParameter("cliente", cliente)
                .list();

        session.getTransaction().commit();
        session.close();
        return rutinas;
    }

    public List<Rutina> findByEmpleado(Empleado empleado) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        // Corregir nombre de propiedad: idEmpleado -> empleado
        List<Rutina> rutinas = session.createQuery(
                        "FROM Rutina r WHERE r.empleado = :empleado",
                        Rutina.class
                )
                .setParameter("empleado", empleado)
                .list();

        session.getTransaction().commit();
        session.close();
        return rutinas;
    }

    public List<Rutina> findByNombre(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Rutina> rutinas = session.createQuery("from Rutina r where r.nombre like :nombre", Rutina.class)
                .setParameter("nombre", "%" + nombre + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return rutinas;
    }

    public List<Rutina> findByTipo(String tipo) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Rutina> rutinas = session.createQuery("from Rutina r where r.tipo like :tipo", Rutina.class)
                .setParameter("tipo", "%" + tipo + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return rutinas;
    }

    // Método para obtener rutinas por cliente, aunque no está implementado en RutinaDAO
    public List<Rutina> findByClienteId(Integer clienteId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Rutina> rutinas = session.createQuery("from Rutina r where r.cliente.id = :clienteId", Rutina.class)
                .setParameter("clienteId", clienteId)
                .list();
        session.getTransaction().commit();
        session.close();
        return rutinas;
    }
}