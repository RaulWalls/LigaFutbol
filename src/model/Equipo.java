package src.model;

import java.io.Serializable;
import java.util.UUID;

public class Equipo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String sede;
    private String nombreEstadio;
    private String directorTecnico;
    private String dueno;

    public Equipo(String nombre, String sede, String nombreEstadio, String directorTecnico, String dueno) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.sede = sede;
        this.nombreEstadio = nombreEstadio;
        this.directorTecnico = directorTecnico;
        this.dueno = dueno;
    }


    // Getters 

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSede() {
        return sede;
    }

    public String getNombreEstadio() {
        return nombreEstadio;
    }

    public String getDirectorTecnico() {
        return directorTecnico;
    }

    public String getDueno() {
        return dueno;
    }

    @Override
    public String toString() {
        return "Equipo[" + nombre + ", " + sede + ", " + nombreEstadio + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        return this.id.equals(((Equipo) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public void setNombreEstadio(String nombreEstadio) {
        this.nombreEstadio = nombreEstadio;
    }

    public void setDirectorTecnico(String directorTecnico) {
        this.directorTecnico = directorTecnico;
    }

    public void setDueno(String dueno) {
        this.dueno = dueno;
    }


}


