package uvg.edu.gt;

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
    throw new Exception("Operador desconocido: " + operator);
    }
}
