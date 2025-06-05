package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.EjercicioDAO;
import com.github.Franfuu.model.entities.Ejercicio;

import java.util.List;

public class EjercicioService {
    private EjercicioDAO ejercicioDAO;

    public EjercicioService() {
        this.ejercicioDAO = new EjercicioDAO();
    }

    public void guardarEjercicio(Ejercicio ejercicio) {
        ejercicioDAO.insert(ejercicio);
    }

    public Ejercicio buscarEjercicioPorId(Integer id) {
        return ejercicioDAO.findById(id);
    }

    public List<Ejercicio> obtenerTodosLosEjercicios() {
        return ejercicioDAO.findAll();
    }

    public void actualizarEjercicio(Ejercicio ejercicio) {
        ejercicioDAO.update(ejercicio);
    }

    public void eliminarEjercicio(Integer ejercicioId) {
        ejercicioDAO.delete(ejercicioId);
    }

    public List<Ejercicio> buscarEjerciciosPorNombre(String nombre) {
        return ejercicioDAO.findByNombre(nombre);
    }

    public List<Ejercicio> buscarEjerciciosPorGrupoMuscular(String grupoMuscular) {
        return ejercicioDAO.findByGrupoMuscular(grupoMuscular);
    }
}