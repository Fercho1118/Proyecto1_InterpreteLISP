package uvg.edu.gt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;

class CondTest {

    @Test
    void testCondEvaluation() throws Exception {
        // Preparar el ambiente y el evaluador
        Environment environment = new Environment();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(environment);

        // Preparar la expresión COND
        String condExpression = "(COND ((> 5 3) 'mayor) ((< 2 3) 'menor))";

        // Analizar y evaluar la expresión
        Object parsedExpression = Parser.parse(condExpression);
        Object result = evaluator.evaluate(parsedExpression);

        // Verificar el resultado
        Assertions.assertEquals("mayor", result);
    }

    @Test
    void testCondFalseEvaluation() throws Exception {
        // Ambiente y evaluador
        Environment environment = new Environment();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(environment);

        // Una expresión COND donde ninguna condición es verdadera
        String condExpression = "(COND ((> 2 3) 'mayor) ((< 5 3) 'menor))";

        // Analizar y evaluar
        Object parsedExpression = Parser.parse(condExpression);
        Object result = evaluator.evaluate(parsedExpression);

        // Esperamos "NIL" como resultado, ya que ninguna condición se cumple
        Assertions.assertEquals("NIL", result);
    }

    @Test
    void testCondWithVariables() throws Exception {
        // Este test asume que tu implementación puede manejar variables
        Environment environment = new Environment();
        environment.defineVariable("x", 10); // Definir una variable x con valor 10
        ExpressionEvaluator evaluator = new ExpressionEvaluator(environment);

        String condExpression = "(COND ((> x 5) 'mayor) ((< x 5) 'menor))";

        Object parsedExpression = Parser.parse(condExpression);
        Object result = evaluator.evaluate(parsedExpression);

        // x es mayor que 5, por lo que esperamos 'mayor' como resultado
        Assertions.assertEquals("mayor", result);
    }
}
