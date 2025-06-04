package com.github.Franfuu.model.entities;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "inscripciones_clases")
public class InscripcionesClase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente idCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clase")
    private Clase idClase;

    @ColumnDefault("curdate()")
    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Clase getIdClase() {
        return idClase;
    }

    public void setIdClase(Clase idClase) {
        this.idClase = idClase;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

}