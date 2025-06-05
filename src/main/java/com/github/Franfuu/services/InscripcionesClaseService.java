package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.InscripcionesClaseDAO;
import com.github.Franfuu.model.entities.InscripcionesClase;
import com.github.Franfuu.model.entities.Cliente;
import com.github.Franfuu.model.entities.Clase;

import java.util.List;

public class InscripcionesClaseService {
    private InscripcionesClaseDAO inscripcionesClaseDAO;

    public InscripcionesClaseService() {
        this.inscripcionesClaseDAO = new InscripcionesClaseDAO();
    }

    public void guardarInscripcion(InscripcionesClase inscripcion) {
        inscripcionesClaseDAO.insert(inscripcion);
    }

    public InscripcionesClase buscarInscripcionPorId(Integer id) {
        return inscripcionesClaseDAO.findById(id);
    }

    public List<InscripcionesClase> obtenerTodasLasInscripciones() {
        return inscripcionesClaseDAO.findAll();
    }

    public void actualizarInscripcion(InscripcionesClase inscripcion) {
        inscripcionesClaseDAO.update(inscripcion);
    }

    public void eliminarInscripcion(InscripcionesClase inscripcion) {
        inscripcionesClaseDAO.delete(inscripcion);
    }

    public List<InscripcionesClase> buscarInscripcionesPorCliente(Cliente cliente) {
        return inscripcionesClaseDAO.findByCliente(cliente);
    }

    public List<InscripcionesClase> buscarInscripcionesPorClase(Clase clase) {
        return inscripcionesClaseDAO.findByClase(clase);
    }

    public InscripcionesClase buscarInscripcionPorClienteYClase(Cliente cliente, Clase clase) {
        return inscripcionesClaseDAO.findByClienteAndClase(cliente, clase);
    }

    public boolean clienteYaInscrito(Cliente cliente, Clase clase) {
        return buscarInscripcionPorClienteYClase(cliente, clase) != null;
    }

    public List<InscripcionesClase> buscarInscripcionesPorEstado(String estado) {
        return inscripcionesClaseDAO.findByEstado(estado);
    }
}