package uvg.edu.gt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LispFunTest {

    @Test
    void testDefunParsingAndEvaluation() throws Exception {
        // Inicializa el entorno y el manejador de funciones definidas por el usuario
        Environment environment = new Environment();
        DefunHandler defunHandler = new DefunHandler(environment);
        Parser parser = new Parser(); // Asegúrate de que Parser puede ser instanciado si es necesario
        
        // Simula la entrada de la expresión DEFUN
        String defunExpression = "(DEFUN sumar (a b) (+ a b))";
        
        // Parsea la expresión DEFUN
        Object parsedExpression = Parser.parse(defunExpression);
        
        // Asumiendo que tienes un método para evaluar la expresión parseada y actualizar el entorno
        ExpressionEvaluator evaluator = new ExpressionEvaluator(environment);
        evaluator.evaluate(parsedExpression);
        
        // Verifica que la función 'sumar' esté definida en el entorno después de la evaluación
        assertNotNull(environment.lookupFunction("sumar"), "La función 'sumar' debería estar definida");
        
        // Simula la llamada a la función 'sumar' con argumentos específicos
        String functionCall = "(sumar 3 5)";
        Object callResult = evaluator.evaluate(Parser.parse(functionCall));
        
        // Verifica el resultado de llamar a la función 'sumar'
        assertEquals(8, callResult, "La llamada a la función 'sumar' debería resultar en 8");
    }
}
