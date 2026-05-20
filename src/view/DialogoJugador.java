package src.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import src.model.Equipo;
import src.model.Jugador;

public class DialogoJugador extends JDialog {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField          txtNombre;
    private JTextField          txtDireccion;
    private JTextField          txtLugarNacimiento;
    private JTextField          txtFechaNacimiento;
    private JComboBox<Equipo>   comboEquipo;

    private Jugador resultado = null;

    public DialogoJugador(Frame parent, Jugador jugadorAEditar, List<Equipo> equipos) {
        super(parent, jugadorAEditar == null ? "Nuevo Jugador" : "Editar Jugador", true);

        setSize(520, 420);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ícono ilustrativo del jugador, centrado en la parte superior
        java.net.URL imgUrl = getClass().getClassLoader().getResource("src/resources/jugador.png");
        JLabel iconoLabel = new JLabel();
        if (imgUrl != null) {
            Image img = new ImageIcon(imgUrl).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            iconoLabel.setIcon(new ImageIcon(img));
        }
        iconoLabel.setPreferredSize(new Dimension(80, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(iconoLabel, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre          = new JTextField(20);
        txtDireccion       = new JTextField(20);
        txtLugarNacimiento = new JTextField(20);
        txtFechaNacimiento = new JTextField(20);
        txtFechaNacimiento.setToolTipText("Formato: dd/MM/yyyy");
        comboEquipo = new JComboBox<>();
        for (Equipo e : equipos) comboEquipo.addItem(e);

        String[]      etiquetas = {"Nombre:", "Dirección:", "Lugar de nacimiento:", "Fecha de nacimiento:", "Equipo:"};
        Object[]      controles = {txtNombre, txtDireccion, txtLugarNacimiento, txtFechaNacimiento, comboEquipo};

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            add((java.awt.Component) controles[i], gbc);
        }

        JButton btnAceptar  = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");

        gbc.gridx = 0; gbc.gridy = etiquetas.length + 1;
        add(btnAceptar, gbc);
        gbc.gridx = 1;
        add(btnCancelar, gbc);

        if (jugadorAEditar != null) {
            precargarDatos(jugadorAEditar, equipos);
        }

        btnAceptar.addActionListener(e -> {
            if (validarCampos()) {
                LocalDate fecha = LocalDate.parse(txtFechaNacimiento.getText().trim(), FORMATO_FECHA);
                Equipo equipoSeleccionado = (Equipo) comboEquipo.getSelectedItem();
                resultado = new Jugador(
                    txtNombre.getText().trim(),
                    txtDireccion.getText().trim(),
                    fecha,
                    txtLugarNacimiento.getText().trim(),
                    equipoSeleccionado.getId()
                );
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void precargarDatos(Jugador jugador, List<Equipo> equipos) {
        txtNombre.setText(jugador.getNombre());
        txtDireccion.setText(jugador.getDireccion());
        txtLugarNacimiento.setText(jugador.getLugarNacimiento());
        txtFechaNacimiento.setText(jugador.getFechaNacimiento().format(FORMATO_FECHA));
        for (int i = 0; i < comboEquipo.getItemCount(); i++) {
            if (comboEquipo.getItemAt(i).getId().equals(jugador.getEquipoId())) {
                comboEquipo.setSelectedIndex(i);
                break;
            }
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() ||
            txtDireccion.getText().trim().isEmpty() ||
            txtLugarNacimiento.getText().trim().isEmpty() ||
            txtFechaNacimiento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            LocalDate.parse(txtFechaNacimiento.getText().trim(), FORMATO_FECHA);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato dd/MM/yyyy.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (comboEquipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un equipo.", "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    public Jugador mostrarYObtenerResultado() {
        setVisible(true);
        return resultado;
    }
}
