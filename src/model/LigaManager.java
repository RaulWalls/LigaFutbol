package src.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.exception.EquipoDuplicadoException;
import src.exception.EquipoNotFoundException;
import src.exception.JugadorNotFoundException;
import src.exception.PartidoInvalidoException;
import src.exception.PersistenciaException;
import src.util.ArchivoUtil;

public class LigaManager {

    private static final String RUTA_EQUIPOS    = "files/equipos.dat";
    private static final String RUTA_JUGADORES  = "files/jugadores.dat";
    private static final String RUTA_CALENDARIO = "files/calendario.dat";

    private List<Equipo>  equipos;
    private List<Jugador> jugadores;
    private Calendario    calendario;

    @SuppressWarnings("unchecked")
    public LigaManager() {
        try {
            Object e = ArchivoUtil.cargarObjeto(RUTA_EQUIPOS);
            equipos = (e != null) ? (List<Equipo>) e : new ArrayList<>();

            Object j = ArchivoUtil.cargarObjeto(RUTA_JUGADORES);
            jugadores = (j != null) ? (List<Jugador>) j : new ArrayList<>();

            Object c = ArchivoUtil.cargarObjeto(RUTA_CALENDARIO);
            calendario = (c != null) ? (Calendario) c : new Calendario();
        } catch (PersistenciaException ex) {
            equipos    = new ArrayList<>();
            jugadores  = new ArrayList<>();
            calendario = new Calendario();
        }
    }

    // -------------------------------------------------------------------------
    // Equipos
    // -------------------------------------------------------------------------

    public void agregarEquipo(Equipo e) throws EquipoDuplicadoException {
        for (Equipo existente : equipos) {
            if (existente.getNombre().equalsIgnoreCase(e.getNombre())) {
                throw new EquipoDuplicadoException(e.getNombre());
            }
        }
        equipos.add(e);
    }

    public void eliminarEquipo(String id) throws EquipoNotFoundException {
        Equipo equipo = buscarEquipoPorId(id);
        equipos.remove(equipo);
        jugadores.removeIf(j -> j.getEquipoId().equals(id));
        if (calendario.isGenerado()) {
            calendario.limpiar();
        }
    }

    public void modificarEquipo(String id, Equipo datosNuevos)
            throws EquipoNotFoundException, EquipoDuplicadoException {
        Equipo equipo = buscarEquipoPorId(id);
        for (Equipo otro : equipos) {
            if (!otro.getId().equals(id) &&
                otro.getNombre().equalsIgnoreCase(datosNuevos.getNombre())) {
                throw new EquipoDuplicadoException(datosNuevos.getNombre());
            }
        }
        equipo.setNombre(datosNuevos.getNombre());
        equipo.setSede(datosNuevos.getSede());
        equipo.setNombreEstadio(datosNuevos.getNombreEstadio());
        equipo.setDirectorTecnico(datosNuevos.getDirectorTecnico());
        equipo.setDueno(datosNuevos.getDueno());
    }

    public Equipo buscarEquipoPorId(String id) throws EquipoNotFoundException {
        for (Equipo e : equipos) {
            if (e.getId().equals(id)) return e;
        }
        throw new EquipoNotFoundException(id);
    }

    public Equipo buscarEquipoPorNombre(String nombre) throws EquipoNotFoundException {
        for (Equipo e : equipos) {
            if (e.getNombre().equalsIgnoreCase(nombre)) return e;
        }
        throw new EquipoNotFoundException(nombre);
    }

    public List<Equipo> getEquipos() {
        return Collections.unmodifiableList(equipos);
    }

    // -------------------------------------------------------------------------
    // Jugadores
    // -------------------------------------------------------------------------

    public void agregarJugador(Jugador j) throws EquipoNotFoundException {
        buscarEquipoPorId(j.getEquipoId());
        jugadores.add(j);
    }

    public void eliminarJugador(String id) throws JugadorNotFoundException {
        Jugador jugador = buscarJugadorPorId(id);
        jugadores.remove(jugador);
    }

    public void modificarJugador(String id, Jugador datosNuevos)
            throws JugadorNotFoundException, EquipoNotFoundException {
        Jugador jugador = buscarJugadorPorId(id);
        buscarEquipoPorId(datosNuevos.getEquipoId());
        jugador.setNombre(datosNuevos.getNombre());
        jugador.setDireccion(datosNuevos.getDireccion());
        jugador.setFechaNacimiento(datosNuevos.getFechaNacimiento());
        jugador.setLugarNacimiento(datosNuevos.getLugarNacimiento());
        jugador.setEquipoId(datosNuevos.getEquipoId());
    }

    public Jugador buscarJugadorPorId(String id) throws JugadorNotFoundException {
        for (Jugador j : jugadores) {
            if (j.getId().equals(id)) return j;
        }
        throw new JugadorNotFoundException(id);
    }

    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    public List<Jugador> getJugadoresPorEquipo(String equipoId) {
        List<Jugador> resultado = new ArrayList<>();
        for (Jugador j : jugadores) {
            if (j.getEquipoId().equals(equipoId)) resultado.add(j);
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Calendario
    // -------------------------------------------------------------------------

    public void generarCalendario() throws PartidoInvalidoException {
        calendario.generarCalendario(new ArrayList<>(equipos));
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void limpiarCalendario() {
        calendario.limpiar();
    }

    // -------------------------------------------------------------------------
    // Persistencia (llamada por los Controllers tras cada cambio)
    // -------------------------------------------------------------------------

    public void guardarEquipos() throws PersistenciaException {
        ArchivoUtil.guardarObjeto(new ArrayList<>(equipos), RUTA_EQUIPOS);
    }

    public void guardarJugadores() throws PersistenciaException {
        ArchivoUtil.guardarObjeto(new ArrayList<>(jugadores), RUTA_JUGADORES);
    }

    public void guardarCalendario() throws PersistenciaException {
        ArchivoUtil.guardarObjeto(calendario, RUTA_CALENDARIO);
    }
}
