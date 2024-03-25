/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;

import java.util.List;

/**
 * Maneja la evaluación de expresiones condicionales en un entorno específico.
 */
public class CondHandler {
    private Environment environment;

    /**
     * Crea un manejador de condiciones para un entorno dado.
     * 
     * @param environment El entorno donde se evaluarán las condiciones.
     */    
    public CondHandler(Environment environment) {
        this.environment = environment;
    }

    /**
     * Evalúa una lista de cláusulas condicionales hasta que una de ellas sea verdadera.
     * Cada cláusula es una lista de dos elementos donde el primero es la condición
     * y el segundo es la acción a ejecutar si la condición es verdadera.
     * 
     * @param clauses Lista de cláusulas condicionales a evaluar.
     * @return El resultado de la primera acción cuya condición evalúa a verdadero,
     *         o "NIL" si ninguna condición es verdadera.
     * @throws Exception Si alguna cláusula no es una lista de dos elementos.
     */
    public Object evaluateCond(List<?> clauses) throws Exception {
        for (Object clause : clauses) {
            if (!(clause instanceof List) || ((List<?>) clause).size() != 2) {
                throw new Exception("Cláusula COND inválida: " + clause.toString());
            }
            List<?> conditionActionPair = (List<?>) clause;
            Object conditionResult = new ExpressionEvaluator(environment).evaluate(conditionActionPair.get(0));
            
            System.out.println("Resultado de condición: " + conditionResult);
            
            if (Boolean.TRUE.equals(conditionResult)) {
                return new ExpressionEvaluator(environment).evaluate(conditionActionPair.get(1));
            }
        }
        return "NIL";
    }
}



