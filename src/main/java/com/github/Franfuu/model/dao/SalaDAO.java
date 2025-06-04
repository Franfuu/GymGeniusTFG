package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Sala;
import org.hibernate.Session;

import java.util.List;

public class SalaDAO {

    public void insert(Sala sala) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(sala);
        session.getTransaction().commit();
        session.close();
    }

    public Sala findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Sala sala = session.get(Sala.class, id);
        session.getTransaction().commit();
        session.close();
        return sala;
    }

    public List<Sala> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Sala> salas = session.createQuery("from Sala", Sala.class).list();
        session.getTransaction().commit();
        session.close();
        return salas;
    }

    public void update(Sala sala) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(sala);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Sala sala) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.delete(sala);
        session.getTransaction().commit();
        session.close();
    }

    public List<Sala> findByNombre(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Sala> salas = session.createQuery("from Sala s where s.nombre like :nombre", Sala.class)
                .setParameter("nombre", "%" + nombre + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return salas;
    }

    public List<Sala> findByCapacidadMaxima(int capacidadMaxima) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Sala> salas = session.createQuery("from Sala s where s.capacidad_maxima >= :capacidadMaxima", Sala.class)
                .setParameter("capacidadMaxima", capacidadMaxima)
                .list();
        session.getTransaction().commit();
        session.close();
        return salas;
    }

    public List<Sala> findByUbicacion(String ubicacion) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Sala> salas = session.createQuery("from Sala s where s.ubicacion like :ubicacion", Sala.class)
                .setParameter("ubicacion", "%" + ubicacion + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return salas;
    }
}