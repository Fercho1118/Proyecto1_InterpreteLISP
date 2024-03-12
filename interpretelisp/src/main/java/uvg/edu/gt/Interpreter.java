package uvg.edu.gt;
import java.util.List;

public class Interpreter {
    private Environment environment;
    private ExpressionEvaluator evaluator;

    public Interpreter() {
        this.environment = new Environment();
        this.evaluator = new ExpressionEvaluator(environment);
    }

    public void interpret(String expression) throws Exception {
        Object parsedExpr = Parser.parse(expression);
        Object result = evaluator.evaluate(parsedExpr);
        System.out.println("Resultado: " + result);
    }

    public static void main(String[] args) throws Exception {
        new Interpreter().interpret("(+ 1 2 3)");
    }
}