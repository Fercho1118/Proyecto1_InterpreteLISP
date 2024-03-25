/**
 * Nicolas Concuá - 23197
 * Fernando Hernández - 23645
 * Fernando Rueda - 23748
 */

package uvg.edu.gt;
import java.util.List;


/**
 * La clase Interpreter se encarga de interpretar expresiones escritas en un lenguaje específico, 
 * evaluando estas expresiones en un entorno dado y mostrando los resultados.
 * Esta clase puede procesar tanto expresiones individuales como archivos completos que contienen múltiples expresiones.
 */
public class Interpreter {
    private Environment environment;
    private ExpressionEvaluator evaluator;

    /**
     * Constructor que inicializa un nuevo intérprete con un nuevo entorno y evaluador de expresiones.
     */
    public Interpreter() {
        this.environment = new Environment();
        this.evaluator = new ExpressionEvaluator(environment);
    }

    /**
     * Interpreta una única expresión pasada como un string, analizándola, evaluándola y mostrando el resultado.
     *
     * @param expression La expresión a interpretar.
     * @throws Exception Si el análisis o la evaluación de la expresión falla.
     */
    public void interpret(String expression) throws Exception {
        Object parsedExpr = Parser.parse(expression);
        Object result = evaluator.evaluate(parsedExpr);
        System.out.println("Resultado: " + result);
    }

    /**
     * Interpreta todas las expresiones encontradas en un archivo, línea por línea.
     * Cada línea del archivo se considera una expresión independiente.
     *
     * @param filePath La ruta del archivo que contiene las expresiones a interpretar.
     * @throws Exception Si la lectura del archivo falla o si alguna de las expresiones en el archivo no puede ser interpretada correctamente.
     */
    public void interpretFile(String filePath) throws Exception {
        List<String> lines = FileUtils.readLines(filePath);
        for (String line : lines) {
            interpret(line);
        }
    }

    /**
     * Punto de entrada del programa. Crea una instancia del intérprete y procesa un archivo de expresiones especificado.
     *
     * @param args Argumentos de la línea de comando. No se utilizan en este ejemplo.
     * @throws Exception Si la interpretación del archivo especificado falla.
     */
    public static void main(String[] args) throws Exception {
        String filePath = "interpretelisp\\testLispExpressions.txt";
        new Interpreter().interpretFile(filePath);
    }
}