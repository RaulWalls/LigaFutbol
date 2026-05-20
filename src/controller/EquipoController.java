package src.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import src.exception.EquipoDuplicadoException;
import src.exception.EquipoNotFoundException;
import src.exception.LigaException;
import src.exception.PersistenciaException;
import src.model.Equipo;
import src.model.LigaManager;
import src.view.DialogoEquipo;
import src.view.EquipoPanel;
import src.view.MainFrame;

public class EquipoController {

    private final LigaManager manager;
    private final EquipoPanel  panel;
    private final MainFrame    frame;

    public EquipoController(LigaManager manager, EquipoPanel panel, MainFrame frame) {
        this.manager = manager;
        this.panel   = panel;
        this.frame   = frame;
    }

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

    public void onBuscarEquipo() {
        String filtro = panel.getFiltroBusqueda();
        List<Equipo> filtrados = manager.getEquipos().stream()
            .filter(e -> e.getNombre().toLowerCase().contains(filtro.toLowerCase()))
            .collect(Collectors.toList());
        panel.refrescarTabla(filtrados);
    }

    public void onRefrescarLista() {
        panel.refrescarTabla(manager.getEquipos());
    }
}
