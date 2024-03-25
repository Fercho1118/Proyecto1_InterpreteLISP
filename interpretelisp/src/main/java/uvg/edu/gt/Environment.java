/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * La clase Environment mantiene un registro de variables y funciones definidas por el usuario.
 * Permite la definición, actualización y búsqueda de variables y funciones dentro de un entorno de ejecución específico.
 */
public class Environment {
    private final Map<String, Object> variables;
    private final Map<String, LispFunction> functions;

    /**
     * Constructor que inicializa un entorno vacío sin variables ni funciones predefinidas.
     */
    public Environment() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    /**
     * Constructor de copia que crea un nuevo entorno copiando las variables y funciones de otro entorno.
     *
     * @param other El entorno del cual copiar las variables y funciones.
     */
    public Environment(Environment other) {
        this.variables = new HashMap<>(other.variables);
        this.functions = new HashMap<>(other.functions);
    }

    /**
     * Define o actualiza una variable en el entorno con un valor específico.
     *
     * @param name  El nombre de la variable a definir o actualizar.
     * @param value El valor de la variable.
     */
    public void defineVariable(String name, Object value) {
        variables.put(name, value);
    }

    /**
     * Busca el valor de una variable en el entorno por su nombre.
     *
     * @param name El nombre de la variable a buscar.
     * @return El valor de la variable si existe, null en caso contrario.
     */
    public Object lookupVariable(String name) {
        return variables.get(name);
    }

    /**
     * Define o actualiza una función en el entorno.
     *
     * @param name     El nombre de la función a definir o actualizar.
     * @param function La función a almacenar.
     */
    public void defineFunction(String name, LispFunction function) {
        functions.put(name, function);
    }

    /**
     * Busca una función en el entorno por su nombre.
     *
     * @param name El nombre de la función a buscar.
     * @return La función si existe, null en caso contrario.
     */
    public LispFunction lookupFunction(String name) {
        return functions.get(name);
    }
}

