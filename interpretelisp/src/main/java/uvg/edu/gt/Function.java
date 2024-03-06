package uvg.edu.gt;

import java.util.List;

public class Function {
    private List<String> parameters;
    private String body;

    public Function(List<String> parameters, String body) {
        this.parameters = parameters;
        this.body = body;
    }

    public Object apply(List<Object> arguments, Environment environment) {
        //Falta lógica
        return null;
    }
}
