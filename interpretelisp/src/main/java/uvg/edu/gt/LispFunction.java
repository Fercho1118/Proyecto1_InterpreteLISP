package uvg.edu.gt;

import java.util.List;

public class LispFunction {
    private List<String> parameters;
    private String body;

    public LispFunction(List<String> parameters, String body) {
        this.parameters = parameters;
        this.body = body;
    }

    public Object apply(List<Object> arguments, Environment environment) {
        //Falta lógica
        return null;
    }
}
