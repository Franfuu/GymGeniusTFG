package com.github.Franfuu.model.entities;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ejercicios")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio", nullable = false)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "grupo_muscular", length = 50)
    private String grupoMuscular;

    @OneToMany(mappedBy = "idEjercicio")
    private Set<com.github.Franfuu.model.entities.RutinaEjercicio> rutinaEjercicios = new LinkedHashSet<>();

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public Set<com.github.Franfuu.model.entities.RutinaEjercicio> getRutinaEjercicios() {
        return rutinaEjercicios;
    }

    public void setRutinaEjercicios(Set<com.github.Franfuu.model.entities.RutinaEjercicio> rutinaEjercicios) {
        this.rutinaEjercicios = rutinaEjercicios;
    }

}