package src.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import src.exception.PersistenciaException;

public class ArchivoUtil {

    private ArchivoUtil() {}

    public static void guardarObjeto(Object objeto, String rutaArchivo) throws PersistenciaException {
        File archivo = new File(rutaArchivo);
        archivo.getParentFile().mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar el archivo: " + rutaArchivo, e);
        }
    }

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

    public static boolean archivoExiste(String rutaArchivo) {
        return new File(rutaArchivo).exists();
    }
}
