package src.controller;

import javax.swing.JOptionPane;

import src.exception.JugadorNotFoundException;
import src.exception.LigaException;
import src.exception.PersistenciaException;
import src.model.Jugador;
import src.model.LigaManager;
import src.view.DialogoJugador;
import src.view.JugadorPanel;
import src.view.MainFrame;

public class JugadorController {

    private final LigaManager  manager;
    private final JugadorPanel panel;
    private final MainFrame    frame;

    public JugadorController(LigaManager manager, JugadorPanel panel, MainFrame frame) {
        this.manager = manager;
        this.panel   = panel;
        this.frame   = frame;
    }

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

    public void onFiltrarPorEquipo(String equipoId) {
        panel.refrescarTabla(manager.getJugadoresPorEquipo(equipoId), manager.getEquipos());
    }

    public void onRefrescarLista() {
        panel.refrescarTabla(manager.getJugadores(), manager.getEquipos());
        panel.refrescarComboEquipos(manager.getEquipos());
    }
}
