package src.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import src.controller.EquipoController;
import src.model.Equipo;

public class EquipoPanel extends JPanel {

    private JTable             tabla;
    private DefaultTableModel  modeloTabla;
    private JTextField         txtBusqueda;
    private JButton            btnBuscar;
    private JButton            btnAgregar;
    private JButton            btnEditar;
    private JButton            btnEliminar;

    private List<Equipo> equiposActuales;

    public EquipoPanel() {
        setLayout(new BorderLayout(5, 5));

        // Norte: búsqueda
        txtBusqueda = new JTextField(20);
        btnBuscar   = new JButton("Buscar");
        JPanel panelNorte = new JPanel();
        panelNorte.add(txtBusqueda);
        panelNorte.add(btnBuscar);
        add(panelNorte, BorderLayout.NORTH);

        // Centro: tabla
        String[] columnas = {"Nombre", "Sede", "Estadio", "Director Técnico", "Dueño"};
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

    public void setController(EquipoController controller) {
        btnAgregar.addActionListener(e  -> controller.onAgregarEquipo());
        btnEditar.addActionListener(e   -> controller.onModificarEquipo());
        btnEliminar.addActionListener(e -> controller.onEliminarEquipo());
        btnBuscar.addActionListener(e   -> controller.onBuscarEquipo());
    }

    public void refrescarTabla(List<Equipo> equipos) {
        this.equiposActuales = equipos;
        modeloTabla.setRowCount(0);
        for (Equipo e : equipos) {
            modeloTabla.addRow(new Object[]{
                e.getNombre(), e.getSede(), e.getNombreEstadio(),
                e.getDirectorTecnico(), e.getDueno()
            });
        }
    }

    public Equipo getEquipoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || equiposActuales == null) return null;
        return equiposActuales.get(fila);
    }

    public String getFiltroBusqueda() {
        return txtBusqueda.getText().trim();
    }

    public void limpiarSeleccion() {
        tabla.clearSelection();
    }
}
