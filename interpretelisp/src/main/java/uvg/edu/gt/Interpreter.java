package uvg.edu.gt;

import java.util.List;

public class Interpreter {
    private Environment environment;

    public Interpreter() {
        this.environment = new Environment();
    }

    public Object eval(Object expr) throws Exception {
        if (expr instanceof List) {
            List<?> list = (List<?>) expr;
            if (list.isEmpty()) {
                throw new Exception("Cannot evaluate an empty list");
            }
            String operator = list.get(0).toString();
            switch (operator) {
                case "+":
                    return evalSum(list.subList(1, list.size()));
                // Agregar casos para "-", "*", "/"
                default:
                    throw new Exception("Unknown operator: " + operator);
            }
        } else if (expr instanceof Integer) {
            return expr; // Los números se evalúan a sí mismos
        } else if (expr instanceof String) {
            // Agregar lógica para manejar variables y símbolos
        }
        throw new Exception("Cannot evaluate expression: " + expr);
    }

    private Integer evalSum(List<?> args) throws Exception {
        int sum = 0;
        for (Object arg : args) {
            sum += (Integer) eval(arg); 
        }
        return sum;
    }

    // Implementar métodos evalSubtract, evalMultiply, evalDivide
}