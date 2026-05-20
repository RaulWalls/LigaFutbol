package src.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import src.controller.CalendarioController;
import src.model.Calendario;
import src.model.Equipo;
import src.model.Partido;



public class CalendarioPanel extends JPanel {

    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private JSpinner          spinnerJornada;
    private JButton           btnFiltrar;
    private JButton           btnGenerar;
    private JButton           btnLimpiar;

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

        // Sur: limpiar
        btnLimpiar = new JButton("Limpiar Calendario");
        JPanel panelSur = new JPanel();
        panelSur.add(btnLimpiar);
        add(panelSur, BorderLayout.SOUTH);
    }

    public void setController(CalendarioController controller) {
        btnGenerar.addActionListener(e -> controller.onGenerarCalendario());
        btnFiltrar.addActionListener(e -> controller.onFiltrarJornada(getJornadaSeleccionada()));
        btnLimpiar.addActionListener(e -> controller.onLimpiarCalendario());
    }

    public void mostrarCalendario(Calendario calendario, List<Equipo> equipos) {
        modeloTabla.setRowCount(0);
        for (Partido p : calendario.getPartidos()) {
            String local = resolverNombre(p.getEquipoLocalId(), equipos);
            String visitante = resolverNombre(p.getEquipoVisitanteId(), equipos);
            String resultado = p.isTieneResultado() ? p.getResultado() : "—";
            modeloTabla.addRow(new Object[]{p.getJornada(), local, visitante, resultado});
        }
    }

    public void mostrarPartidos(List<Partido> partidos, List<Equipo> equipos) {
        modeloTabla.setRowCount(0);
        for (Partido p : partidos) {
            String local     = resolverNombre(p.getEquipoLocalId(), equipos);
            String visitante = resolverNombre(p.getEquipoVisitanteId(), equipos);
            String resultado = p.isTieneResultado() ? p.getResultado() : "—";
            modeloTabla.addRow(new Object[]{p.getJornada(), local, visitante, resultado});
        }
    }

    public void limpiarCalendario() {
        modeloTabla.setRowCount(0);
    }

    public int getJornadaSeleccionada() {
        return (int) spinnerJornada.getValue();
    }

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
