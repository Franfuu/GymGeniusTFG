package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.RutinaEjercicioDAO;
import com.github.Franfuu.model.entities.Ejercicio;
import com.github.Franfuu.model.entities.Rutina;
import com.github.Franfuu.model.entities.RutinaEjercicio;

import java.util.List;

public class RutinaEjercicioService {
    private RutinaEjercicioDAO rutinaEjercicioDAO;

    public RutinaEjercicioService() {
        this.rutinaEjercicioDAO = new RutinaEjercicioDAO();
    }

    public void guardarRutinaEjercicio(RutinaEjercicio rutinaEjercicio) {
        rutinaEjercicioDAO.insert(rutinaEjercicio);
    }

    public RutinaEjercicio buscarRutinaEjercicioPorId(Integer id) {
        return rutinaEjercicioDAO.findById(id);
    }

    public List<RutinaEjercicio> obtenerTodosLosRutinaEjercicios() {
        return rutinaEjercicioDAO.findAll();
    }

    public void actualizarRutinaEjercicio(RutinaEjercicio rutinaEjercicio) {
        rutinaEjercicioDAO.update(rutinaEjercicio);
    }

    public void eliminarRutinaEjercicio(Integer rutinaEjercicioId) {
        rutinaEjercicioDAO.delete(rutinaEjercicioId);
    }

    public List<RutinaEjercicio> buscarRutinaEjerciciosPorRutina(Rutina rutina) {
        return rutinaEjercicioDAO.findByRutina(rutina);
    }

    public List<RutinaEjercicio> buscarRutinaEjerciciosPorEjercicio(Ejercicio ejercicio) {
        return rutinaEjercicioDAO.findByEjercicio(ejercicio);
    }

    public void eliminarEjerciciosDeLaRutina(Integer rutinaId) {
        rutinaEjercicioDAO.deleteByRutina(rutinaId);
    }
}