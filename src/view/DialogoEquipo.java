package src.view;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import src.model.Equipo;

public class DialogoEquipo extends JDialog {

    private JTextField txtNombre;
    private JTextField txtSede;
    private JTextField txtNombreEstadio;
    private JTextField txtDirectorTecnico;
    private JTextField txtDueno;

    private Equipo resultado = null;

    public DialogoEquipo(Frame parent, Equipo equipoAEditar) {
        super(parent, equipoAEditar == null ? "Nuevo Equipo" : "Editar Equipo", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre          = new JTextField(20);
        txtSede            = new JTextField(20);
        txtNombreEstadio   = new JTextField(20);
        txtDirectorTecnico = new JTextField(20);
        txtDueno           = new JTextField(20);

        String[] etiquetas = {"Nombre:", "Sede:", "Estadio:", "Director técnico:", "Dueño:"};
        JTextField[] campos = {txtNombre, txtSede, txtNombreEstadio, txtDirectorTecnico, txtDueno};

        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            add(campos[i], gbc);
        }

        JButton btnAceptar   = new JButton("Aceptar");
        JButton btnCancelar  = new JButton("Cancelar");

        gbc.gridx = 0; gbc.gridy = etiquetas.length;
        add(btnAceptar, gbc);
        gbc.gridx = 1;
        add(btnCancelar, gbc);

        if (equipoAEditar != null) {
            precargarDatos(equipoAEditar);
        }

        btnAceptar.addActionListener(e -> {
            if (validarCampos()) {
                resultado = new Equipo(
                    txtNombre.getText().trim(),
                    txtSede.getText().trim(),
                    txtNombreEstadio.getText().trim(),
                    txtDirectorTecnico.getText().trim(),
                    txtDueno.getText().trim()
                );
                dispose();
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void precargarDatos(Equipo equipo) {
        txtNombre.setText(equipo.getNombre());
        txtSede.setText(equipo.getSede());
        txtNombreEstadio.setText(equipo.getNombreEstadio());
        txtDirectorTecnico.setText(equipo.getDirectorTecnico());
        txtDueno.setText(equipo.getDueno());
    }

    private boolean validarCampos() {
        JTextField[] campos = {txtNombre, txtSede, txtNombreEstadio, txtDirectorTecnico, txtDueno};
        for (JTextField campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public Equipo mostrarYObtenerResultado() {
        setVisible(true);
        return resultado;
    }
}
