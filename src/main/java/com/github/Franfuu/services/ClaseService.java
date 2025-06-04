package com.github.Franfuu.services;

import com.github.Franfuu.model.dao.ClaseDAO;
import com.github.Franfuu.model.entities.Clase;
import com.github.Franfuu.model.entities.Empleado;
import com.github.Franfuu.model.entities.Sala;

import java.util.List;

public class ClaseService {
    private ClaseDAO claseDAO;

    public ClaseService() {
        this.claseDAO = new ClaseDAO();
    }

    public void guardarClase(Clase clase) {
        claseDAO.insert(clase);
    }

    public Clase buscarClasePorId(Integer id) {
        return claseDAO.findById(id);
    }

    public List<Clase> obtenerTodasLasClases() {
        return claseDAO.findAll();
    }

    public void actualizarClase(Clase clase) {
        // Validar y formatear los días de semana antes de actualizar
        String diasValidados = validarDiasSemana(clase.getDiasSemana());
        clase.setDiasSemana(diasValidados);
        claseDAO.update(clase);
    }

    public void eliminarClase(Integer clase) {
        claseDAO.delete(clase);
    }

    private String validarDiasSemana(String diasSemana) {
        if (diasSemana == null || diasSemana.isEmpty()) {
            return null;
        }

        // Verificar que los valores estén exactamente como en la definición SQL
        String[] valoresPermitidos = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};
        String[] diasArray = diasSemana.split(",");
        StringBuilder resultado = new StringBuilder();

        // Validar cada valor contra los valores permitidos
        for (int i = 0; i < diasArray.length; i++) {
            String dia = diasArray[i].trim();
            boolean encontrado = false;

            for (String valorPermitido : valoresPermitidos) {
                if (valorPermitido.equals(dia)) {
                    encontrado = true;
                    if (i > 0) {
                        resultado.append(",");
                    }
                    resultado.append(valorPermitido);
                    break;
                }
            }
            if (!encontrado) {
                throw new IllegalArgumentException("Día de la semana no válido: " + dia);
            }
        }

        return resultado.toString();
    }

    public List<Clase> buscarClasesPorInstructor(Empleado empleado) {
        return claseDAO.findByEmpleado(empleado);
    }

    public List<Clase> buscarClasesPorSala(Sala sala) {
        return claseDAO.findBySala(sala);
    }

    public List<Clase> buscarClasesPorNombre(String nombre) {
        return claseDAO.findByNombre(nombre);
    }
}