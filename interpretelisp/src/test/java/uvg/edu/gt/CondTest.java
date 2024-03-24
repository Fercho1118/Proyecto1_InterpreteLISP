package uvg.edu.gt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    @Test
    void testCondTrueCondition() throws Exception {
        Interpreter interpreter = new Interpreter();
        interpreter.interpret("(COND ((> 3 2) 'mayor))");
    }

    @Test
    void testCondFalseCondition() throws Exception {
        Interpreter interpreter = new Interpreter();
        interpreter.interpret("(COND ((< 3 2) 'menor) (T 'otro))");
    }

    @Test
    void testCondNoTrueCondition() throws Exception {
        Interpreter interpreter = new Interpreter();
        interpreter.interpret("(COND ((< 3 3) 'menor))");
    }
}
