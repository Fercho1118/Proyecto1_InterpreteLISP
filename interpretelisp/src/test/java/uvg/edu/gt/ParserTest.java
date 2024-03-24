package uvg.edu.gt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ParserTest {

    @Test
    public void testParseSimpleExpression() throws Exception {
        String input = "(+ 1 2)";
        Object result = Parser.parse(input);
        assertTrue(result instanceof List);

        List<?> resultList = (List<?>) result;
        assertEquals("+", resultList.get(0));
        assertEquals(1, resultList.get(1));
        assertEquals(2, resultList.get(2));
    }

    @Test
    public void testParseNestedExpression() throws Exception {
        String input = "(DEFUN sumar (a b) (+ a b))";
        Object result = Parser.parse(input);
        System.out.println(result); // Agregado para depuración
        assertTrue(result instanceof List);

        List<?> resultList = (List<?>) result;
        assertEquals("DEFUN", resultList.get(0));
        assertEquals("sumar", resultList.get(1));
        assertTrue(resultList.get(2) instanceof List);
        assertTrue(resultList.get(3) instanceof List);
        
        List<?> paramsList = (List<?>) resultList.get(2);
        assertEquals("a", paramsList.get(0));
        assertEquals("b", paramsList.get(1));
        
        List<?> bodyList = (List<?>) resultList.get(3);
        assertEquals("+", bodyList.get(0));
        assertEquals("a", bodyList.get(1));
        assertEquals("b", bodyList.get(2));
    }

    @Test
    public void testParseUnbalancedParentheses() {
        String input = "(DEFUN sumar (a b (+ a b)";
        Exception exception = assertThrows(Exception.class, () -> Parser.parse(input));
        String expectedMessage = "Unbalanced parentheses";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testParseMultipleExpressions() throws Exception {
        String input = "(DEFUN sumar (a b) (+ a b)) (DEFUN restar (a b) (- a b))";
        Object result = Parser.parse(input); // parse retorna Object
        System.out.println(result.getClass().getName()); // Esto te dirá el tipo real de `result`
        System.out.println(result); // Esto mostrará la representación de `result`
        assertTrue(result instanceof List); // La verificación que está fallando
        
        @SuppressWarnings("unchecked") // Suprime advertencias sobre el casting no comprobado
        List<Object> resultList = (List<Object>) result; // Hace el casting a List<Object>
        
        assertNotNull(resultList);
        assertEquals(2, resultList.size()); // Asegurándote de que se analizaron dos expresiones
        // Agrega más aserciones aquí para verificar la estructura de las expresiones analizadas
    }
}
