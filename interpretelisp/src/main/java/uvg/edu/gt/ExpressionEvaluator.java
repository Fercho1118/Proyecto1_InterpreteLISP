package uvg.edu.gt;

import java.util.ArrayList;
import java.util.List;


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
            if (arg instanceof String){
                System.out.println("Evaluando un string ... !!!");
                Object parsedExpr = Parser.parse((String) arg);
                args.add(evaluate(parsedExpr));              
            } else {
                args.add(arg);
            }
        }

        if ("DEFUN".equals(list.get(0))) {
            String functionName = (String) list.get(1);
            List<String> parameters = (List<String>) list.get(2);
            Object body = list.get(3);
            defunHandler.defineFunction(functionName, parameters, body);
            return null; // O cualquier valor apropiado
        }
    
        // Ejemplo de manejo de llamada a función
        if (environment.lookupFunction(list.get(0).toString()) != null) {
            String functionName = list.get(0).toString();
            List<Object> arguments = new ArrayList<>(list.subList(1, list.size()));
            return defunHandler.applyFunction(functionName, arguments);
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
            case "COND":{
                return evaluateCond(args); // Asume que evaluateCond ya maneja la evaluación correctamente.
            }
            case "QUOTE":
            case "'":{
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
                throw new Exception("El primer argumento de SETQ debe ser una cadena que represente el nombre de la variable.");
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
        return "NIL";
    }
    
}
