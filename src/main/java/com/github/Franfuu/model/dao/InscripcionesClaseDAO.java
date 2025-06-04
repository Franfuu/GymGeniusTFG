package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.InscripcionesClase;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Clase;
import org.hibernate.Session;

import java.util.List;

public class InscripcionesClaseDAO {

    public void insert(InscripcionesClase inscripcion) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(inscripcion);
        session.getTransaction().commit();
        session.close();
    }

    public InscripcionesClase findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        InscripcionesClase inscripcion = session.get(InscripcionesClase.class, id);
        session.getTransaction().commit();
        session.close();
        return inscripcion;
    }

    public List<InscripcionesClase> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<InscripcionesClase> inscripciones = session.createQuery("from InscripcionesClase", InscripcionesClase.class).list();
        session.getTransaction().commit();
        session.close();
        return inscripciones;
    }

    public void update(InscripcionesClase inscripcion) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(inscripcion);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(InscripcionesClase inscripcion) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.delete(inscripcion);
        session.getTransaction().commit();
        session.close();
    }

    public List<InscripcionesClase> findByCliente(Cliente cliente) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<InscripcionesClase> inscripciones = session.createQuery("from InscripcionesClase i where i.idCliente = :cliente", InscripcionesClase.class)
                .setParameter("cliente", cliente)
                .list();
        session.getTransaction().commit();
        session.close();
        return inscripciones;
    }

    public List<InscripcionesClase> findByClase(Clase clase) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<InscripcionesClase> inscripciones = session.createQuery("from InscripcionesClase i where i.idClase = :clase", InscripcionesClase.class)
                .setParameter("clase", clase)
                .list();
        session.getTransaction().commit();
        session.close();
        return inscripciones;
    }

    public InscripcionesClase findByClienteAndClase(Cliente cliente, Clase clase) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        InscripcionesClase inscripcion = session.createQuery(
                        "from InscripcionesClase i where i.idCliente = :cliente and i.idClase = :clase",
                        InscripcionesClase.class)
                .setParameter("cliente", cliente)
                .setParameter("clase", clase)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return inscripcion;
    }
}