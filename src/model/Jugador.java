package src.model;

import java.io.Serializable;                    // Aseguramos que la clase implementa Serializable para permitir su serialización
import java.time.LocalDate;                     // Importamos LocalDate para manejar las fechas de nacimiento de los jugadores
import java.time.Period;                        // Importamos Period para calcular la edad de los jugadores a partir de su fecha de nacimiento
import java.util.UUID;                          // Importamos UUID para generar identificadores únicos para cada jugador

public class Jugador implements Serializable {

    /**
     * Clase que representa un jugador de fútbol en la liga. Cada jugador tiene un identificador único, nombre, dirección, fecha de nacimiento, lugar de nacimiento y el ID del equipo al que pertenece. La clase implementa Serializable para permitir su almacenamiento y recuperación desde archivos.
     * Atributos:
     * - id: Identificador único del jugador, generado automáticamente al crear un nuevo jugador.
     * - nombre: Nombre del jugador.
     * - direccion: Dirección de residencia del jugador.
     * - fechaNacimiento: Fecha de nacimiento del jugador, utilizada para calcular su edad.
     * - lugarNacimiento: Lugar de nacimiento del jugador.
     * - equipoId: Identificador del equipo al que pertenece el jugador, utilizado para relacionar el jugador con su equipo en la liga.
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String lugarNacimiento;
    private String equipoId;

    // Constructor público para crear un nuevo jugador. Este constructor se utiliza al agregar un nuevo jugador a la liga.
    public Jugador(String nombre, String direccion, LocalDate fechaNacimiento, String lugarNacimiento, String equipoId) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.equipoId = equipoId;
    }


    //Getters para acceder a los atributos del jugador.
    // Estos métodos se utilizan para mostrar la información del jugador en la vista.
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public String getEquipoId() {
        return equipoId;
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "Jugador[" + nombre + ", equipoId=" + equipoId + ", edad=" + getEdad() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jugador)) return false;
        return this.id.equals(((Jugador) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    //Setters para modificar los atributos del jugador.
    // Estos métodos se utilizan al editar la información de un jugador existente en la liga.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public void setEquipoId(String equipoId) {
        this.equipoId = equipoId;
    }


}
