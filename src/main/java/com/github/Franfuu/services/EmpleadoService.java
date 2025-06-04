package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.EmpleadoDAO;
import com.github.Franfuu.model.entities.Empleado;

import java.util.List;

public class EmpleadoService {
    private EmpleadoDAO empleadoDAO;

    public EmpleadoService() {
        this.empleadoDAO = new EmpleadoDAO();
    }

    public void guardarEmpleado(Empleado empleado) {
        empleadoDAO.insert(empleado);
    }
    public Empleado autenticarEmpleado(String email, String password) {
        return empleadoDAO.autenticarEmpleado(email, password);
    }

    public Empleado buscarEmpleadoPorId(Integer id) {
        return empleadoDAO.findById(id);
    }

    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoDAO.findAll();
    }

    public void actualizarEmpleado(Empleado empleado) {
        empleadoDAO.update(empleado);
    }

    public void eliminarEmpleado(Empleado empleado) {
        empleadoDAO.delete(empleado);
    }

    public Empleado buscarPorEmail(String email) {
        return empleadoDAO.findByEmail(email);
    }

    public Empleado autenticar(String email, String contraseña) {
        return empleadoDAO.findByEmailAndPassword(email, contraseña);
    }
}