package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Ejercicio;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.RutinaEjercicio;
import org.hibernate.Session;

import java.util.List;

public class RutinaEjercicioDAO {

    public void insert(RutinaEjercicio rutinaEjercicio) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(rutinaEjercicio);
        session.getTransaction().commit();
        session.close();
    }

    public RutinaEjercicio findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        RutinaEjercicio rutinaEjercicio = session.get(RutinaEjercicio.class, id);
        session.getTransaction().commit();
        session.close();
        return rutinaEjercicio;
    }

    public List<RutinaEjercicio> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<RutinaEjercicio> rutinaEjercicios = session.createQuery(
                "SELECT DISTINCT re FROM RutinaEjercicio re LEFT JOIN FETCH re.idRutina LEFT JOIN FETCH re.idEjercicio",
                RutinaEjercicio.class).list();
        session.getTransaction().commit();
        session.close();
        return rutinaEjercicios;
    }

    public void update(RutinaEjercicio rutinaEjercicio) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(rutinaEjercicio);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Integer rutinaEjercicioId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        RutinaEjercicio rutinaEjercicio = session.get(RutinaEjercicio.class, rutinaEjercicioId);
        if (rutinaEjercicio != null) {
            session.delete(rutinaEjercicio);
        }
        session.getTransaction().commit();
        session.close();
    }

    public List<RutinaEjercicio> findByRutina(Rutina rutina) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<RutinaEjercicio> rutinaEjercicios = session.createQuery(
                        "from RutinaEjercicio re where re.idRutina = :rutina", RutinaEjercicio.class)
                .setParameter("rutina", rutina)
                .list();
        session.getTransaction().commit();
        session.close();
        return rutinaEjercicios;
    }

    public List<RutinaEjercicio> findByEjercicio(Ejercicio ejercicio) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<RutinaEjercicio> rutinaEjercicios = session.createQuery(
                        "from RutinaEjercicio re where re.idEjercicio = :ejercicio", RutinaEjercicio.class)
                .setParameter("ejercicio", ejercicio)
                .list();
        session.getTransaction().commit();
        session.close();
        return rutinaEjercicios;
    }

    public void deleteByRutina(Integer rutinaId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.createQuery("delete from RutinaEjercicio re where re.idRutina.id = :rutinaId")
                .setParameter("rutinaId", rutinaId)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}