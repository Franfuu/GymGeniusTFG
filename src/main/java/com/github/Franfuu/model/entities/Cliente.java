package com.github.Franfuu.model.entities;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "apellido", length = 100)
    private String apellido;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @ColumnDefault("curdate()")
    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @ColumnDefault("'activo'")
    @Column(name = "estado_membresia")
    private String estadoMembresia;

    @Column(name = "`contraseña`", length = 30)
    private String contraseña;

    @Column(name = "foto")
    private byte[] foto;

    @OneToMany(mappedBy = "idCliente")
    private Set<com.github.Franfuu.model.entities.InscripcionesClase> inscripcionesClases = new LinkedHashSet<>();


    public Cliente() {
    }
    public Cliente(String nombre, String apellido, String email, String telefono, Date fechaNacimiento, LocalDate fechaRegistro, String estadoMembresia, String contraseña, byte[] foto) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento.toLocalDate();
        this.fechaRegistro = fechaRegistro;
        this.estadoMembresia = estadoMembresia;
        this.contraseña = contraseña;
        this.foto = foto;
    }
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento.toLocalDate();
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstadoMembresia() {
        return estadoMembresia;
    }

    public void setEstadoMembresia(String estadoMembresia) {
        this.estadoMembresia = estadoMembresia;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Set<com.github.Franfuu.model.entities.InscripcionesClase> getInscripcionesClases() {
        return inscripcionesClases;
    }

    public void setInscripcionesClases(Set<com.github.Franfuu.model.entities.InscripcionesClase> inscripcionesClases) {
        this.inscripcionesClases = inscripcionesClases;
    }

}