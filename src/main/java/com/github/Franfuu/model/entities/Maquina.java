package com.github.Franfuu.model.entities;

import javax.persistence.*;


@Entity
@Table(name = "maquinas")
public class Maquina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maquina", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "modelo", length = 100)
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "id_sala")
    private Sala sala;

    @Column(name = "foto")
    private byte[] foto;

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Sala getSala() {
        return sala;
    }
    public void setSala(Sala sala) {
        this.sala = sala;
    }
    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

}