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
            // Aquí iría la lógica para manejar variables y símbolos
            return environment.lookupVariable((String) expr);
        } else {
            // Números y otros literales se evalúan a sí mismos
            return expr;
        }
    }

    private Object evaluateList(List<?> list) throws Exception {
        if (list.isEmpty()) {
            throw new Exception("no se puede evaluar una lista vacía");
        }
        String operator = list.get(0).toString();
        List<?> args = list.subList(1, list.size());

        if ("+".equals(operator)) {
            int sum = 0;
            for (Object arg : args) {
                sum += (Integer) evaluate(arg); 
            }
            return sum;
        }
        throw new Exception("operador desconocido " + operator);
    }
}
