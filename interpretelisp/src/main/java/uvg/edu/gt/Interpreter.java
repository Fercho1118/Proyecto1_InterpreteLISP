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

    public void interpretFile(String filePath) throws Exception {
        List<String> lines = FileUtils.readLines(filePath);
        for (String line : lines) {
            interpret(line);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            String filePath = args[0];
            new Interpreter().interpretFile(filePath);
        } else {
            System.out.println("Por favor, proporcione el camino del archivo como argumento.");
        }
    }
}