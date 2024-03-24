package uvg.edu.gt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CondTest {

    @Test
    void testGreaterThanOperator() throws Exception {
        String expression = "(> 3 2)";
        Object result = Parser.parse(expression);
        assertTrue((Boolean) result);
    }

    @Test
    void testLessThanOperator() throws Exception {
        String expression = "(< 2 3)";
        Object result = Parser.parse(expression);
        assertTrue((Boolean) result);
    }

    @Test
    void testCondEvaluation() throws Exception {
        String expression = "(COND ((> 3 2) 'true) ((< 3 2) 'false))";
        Object result = Parser.parse(expression);
        assertEquals("true", result);
    }

    @Test
    void testCondDefaultCase() throws Exception {
        String expression = "(COND ((> 2 3) 'false) (T 'true))";
        Object result = Parser.parse(expression);
        assertEquals("true", result);
    }
}

