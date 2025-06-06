package com.github.Franfuu.model.dao;

import com.github.Franfuu.model.connection.Connection;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.utils.PasswordUtils;
import org.hibernate.Session;

import java.util.List;

public class ClienteDAO {

    public void insert(Cliente cliente) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.save(cliente);
        session.getTransaction().commit();
        session.close();
    }

    public Cliente findById(Integer id) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Cliente cliente = session.get(Cliente.class, id);
        session.getTransaction().commit();
        session.close();
        return cliente;
    }

    public Cliente findByEmail(String email) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Cliente cliente = session.createQuery("FROM Cliente c WHERE c.email = :email", Cliente.class)
                .setParameter("email", email)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return cliente;
    }

    public Cliente findByEmailAndPassword(String email, String password) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Cliente cliente = session.createQuery("FROM Cliente c WHERE c.email = :email AND c.contraseña = :password", Cliente.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .uniqueResult();
        session.getTransaction().commit();
        session.close();
        return cliente;
    }

    public List<Cliente> findAll() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Cliente> clientes = session.createQuery("FROM Cliente", Cliente.class).list();
        session.getTransaction().commit();
        session.close();
        return clientes;
    }

    public void update(Cliente cliente) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        session.update(cliente);
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Integer clienteId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        Cliente cliente = session.get(Cliente.class, clienteId);
        if (cliente != null) {
            session.delete(cliente);
        }
        session.getTransaction().commit();
        session.close();
    }

    public List<Cliente> findByNombre(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();
        List<Cliente> clientes = session.createQuery("FROM Cliente c WHERE c.nombre LIKE :nombre", Cliente.class)
                .setParameter("nombre", "%" + nombre + "%")
                .list();
        session.getTransaction().commit();
        session.close();
        return clientes;
    }

    public Cliente autenticarCliente(String email, String password) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSessionFactory();
        session.beginTransaction();

        Cliente cliente = session.createQuery("FROM Cliente c WHERE c.email = :email", Cliente.class)
                .setParameter("email", email)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();

        // Verifica la contraseña si el cliente existe
        if (cliente != null && PasswordUtils.checkPassword(password, cliente.getContraseña())) {
            return cliente;
        }

        return null;
    }
}