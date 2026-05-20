package src.controller;

import java.util.List;                              // Para manejar listas de equipos y partidos
import java.util.stream.Collectors;                 // Para filtrar equipos en la búsqueda y en el filtrado de partidos

import javax.swing.JOptionPane;                     // Para diálogos de confirmación y mensajes

import src.exception.EquipoDuplicadoException;      // Para manejar errores específicos de equipos duplicados
import src.exception.EquipoNotFoundException;       // Para manejar errores específicos de equipos no encontrados
import src.exception.LigaException;                 // Para manejar errores generales relacionados con la lógica de la liga
import src.exception.PersistenciaException;         // Para manejar errores relacionados con la persistencia de datos
import src.model.Equipo;                            // Para representar los equipos de la liga
import src.model.LigaManager;                       // Para gestionar la lógica de la liga, los equipos y el calendario
import src.view.DialogoEquipo;                      // Para mostrar un diálogo de entrada para agregar o modificar equipos
import src.view.EquipoPanel;                        // Para actualizar la vista de la lista de equipos
import src.view.MainFrame;                          // Para mostrar mensajes y manejar la interfaz principal de la aplicación

public class EquipoController {


    /**
     * Controlador para gestionar las acciones relacionadas con los equipos de la liga, como agregar, eliminar, modificar y buscar equipos.
     * Se encarga de interactuar con el modelo (LigaManager) y actualizar la vista (EquipoPanel y MainFrame) según las acciones del usuario.
     * Atributos:
     * - manager: Instancia de LigaManager para acceder a la lógica de la liga y los equipos.
     * - panel: Instancia de EquipoPanel para actualizar la vista de la lista de equipos.
     * - frame: Instancia de MainFrame para mostrar mensajes y manejar la interfaz principal.
     */
    private final LigaManager manager;
    private final EquipoPanel  panel;
    private final MainFrame    frame;

    public EquipoController(LigaManager manager, EquipoPanel panel, MainFrame frame) {
        this.manager = manager;
        this.panel   = panel;
        this.frame   = frame;
    }

    /**
     * Agrega un nuevo equipo a la liga. Muestra un diálogo para ingresar los datos del equipo, y si el usuario confirma, 
     * intenta agregar el equipo al modelo, guardarlo y actualizar la vista. Si ocurre un error durante la adición o el guardado, 
     * muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - EquipoDuplicadoException: Si se intenta agregar un equipo con un nombre que ya existe en la liga.
     * - PersistenciaException: Si ocurre un error al guardar el nuevo equipo en la persistencia de datos.
     */
    public void onAgregarEquipo() {
        DialogoEquipo dialogo = new DialogoEquipo(frame, null);
        Equipo nuevo = dialogo.mostrarYObtenerResultado();
        if (nuevo == null) return;
        try {
            manager.agregarEquipo(nuevo);
            manager.guardarEquipos();
            panel.refrescarTabla(manager.getEquipos());
            frame.mostrarMensajeExito("Equipo agregado correctamente.");
        } catch (EquipoDuplicadoException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        }
    }

    /**
     * Elimina un equipo seleccionado de la liga. Si no se ha seleccionado ningún equipo, muestra un mensaje de error. 
     * Si se ha seleccionado un equipo, solicita confirmación para eliminarlo y todos sus jugadores. Luego, intenta eliminar el equipo del modelo, 
     * guardar los cambios y actualizar la vista. Si ocurre un error durante la eliminación o el guardado, muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - EquipoNotFoundException: Si el equipo seleccionado no se encuentra en la liga al intentar eliminarlo.
     * - PersistenciaException: Si ocurre un error al guardar los cambios después de eliminar el equipo en la persistencia de datos.
     * - LigaException: Si ocurre un error general relacionado con la lógica de la liga durante la eliminación del equipo.
     */
    public void onEliminarEquipo() {
        Equipo seleccionado = panel.getEquipoSeleccionado();
        if (seleccionado == null) {
            frame.mostrarMensajeError("Selecciona un equipo de la lista.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame,
            "¿Eliminar el equipo \"" + seleccionado.getNombre() + "\" y todos sus jugadores?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            manager.eliminarEquipo(seleccionado.getId());
            manager.guardarEquipos();
            manager.guardarJugadores();
            manager.guardarCalendario();
            panel.refrescarTabla(manager.getEquipos());
            panel.limpiarSeleccion();
            frame.mostrarMensajeExito("Equipo eliminado.");
        } catch (EquipoNotFoundException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        }
    }


    /**
     * Modifica un equipo seleccionado de la liga. Si no se ha seleccionado ningún equipo, muestra un mensaje de error. 
     * Si se ha seleccionado un equipo, muestra un diálogo para modificar los datos del equipo, y si el usuario confirma, 
     * intenta modificar el equipo en el modelo, guardarlo y actualizar la vista. 
     * Si ocurre un error durante la modificación o el guardado, muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - PersistenciaException: Si ocurre un error al guardar los cambios después de modificar el equipo en la persistencia de datos.
     * - LigaException: Si ocurre un error general relacionado con la lógica de la liga durante la modificación del equipo.
     */
    public void onModificarEquipo() {
        Equipo seleccionado = panel.getEquipoSeleccionado();
        if (seleccionado == null) {
            frame.mostrarMensajeError("Selecciona un equipo de la lista.");
            return;
        }
        DialogoEquipo dialogo = new DialogoEquipo(frame, seleccionado);
        Equipo datosNuevos = dialogo.mostrarYObtenerResultado();
        if (datosNuevos == null) return;
        try {
            manager.modificarEquipo(seleccionado.getId(), datosNuevos);
            manager.guardarEquipos();
            panel.refrescarTabla(manager.getEquipos());
            frame.mostrarMensajeExito("Equipo modificado correctamente.");
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        } catch (LigaException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        }
    }


    /**
     * Busca equipos por nombre. Obtiene el texto ingresado en el campo de búsqueda, filtra la lista de equipos del modelo para encontrar 
     * aquellos cuyo nombre contiene el texto ingresado (ignorando mayúsculas/minúsculas), y actualiza la vista para mostrar solo los equipos filtrados. 
     * Si el campo de búsqueda está vacío, muestra todos los equipos.
     */
    public void onBuscarEquipo() {
        String filtro = panel.getFiltroBusqueda();
        List<Equipo> filtrados = manager.getEquipos().stream()
            .filter(e -> e.getNombre().toLowerCase().contains(filtro.toLowerCase()))
            .collect(Collectors.toList());
        panel.refrescarTabla(filtrados);
    }


    /**
     * Refresca la lista de equipos en la vista. Obtiene la lista actualizada de equipos del modelo y actualiza la vista para mostrarla. 
     * Esta acción se puede utilizar después de realizar cambios en los equipos para asegurarse de que la vista refleje el estado actual del modelo.
     */
    public void onRefrescarLista() {
        panel.refrescarTabla(manager.getEquipos());
    }
}
