package src.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import src.controller.JugadorController;
import src.model.Equipo;
import src.model.Jugador;

public class JugadorPanel extends JPanel {

    private JTable             tabla;
    private DefaultTableModel  modeloTabla;
    private JComboBox<String>  comboEquipos;
    private JButton            btnTodos;
    private JButton            btnAgregar;
    private JButton            btnEditar;
    private JButton            btnEliminar;

    private List<Jugador> jugadoresActuales;
    private List<Equipo>  equiposActuales;

    public JugadorPanel() {
        setLayout(new BorderLayout(5, 5));

        // Norte: filtro por equipo
        comboEquipos = new JComboBox<>();
        btnTodos     = new JButton("Todos");
        JPanel panelNorte = new JPanel();
        panelNorte.add(comboEquipos);
        panelNorte.add(btnTodos);
        add(panelNorte, BorderLayout.NORTH);

        // Centro: tabla
        String[] columnas = {"Nombre", "Dirección", "Fecha Nac.", "Lugar Nac.", "Equipo", "Edad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Sur: botones
        btnAgregar  = new JButton("Agregar");
        btnEditar   = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        JPanel panelSur = new JPanel();
        panelSur.add(btnAgregar);
        panelSur.add(btnEditar);
        panelSur.add(btnEliminar);
        add(panelSur, BorderLayout.SOUTH);
    }

    public void setController(JugadorController controller) {
        btnAgregar.addActionListener(e  -> controller.onAgregarJugador());
        btnEditar.addActionListener(e   -> controller.onModificarJugador());
        btnEliminar.addActionListener(e -> controller.onEliminarJugador());
        btnTodos.addActionListener(e    -> controller.onRefrescarLista());
        comboEquipos.addActionListener(e -> {
            int idx = comboEquipos.getSelectedIndex();
            if (idx >= 0 && equiposActuales != null && idx < equiposActuales.size()) {
                controller.onFiltrarPorEquipo(equiposActuales.get(idx).getId());
            }
        });
    }

    public void refrescarTabla(List<Jugador> jugadores, List<Equipo> equipos) {
        this.jugadoresActuales = jugadores;
        this.equiposActuales   = equipos;
        modeloTabla.setRowCount(0);
        for (Jugador j : jugadores) {
            String nombreEquipo = equipos.stream()
                .filter(e -> e.getId().equals(j.getEquipoId()))
                .map(Equipo::getNombre)
                .findFirst().orElse("—");
            modeloTabla.addRow(new Object[]{
                j.getNombre(), j.getDireccion(),
                j.getFechaNacimiento(), j.getLugarNacimiento(),
                nombreEquipo, j.getEdad()
            });
        }
    }

    public void refrescarComboEquipos(List<Equipo> equipos) {
        this.equiposActuales = equipos;
        comboEquipos.removeAllItems();
        for (Equipo e : equipos) comboEquipos.addItem(e.getNombre());
    }

    public Jugador getJugadorSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || jugadoresActuales == null) return null;
        return jugadoresActuales.get(fila);
    }

    public List<Equipo> getEquiposActuales() {
        return equiposActuales;
    }

    public void limpiarSeleccion() {
        tabla.clearSelection();
    }
}
