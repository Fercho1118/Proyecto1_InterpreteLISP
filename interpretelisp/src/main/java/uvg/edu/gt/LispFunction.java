package uvg.edu.gt;

import java.util.List;

public class LispFunction {
    private List<String> parameters;
    private String body;

    public LispFunction(List<String> parameters, String body) {
        this.parameters = parameters;
        this.body = body;
    }

    public Object apply(List<Object> arguments, Environment environment) throws Exception {
        Environment functionEnv = new Environment(environment); // Crear un nuevo entorno basado en el global
        if (parameters.size() != arguments.size()) {
            throw new Exception("Número incorrecto de argumentos.");
        }
        for (int i = 0; i < parameters.size(); i++) {
            functionEnv.defineVariable(parameters.get(i), arguments.get(i)); // Mapear parámetros a argumentos
        }
        
        return null; 
    }
}
