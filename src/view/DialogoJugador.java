package src.view;

import java.awt.Dimension;                              // Importamos Dimension para establecer el tamaño preferido de componentes visuales.
import java.awt.Frame;                                  // Importamos Frame para establecer el padre del diálogo, lo que permite centrarlo correctamente en la pantalla.
import java.awt.GridBagConstraints;                     // Importamos GridBagConstraints para organizar los componentes del diálogo utilizando un layout flexible y adaptable.
import java.awt.GridBagLayout;                          // Importamos GridBagLayout para organizar los componentes del diálogo en una cuadrícula.
import java.awt.Image;                                  // Importamos Image para manejar la imagen del ícono del jugador en el diálogo.
import java.awt.Insets;                                 // Importamos Insets para establecer los márgenes entre los componentes del diálogo, mejorando la apariencia visual.
import java.time.LocalDate;                             // Importamos LocalDate para manejar las fechas de nacimiento de los jugadores y calcular su edad.
import java.time.format.DateTimeFormatter;              // Importamos DateTimeFormatter para definir el formato de fecha.
import java.time.format.DateTimeParseException;         // Importamos DateTimeParseException para manejar errores de formato.
import java.util.List;                                  // Importamos List para manejar la lista de equipos disponibles al seleccionar el equipo al que pertenece un jugador.

import javax.swing.ImageIcon;                           // Importamos ImageIcon para cargar y mostrar la imagen del ícono del jugador en el diálogo.
import javax.swing.JButton;                             // Importamos JButton para los botones de aceptar y cancelar en el diálogo.
import javax.swing.JComboBox;                           // Importamos JComboBox para permitir al usuario seleccionar el equipo al que pertenece el jugador desde una lista desplegable.
import javax.swing.JDialog;                             // Importamos JDialog para crear un diálogo modal que permita al usuario ingresar los datos de un jugador.
import javax.swing.JLabel;                              // Importamos JLabel para las etiquetas de los campos de entrada en el diálogo.
import javax.swing.JOptionPane;                         // Importamos JOptionPane para mostrar mensajes de validación y error al usuario en el diálogo.
import javax.swing.JTextField;                          // Importamos JTextField para los campos de entrada de texto en el diálogo.

import src.model.Equipo;
import src.model.Jugador;

public class DialogoJugador extends JDialog {


    /**
     * Diálogo para agregar o editar un jugador en la liga. Este diálogo permite al usuario ingresar los datos de un jugador,
     * como el nombre, la dirección, la fecha de nacimiento, el lugar de nacimiento y el equipo al que pertenece. El diálogo se muestra de forma modal, bloqueando la interacción con la ventana principal hasta que el usuario confirme o cancele la acción. La clase se encarga de organizar
     * los componentes visuales del diálogo, validar los datos ingresados por el usuario y devolver un objeto Jugador con los datos ingresados si el usuario confirma la acción, o null si el usuario cancela.
     * Atributos:
     * - txtNombre: Campo de texto para ingresar el nombre del jugador.
     * - txtDireccion: Campo de texto para ingresar la dirección del jugador.
     * - txtLugarNacimiento: Campo de texto para ingresar el lugar de nacimiento del jugador.
     * - txtFechaNacimiento: Campo de texto para ingresar la fecha de nacimiento del jugador, con un formato específico (dd/MM/yyyy).
     * - comboEquipo: ComboBox para seleccionar el equipo al que pertenece el jugador, mostrando una lista de los equipos disponibles en la liga.
     */
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField          txtNombre;
    private JTextField          txtDireccion;
    private JTextField          txtLugarNacimiento;
    private JTextField          txtFechaNacimiento;
    private JComboBox<Equipo>   comboEquipo;

    private Jugador resultado = null;

    // Constructor para inicializar el diálogo, organizar los componentes visuales y configurar los listeners de los botones.
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

        // Campos de entrada para los datos del jugador, organizados en una cuadrícula con etiquetas a la izquierda y campos de texto a la derecha.
        txtNombre          = new JTextField(20);
        txtDireccion       = new JTextField(20);
        txtLugarNacimiento = new JTextField(20);
        txtFechaNacimiento = new JTextField(20);
        txtFechaNacimiento.setToolTipText("Formato: dd/MM/yyyy");
        comboEquipo = new JComboBox<>();
        for (Equipo e : equipos) comboEquipo.addItem(e);

        String[]      etiquetas = {"Nombre:", "Dirección:", "Lugar de nacimiento:", "Fecha de nacimiento:", "Equipo:"};
        Object[]      controles = {txtNombre, txtDireccion, txtLugarNacimiento, txtFechaNacimiento, comboEquipo};

        // Agrega las etiquetas y los campos de texto al diálogo utilizando GridBagLayout para organizar su disposición.
        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            add((java.awt.Component) controles[i], gbc);
        }

        // Botones de aceptar y cancelar, organizados en la parte inferior del diálogo.
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

    // Precarga los datos de un jugador en los campos de texto del diálogo para facilitar la edición. 
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

    // Método privado para validar que todos los campos de texto del diálogo estén llenos antes de aceptar la entrada del usuario.
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

    // Método público para mostrar el diálogo y obtener el resultado ingresado por el usuario.
    public Jugador mostrarYObtenerResultado() {
        setVisible(true);
        return resultado;
    }
}
