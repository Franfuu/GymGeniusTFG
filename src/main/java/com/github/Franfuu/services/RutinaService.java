package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.RutinaDAO;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Empleado;

import java.util.List;

public class RutinaService {
    private RutinaDAO rutinaDAO;

    public RutinaService() {
        this.rutinaDAO = new RutinaDAO();
    }

    public void guardarRutina(Rutina rutina) {
        rutinaDAO.insert(rutina);
    }

    public Rutina buscarRutinaPorId(Integer id) {
        return rutinaDAO.findById(id);
    }

    public List<Rutina> obtenerTodasLasRutinas() {
        return rutinaDAO.findAll();
    }

    public void actualizarRutina(Rutina rutina) {
        rutinaDAO.update(rutina);
    }

    public void eliminarRutina(Integer rutina) {
        rutinaDAO.delete(rutina);
    }

    public List<Rutina> buscarRutinasPorCliente(Cliente cliente) {
        return rutinaDAO.findByCliente(cliente);
    }

    public List<Rutina> buscarRutinasPorEmpleado(Empleado empleado) {
        return rutinaDAO.findByEmpleado(empleado);
    }

    public List<Rutina> buscarRutinasPorNombre(String nombre) {
        return rutinaDAO.findByNombre(nombre);
    }
}