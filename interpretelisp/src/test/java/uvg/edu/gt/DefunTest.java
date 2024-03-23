package uvg.edu.gt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class DefunTest {
    
    @Test
    public void testDefunSumar() throws Exception {
        // Preparar el entorno y el evaluador
        Environment env = new Environment();
        DefunHandler defunHandler = new DefunHandler(env);
        ExpressionEvaluator evaluator = new ExpressionEvaluator(env);
        
        // Definir una función sumar
        List<String> parameters = new ArrayList<>();
        parameters.add("a");
        parameters.add("b");
        List<Object> body = List.of("+", "a", "b");
        
        defunHandler.defineFunction("sumar", parameters, body);
        
        // Simular la llamada a la función sumar con argumentos 3 y 7
        List<Object> functionCall = List.of("sumar", 3, 7);
        
        // Evaluar la llamada a la función
        Object result = evaluator.evaluate(functionCall);
        
        // Verificar el resultado
        assertEquals(10, result);
    }
}
