package com.github.Franfuu.model.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "rutina_ejercicios")
public class RutinaEjercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_ejercicio", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_rutina", nullable = false)
    private com.github.Franfuu.model.entities.Rutina idRutina;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ejercicio", nullable = false)
    private Ejercicio idEjercicio;

    @Column(name = "series")
    private Integer series;

    @Column(name = "repeticiones")
    private Integer repeticiones;

    @Column(name = "descanso_segundos")
    private Integer descansoSegundos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.github.Franfuu.model.entities.Rutina getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(com.github.Franfuu.model.entities.Rutina idRutina) {
        this.idRutina = idRutina;
    }

    public Ejercicio getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Ejercicio idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(Integer repeticiones) {
        this.repeticiones = repeticiones;
    }

    public Integer getDescansoSegundos() {
        return descansoSegundos;
    }

    public void setDescansoSegundos(Integer descansoSegundos) {
        this.descansoSegundos = descansoSegundos;
    }

}