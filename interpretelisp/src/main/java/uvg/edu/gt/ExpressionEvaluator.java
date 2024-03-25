/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * La clase ExpressionEvaluator se encarga de evaluar expresiones definidas en una estructura de datos similar a Lisp
 * usando un entorno proporcionado para resolver variables y funciones.
 */
public class ExpressionEvaluator {
    private Environment environment;
    private DefunHandler defunHandler;


    /**
     * Crea un evaluador de expresiones con un entorno específico.
     *
     * @param environment El entorno que contiene las definiciones de variables y funciones.
     */
    public ExpressionEvaluator(Environment environment) {
        this.environment = environment;
        this.defunHandler = new DefunHandler(environment);
    }


    /**
     * Evalúa una expresión dada. La expresión puede ser un entero, una cadena (considerada como una variable),
     * o una lista que representa una expresión de función o operación.
     *
     * @param expr La expresión a evaluar.
     * @return El resultado de la evaluación de la expresión.
     * @throws Exception Si se encuentra con un tipo de expresión no manejado o si hay errores en la evaluación.
     */
    public Object evaluate(Object expr) throws Exception {
        if (expr instanceof List) {
            return evaluateList((List<?>) expr);
        } else if (expr instanceof String) {
            return environment.lookupVariable((String) expr);
        } else if (expr instanceof Integer) {
            return expr;
        } else { 
            throw new Exception("Tipo de expresión no manejado: " + expr.getClass().getName());
        }
    }


    /**
     * Evalúa una lista que representa una expresión Lisp. Soporta operaciones básicas como +, -, *, /,
     * así como funciones definidas por el usuario a través de DEFUN, y construcciones especiales como QUOTE, COND, y SETQ.
     *
     * @param list La lista que representa la expresión a evaluar.
     * @return El resultado de la evaluación de la lista.
     * @throws Exception Si hay errores en la evaluación, como listas vacías o tipos inesperados.
     */
    private Object evaluateList(List<?> list) throws Exception {
        System.out.println("Evaluando lista: " + list);
        if (list.isEmpty()) {
            throw new Exception("no se puede evaluar una lista vacía");
        }
        if (!(list.get(0) instanceof String)) {
            throw new Exception("El primer elemento de la lista debe ser un operador en forma de String");
        }
        String operator = list.get(0).toString();
        List<?> originalArgs = list.subList(1, list.size());
        List<Object> args = new ArrayList<>();
    
        // Preprocesa los argumentos para manejar strings como expresiones
        for (Object arg : originalArgs) {
            if (arg instanceof String) {
                System.out.println("Evaluando un string ... !!!");
                Object parsedExpr = Parser.parse((String) arg);
                args.add(evaluate(parsedExpr));
            } else {
                args.add(arg);
            }
        }
        
        // Dentro de la estructura switch en el método evaluateList
        switch (operator) {
            case "+":{
                int sum = 0;
                for (Object arg : args) {
                    sum += (Integer) evaluate(arg);
                }
                return sum;
            }
            case "-":{
                if (args.isEmpty()) {
                    throw new Exception("La operación resta requiere al menos un argumento");
                }
                int result = (Integer) evaluate(args.get(0));
                for (int i = 1; i < args.size(); i++) {
                    result -= (Integer) evaluate(args.get(i));
                }
                return result;
            }
            case "*":{
                int product = 1;
                for (Object arg : args) {
                    product *= (Integer) evaluate(arg);
                }
                return product;
            }
            case "/":{
                if (args.isEmpty()) {
                    throw new Exception("La operación división requiere al menos un argumento");
                }
                int quotient = (Integer) evaluate(args.get(0));
                for (int i = 1; i < args.size(); i++) {
                    int divisor = (Integer) evaluate(args.get(i));
                    if (divisor == 0) {
                        throw new Exception("División por cero");
                    }
                    quotient /= divisor;
                }
                return quotient;
            }
            case "QUOTE":
            case "'": { 
                if (args.size() == 1) {
                    return args.get(0); 
                } else {
                    throw new Exception("QUOTE espera exactamente un argumento");
                }
            }
            case "LIST":{
                return args; // Los argumentos ya fueron evaluados previamente.
            }
            case "EQUAL": {
                if (args.size() != 2) {
                    throw new Exception("EQUAL espera exactamente dos argumentos");
                }
                Object firstArg = evaluate(args.get(0));
                Object secondArg = evaluate(args.get(1));
                return firstArg.equals(secondArg);
            }
            case "DEFUN": {
                String functionName = (String) list.get(1);
                List<?> paramsList = (List<?>) list.get(2); // La lista de parámetros.
                Object functionBody = list.get(3); // El cuerpo de la función.

                // Verificar que paramsList contiene solo Strings.
                for (Object param : paramsList) {
                    if (!(param instanceof String)) {
                        throw new Exception("Los parámetros de DEFUN deben ser strings.");
                    }
                }
                @SuppressWarnings("unchecked")
                List<String> params = (List<String>) paramsList;

                // Guarda la definición de la función sin intentar evaluar los parámetros o el cuerpo.
                defunHandler.defineFunction(functionName, params, functionBody);

                return "Function " + functionName + " defined";
            }
            case ">": {
                if (args.size() != 2) {
                    throw new Exception("El operador > requiere exactamente dos argumentos.");
                }
                int left = (Integer) evaluate(args.get(0));
                int right = (Integer) evaluate(args.get(1));
                return left > right;
            }
            case "<": {
                if (args.size() != 2) {
                    throw new Exception("El operador < requiere exactamente dos argumentos.");
                }
                int left = (Integer) evaluate(args.get(0));
                int right = (Integer) evaluate(args.get(1));
                return left < right;
            }
            case ">=": {
                if (args.size() != 2) {
                    throw new Exception("El operador >= requiere exactamente dos argumentos.");
                }
                int left = (Integer) evaluate(args.get(0));
                int right = (Integer) evaluate(args.get(1));
                return left >= right;
            }
            case "<=": {
                if (args.size() != 2) {
                    throw new Exception("El operador <= requiere exactamente dos argumentos.");
                }
                int left = (Integer) evaluate(args.get(0));
                int right = (Integer) evaluate(args.get(1));
                return left <= right;
            }
            case "COND": {
                CondHandler condHandler = new CondHandler(environment);
                List<Object> formattedArgs = formatCondArgs(list.subList(1, list.size()));
                return condHandler.evaluateCond(formattedArgs);
            }            
        }
    
        LispFunction userFunction = environment.lookupFunction(operator);
        if (userFunction != null) {
            List<Object> evaluatedArgs = new ArrayList<>();
            for (Object arg : originalArgs) {
                evaluatedArgs.add(evaluate(arg));
            }
            return userFunction.apply(evaluatedArgs, environment);
        }
    
        if ("SETQ".equals(operator)) {
            if (originalArgs.size() != 2) {
                throw new Exception("SETQ espera exactamente dos argumentos.");
            }

            if (!(originalArgs.get(0) instanceof String)) {
                throw new Exception(
                        "El primer argumento de SETQ debe ser una cadena que represente el nombre de la variable.");
            }
            String variableName = (String) originalArgs.get(0);

            Object value = evaluate(originalArgs.get(1));

            environment.defineVariable(variableName, value);

            return value;
        }

    
        throw new Exception("Operador o función desconocido/a: " + operator);
    
    }
    

