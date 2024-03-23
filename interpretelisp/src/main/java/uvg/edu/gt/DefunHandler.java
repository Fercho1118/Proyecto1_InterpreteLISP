package uvg.edu.gt;

import java.util.List;

public class DefunHandler {
    private Environment environment;

    public DefunHandler(Environment environment) {
        this.environment = environment;
    }

    public void defineFunction(String functionName, List<String> parameters, Object body) {
        LispFunction newFunction = new LispFunction(parameters, body);
        environment.defineFunction(functionName, newFunction);
    }

    public Object applyFunction(String functionName, List<Object> arguments) throws Exception {
        LispFunction function = environment.lookupFunction(functionName);
        if (function == null) {
            throw new Exception("Función no definida: " + functionName);
        }
        return function.apply(arguments, environment);
    }
}

