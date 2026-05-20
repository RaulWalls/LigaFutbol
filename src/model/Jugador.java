package src.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nombre;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String lugarNacimiento;
    private String equipoId;


    public Jugador(String nombre, String direccion, LocalDate fechaNacimiento, String lugarNacimiento, String equipoId) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimiento = lugarNacimiento;
        this.equipoId = equipoId;
    }


    //Getters

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

    //Setters

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
