package uvg.edu.gt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Environment {
    private final Map<String, Object> variables;
    private final Map<String, Function> functions;

    public Environment() {
        this.variables = new HashMap<>();
        this.functions = new HashMap<>();
    }

    public void defineVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Object lookupVariable(String name) {
        return variables.get(name);
    }

    public void defineFunction(String name, Function function) {
        functions.put(name, function);
    }

    public Function lookupFunction(String name) {
        return functions.get(name);
    }
}
