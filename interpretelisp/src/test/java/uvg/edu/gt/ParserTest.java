package uvg.edu.gt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ParserTest {

    @Test
    void testParseSimpleExpression() throws Exception {
        Object result = Parser.parse("(+ 1 2)");
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        assertEquals("+", resultList.get(0));
        assertEquals(1, resultList.get(1));
        assertEquals(2, resultList.get(2));
    }

    @Test
    void testParseNestedExpression() throws Exception {
        Object result = Parser.parse("(+ 1 (* 2 3))");
        assertTrue(result instanceof List);
        List<?> resultList = (List<?>) result;
        assertEquals("+", resultList.get(0));
        assertEquals(1, resultList.get(1));
        assertTrue(resultList.get(2) instanceof List);
        List<?> nestedList = (List<?>) resultList.get(2);
        assertEquals("*", nestedList.get(0));
        assertEquals(2, nestedList.get(1));
        assertEquals(3, nestedList.get(2));
    }


}
