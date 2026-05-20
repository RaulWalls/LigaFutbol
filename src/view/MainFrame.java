package src.view;

import javax.swing.ImageIcon;               // Importamos ImageIcon para establecer el ícono de la ventana principal.
import javax.swing.JFrame;                  // Importamos JFrame para crear la ventana principal de la aplicación.
import javax.swing.JOptionPane;             // Importamos JOptionPane para mostrar mensajes de error o éxito al usuario.
import javax.swing.JTabbedPane;             // Importamos JTabbedPane para organizar las diferentes secciones de la aplicación (Equipos, Jugadores, Calendario) en pestañas.

public class MainFrame extends JFrame {

    /**
     * Ventana principal de la aplicación que contiene las pestañas para gestionar equipos, jugadores y el calendario de partidos. Esta clase se encarga de inicializar la interfaz gráfica, organizar los paneles correspondientes a cada sección y proporcionar métodos para mostrar mensajes al usuario.
     * Atributos:
     * - equipoPanel: Panel que representa la vista de la lista de equipos en la liga.
     * - jugadorPanel: Panel que representa la vista de la lista de jugadores en la liga.
     * - calendarioPanel: Panel que representa la vista del calendario de partidos de la liga.
     */
    private EquipoPanel     equipoPanel;
    private JugadorPanel    jugadorPanel;
    private CalendarioPanel calendarioPanel;

    // Constructor para inicializar la ventana principal, configurar su título, tamaño, comportamiento de cierre, ubicación y establecer el ícono de la aplicación.
    public MainFrame() {
        setTitle("Liga Mexicana de Futbol");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("src/resources/logo.png").getImage());

        equipoPanel     = new EquipoPanel();
        jugadorPanel    = new JugadorPanel();
        calendarioPanel = new CalendarioPanel();

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Equipos",     equipoPanel);
        pestanas.addTab("Jugadores",   jugadorPanel);
        pestanas.addTab("Calendario",  calendarioPanel);

        add(pestanas);
    }

    // Método para mostrar la ventana principal, estableciendo su visibilidad a true.
    public void inicializar() {
        setVisible(true);
    }

    // Métodos para mostrar mensajes de error o éxito al usuario utilizando JOptionPane, con el título y tipo de mensaje correspondientes.
    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Método para mostrar un mensaje de éxito al usuario utilizando JOptionPane, con el título y tipo de mensaje correspondientes.
    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    // Metodo para mostrar la vista de equipos.
    public EquipoPanel getEquipoPanel() { 
        return equipoPanel; 
    }

    // Metodo para mostrar la vista de jugadores.
    public JugadorPanel getJugadorPanel() { 
        return jugadorPanel; 
    }

    // Metodo para mostrar la vista del calendario.
    public CalendarioPanel getCalendarioPanel() { 
        return calendarioPanel; 
    }
}
