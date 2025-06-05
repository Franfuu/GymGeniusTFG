package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Ejercicio;
import org.hibernate.Session;

import java.util.List;

public class EjercicioDAO {

    public void insert(Ejercicio ejercicio) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(ejercicio);
        session.getTransaction().commit();
        session.close();
    }

    public Ejercicio findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Ejercicio ejercicio = session.get(Ejercicio.class, id);
        session.getTransaction().commit();
        session.close();
        return ejercicio;
    }

    public List<Ejercicio> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Ejercicio> ejercicios = session.createQuery("from Ejercicio", Ejercicio.class).list();
        session.getTransaction().commit();
        session.close();
        return ejercicios;
    }

    public void update(Ejercicio ejercicio) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(ejercicio);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Integer ejercicioId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Ejercicio ejercicio = session.get(Ejercicio.class, ejercicioId);
        if (ejercicio != null) {
            session.delete(ejercicio);
        }
        session.getTransaction().commit();
        session.close();
    }

    public List<Ejercicio> findByNombre(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Ejercicio> ejercicios = session.createQuery("from Ejercicio e where e.nombre like :nombre", Ejercicio.class)
                .setParameter("nombre", "%" + nombre + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return ejercicios;
    }

    public List<Ejercicio> findByGrupoMuscular(String grupoMuscular) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Ejercicio> ejercicios = session.createQuery("from Ejercicio e where e.grupoMuscular like :grupoMuscular", Ejercicio.class)
                .setParameter("grupoMuscular", "%" + grupoMuscular + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return ejercicios;
    }
}