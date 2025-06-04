package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.ClienteDAO;
import com.github.Franfuu.model.entities.Cliente;

import java.time.LocalDate;
import java.util.List;

public class ClienteService {
    private ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public void guardarCliente(Cliente cliente) {
        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDate.now());
        }
        clienteDAO.insert(cliente);
    }

    public Cliente buscarClientePorId(Integer id) {
        return clienteDAO.findById(id);
    }

    public Cliente buscarClientePorEmail(String email) {
        return clienteDAO.findByEmail(email);
    }

    public Cliente buscarClientePorEmailYPassword(String email, String password) {
        return clienteDAO.findByEmailAndPassword(email, password);
    }

    public Cliente autenticarCliente(String email, String password) {
        return clienteDAO.autenticarCliente(email, password);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteDAO.findAll();
    }

    public void actualizarCliente(Cliente cliente) {
        clienteDAO.update(cliente);
    }

    public void eliminarCliente(Integer cliente) {
        clienteDAO.delete(cliente);
    }

    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clienteDAO.findByNombre(nombre);
    }
}