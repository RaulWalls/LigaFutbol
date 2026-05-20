package src.model;

import java.util.ArrayList;                         // Importamos ArrayList para manejar las listas de equipos, jugadores y partidos en la liga
import java.util.Collections;                       // Importamos Collections para devolver listas inmodificables desde los métodos getEquipos() y getJugadores()
import java.util.List;                              // Importamos List para definir los tipos de las listas de equipos, jugadores y partidos en la liga

import src.exception.EquipoDuplicadoException;      // Para manejar errores específicos de equipos duplicados al agregar o modificar equipos en la liga
import src.exception.EquipoNotFoundException;       // Para manejar errores específicos de equipos no encontrados al buscar, eliminar o modificar equipos en la liga
import src.exception.JugadorNotFoundException;      // Para manejar errores específicos de jugadores no encontrados al buscar, eliminar o modificar jugadores en la liga
import src.exception.PartidoInvalidoException;      // Para manejar errores específicos de partidos inválidos al generar el calendario de partidos de la liga
import src.exception.PersistenciaException;         // Para manejar errores relacionados con la persistencia de datos al guardar equipos, jugadores o el calendario de la liga
import src.util.ArchivoUtil;                        // Para manejar la carga y guardado de objetos en archivos, utilizado para la persistencia de equipos, jugadores y el calendario de la liga

public class LigaManager {


    /**
     * Clase principal del modelo que gestiona la lógica de la liga de fútbol, incluyendo los equipos, jugadores y el calendario de partidos.
     * Esta clase se encarga de agregar, eliminar, modificar y buscar equipos y jugadores, así como de generar el calendario de partidos y manejar la persistencia de datos.
     * Atributos:
     * - equipos: Lista de equipos registrados en la liga.
     * - jugadores: Lista de jugadores registrados en la liga.
     * - calendario: Objeto que representa el calendario de partidos de la liga, incluyendo las jornadas y los partidos programados.
     */
    private static final String RUTA_EQUIPOS    = "files/equipos.dat";
    private static final String RUTA_JUGADORES  = "files/jugadores.dat";
    private static final String RUTA_CALENDARIO = "files/calendario.dat";

    private List<Equipo>  equipos;
    private List<Jugador> jugadores;
    private Calendario    calendario;

    @SuppressWarnings("unchecked") // Para evitar advertencias de conversión al cargar objetos desde archivos.
    /**
     * Constructor que carga los datos de equipos, jugadores y calendario desde los archivos de persistencia utilizando ArchivoUtil. 
     * Si ocurre un error al cargar, inicializa las listas de equipos y jugadores, así como el calendario, con valores vacíos o por defecto.
     */
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

    /**
     * Agrega un nuevo equipo a la liga. Antes de agregar, verifica que no exista otro equipo con el mismo nombre (ignorando mayúsculas y minúsculas) 
     * para evitar duplicados. Si se encuentra un equipo con el mismo nombre, lanza una excepción EquipoDuplicadoException con un mensaje descriptivo del error. 
     * Si el equipo se agrega correctamente, se añade a la lista de equipos y se espera que el controlador que llama a este método se encargue de guardar 
     * los cambios en la persistencia y actualizar la vista. 
     * Excepciones manejadas:
     * - EquipoDuplicadoException: Si se intenta agregar un equipo con un nombre que ya existe en la liga (ignorando mayúsculas y minúsculas). 
     *   El mensaje de la excepción incluye el nombre del equipo duplicado para facilitar la identificación del error.
     */
    public void agregarEquipo(Equipo e) throws EquipoDuplicadoException {
        for (Equipo existente : equipos) {
            if (existente.getNombre().equalsIgnoreCase(e.getNombre())) {
                throw new EquipoDuplicadoException(e.getNombre());
            }
        }
        equipos.add(e);
    }

    /**
     * Elimina un equipo de la liga. Si el equipo no existe, lanza una excepción EquipoNotFoundException.
     */
    public void eliminarEquipo(String id) throws EquipoNotFoundException {
        Equipo equipo = buscarEquipoPorId(id);
        equipos.remove(equipo);
        jugadores.removeIf(j -> j.getEquipoId().equals(id));
        if (calendario.isGenerado()) {
            calendario.limpiar();
        }
    }

    /**
     * Modifica los datos de un equipo existente en la liga. Primero, busca el equipo por su ID, 
     * y si no lo encuentra, lanza una excepción EquipoNotFoundException. 
     * Luego, verifica que el nuevo nombre del equipo no cause un conflicto con otro equipo existente 
     * (ignorando mayúsculas y minúsculas), y si encuentra un conflicto, lanza una excepción EquipoDuplicadoException. 
     * Si no hay conflictos, actualiza los datos del equipo con la nueva información proporcionada.
     * Excepciones manejadas:
     * - EquipoNotFoundException: Si el equipo a modificar no existe en la liga al intentar buscarlo por su ID. 
     *   El mensaje de la excepción incluye el ID del equipo que no se encontró para facilitar la identificación del error.
     * - EquipoDuplicadoException: Si el nuevo nombre del equipo causa un conflicto con otro equipo existente en la liga 
     *   (ignorando mayúsculas y minúsculas). El mensaje de la excepción incluye el nombre del equipo duplicado para facilitar la identificación del error.
     */
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


    /**
     * Busca un equipo por su ID. Si el equipo no existe, lanza una excepción EquipoNotFoundException.
     * Excepciones manejadas:
     * - EquipoNotFoundException: Si el equipo a buscar no existe en la liga al intentar buscarlo por su ID.
     */
    public Equipo buscarEquipoPorId(String id) throws EquipoNotFoundException {
        for (Equipo e : equipos) {
            if (e.getId().equals(id)) return e;
        }
        throw new EquipoNotFoundException(id);
    }

