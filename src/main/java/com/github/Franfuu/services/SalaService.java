package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.SalaDAO;
import com.github.Franfuu.model.entities.Sala;

import java.util.List;

public class SalaService {
    private SalaDAO salaDAO;

    public SalaService() {
        this.salaDAO = new SalaDAO();
    }

    public void guardarSala(Sala sala) {
        salaDAO.insert(sala);
    }

    public Sala buscarSalaPorId(Integer id) {
        return salaDAO.findById(id);
    }

    public List<Sala> obtenerTodasLasSalas() {
        return salaDAO.findAll();
    }

    public void actualizarSala(Sala sala) {
        salaDAO.update(sala);
    }

    public void eliminarSala(Sala sala) {
        salaDAO.delete(sala);
    }

    public List<Sala> buscarSalasPorNombre(String nombre) {
        return salaDAO.findByNombre(nombre);
    }

    public List<Sala> buscarSalasPorCapacidadMaxima(int capacidadMaxima) {
        return salaDAO.findByCapacidadMaxima(capacidadMaxima);
    }

    public List<Sala> buscarSalasPorUbicacion(String ubicacion) {
        return salaDAO.findByUbicacion(ubicacion);
    }
}