package src.controller;

import javax.swing.JOptionPane;                 // Para diálogos de confirmación y mensajes

import src.exception.PartidoInvalidoException;  // Para manejar errores específicos de partidos inválidos
import src.exception.PersistenciaException;     // Para manejar errores relacionados con la persistencia de datos
import src.model.LigaManager;                   // Para gestionar la lógica de la liga y el calendario
import src.model.Partido;                       // Para acceder al objeto Partido seleccionado y registrar su resultado
import src.view.CalendarioPanel;                // Para actualizar la vista del calendario
import src.view.MainFrame;                      // Para mostrar mensajes y manejar la interfaz principal de la aplicación

public class CalendarioController {

    /**
     * Controlador para gestionar la generación, filtrado y limpieza del calendario de partidos.
     * Se encarga de interactuar con el modelo (LigaManager) y actualizar la vista (CalendarioPanel y MainFrame) según las acciones del usuario.
     * 
     * Atributos:
     * - manager: Instancia de LigaManager para acceder a la lógica de la liga y el calendario.
     * - panel: Instancia de CalendarioPanel para actualizar la vista del calendario.
     * - frame: Instancia de MainFrame para mostrar mensajes y manejar la interfaz principal.
     */
    private final LigaManager    manager;
    private final CalendarioPanel panel;
    private final MainFrame       frame;

    public CalendarioController(LigaManager manager, CalendarioPanel panel, MainFrame frame) {
        this.manager = manager;
        this.panel   = panel;
        this.frame   = frame;
    }


    /**
     * Genera el calendario de partidos para la liga. Si el calendario ya fue generado, solicita confirmación para regenerarlo.
     * Luego, intenta generar el calendario, guardarlo y actualizar la vista. Si ocurre un error durante la generación o el guardado, muestra un mensaje de error correspondiente.
     * Excepciones manejadas:
     * - PartidoInvalidoException: Si se genera un partido inválido durante la creación del calendario.
     * - PersistenciaException: Si ocurre un error al guardar el calendario generado.
     */
    public void onGenerarCalendario() {
        if (manager.getCalendario().isGenerado()) {
            int confirm = JOptionPane.showConfirmDialog(frame,
                "El calendario ya fue generado. ¿Deseas regenerarlo?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            manager.limpiarCalendario();
        }
        try {
            manager.generarCalendario();
            manager.guardarCalendario();
            panel.mostrarCalendario(manager.getCalendario(), manager.getEquipos());
            frame.mostrarMensajeExito("Calendario generado correctamente.");
        } catch (PartidoInvalidoException ex) {
            frame.mostrarMensajeError(ex.getMessage());
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        }
    }


    /**
     * Filtra los partidos del calendario por jornada. Si el calendario no ha sido generado, muestra un mensaje de error.
      * Luego, obtiene los partidos correspondientes a la jornada seleccionada y actualiza la vista para mostrar solo esos partidos.
     */
    public void onFiltrarJornada(int jornada) {
        if (!manager.getCalendario().isGenerado()) {
            frame.mostrarMensajeError("Primero genera el calendario.");
            return;
        }
        panel.mostrarPartidos(manager.getCalendario().getPartidosPorJornada(jornada), manager.getEquipos());
    }


    /**
     * Registra el resultado de un partido seleccionado en la tabla.
     * Solicita al usuario el resultado en formato "GolesLocal-GolesVisitante" mediante un diálogo.
     * Valida que ambos valores sean números enteros no negativos antes de persistir el cambio.
     * Excepciones manejadas:
     * - PersistenciaException: Si ocurre un error al guardar el calendario tras registrar el resultado.
     */
    public void onRegistrarResultado() {
        if (!manager.getCalendario().isGenerado()) {
            frame.mostrarMensajeError("Primero genera el calendario.");
            return;
        }
        Partido partido = panel.getPartidoSeleccionado();
        if (partido == null) {
            frame.mostrarMensajeError("Selecciona un partido de la tabla.");
            return;
        }
        String input = JOptionPane.showInputDialog(
            frame,
            "Ingresa el resultado (formato: GolesLocal-GolesVisitante):",
            partido.isTieneResultado() ? partido.getResultado() : ""
        );
        if (input == null) return;
        String[] partes = input.trim().split("-");
        if (partes.length != 2) {
            frame.mostrarMensajeError("Formato inválido. Usa el formato: GolesLocal-GolesVisitante (ej. 2-1).");
            return;
        }
        try {
            int golesLocal     = Integer.parseInt(partes[0].trim());
            int golesVisitante = Integer.parseInt(partes[1].trim());
            if (golesLocal < 0 || golesVisitante < 0) {
                frame.mostrarMensajeError("Los goles no pueden ser negativos.");
                return;
            }
            partido.setResultado(golesLocal + "-" + golesVisitante);
            manager.guardarCalendario();
            panel.actualizarResultadoEnTabla(partido);
            frame.mostrarMensajeExito("Resultado registrado correctamente.");
        } catch (NumberFormatException ex) {
            frame.mostrarMensajeError("Formato inválido. Los goles deben ser números enteros (ej. 2-1).");
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        }
    }


    /**
     * Limpia el calendario actual. Si el calendario no ha sido generado, no hace nada. Si el calendario ya fue generado, solicita confirmación para limpiarlo.
     * Luego, intenta limpiar el calendario, guardarlo y actualizar la vista. Si ocurre un error durante el guardado, muestra un mensaje de error correspondiente.
      * Excepciones manejadas:
     * - PersistenciaException: Si ocurre un error al guardar el calendario después de limpiarlo
     */
    public void onLimpiarCalendario() {
        if (!manager.getCalendario().isGenerado()) return;
        int confirm = JOptionPane.showConfirmDialog(frame,
            "¿Limpiar el calendario actual?",
            "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            manager.limpiarCalendario();
            manager.guardarCalendario();
            panel.limpiarCalendario();
        } catch (PersistenciaException ex) {
            frame.mostrarMensajeError("Error al guardar: " + ex.getMessage());
        }
    }
}
