package uvg.edu.gt;

import java.util.ArrayList;
import java.util.List;

public class ExpressionEvaluator {
    private Environment environment;

    public ExpressionEvaluator(Environment environment) {
        this.environment = environment;
    }

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

    private Object evaluateList(List<?> list) throws Exception {
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
    else if ("DEFUN".equals(operator)) {
        if (list.size() != 4 || !(list.get(1) instanceof String) || !(list.get(2) instanceof List) || !(list.get(3) instanceof List)) {
            throw new Exception("Formato incorrecto para DEFUN");
        }
        String functionName = (String) list.get(1);
        List<String> parameters = (List<String>) list.get(2);
        Object body = list.get(3);
        LispFunction function = new LispFunction(parameters, body);
        environment.defineFunction(functionName, function);
        return "Function " + functionName + " defined"; // O algún otro valor indicativo de éxito
    } 

    throw new Exception("Operador desconocido: " + operator);
    }

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
        return null; 
    }
}