    /**
     * Evalúa las cláusulas de una construcción COND, retornando el resultado de la primera cláusula verdadera.
     *
     * @param clauses Las cláusulas COND a evaluar.
     * @return El resultado de la primera cláusula verdadera, o null si ninguna cláusula es verdadera.
     * @throws Exception Si alguna de las cláusulas es inválida.
     */
    public Object evaluateCond(List<?> clauses) throws Exception {
        for (Object clauseObj : clauses) {
            if (!(clauseObj instanceof List)) {
                throw new Exception("Formato de cláusula COND inválido: " + clauseObj.toString());
            }
            List<?> clause = (List<?>) clauseObj;
            if (clause.size() != 2) {
                throw new Exception("Formato de cláusula COND inválido: " + clause.toString());
            }
            Object conditionResult = evaluate(clause.get(0)); // Evalúa la condición
            if (conditionResult.equals(Boolean.TRUE)) { // Asume que `evaluate` devuelve un Boolean
                return evaluate(clause.get(1)); // Evalúa y devuelve el resultado de la acción si la condición es verdadera
            }
        }
        return "NIL"; // Devuelve "NIL" si ninguna condición se cumple
    }
    
    private List<Object> formatCondArgs(List<?> originalArgs) throws Exception {
        List<Object> formattedArgs = new ArrayList<>();
        for (Object arg : originalArgs) {
            if (!(arg instanceof List) || ((List<?>) arg).size() != 2) {
                throw new Exception("Cada cláusula de COND debe ser una lista con exactamente dos elementos.");
            }
            formattedArgs.add(arg);
        }
        return formattedArgs;
    }
    
}
