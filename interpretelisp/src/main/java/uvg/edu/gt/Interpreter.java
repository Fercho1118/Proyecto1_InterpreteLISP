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
        String filePath = "C:\\Users\\ferna\\OneDrive\\UVG\\Tercer Semestre\\Algoritmos y Estructura de Datos\\Proy 1\\Proyecto1_InterpreteLISP\\interpretelisp\\testLispExpressions.txt";
        new Interpreter().interpretFile(filePath);
    }
}