/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;

import java.util.List;

/**
 * DefunHandler maneja la definición y aplicación de funciones definidas por el usuario.
 */
public class DefunHandler {
    private final Environment environment;

    public DefunHandler(Environment environment) {
        this.environment = environment;
    }

    /**
     * Define una nueva función en el entorno.
     * 
     * @param name El nombre de la función.
     * @param parameters Los parámetros de la función como una lista de nombres de variables.
     * @param body El cuerpo de la función, que puede ser cualquier objeto representando una expresión Lisp.
     */
    public void defineFunction(String name, List<String> parameters, Object body) {
        LispFunction function = new LispFunction(parameters, body);
        environment.defineFunction(name, function);
    }

    /**
     * Aplica una función definida por el usuario, buscándola por nombre y evaluando su cuerpo con los argumentos proporcionados.
     * 
     * @param name El nombre de la función a aplicar.
     * @param arguments Los argumentos para pasar a la función.
     * @return El resultado de aplicar la función.
     * @throws Exception Si la función no se encuentra o si ocurre un error durante la evaluación del cuerpo de la función.
     */
    public Object applyFunction(String name, List<Object> arguments) throws Exception {
        LispFunction function = environment.lookupFunction(name);
        if (function == null) {
            throw new Exception("Función no definida: " + name);
        }
        return function.apply(arguments, environment);
    }
}
