package uvg.edu.gt;

import java.util.List;

public class LispFunction {
    private List<String> parameters;
    private Object body;

    public LispFunction(List<String> parameters, Object body) {
        this.parameters = parameters;
        this.body = body;
    }

    public Object apply(List<Object> arguments, Environment environment) throws Exception {
        Environment functionEnv = new Environment();
        if (parameters.size() != arguments.size()) {
            throw new Exception("Número incorrecto de argumentos.");
        }
        for (int i = 0; i < parameters.size(); i++) {
            functionEnv.defineVariable(parameters.get(i), arguments.get(i)); 
        }
        
        return new ExpressionEvaluator(functionEnv).evaluate(body); 
    }
}
