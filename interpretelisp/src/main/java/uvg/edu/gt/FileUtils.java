/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


/**
 * La clase FileUtils proporciona utilidades para trabajar con archivos.
 * Contiene métodos estáticos para realizar operaciones comunes de archivos de manera simplificada.
 */
public class FileUtils {

    /**
     * Lee todas las líneas de un archivo de texto y las devuelve como una lista de cadenas.
     * Este método utiliza la codificación de caracteres predeterminada para leer el archivo.
     *
     * @param filePath La ruta del archivo a leer. La ruta debe ser absoluta o relativa al directorio de trabajo actual.
     * @return Una lista de {@link String} donde cada elemento representa una línea del archivo.
     * @throws IOException Si ocurre un error de entrada/salida al leer el archivo. Esto puede suceder si el archivo
     *                     no existe, no se tiene permiso de lectura, o si ocurren otros errores relacionados con el
     *                     sistema de archivos.
     */
    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
}

