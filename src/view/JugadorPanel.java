package src.view;

import java.awt.BorderLayout;                       // Importamos BorderLayout para organizar los componentes en el panel.
import java.util.List;                              // Importamos List para manejar listas de jugadores y equipos en los métodos de la vista.

import javax.swing.JButton;                         // Importamos JButton para los botones de agregar, editar, eliminar, filtrar y mostrar todos los jugadores.
import javax.swing.JComboBox;                       // Importamos JComboBox para el combo de selección de equipos para filtrar los jugadores por equipo.
import javax.swing.JPanel;                          // Importamos JPanel para crear el panel principal de la vista de jugadores.
import javax.swing.JScrollPane;                     // Importamos JScrollPane para agregar scroll a la tabla de jugadores.
import javax.swing.JTable;                          // Importamos JTable para mostrar la lista de jugadores en una tabla.
import javax.swing.ListSelectionModel;              // Importamos ListSelectionModel para configurar la selección de filas en la tabla de jugadores.
import javax.swing.table.DefaultTableModel;         // Importamos DefaultTableModel para manejar el modelo de datos de la tabla de jugadores.

import src.controller.JugadorController;
import src.model.Equipo;
import src.model.Jugador;

public class JugadorPanel extends JPanel {

    /**
     * Panel que representa la vista de la lista de jugadores en la liga. Este panel muestra una tabla con los jugadores registrados,
     * y proporciona botones para agregar, editar, eliminar, filtrar por equipo y mostrar todos los jugadores. La clase se encarga de organizar los componentes visuales y de proporcionar métodos para actualizar la vista según las acciones del usuario.
     * Atributos:
     * - tabla: JTable que muestra la lista de jugadores en la liga, con columnas para el nombre, la dirección,
     *   la fecha de nacimiento, el lugar de nacimiento, el equipo al que pertenecen y la edad de cada jugador.
     * - modeloTabla: DefaultTableModel que maneja los datos de la tabla de jugadores, permitiendo agregar, 
     *   eliminar y actualizar las filas de la tabla según los jugadores actuales en la liga.
     * - comboEquipos: JComboBox que permite al usuario seleccionar un equipo para filtrar los jugadores en la tabla, 
     *   mostrando solo los jugadores que pertenecen al equipo seleccionado.
     * - btnTodos: JButton que, al hacer clic, muestra todos los jugadores en la tabla, eliminando cualquier filtro aplicado por el combo de equipos.
     * - btnAgregar: JButton que, al hacer clic, muestra un diálogo para agregar un nuevo jugador a la liga.
     * - btnEditar: JButton que, al hacer clic, muestra un diálogo para editar el jugador seleccionado en la tabla.
     * - btnEliminar: JButton que, al hacer clic, muestra un mensaje de confirmación para eliminar el jugador seleccionado en la tabla.
     * - jugadoresActuales: Lista de los jugadores actualmente mostrados en la tabla.
     * - equiposActuales: Lista de los equipos actualmente disponibles en la liga.
     */
    private JTable             tabla;
    private DefaultTableModel  modeloTabla;
    private JComboBox<String>  comboEquipos;
    private JButton            btnTodos;
    private JButton            btnAgregar;
    private JButton            btnEditar;
    private JButton            btnEliminar;

    private List<Jugador> jugadoresActuales;
    private List<Equipo>  equiposActuales;

    // Constructor para inicializar los componentes visuales del panel y organizar su disposición.
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

    // Método para configurar el controlador que manejará las acciones de los botones en la vista de jugadores
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

    // Método para actualizar la tabla de jugadores con una nueva lista de jugadores y equipos,
    // limpiando la tabla y agregando las filas correspondientes a cada jugador.
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

    // Método para actualizar el combo de equipos con una nueva lista de equipos,
    // limpiando el combo y agregando las opciones correspondientes a cada equipo.
    public void refrescarComboEquipos(List<Equipo> equipos) {
        this.equiposActuales = equipos;
        comboEquipos.removeAllItems();
        for (Equipo e : equipos) comboEquipos.addItem(e.getNombre());
    }

    // Método para obtener el jugador seleccionado en la tabla.
    public Jugador getJugadorSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || jugadoresActuales == null) return null;
        return jugadoresActuales.get(fila);
    }

    // Método para obtener el término de búsqueda ingresado por el usuario en el campo de texto de búsqueda.
    public List<Equipo> getEquiposActuales() {
        return equiposActuales;
    }

    // Método para limpiar la selección actual en la tabla de jugadores, deseleccionando cualquier fila seleccionada.
    public void limpiarSeleccion() {
        tabla.clearSelection();
    }
}
