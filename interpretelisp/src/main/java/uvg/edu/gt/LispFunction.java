/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;

import java.util.List;

/**
 * Representa una función definida en el lenguaje Lisp, que consta de una lista de parámetros y un cuerpo de función.
 * Esta clase permite aplicar la función a un conjunto de argumentos dentro de un entorno específico.
 */
public class LispFunction {
    private List<String> parameters;
    private Object body;

    /**
     * Crea una nueva función Lisp con los parámetros y el cuerpo especificados.
     *
     * @param parameters La lista de nombres de los parámetros que la función acepta.
     * @param body El cuerpo de la función, que se evaluará cuando se aplique la función.
     */
    public LispFunction(List<String> parameters, Object body) {
        this.parameters = parameters;
        this.body = body;
    }

    /**
     * Aplica la función a un conjunto de argumentos dentro de un entorno dado, creando un nuevo entorno para la evaluación de la función.
     *
     * @param arguments Los argumentos con los que se llama a la función. Deben coincidir en número con la lista de parámetros de la función.
     * @param environment El entorno en el que se ejecuta la función, utilizado para resolver referencias a otras variables y funciones.
     * @return El resultado de evaluar el cuerpo de la función con los argumentos proporcionados.
     * @throws Exception Si el número de argumentos no coincide con el número de parámetros o si ocurre un error durante la evaluación del cuerpo de la función.
     */
        public Object apply(List<Object> arguments, Environment globalEnvironment) throws Exception {
            Environment localEnvironment = new Environment(globalEnvironment);
            for (int i = 0; i < parameters.size(); i++) {
                localEnvironment.defineVariable(parameters.get(i), arguments.get(i));
            }
            return new ExpressionEvaluator(localEnvironment).evaluate(body);
        }
    
    } 
    
    