    /**
     * Busca un equipo por su nombre (ignorando mayúsculas y minúsculas). Si el equipo no existe, lanza una excepción EquipoNotFoundException.
     * Excepciones manejadas:
     * - EquipoNotFoundException: Si el equipo a buscar no existe en la liga al intentar buscarlo por su nombre.
     */
    public Equipo buscarEquipoPorNombre(String nombre) throws EquipoNotFoundException {
        for (Equipo e : equipos) {
            if (e.getNombre().equalsIgnoreCase(nombre)) return e;
        }
        throw new EquipoNotFoundException(nombre);
    }


    /**
     * Devuelve una lista inmodificable de los equipos registrados en la liga.
     */
    public List<Equipo> getEquipos() {
        return Collections.unmodifiableList(equipos);
    }

    // -------------------------------------------------------------------------
    // Jugadores
    // -------------------------------------------------------------------------

    /**
     * Agrega un nuevo jugador a la liga. Antes de agregar, verifica que el equipo al que se asigna el jugador exista en la liga. 
     * Si el equipo no existe, lanza una excepción EquipoNotFoundException con un mensaje descriptivo del error. 
     * Si el jugador se agrega correctamente, se añade a la lista de jugadores y se espera que el controlador que llama a este método se encargue de guardar 
     * los cambios en la persistencia y actualizar la vista. 
     * Excepciones manejadas:
     * - EquipoNotFoundException: Si se intenta agregar un jugador asignado a un equipo que no existe en la liga. 
     *   El mensaje de la excepción incluye el ID del equipo no encontrado para facilitar la identificación del error.
     */
    public void agregarJugador(Jugador j) throws EquipoNotFoundException {
        buscarEquipoPorId(j.getEquipoId());
        jugadores.add(j);
    }

    /**
     * Elimina un jugador de la liga. Si el jugador no existe, lanza una excepción JugadorNotFoundException.
     * Excepciones manejadas:
     * - JugadorNotFoundException: Si el jugador a eliminar no existe en la liga.
     */
    public void eliminarJugador(String id) throws JugadorNotFoundException {
        Jugador jugador = buscarJugadorPorId(id);
        jugadores.remove(jugador);
    }


    /** 
     * Modifica los datos de un jugador existente en la liga.
     * Luego, verifica que el nuevo equipo al que se asigna el jugador exista en la liga, y si no lo encuentra, lanza una excepción EquipoNotFoundException. 
     * Si el jugador se encuentra y el nuevo equipo es válido, actualiza los datos del jugador con la nueva información proporcionada.
     * Excepciones manejadas:
     * - JugadorNotFoundException: Si el jugador a modificar no existe en la liga al intentar buscarlo por su ID. 
     * - EquipoNotFoundException: Si el nuevo equipo al que se asigna el jugador no existe en la liga al intentar buscarlo por su ID.
     */
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


    /** 
     * Busca un jugador en la liga por su ID.
     * Si el jugador no existe, lanza una excepción JugadorNotFoundException.
     * Excepciones manejadas:
     * - JugadorNotFoundException: Si el jugador no existe en la liga al intentar buscarlo por su ID.
     */
    public Jugador buscarJugadorPorId(String id) throws JugadorNotFoundException {
        for (Jugador j : jugadores) {
            if (j.getId().equals(id)) return j;
        }
        throw new JugadorNotFoundException(id);
    }

    /**
     * Devuelve una lista inmodificable de los jugadores registrados en la liga.
     */
    public List<Jugador> getJugadores() {
        return Collections.unmodifiableList(jugadores);
    }

    /**
     * Devuelve una lista de jugadores que pertenecen a un equipo específico, identificado por su ID. 
     * Si el equipo no tiene jugadores asignados, devuelve una lista vacía.
     */
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

    /** 
     * Genera el calendario de partidos para la liga.
     * Excepciones manejadas:
     * - PartidoInvalidoException: Si hay algún partido inválido en la generación del calendario.
     */
    public void generarCalendario() throws PartidoInvalidoException {
        calendario.generarCalendario(new ArrayList<>(equipos));
    }

    // Devuelve el calendario actual de la liga.
    public Calendario getCalendario() {
        return calendario;
    }

    // Método para limpiar el calendario actual, eliminando todos los partidos y restableciendo el estado a no generado. 
    // Si el calendario no ha sido generado, no hace nada.
    public void limpiarCalendario() {
        calendario.limpiar();
    }

    // -------------------------------------------------------------------------
    // Persistencia (llamada por los Controllers tras cada cambio)
    // -------------------------------------------------------------------------

    // Métodos para guardar los datos de equipos, jugadores y calendario en archivos utilizando ArchivoUtil.
    public void guardarEquipos() throws PersistenciaException {
        ArchivoUtil.guardarObjeto(new ArrayList<>(equipos), RUTA_EQUIPOS);
    }

    // Método para guardar los datos de jugadores en un archivo utilizando ArchivoUtil.
    public void guardarJugadores() throws PersistenciaException {
        ArchivoUtil.guardarObjeto(new ArrayList<>(jugadores), RUTA_JUGADORES);
    }

    // Método para guardar el calendario de partidos en un archivo utilizando ArchivoUtil.
    public void guardarCalendario() throws PersistenciaException {
        ArchivoUtil.guardarObjeto(calendario, RUTA_CALENDARIO);
    }
}
