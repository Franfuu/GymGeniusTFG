package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.utils.PasswordUtils;
import org.hibernate.Session;

import java.util.List;

public class EmpleadoDAO {

    public void insert(Empleado empleado) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(empleado);
        session.getTransaction().commit();
        session.close();
    }

    public Empleado findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Empleado empleado = session.get(Empleado.class, id);
        session.getTransaction().commit();
        session.close();
        return empleado;
    }

    public List<Empleado> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Empleado> empleados = session.createQuery("from Empleado", Empleado.class).list();
        session.getTransaction().commit();
        session.close();
        return empleados;
    }

    public void update(Empleado empleado) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(empleado);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Empleado empleado) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.delete(empleado);
        session.getTransaction().commit();
        session.close();
    }

    public Empleado findByEmail(String email) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Empleado empleado = session.createQuery("from Empleado e where e.email = :email", Empleado.class)
                .setParameter("email", email)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return empleado;
    }

    public Empleado findByEmailAndPassword(String email, String password) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Empleado empleado = session.createQuery("FROM Empleado e WHERE e.email = :email AND e.contraseña = :contraseña", Empleado.class)
                .setParameter("email", email)
                .setParameter("contraseña", password)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return empleado;
    }
    public Empleado autenticarEmpleado(String email, String password) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        Empleado empleado = session.createQuery("FROM Empleado e WHERE e.email = :email", Empleado.class)
                .setParameter("email", email)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();

        // Verifica la contraseña si el empleado existe
        if (empleado != null && PasswordUtils.checkPassword(password, empleado.getContraseña())) {
            return empleado;
        }

        return null;
    }
}