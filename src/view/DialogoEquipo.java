package src.view;

import java.awt.Dimension;                      // Importamos Dimension para establecer el tamaño preferido del ícono del equipo en el diálogo.
import java.awt.Frame;                          // Importamos Frame para establecer el padre del diálogo, lo que permite centrarlo correctamente en la pantalla.
import java.awt.GridBagConstraints;             // Importamos GridBagConstraints para organizar los componentes del diálogo utilizando un layout flexible y adaptable.
import java.awt.GridBagLayout;                  // Importamos GridBagLayout para organizar los componentes del diálogo en una cuadrícula.
import java.awt.Image;                          // Importamos Image para manejar la imagen del ícono del equipo en el diálogo.
import java.awt.Insets;                         // Importamos Insets para establecer los márgenes entre los componentes del diálogo, mejorando la apariencia visual.

import javax.swing.ImageIcon;                   // Importamos ImageIcon para cargar y mostrar la imagen del ícono del equipo en el diálogo.
import javax.swing.JButton;                     // Importamos JButton para los botones de aceptar y cancelar en el diálogo.
import javax.swing.JDialog;                     // Importamos JDialog para crear un diálogo modal que permita al usuario ingresar los datos de un equipo.
import javax.swing.JLabel;                      // Importamos JLabel para las etiquetas de los campos de entrada en el diálogo.
import javax.swing.JOptionPane;                 // Importamos JOptionPane para mostrar mensajes de validación y error al usuario en el diálogo.
import javax.swing.JTextField;                  // Importamos JTextField para los campos de entrada de texto en el diálogo.

import src.model.Equipo;

public class DialogoEquipo extends JDialog {


    /**
     * Diálogo para agregar o editar un equipo en la liga. Este diálogo permite al usuario ingresar los datos de un equipo, 
     * como el nombre, la sede, el estadio, el director técnico y el dueño.
     * El diálogo se muestra de forma modal, bloqueando la interacción con la ventana principal hasta que el usuario confirme 
     * o cancele la acción. La clase se encarga de organizar los componentes visuales del diálogo, validar los datos ingresados 
     * por el usuario y devolver un objeto Equipo con los datos ingresados si el usuario confirma la acción, o null si el usuario cancela.
     * Atributos:
     * - txtNombre: Campo de texto para ingresar el nombre del equipo.
     * - txtSede: Campo de texto para ingresar la sede del equipo.
     * - txtNombreEstadio: Campo de texto para ingresar el nombre del estadio del equipo.
     * - txtDirectorTecnico: Campo de texto para ingresar el nombre del director técnico del equipo.
     * - txtDueno: Campo de texto para ingresar el nombre del dueño del equipo.
     * - resultado: Objeto Equipo que almacena el resultado del diálogo, con los datos ingresados por el usuario si confirma la acción, o null si cancela.
     */
    private JTextField txtNombre;
    private JTextField txtSede;
    private JTextField txtNombreEstadio;
    private JTextField txtDirectorTecnico;
    private JTextField txtDueno;

    private Equipo resultado = null;

    // Constructor para inicializar el diálogo, organizar los componentes visuales y configurar los listeners de los botones.
    public DialogoEquipo(Frame parent, Equipo equipoAEditar) {
        super(parent, equipoAEditar == null ? "Nuevo Equipo" : "Editar Equipo", true);

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Icono ilustrativo del equipo, centrado en la parte superior
        java.net.URL imgUrl = getClass().getClassLoader().getResource("src/resources/equipo.png");
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


        // Campos de entrada para los datos del equipo, organizados en una cuadrícula con etiquetas a la izquierda y campos de texto a la derecha.
        txtNombre          = new JTextField(20);
        txtSede            = new JTextField(20);
        txtNombreEstadio   = new JTextField(20);
        txtDirectorTecnico = new JTextField(20);
        txtDueno           = new JTextField(20);

        String[] etiquetas = {"Nombre:", "Sede:", "Estadio:", "Director técnico:", "Dueño:"};
        JTextField[] campos = {txtNombre, txtSede, txtNombreEstadio, txtDirectorTecnico, txtDueno};

        // Agrega las etiquetas y los campos de texto al diálogo utilizando GridBagLayout para organizar su disposición.
        for (int i = 0; i < etiquetas.length; i++) {
            gbc.gridx = 0; gbc.gridy = i + 1;
            add(new JLabel(etiquetas[i]), gbc);
            gbc.gridx = 1;
            add(campos[i], gbc);
        }

        // Botones de aceptar y cancelar, organizados en la parte inferior del diálogo.
        JButton btnAceptar   = new JButton("Aceptar");
        JButton btnCancelar  = new JButton("Cancelar");

        gbc.gridx = 0; gbc.gridy = etiquetas.length + 1;
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

    //Precarga los datos de un equipo en los campos de texto del diálogo para facilitar la edición. 
    private void precargarDatos(Equipo equipo) {
        txtNombre.setText(equipo.getNombre());
        txtSede.setText(equipo.getSede());
        txtNombreEstadio.setText(equipo.getNombreEstadio());
        txtDirectorTecnico.setText(equipo.getDirectorTecnico());
        txtDueno.setText(equipo.getDueno());
    }

    // Método privado para validar que todos los campos de texto del diálogo estén llenos antes de aceptar la entrada del usuario.
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

    // Método público para mostrar el diálogo y obtener el resultado ingresado por el usuario. 
    public Equipo mostrarYObtenerResultado() {
        setVisible(true);
        return resultado;
    }
}
