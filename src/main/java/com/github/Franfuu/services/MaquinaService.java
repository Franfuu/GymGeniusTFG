package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.MaquinaDAO;
import com.github.Franfuu.model.entities.Maquina;
import com.github.Franfuu.model.entities.Sala;

import java.util.List;

public class MaquinaService {
    private MaquinaDAO maquinaDAO;

    public MaquinaService() {
        this.maquinaDAO = new MaquinaDAO();
    }

    public void guardarMaquina(Maquina maquina) {
        maquinaDAO.insert(maquina);
    }

    public Maquina buscarMaquinaPorId(Integer id) {
        return maquinaDAO.findById(id);
    }

    public List<Maquina> obtenerTodasLasMaquinas() {
        return maquinaDAO.findAll();
    }

    public void actualizarMaquina(Maquina maquina) {
        maquinaDAO.update(maquina);
    }

    public void eliminarMaquina(Maquina maquina) {
        maquinaDAO.delete(maquina);
    }

    public List<Maquina> buscarMaquinasPorSala(Sala sala) {
        return maquinaDAO.findBySala(sala);
    }

    public List<Maquina> buscarMaquinasPorNombre(String nombre) {
        return maquinaDAO.findByNombre(nombre);
    }

    public List<Maquina> buscarMaquinasPorMarca(String marca) {
        return maquinaDAO.findByMarca(marca);
    }

    public List<Maquina> buscarMaquinasPorModelo(String modelo) {
        return maquinaDAO.findByModelo(modelo);
    }
}