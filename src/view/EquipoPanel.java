package src.view;

import java.awt.BorderLayout;                           // Importamos BorderLayout para organizar los componentes visuales del panel.
import java.util.List;                                  // Importamos List para manejar listas de equipos en los métodos de la vista.
import javax.swing.JButton;                             // Importamos JButton para los botones de agregar, editar, eliminar y buscar equipos.
import javax.swing.JPanel;                              // Importamos JPanel para crear el panel principal de la vista de equipos.
import javax.swing.JScrollPane;                         // Importamos JScrollPane para agregar scroll a la tabla de equipos.
import javax.swing.JTable;                              // Importamos JTable para mostrar la lista de equipos en una tabla.
import javax.swing.JTextField;                          // Importamos JTextField para el campo de búsqueda de equipos.
import javax.swing.ListSelectionModel;                  // Importamos ListSelectionModel para configurar la selección de filas en la tabla de equipos.
import javax.swing.table.DefaultTableModel;             // Importamos DefaultTableModel para manejar el modelo de datos de la tabla de equipos.

import src.controller.EquipoController;                
import src.model.Equipo;

public class EquipoPanel extends JPanel {

    /**
     * Panel que representa la vista de la lista de equipos en la liga. Este panel muestra una tabla con los equipos registrados,
     * y proporciona botones para agregar, editar, eliminar y buscar equipos. La clase se encarga de organizar los componentes 
     * visuales y de proporcionar métodos para actualizar la vista según las acciones del usuario.
     * Atributos:
     * - tabla: JTable que muestra la lista de equipos en la liga, con columnas para el nombre, la sede, el estadio, 
     *   el director técnico y el dueño de cada equipo.
     * - modeloTabla: DefaultTableModel que maneja los datos de la tabla de equipos, permitiendo agregar, eliminar 
     *   y actualizar las filas de la tabla según los equipos actuales en la liga.
     * - txtBusqueda: JTextField que permite al usuario ingresar un término de búsqueda para filtrar los equipos en la tabla.
     * - btnBuscar: JButton que, al hacer clic, filtra los equipos en la tabla según el término de búsqueda.
     * - btnAgregar: JButton que, al hacer clic, muestra un diálogo para agregar un nuevo equipo a la liga.
     * - btnEditar: JButton que, al hacer clic, muestra un diálogo para editar el equipo seleccionado en la tabla.
     * - btnEliminar: JButton que, al hacer clic, muestra un mensaje de confirmación para eliminar el equipo seleccionado en la tabla.
     * - equiposActuales: Lista de los equipos actualmente mostrados en la tabla.
     */

    private JTable             tabla;
    private DefaultTableModel  modeloTabla;
    private JTextField         txtBusqueda;
    private JButton            btnBuscar;
    private JButton            btnAgregar;
    private JButton            btnEditar;
    private JButton            btnEliminar;

    private List<Equipo> equiposActuales;

    // Constructor para inicializar los componentes visuales del panel y organizar su disposición.
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

    // Método para configurar el controlador que manejará las acciones de los botones en la vista de equipos.
    public void setController(EquipoController controller) {
        btnAgregar.addActionListener(e  -> controller.onAgregarEquipo());
        btnEditar.addActionListener(e   -> controller.onModificarEquipo());
        btnEliminar.addActionListener(e -> controller.onEliminarEquipo());
        btnBuscar.addActionListener(e   -> controller.onBuscarEquipo());
    }

    // Método para actualizar la tabla de equipos con una nueva lista de equipos, limpiando la tabla y agregando las filas correspondientes a cada equipo.
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

    // Método para obtener el equipo seleccionado en la tabla, devolviendo null si no hay ningún equipo seleccionado o si la lista de equipos actuales es null.
    public Equipo getEquipoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || equiposActuales == null) return null;
        return equiposActuales.get(fila);
    }

    // Método para obtener el término de búsqueda ingresado por el usuario en el campo de texto de búsqueda, devolviendo una cadena vacía si el campo está vacío.
    public String getFiltroBusqueda() {
        return txtBusqueda.getText().trim();
    }

    // Método para limpiar la selección actual en la tabla de equipos, deseleccionando cualquier fila seleccionada.
    public void limpiarSeleccion() {
        tabla.clearSelection();
    }
}
