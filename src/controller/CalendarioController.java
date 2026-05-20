package src.controller;

import javax.swing.JOptionPane;

import src.exception.PartidoInvalidoException;
import src.exception.PersistenciaException;
import src.model.LigaManager;
import src.view.CalendarioPanel;
import src.view.MainFrame;

public class CalendarioController {

    private final LigaManager    manager;
    private final CalendarioPanel panel;
    private final MainFrame       frame;

    public CalendarioController(LigaManager manager, CalendarioPanel panel, MainFrame frame) {
        this.manager = manager;
        this.panel   = panel;
        this.frame   = frame;
    }

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

    public void onFiltrarJornada(int jornada) {
        if (!manager.getCalendario().isGenerado()) {
            frame.mostrarMensajeError("Primero genera el calendario.");
            return;
        }
        panel.mostrarPartidos(manager.getCalendario().getPartidosPorJornada(jornada), manager.getEquipos());
    }

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
