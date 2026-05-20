package src.view;

import java.awt.BorderLayout;                       // Importamos BorderLayout para organizar los componentes en el panel
import java.util.ArrayList;                         // Importamos ArrayList para la lista paralela de partidos actuales en la tabla
import java.util.List;                              // Importamos List para manejar listas de equipos y partidos en los métodos de la vista

import javax.swing.JButton;                         // Importamos JButton para los botones de generar, filtrar y limpiar el calendario
import javax.swing.JLabel;                          // Importamos JLabel para las etiquetas de los controles
import javax.swing.JPanel;                          // Importamos JPanel para crear el panel principal de la vista del calendario
import javax.swing.JScrollPane;                     // Importamos JScrollPane para agregar scroll a la tabla de partidos
import javax.swing.JSpinner;                        // Importamos JSpinner para seleccionar la jornada a filtrar en el calendario
import javax.swing.JTable;                          // Importamos JTable para mostrar la lista de partidos en el calendario
import javax.swing.ListSelectionModel;              // Importamos ListSelectionModel para configurar la selección de filas en la tabla
import javax.swing.SpinnerNumberModel;              // Importamos SpinnerNumberModel para configurar el modelo del JSpinner que selecciona la jornada a filtrar
import javax.swing.table.DefaultTableModel;         // Importamos DefaultTableModel para manejar el modelo de datos de la tabla de partidos en el calendario

import src.controller.CalendarioController;         // Para manejar las acciones del usuario relacionadas con el calendario
import src.model.Calendario;                        // Para representar el calendario de partidos de la liga
import src.model.Equipo;                            // Para representar los equipos de la liga
import src.model.Partido;                           // Para representar los partidos de fútbol en la liga



public class CalendarioPanel extends JPanel {


    /**
     * Panel que representa la vista del calendario de partidos de la liga. Este panel muestra una tabla con los partidos 
     * programados para cada jornada, y permite al usuario generar el calendario, filtrar por jornada y limpiar el calendario. 
     * La clase se encarga de organizar los componentes visuales y de proporcionar métodos para actualizar la vista según las acciones del usuario.
     * Atributos:
     * - tabla: JTable que muestra la lista de partidos en el calendario, con columnas para la jornada, 
     *   el equipo local, el equipo visitante y el resultado del partido.
     * - modeloTabla: DefaultTableModel que maneja los datos de la tabla de partidos, permitiendo agregar, 
     *   eliminar y actualizar las filas de la tabla según el calendario actual.
     * - spinnerJornada: JSpinner que permite al usuario seleccionar la jornada para filtrar los partidos en la tabla, 
     *   mostrando solo los partidos correspondientes a la jornada seleccionada.
     * - btnFiltrar: JButton que, al hacer clic, filtra los partidos en la tabla según la jornada seleccionada en el spinnerJornada,
     *   mostrando solo los partidos correspondientes a esa jornada.
     * - btnGenerar: JButton que, al hacer clic, genera el calendario de partidos para la liga, actualiza la tabla con los nuevos partidos
     *   y muestra un mensaje de éxito o error según el resultado de la generación.
     * - btnLimpiar: JButton que, al hacer clic, limpia el calendario actual, eliminando todos los partidos de la tabla y restableciendo 
     *   el estado a no generado, mostrando un mensaje de confirmación antes de limpiar el calendario.
     */
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private JSpinner          spinnerJornada;
    private JButton           btnFiltrar;
    private JButton           btnGenerar;
    private JButton           btnLimpiar;
    private JButton           btnRegistrarResultado;
    // Lista paralela que mantiene los objetos Partido en el mismo orden que las filas de la tabla,
    // necesaria para identificar qué partido editar al seleccionar una fila.
    private final List<Partido> partidosActuales = new ArrayList<>();

