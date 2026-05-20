package src.model;

import java.io.Serializable;
import java.util.UUID;

public class Partido implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String equipoLocalId;
    private String equipoVisitanteId;
    private int jornada;
    private String resultado;

    public Partido(String equipoLocalId, String equipoVisitanteId, int jornada) {
        this.id = UUID.randomUUID().toString();
        this.equipoLocalId = equipoLocalId;
        this.equipoVisitanteId = equipoVisitanteId;
        this.jornada = jornada;
        this.resultado = null;
    }

    public String getId() { return id; }
    public String getEquipoLocalId() { return equipoLocalId; }
    public String getEquipoVisitanteId() { return equipoVisitanteId; }
    public int getJornada() { return jornada; }
    public String getResultado() { return resultado; }

    public void setResultado(String resultado) { this.resultado = resultado; }

    public boolean isTieneResultado() { return resultado != null; }

    @Override
    public String toString() {
        return "Jornada " + jornada + ": [" + equipoLocalId + "] vs [" + equipoVisitanteId + "]";
    }
}
