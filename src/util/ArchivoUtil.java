package src.util;

import java.io.File;                            // Importamos File para manejar archivos y directorios
import java.io.FileInputStream;                 // Importamos FileInputStream para leer archivos binarios
import java.io.FileOutputStream;                // Importamos FileOutputStream para escribir archivos binarios
import java.io.IOException;                     // Importamos IOException para manejar errores de entrada/salida al trabajar con archivos
import java.io.ObjectInputStream;               // Importamos ObjectInputStream para leer objetos serializados desde archivos
import java.io.ObjectOutputStream;              // Importamos ObjectOutputStream para escribir objetos serializados en archivos

import src.exception.PersistenciaException;

public class ArchivoUtil {

    private ArchivoUtil() {}


    // Método para guardar un objeto en un archivo utilizando serialización.
    public static void guardarObjeto(Object objeto, String rutaArchivo) throws PersistenciaException {
        File archivo = new File(rutaArchivo);
        archivo.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar el archivo: " + rutaArchivo, e);
        }
    }

    // Método para cargar un objeto desde un archivo utilizando serialización.
    public static Object cargarObjeto(String rutaArchivo) throws PersistenciaException {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Error al cargar el archivo: " + rutaArchivo, e);
        }
    }

    // Método para verificar si un archivo existe en la ruta especificada, devolviendo true si el archivo existe y false si no existe.
    public static boolean archivoExiste(String rutaArchivo) {
        return new File(rutaArchivo).exists();
    }
}
