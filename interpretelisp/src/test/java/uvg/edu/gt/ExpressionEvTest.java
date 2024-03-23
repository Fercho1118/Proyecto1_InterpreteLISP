package uvg.edu.gt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ExpressionEvTest {
    
    private Environment environment;
    private ExpressionEvaluator evaluator;

    @BeforeEach
    public void setUp() {
        environment = new Environment();
        evaluator = new ExpressionEvaluator(environment);
    }

    @Test
    public void testEvaluateSimpleAddition() throws Exception {
        // Preparar
        List<Object> expression = Arrays.asList("+", 1, 2);

        // Actuar
        Object result = evaluator.evaluate(expression);

        // Afirmar
        assertEquals(3, result);
    }
    
    @Test
    public void testEvaluateVariableLookup() throws Exception {
        // Definir una variable en el entorno
        environment.defineVariable("x", 10);

        // Evaluar el uso de la variable
        Object result = evaluator.evaluate("x");

        // Verificar que el valor de la variable se recupera correctamente
        assertEquals(10, result);
    }

    @Test
    public void testDefunSumarFunction() throws Exception {
        // Definir la función 'sumar' usando DEFUN
        List<Object> defunExpression = List.of(
            "DEFUN", "sumar", List.of("a", "b"), List.of("+", "a", "b")
        );
        evaluator.evaluate(defunExpression);

        // Luego, llamar a la función 'sumar' con argumentos 3 y 5
        List<Object> callExpression = List.of("sumar", 3, 5);

        // Evaluar la llamada a la función
        Object result = evaluator.evaluate(callExpression);

        // Verificar el resultado de la suma
        assertEquals(8, result);
    }
}