    // Constructor para inicializar los componentes visuales del panel y organizar su disposición.
    public CalendarioPanel() {
        setLayout(new BorderLayout(5, 5));

        // Norte: controles de jornada y generación
        spinnerJornada = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        btnFiltrar     = new JButton("Filtrar jornada");
        btnGenerar     = new JButton("Generar Calendario");
        JPanel panelNorte = new JPanel();
        panelNorte.add(new JLabel("Jornada:"));
        panelNorte.add(spinnerJornada);
        panelNorte.add(btnFiltrar);
        panelNorte.add(btnGenerar);
        add(panelNorte, BorderLayout.NORTH);

        // Centro: tabla
        String[] columnas = {"Jornada", "Local", "Visitante", "Resultado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Sur: registrar resultado y limpiar
        btnRegistrarResultado = new JButton("Registrar Resultado");
        btnLimpiar = new JButton("Limpiar Calendario");
        JPanel panelSur = new JPanel();
        panelSur.add(btnRegistrarResultado);
        panelSur.add(btnLimpiar);
        add(panelSur, BorderLayout.SOUTH);
    }


    /** 
     * Configura los listeners para los botones de la vista, conectándolos con los métodos correspondientes del controlador.
     * - El botón "Generar Calendario" llama al método onGenerarCalendario() del controlador para generar el calendario de partidos.
     * - El botón "Filtrar jornada" llama al método onFiltrarJornada() del controlador, pasando la jornada seleccionada en el 
     *   spinnerJornada para filtrar los partidos en la tabla.
     * - El botón "Limpiar Calendario" llama al método onLimpiarCalendario() del controlador para limpiar el calendario actual 
     *   y eliminar todos los partidos de la tabla.
     */
    public void setController(CalendarioController controller) {
        btnGenerar.addActionListener(e -> controller.onGenerarCalendario());
        btnFiltrar.addActionListener(e -> controller.onFiltrarJornada(getJornadaSeleccionada()));
        btnLimpiar.addActionListener(e -> controller.onLimpiarCalendario());
        btnRegistrarResultado.addActionListener(e -> controller.onRegistrarResultado());
    }


    /** 
     * Muestra el calendario de partidos en la tabla de la vista. Este método se llama después de generar el calendario o al filtrar por jornada,
     * y actualiza la tabla con los partidos correspondientes. Para cada partido, se resuelve el nombre del equipo local y visitante utilizando 
     * el método resolverNombre(), y se muestra el resultado del partido si ya ha sido registrado, o un guion "—" si el partido aún no tiene resultado registrado.
     * Este método se utiliza para actualizar la vista con el calendario actual de partidos, mostrando toda la información relevante de cada partido en la tabla.
     */
    public void mostrarCalendario(Calendario calendario, List<Equipo> equipos) {
        modeloTabla.setRowCount(0);
        partidosActuales.clear();
        for (Partido p : calendario.getPartidos()) {
            String local     = resolverNombre(p.getEquipoLocalId(), equipos);
            String visitante = resolverNombre(p.getEquipoVisitanteId(), equipos);
            String resultado = p.isTieneResultado() ? p.getResultado() : "—";
            modeloTabla.addRow(new Object[]{p.getJornada(), local, visitante, resultado});
            partidosActuales.add(p);
        }
    }

    /** 
     * Muestra una lista de partidos en la tabla de la vista. Este método se utiliza para mostrar partidos específicos, como los filtrados por jornada.
     * Para cada partido, se resuelve el nombre del equipo local y visitante utilizando el método resolverNombre(), y se muestra el resultado del partido
     * si ya ha sido registrado, o un guion "—" si el partido aún no tiene resultado registrado.
     */
    public void mostrarPartidos(List<Partido> partidos, List<Equipo> equipos) {
        modeloTabla.setRowCount(0);
        partidosActuales.clear();
        for (Partido p : partidos) {
            String local     = resolverNombre(p.getEquipoLocalId(), equipos);
            String visitante = resolverNombre(p.getEquipoVisitanteId(), equipos);
            String resultado = p.isTieneResultado() ? p.getResultado() : "—";
            modeloTabla.addRow(new Object[]{p.getJornada(), local, visitante, resultado});
            partidosActuales.add(p);
        }
    }

    /**
     * Retorna el objeto Partido correspondiente a la fila seleccionada en la tabla,
     * usando la lista paralela partidosActuales. Retorna null si no hay selección.
     */
    public Partido getPartidoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= partidosActuales.size()) return null;
        return partidosActuales.get(fila);
    }

    /**
     * Actualiza solo la celda de resultado de la fila correspondiente al partido dado,
     * buscándolo por referencia de objeto en la lista paralela.
     */
    public void actualizarResultadoEnTabla(Partido partido) {
        int fila = partidosActuales.indexOf(partido);
        if (fila >= 0) {
            modeloTabla.setValueAt(partido.getResultado(), fila, 3);
        }
    }

    // Limpia la tabla de partidos, eliminando todas las filas y restableciendo el estado a no generado.
    public void limpiarCalendario() {
        modeloTabla.setRowCount(0);
    }

    // Devuelve la jornada seleccionada en el spinnerJornada para filtrar los partidos en la tabla.
    public int getJornadaSeleccionada() {
        return (int) spinnerJornada.getValue();
    }

    // Método privado para resolver el nombre de un equipo a partir de su ID, utilizando la lista de equipos de la liga.
    private String resolverNombre(String equipoId, List<Equipo> equipos) {
        if (Calendario.BYE_ID.equals(equipoId)) 
            return new Equipo(
                                "LOBOS DE LA FCC", 
                                "Puebla", 
                                "Estadio BUAP", 
                                "El Chanfle", 
                                "BUAP FCC"
                            ).getNombre();
        return equipos.stream()
            .filter(e -> e.getId().equals(equipoId))
            .map(Equipo::getNombre)
            .findFirst().orElse(new Equipo(
                                    "LOBOS DE LA FCC", 
                                    "Puebla", 
                                    "Estadio BUAP", 
                                    "El Chanfle", 
                                    "BUAP FCC"
                                ).getNombre());
    }
}
