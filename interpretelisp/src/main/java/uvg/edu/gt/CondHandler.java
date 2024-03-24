package uvg.edu.gt;

import java.util.List;

public class CondHandler {
    private Environment environment;

    public CondHandler(Environment environment) {
        this.environment = environment;
    }

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



