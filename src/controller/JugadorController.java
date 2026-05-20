package src.controller;

import javax.swing.JOptionPane;                         // Para diálogos de confirmación y mensajes

import src.exception.JugadorNotFoundException;          // Para manejar errores específicos de jugadores no encontrados
import src.exception.LigaException;                     // Para manejar errores generales relacionados con la lógica de la liga
import src.exception.PersistenciaException;             // Para manejar errores relacionados con la persistencia de datos
import src.model.Jugador;                               // Para representar los jugadores de la liga
import src.model.LigaManager;                           // Para gestionar la lógica de la liga, los equipos, los jugadores y el calendario
import src.view.DialogoJugador;                         // Para mostrar un diálogo de entrada para agregar o modificar jugadores    
import src.view.JugadorPanel;                           // Para actualizar la vista de la lista de jugadores
import src.view.MainFrame;                              // Para mostrar mensajes y manejar la interfaz principal de la aplicación

public class JugadorController {


    /**
     * Controlador para gestionar las acciones relacionadas con los jugadores de la liga, como agregar, eliminar, modificar y filtrar jugadores por equipo.
     * Se encarga de interactuar con el modelo (LigaManager) y actualizar la vista (JugadorPanel y MainFrame) según las acciones del usuario.
     * Atributos:
     * - manager: Instancia de LigaManager para acceder a la lógica de la liga, los equipos, los jugadores y el calendario.
     * - panel: Instancia de JugadorPanel para actualizar la vista de la lista de jugadores.
     * - frame: Instancia de MainFrame para mostrar mensajes y manejar la interfaz principal.
     */
    private final LigaManager  manager;
    private final JugadorPanel panel;
    private final MainFrame    frame;

    public JugadorController(LigaManager manager, JugadorPanel panel, MainFrame frame) {
        this.manager = manager;
        this.panel   = panel;
        this.frame   = frame;
    }


    /**
     * Agrega un nuevo jugador a la liga. Muestra un diálogo para ingresar los datos del jugador, y si el usuario confirma, 
     * intenta agregar el jugador al modelo, guardarlo y actualizar la vista. Si ocurre un error durante la adición o el guardado, 
     * muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - PersistenciaException: Si ocurre un error al guardar el nuevo jugador en la persistencia de datos.
     * - LigaException: Si ocurre un error general relacionado con la lógica de la liga al intentar agregar el jugador (por ejemplo, si se asigna a un equipo que no existe).
     */
    public void onAgregarJugador() {
        if (manager.getEquipos().isEmpty()) {
            frame.mostrarMensajeError("Registra al menos un equipo antes de agregar jugadores.");
            return;
        }
        DialogoJugador dialogo = new DialogoJugador(frame, null, manager.getEquipos());
        Jugador nuevo = dialogo.mostrarYObtenerResultado();
        if (nuevo == null) return;
        try {
            manager.agregarJugador(nuevo);
            manager.guardarJugadores();
            panel.refrescarTabla(manager.getJugadores(), manager.getEquipos());
            frame.mostrarMensajeExito("Jugador agregado correctamente.");
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        } catch (LigaException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        }
    }

    /**
     * Elimina un jugador de la liga. Muestra un diálogo de confirmación, y si el usuario confirma, intenta eliminar el jugador del modelo, 
     * guardarlo y actualizar la vista. Si ocurre un error durante la eliminación o el guardado, muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - JugadorNotFoundException: Si el jugador a eliminar no existe en la liga.
     * - PersistenciaException: Si ocurre un error al guardar los cambios en la persistencia de datos.
     */
    public void onEliminarJugador() {
        Jugador seleccionado = panel.getJugadorSeleccionado();
        if (seleccionado == null) {
            frame.mostrarMensajeError("Selecciona un jugador de la lista.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame,
            "¿Eliminar al jugador \"" + seleccionado.getNombre() + "\"?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            manager.eliminarJugador(seleccionado.getId());
            manager.guardarJugadores();
            panel.refrescarTabla(manager.getJugadores(), manager.getEquipos());
            panel.limpiarSeleccion();
            frame.mostrarMensajeExito("Jugador eliminado.");
        } catch (JugadorNotFoundException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        }
    }

    /**
     * Modifica los datos de un jugador existente. Muestra un diálogo para ingresar los nuevos datos del jugador, y si el usuario confirma, 
     * intenta actualizar el jugador en el modelo, guardarlo y actualizar la vista. Si ocurre un error durante la modificación o el guardado, 
     * muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - PersistenciaException: Si ocurre un error al guardar los cambios en la persistencia de datos.
     * - LigaException: Si ocurre un error general relacionado con la lógica de la liga al intentar modificar el jugador.
     */
    public void onModificarJugador() {
        Jugador seleccionado = panel.getJugadorSeleccionado();
        if (seleccionado == null) {
            frame.mostrarMensajeError("Selecciona un jugador de la lista.");
            return;
        }
        DialogoJugador dialogo = new DialogoJugador(frame, seleccionado, manager.getEquipos());
        Jugador datosNuevos = dialogo.mostrarYObtenerResultado();
        if (datosNuevos == null) return;
        try {
            manager.modificarJugador(seleccionado.getId(), datosNuevos);
            manager.guardarJugadores();
            panel.refrescarTabla(manager.getJugadores(), manager.getEquipos());
            frame.mostrarMensajeExito("Jugador modificado correctamente.");
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        } catch (LigaException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        }
    }

        /**
        * Filtra la lista de jugadores por equipo. Obtiene el ID del equipo seleccionado en el filtro, obtiene la lista de jugadores 
        * que pertenecen a ese equipo del modelo y actualiza la vista para mostrar solo esos jugadores. 
        * Si no se selecciona ningún equipo (ID vacío), muestra todos los jugadores.
        */
    public void onFiltrarPorEquipo(String equipoId) {
        panel.refrescarTabla(manager.getJugadoresPorEquipo(equipoId), manager.getEquipos());
    }


    /**
     * Refresca la lista de jugadores en la vista. Obtiene la lista actualizada de jugadores del modelo y actualiza la vista para mostrarla. 
     * Esta acción se puede utilizar después de realizar cambios en los jugadores para asegurarse de que la vista refleje el estado actual del modelo.
     */
    public void onRefrescarLista() {
        panel.refrescarTabla(manager.getJugadores(), manager.getEquipos());
        panel.refrescarComboEquipos(manager.getEquipos());
    }
}
