package uvg.edu.gt;

import java.util.ArrayList;
import java.util.List;


/**
 * La clase ExpressionEvaluator se encarga de evaluar expresiones definidas en una estructura de datos similar a Lisp
 * usando un entorno proporcionado para resolver variables y funciones.
 */
public class ExpressionEvaluator {
    private Environment environment;


    /**
     * Crea un evaluador de expresiones con un entorno específico.
     *
     * @param environment El entorno que contiene las definiciones de variables y funciones.
     */
    public ExpressionEvaluator(Environment environment) {
        this.environment = environment;
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
        List<?> args = list.subList(1, list.size());

        // Suma
    if ("+".equals(operator)) {
        int sum = 0;
        for (Object arg : args) {
            if (arg instanceof String){
                System.out.println("Evaluando un string ... !!!");
                Object parsedExpr = Parser.parse((String) arg);
                // Object result = evaluate(parsedExpr);
                // System.out.println("Resultado: " + result);
                arg = parsedExpr;              
            }
            sum += (Integer) evaluate(arg);
        }
        return sum;
    }
    // Resta
    else if ("-".equals(operator)) {
        if (args.isEmpty()) {
            throw new Exception("La operación resta requiere al menos un argumento");
        }
        int result = (Integer) evaluate(args.get(0));
        for (int i = 1; i < args.size(); i++) {
            result -= (Integer) evaluate(args.get(i));
        }
        return result;
    }
    // Multiplicación
    else if ("*".equals(operator)) {
        int product = 1;
        for (Object arg : args) {
            product *= (Integer) evaluate(arg);
        }
        return product;
    }
    // División
    else if ("/".equals(operator)) {
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
    // COND
    else if ("COND".equals(operator)) {
        return evaluateCond(args);
    }
    // QUOTE
    else if ("QUOTE".equals(operator) || "'".equals(operator) && args.size() == 1) {
        return args.get(0);
    }
    //SETQ
    else if ("SETQ".equals(operator) && args.size() == 2 && args.get(0) instanceof String) {
        String varName = (String) args.get(0);
        Object value = evaluate(args.get(1));
        environment.defineVariable(varName, value);
        return value;
    }
    else if ("ATOM".equals(operator)) {
    if (args.size() != 1) {
        throw new Exception("ATOM espera exactamente un argumento");
    }
    Object arg = evaluate(args.get(0));
    return !(arg instanceof List);
}
    // LIST
    else if ("LIST".equals(operator)) {
        if (args.isEmpty()) {
            throw new Exception("LIST espera al menos un argumento");
        }
        List<Object> result = new ArrayList<>();
        for (Object arg : args) {
            result.add(evaluate(arg));
        }
        return result;
    }
    //Equal
    else if ("EQUAL".equals(operator)) {
        if (args.size() != 2) {
            throw new Exception("EQUAL espera exactamente dos argumentos");
        }
        Object firstArg = evaluate(args.get(0));
        Object secondArg = evaluate(args.get(1));
        return firstArg.equals(secondArg);
    }
    else if ("<".equals(operator)) {
        if (args.size() != 2) {
            throw new Exception("< espera exactamente dos argumentos");
        }
        int firstArg = (Integer) evaluate(args.get(0));
        int secondArg = (Integer) evaluate(args.get(1));
        return firstArg < secondArg;
    }
    else if (">".equals(operator)) {
        if (args.size() != 2) {
            throw new Exception("> espera exactamente dos argumentos");
        }
        int firstArg = (Integer) evaluate(args.get(0));
        int secondArg = (Integer) evaluate(args.get(1));
        return firstArg > secondArg;
    }
    //DEFUN
    else if ("DEFUN".equals(operator)) {
        if (list.size() != 4 || !(list.get(1) instanceof String) || !(list.get(2) instanceof List)) {
            throw new Exception("Formato incorrecto para DEFUN");
        }
        String functionName = (String) list.get(1);
        List<?> parametersRaw = (List<?>) list.get(2);
        List<String> parameters = new ArrayList<>();
        for (Object param : parametersRaw) {
            if (!(param instanceof String)) {
                throw new Exception("Los parámetros de la función deben ser cadenas.");
            }
            parameters.add((String) param);
        }
        Object body = list.get(3);
    
        System.out.println("Definiendo función: " + functionName);
        System.out.println("Parámetros: " + parameters);
        System.out.println("Cuerpo: " + body);
    
        LispFunction function = new LispFunction(parameters, body);
        environment.defineFunction(functionName, function);
        return "Function " + functionName + " defined";
    }
    
    throw new Exception("Operador desconocido: " + operator);
    }

    /**
     * Evalúa las cláusulas de una construcción COND, retornando el resultado de la primera cláusula verdadera.
     *
     * @param clauses Las cláusulas COND a evaluar.
     * @return El resultado de la primera cláusula verdadera, o null si ninguna cláusula es verdadera.
     * @throws Exception Si alguna de las cláusulas es inválida.
     */
    private Object evaluateCond(List<?> clauses) throws Exception {
        for (Object clause : clauses) {
            if (!(clause instanceof List) || ((List<?>) clause).size() != 2) {
                throw new Exception("Clausula COND inválida: " + clause.toString());
            }
            List<?> pair = (List<?>) clause;
            Object conditionResult = evaluate(pair.get(0));
            if (Boolean.TRUE.equals(conditionResult)) {
                return evaluate(pair.get(1));
            }
        }
        return "NIL"; // Cambio aquí
    }
    
}
