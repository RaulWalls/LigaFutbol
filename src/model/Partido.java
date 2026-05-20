package src.model;

import java.io.Serializable;                    // Aseguramos que la clase implementa Serializable para permitir su serialización
import java.util.UUID;                          // Importamos UUID para generar identificadores únicos para cada partido

public class Partido implements Serializable {

    /**
     * Clase que representa un partido de fútbol en la liga. Cada partido tiene un identificador único, el ID del equipo local, el ID del equipo visitante, la jornada en la que se juega y el resultado del partido. La clase implementa Serializable para permitir su almacenamiento y recuperación desde archivos.
     * Atributos:
     * - id: Identificador único del partido, generado automáticamente al crear un nuevo partido.
     * - equipoLocalId: Identificador del equipo local que juega el partido, utilizado para relacionar el partido con los equipos en la liga.
     * - equipoVisitanteId: Identificador del equipo visitante que juega el partido, utilizado para relacionar el partido con los equipos en la liga.
     * - jornada: Número de la jornada en la que se juega el partido, utilizado para organizar los partidos en el calendario de la liga.
     * - resultado: Resultado del partido, que puede ser un string con el formato "GolesLocal-GolesVisitante" o null si el partido aún no tiene resultado registrado. Este atributo se utiliza para mostrar el resultado del partido en la vista y para calcular las estadísticas de los equipos en la liga.
     */
    private static final long serialVersionUID = 1L;

    private String id;
    private String equipoLocalId;
    private String equipoVisitanteId;
    private int jornada;
    private String resultado;

    // Constructor público para crear un nuevo partido, generando automáticamente un ID único. Este constructor se utiliza al generar el calendario de partidos para la liga.
    public Partido(String equipoLocalId, String equipoVisitanteId, int jornada) {
        this.id = UUID.randomUUID().toString();
        this.equipoLocalId = equipoLocalId;
        this.equipoVisitanteId = equipoVisitanteId;
        this.jornada = jornada;
        this.resultado = null;
    }

    // Getters para acceder a los atributos del partido.
    public String getId() { 
        return id; 
    }
    public String getEquipoLocalId() { 
        return equipoLocalId; 
    }
    public String getEquipoVisitanteId() { 
        return equipoVisitanteId; 
    }
    public int getJornada() { 
        return jornada; 
    }
    public String getResultado() { 
        return resultado; 
    }

    // Setter para actualizar el resultado del partido. Este método se utiliza para registrar el resultado 
    // de un partido después de que se haya jugado, y para actualizar la vista con el nuevo resultado.
    public void setResultado(String resultado) { 
        this.resultado = resultado; 
    }

    // Método para verificar si el partido tiene un resultado registrado. 
    // Este método se utiliza en la vista para mostrar el resultado del partido solo si ya ha sido registrado, 
    // y para mostrar un mensaje de "Pendiente" o similar si el partido aún no tiene resultado.
    public boolean isTieneResultado() { 
        return resultado != null; 
    }

    @Override
    public String toString() {
        return "Jornada " + jornada + ": [" + equipoLocalId + "] vs [" + equipoVisitanteId + "]";
    }
}
