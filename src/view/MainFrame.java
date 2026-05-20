package src.view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {

    private EquipoPanel     equipoPanel;
    private JugadorPanel    jugadorPanel;
    private CalendarioPanel calendarioPanel;

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

    public void inicializar() {
        setVisible(true);
    }

    public void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public EquipoPanel     getEquipoPanel()     { return equipoPanel; }
    public JugadorPanel    getJugadorPanel()    { return jugadorPanel; }
    public CalendarioPanel getCalendarioPanel() { return calendarioPanel; }
}
