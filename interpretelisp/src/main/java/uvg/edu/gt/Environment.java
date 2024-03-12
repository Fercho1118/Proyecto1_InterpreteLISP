package uvg.edu.gt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Environment {
    private final Map<String, Object> variables;
    private final Map<String, LispFunction> functions;

    public Environment() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public Environment(Environment other) {
        this.variables = new HashMap<>(other.variables);
        this.functions = new HashMap<>(other.functions);
    }

    public void defineVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object lookupVariable(String name) {
        return variables.get(name);
    }

    public void defineFunction(String name, LispFunction function) {
        functions.put(name, function);
    }

    public LispFunction lookupFunction(String name) {
        return functions.get(name);
    }
}
