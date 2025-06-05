package com.github.Franfuu.model.entities;


public class InscripcionClaseDTO {
    private String nombreClase;
    private String descripcionClase;
    private String fechaInscripcion;

    public InscripcionClaseDTO(String nombreClase, String descripcionClase, String fechaInscripcion) {
        this.nombreClase = nombreClase;
        this.descripcionClase = descripcionClase;
        this.fechaInscripcion = fechaInscripcion;
    }

    // Getters
    public String getNombreClase() {
        return nombreClase;
    }

    public String getDescripcionClase() {
        return descripcionClase;
    }

    public String getFechaInscripcion() {
        return fechaInscripcion;
    }
}