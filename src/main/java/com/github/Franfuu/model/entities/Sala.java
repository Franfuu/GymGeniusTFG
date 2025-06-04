package com.github.Franfuu.model.entities;

import javax.persistence.*;


import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "salas")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sala", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL)
    private List<Clase> clases;

    @OneToMany(mappedBy = "sala")
    private List<Maquina> maquinas;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Set<Clase> getClases() {
        return (Set<Clase>) clases;
    }

    public void setClases(Set<Clase> clases) {
        this.clases = (List<Clase>) clases;
    }

    public List<Maquina> getMaquinas() {
        return maquinas;
    }

    public void setMaquinas(List<Maquina> maquinas) {
        this.maquinas = maquinas;
    }

}