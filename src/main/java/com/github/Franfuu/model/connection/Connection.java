package com.github.Franfuu.model.connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Connection {
    private static Connection _instance;
    public static SessionFactory sessionFactory;

    private Connection() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Connection getInstance() {
        if (_instance == null) {
            _instance = new Connection();
        }
        return _instance;
    }

    public static Session getSessionFactory() {
        return sessionFactory.openSession();
    }

    public void close() {

        if (_instance != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }

    }
}
