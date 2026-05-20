package src.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import src.exception.PartidoInvalidoException;

public class Calendario implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Partido> partidos;
    private int totalJornadas;
    private boolean generado;

    public Calendario() {
        this.partidos = new ArrayList<>();
        this.totalJornadas = 0;
        this.generado = false;
    }

    public void generarCalendario(List<Equipo> equipos) throws PartidoInvalidoException {
        if (equipos == null || equipos.size() < 2) {
            throw new PartidoInvalidoException("Se necesitan al menos 2 equipos para generar el calendario.");
        }

        partidos.clear();

        List<Equipo> lista = new ArrayList<>(equipos);

        // Si N es impar, agregar equipo predeterminado para emparejar jornadas
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

    public List<Partido> getPartidos() {
        return partidos;
    }

    public List<Partido> getPartidosPorJornada(int jornada) {
        List<Partido> resultado = new ArrayList<>();
        for (Partido p : partidos) {
            if (p.getJornada() == jornada) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public int getTotalJornadas() { 
        
        return totalJornadas;
    }
    public boolean isGenerado() { 
        
        return generado;
    }

    public void limpiar() {
        partidos.clear();
        totalJornadas = 0;
        generado = false;
    }
}
