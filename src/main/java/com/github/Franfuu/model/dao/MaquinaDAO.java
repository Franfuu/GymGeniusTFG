package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Maquina;
import com.github.Franfuu.model.entities.Sala;
import org.hibernate.Session;

import java.util.List;

public class MaquinaDAO {

    public void insert(Maquina maquina) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(maquina);
        session.getTransaction().commit();
        session.close();
    }

    public Maquina findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Maquina maquina = session.get(Maquina.class, id);
        session.getTransaction().commit();
        session.close();
        return maquina;
    }

    public List<Maquina> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Maquina> maquinas = session.createQuery("from Maquina", Maquina.class).list();
        session.getTransaction().commit();
        session.close();
        return maquinas;
    }

    public void update(Maquina maquina) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(maquina);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Maquina maquina) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.delete(maquina);
        session.getTransaction().commit();
        session.close();
    }

    public List<Maquina> findBySala(Sala sala) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Maquina> maquinas = session.createQuery("from Maquina m where m.id_sala = :sala", Maquina.class)
                .setParameter("sala", sala)
                .list();
        session.getTransaction().commit();
        session.close();
        return maquinas;
    }

    public List<Maquina> findByNombre(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Maquina> maquinas = session.createQuery("from Maquina m where m.nombre like :nombre", Maquina.class)
                .setParameter("nombre", "%" + nombre + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return maquinas;
    }

    public List<Maquina> findByMarca(String marca) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Maquina> maquinas = session.createQuery("from Maquina m where m.marca like :marca", Maquina.class)
                .setParameter("marca", "%" + marca + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return maquinas;
    }

    public List<Maquina> findByModelo(String modelo) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Maquina> maquinas = session.createQuery("from Maquina m where m.modelo like :modelo", Maquina.class)
                .setParameter("modelo", "%" + modelo + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return maquinas;
    }
}