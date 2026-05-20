package src.model;

import java.io.Serializable;                    // Aseguramos que la clase implementa Serializable para permitir su serialización
import java.util.ArrayList;                     // Importamos ArrayList para manejar la lista de partidos
import java.util.List;                          // Importamos List para usarlo como tipo de la lista de partidos

import src.exception.PartidoInvalidoException;  // Importamos la excepción personalizada para manejar errores al generar el calendario

public class Calendario implements Serializable {

    /**
     * Clase que representa el calendario de partidos de la liga. Esta clase es responsable de generar el calendario 
     * de partidos utilizando el algoritmo de Berger, asegurando que cada equipo juegue contra todos los demás equipos 
     * tanto en casa como fuera, y manejando casos con un número impar de equipos mediante la inclusión de un equipo default (LOBOS DE LA FCC). 
     * Además, proporciona métodos para obtener los partidos por jornada y limpiar el calendario.
     * Atributos:
     * - partidos: Lista de partidos programados en el calendario.
     * - totalJornadas: Número total de jornadas en el calendario, calculado en función del número de equipos.
     * - generado: Indicador booleano que indica si el calendario ha sido generado o no, para evitar regeneraciones innecesarias sin confirmación.
     * Excepciones:
     * - PartidoInvalidoException: Se lanza si se intenta generar un calendario con un número insuficiente de equipos (menos de 2) 
     *   o si se genera un partido inválido durante la creación del calendario.
     * Nota: El equipo "LOBOS DE LA FCC" se utiliza como un equipo de descanso (bye) cuando el número de equipos es impar, 
     *       y tiene un ID fijo para facilitar su identificación en el calendario.
     */
    private static final long serialVersionUID = 1L;

    private List<Partido> partidos;
    private int totalJornadas;
    private boolean generado;

    public Calendario() {
        this.partidos = new ArrayList<>();
        this.totalJornadas = 0;
        this.generado = false;
    }

    // ID fijo para el equipo de descanso (bye) cuando el número de equipos es impar, para facilitar su identificación en el calendario.
    public static final String BYE_ID = "equipo-bye-descanso";


    /**
     * Genera el calendario de partidos para la liga utilizando el algoritmo de Berger.
     * Se generan los partidos para la fase de ida y vuelta, asegurando que cada equipo juegue contra todos los demás 
     * equipos tanto en casa como fuera. Si el calendario ya ha sido generado, se lanza una excepción para evitar regeneraciones 
     * innecesarias sin confirmación. Si ocurre un error durante la generación del calendario, se lanza una excepción 
     * PartidoInvalidoException con un mensaje descriptivo del error.
     * Excepciones manejadas:
     * - PartidoInvalidoException: Si se intenta generar un calendario con un número insuficiente de equipos (menos de 2) 
     *   o si se genera un partido inválido durante la creación del calendario.    
     */
    public void generarCalendario(List<Equipo> equipos) throws PartidoInvalidoException {
        if (equipos == null || equipos.size() < 2) {
            throw new PartidoInvalidoException("Se necesitan al menos 2 equipos para generar el calendario.");
        }

        partidos.clear();

        List<Equipo> lista = new ArrayList<>(equipos);

        // Si N es impar, agregar equipo bye con ID fijo para que siempre sea reconocible
        if (lista.size() % 2 != 0) {
            lista.add(new Equipo(
                                    "LOBOS DE LA FCC", 
                                    "Puebla", 
                                    "Estadio BUAP", 
                                    "El Chanfle", 
                                    "BUAP FCC"
                                ));
        }

        int n = lista.size();
        int jornadasIda = n - 1;

        // Arreglo rotante: todos excepto el ancla (índice 0)
        List<Equipo> rot = new ArrayList<>(lista.subList(1, n));

        for (int j = 0; j < jornadasIda; j++) {
            Equipo ancla = lista.get(0);

            // Par con el ancla: ancla vs rot[n-2]
            Equipo rival = rot.get(n - 2);
            partidos.add(new Partido(ancla.getId(), rival.getId(), j + 1));

            // Pares internos del arreglo rotante
            for (int i = 0; i < (n / 2) - 1; i++) {
                Equipo local = rot.get(i);
                Equipo visitante = rot.get(n - 3 - i);
                partidos.add(new Partido(local.getId(), visitante.getId(), j + 1));
            }

            // Rotar un lugar a la izquierda
            Equipo primero = rot.remove(0);
            rot.add(primero);
        }

        // Fase vuelta: invertir local/visitante con jornadas desplazadas
        int cantidadIda = partidos.size();
        for (int i = 0; i < cantidadIda; i++) {
            Partido p = partidos.get(i);
            partidos.add(new Partido(p.getEquipoVisitanteId(), p.getEquipoLocalId(), p.getJornada() + jornadasIda));
        }

        totalJornadas = 2 * jornadasIda;
        generado = true;
    }

    // Método para obtener la lista completa de partidos en el calendario
    public List<Partido> getPartidos() {
        return partidos;
    }

    // Método para obtener los partidos correspondientes a una jornada específica. Si el calendario no ha sido generado, devuelve una lista vacía.
    public List<Partido> getPartidosPorJornada(int jornada) {
        List<Partido> resultado = new ArrayList<>();
        for (Partido p : partidos) {
            if (p.getJornada() == jornada) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // Método para obtener el número total de jornadas en el calendario. Si el calendario no ha sido generado, devuelve 0.
    public int getTotalJornadas() { 
        
        return totalJornadas;
    }

    // Método para verificar si el calendario ha sido generado o no, para evitar regeneraciones innecesarias sin confirmación.
    public boolean isGenerado() { 
        
        return generado;
    }

    // Método para limpiar el calendario actual, eliminando todos los partidos y restableciendo el estado a no generado. Si el calendario no ha sido generado, no hace nada.
    public void limpiar() {
        partidos.clear();
        totalJornadas = 0;
        generado = false;
    }
}
