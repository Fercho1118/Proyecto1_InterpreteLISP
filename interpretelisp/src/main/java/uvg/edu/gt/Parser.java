package uvg.edu.gt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Clase Parser que ofrece la funcionalidad para analizar cadenas de texto que representan expresiones en notación
 * de Lisp y convertirlas en una estructura de datos manejable en Java, como Listas y Strings.
 */
public class Parser {

    /**
     * Analiza una expresión en notación Lisp y la convierte en una estructura de datos de Java.
     * Las expresiones pueden incluir números enteros y operadores.
     *
     * @param expression La expresión Lisp como un String.
     * @return Una estructura de datos representando la expresión analizada que puede ser una Lista o un valor primitivo.
     * @throws Exception Si la expresión tiene paréntesis no balanceados o contiene errores de sintaxis.
     */
    public static Object parse(String expression) throws Exception {
        expression = expression.trim();

        if (expression.startsWith("(DEFUN")) {
            return parseDefun(expression);
        }
        else if (expression.startsWith("(COND")) {
            return parseCond(expression);
        }

        if (!isBalanced(expression)) {
            throw new Exception("Unbalanced parentheses in expression: " + expression);
        }

        return parseExpression(expression);
    }
    

    /**
     * Verifica si los paréntesis en la expresión están correctamente balanceados.
     *
     * @param expression La expresión a verificar.
     * @return true si los paréntesis están balanceados; false en caso contrario.
     */
    private static boolean isBalanced(String expression) {
        Stack<Character> stack = new Stack<>();
        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    /**
     * Analiza una expresión individual o una lista de expresiones contenida entre paréntesis.
     *
     * @param expression La expresión a analizar.
     * @return Un objeto que representa la expresión analizada, que puede ser una Lista o un valor primitivo.
     */
    public static Object parseExpression(String expression) throws Exception {
        // Manejo de expresiones que empiezan con una comilla simple
        if (expression.startsWith("'")) {
            String quotedExpression = expression.substring(1);
            List<Object> quotedList = new ArrayList<>();
            quotedList.add("QUOTE");
            // Asume que la expresión citada puede ser un valor literal o una lista
            quotedList.add(parse(quotedExpression)); // Reutiliza parse para analizar la expresión citada
            return quotedList;
        } 
        // Manejo de expresiones que empiezan con paréntesis
        else if (expression.startsWith("(")) {
            List<Object> list = new ArrayList<>();
            int start = 1; // Skip initial '('
            for (int i = 1, depth = 0; i < expression.length(); i++) {
                char ch = expression.charAt(i);
                if (ch == '(') depth++;
                else if (ch == ')') {
                    if (depth == 0) {
                        String subExpr = expression.substring(start, i).trim();
                        if (!subExpr.isEmpty()) {
                            list.add(parseExpression(subExpr));
                        }
                        start = i + 1;
                    } else {
                        depth--;
                    }
                } else if (ch == ' ' && depth == 0) {
                    String token = expression.substring(start, i).trim();
                    if (!token.isEmpty()) {
                        list.add(parseToken(token));
                    }
                    start = i + 1;
                }
            }
            return list;
        } 
        // Manejo de tokens individuales
        else {
            return parseToken(expression);
        }
    }
    
    /**
     * Convierte un token individual en un objeto. Intenta convertir el token en un entero; si falla, lo trata como una cadena.
     *
     * @param token El token a convertir.
     * @return Un Object que puede ser un Integer o un String dependiendo del contenido del token.
     */
    private static Object parseToken(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return token;
        }
    }
    
    /**
     * Analiza una expresión DEFUN, extrayendo y procesando el nombre de la función,
     * los parámetros y el cuerpo de la función.
     * 
     * @param expression La expresión DEFUN completa como una cadena de texto.
     * @return Una representación de la definición de la función en forma de lista,
     *         que incluye el identificador "DEFUN", el nombre de la función, la lista de parámetros,
     *         y el cuerpo de la función.
     * @throws Exception Si hay un error de sintaxis en los parámetros de DEFUN o si los paréntesis no están balanceados.
     */
    private static Object parseDefun(String expression) throws Exception {
        String defunBody = expression.substring("(DEFUN ".length(), expression.length() - 1).trim();

        int endOfFunctionNameIndex = defunBody.indexOf(' ');
        String functionName = defunBody.substring(0, endOfFunctionNameIndex).trim();

        String paramsAndBody = defunBody.substring(endOfFunctionNameIndex).trim();

        int startOfParamsIndex = paramsAndBody.indexOf('(');
        int endOfParamsIndex = paramsAndBody.indexOf(')') + 1; 
        if (startOfParamsIndex == -1 || endOfParamsIndex == 0) {
            throw new Exception("Syntax error in DEFUN parameters.");
        }

        String paramString = paramsAndBody.substring(startOfParamsIndex, endOfParamsIndex).trim();
        List<String> parameters = parseParameters(paramString);

        String bodyString = paramsAndBody.substring(endOfParamsIndex).trim();
        Object body = parse(bodyString); 

        List<Object> defunExpression = new ArrayList<>();
        defunExpression.add("DEFUN");
        defunExpression.add(functionName);
        defunExpression.add(parameters);
        defunExpression.add(body);

        return defunExpression;
    }
    
    private static Object parseCond(String expression) throws Exception {
        if (expression.length() <= 5) {
            throw new Exception("Expresión COND vacía o incompleta.");
        }
        
        String condBody = expression.substring(5, expression.length() - 1).trim();
        List<Object> condExpression = new ArrayList<>();
        condExpression.add("COND");

        int start = 0; 
        int depth = 0; 
        for (int i = 0; i < condBody.length(); i++) {
            char ch = condBody.charAt(i);
            if (ch == '(') depth++;
            else if (ch == ')') depth--;

            if ((depth == 0 && ch == ' ') || i == condBody.length() - 1) {
                String subExpr = condBody.substring(start, i + 1).trim();
                if (!subExpr.isEmpty()) {
                    condExpression.add(parse(subExpr));
                }
                start = i + 1;
            }
        }

        if (depth != 0) {
            throw new Exception("Unbalanced parentheses in COND expression: " + expression);
        }

        return condExpression;
    }

    
    

    /**
     * Analiza la cadena de parámetros de una función, esperando que estén separados por espacios
     * y encerrados entre paréntesis. Los parámetros son devueltos como una lista de cadenas.
     * 
     * @param paramString La cadena de texto que contiene los parámetros de la función, incluyendo los paréntesis.
     * @return Una lista de cadenas, cada una representando un parámetro de la función.
     * @throws Exception Si la cadena de parámetros está vacía o los parámetros no están en el formato esperado.
     */
    
    private static List<String> parseParameters(String paramString) throws Exception {
    // Asume que los parámetros están encerrados en paréntesis y separados por espacios.
    // Elimina los paréntesis iniciales y finales.
    String params = paramString.substring(1, paramString.length() - 1).trim();
    if (params.isEmpty()) {
        return new ArrayList<>();
    }
    // Divide los parámetros por espacios. Esto es simplista y podría necesitar ajuste para casos más complejos.
    return Arrays.asList(params.split("\\s+"));
}
}

