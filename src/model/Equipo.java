package src.model;

import java.io.Serializable;                // Aseguramos que la clase implementa Serializable para permitir su serialización
import java.util.UUID;                      // Importamos UUID para generar identificadores únicos para cada equipo

public class Equipo implements Serializable {

    /**
     * Clase que representa un equipo de fútbol en la liga. Cada equipo tiene un identificador único, nombre, sede, nombre del estadio, director técnico y dueño. La clase implementa Serializable para permitir su almacenamiento y recuperación desde archivos.
     * Atributos:
     * - id: Identificador único del equipo, generado automáticamente al crear un nuevo equipo.
     * - nombre: Nombre del equipo.
     * - sede: Ciudad o localidad donde se encuentra el equipo.
     * - nombreEstadio: Nombre del estadio donde el equipo juega sus partidos como local.
     * - directorTecnico: Nombre del director técnico del equipo.
     * - dueno: Nombre del dueño del equipo.
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String sede;
    private String nombreEstadio;
    private String directorTecnico;
    private String dueno;

    // Constructor público para crear un nuevo equipo, generando automáticamente un ID único. Este constructor se utiliza al agregar un nuevo equipo a la liga.
    public Equipo(String nombre, String sede, String nombreEstadio, String directorTecnico, String dueno) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.sede = sede;
        this.nombreEstadio = nombreEstadio;
        this.directorTecnico = directorTecnico;
        this.dueno = dueno;
    }

    Equipo(String id, String nombre, String sede, String nombreEstadio, String directorTecnico, String dueno) {
        this.id = id;
        this.nombre = nombre;
        this.sede = sede;
        this.nombreEstadio = nombreEstadio;
        this.directorTecnico = directorTecnico;
        this.dueno = dueno;
    }


    // Getters para acceder a los atributos del equipo. 
    // Estos métodos se utilizan para mostrar la información del equipo en la vista.
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

    // Setters para modificar los atributos del equipo. 
    // Estos métodos se utilizan al editar la información de un equipo existente en la liga.
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


