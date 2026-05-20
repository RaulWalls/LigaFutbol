import javax.swing.SwingUtilities;

import src.controller.CalendarioController;
import src.controller.EquipoController;
import src.controller.JugadorController;
import src.model.LigaManager;
import src.view.MainFrame;

public class Main {

    public static void main(String[] args) {

        // 1. Modelo — carga datos desde archivos si existen
        LigaManager manager = new LigaManager();

        // 2. Vista — crea la ventana y los tres paneles vacíos
        MainFrame frame = new MainFrame();

        // 3. Controllers
        EquipoController     ec = new EquipoController(manager, frame.getEquipoPanel(), frame);
        JugadorController    jc = new JugadorController(manager, frame.getJugadorPanel(), frame);
        CalendarioController cc = new CalendarioController(manager, frame.getCalendarioPanel(), frame);

        // 4. Conectar controllers con sus paneles
        frame.getEquipoPanel().setController(ec);
        frame.getJugadorPanel().setController(jc);
        frame.getCalendarioPanel().setController(cc);

        // 5. Poblar vistas con los datos recuperados
        ec.onRefrescarLista();
        jc.onRefrescarLista();
        if (manager.getCalendario().isGenerado()) {
            frame.getCalendarioPanel().mostrarCalendario(
                manager.getCalendario(), manager.getEquipos()
            );
        }

        // 6. Mostrar ventana en el hilo de Swing (EDT)
        SwingUtilities.invokeLater(() -> frame.inicializar());
    }
}
