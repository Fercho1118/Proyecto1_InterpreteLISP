package uvg.edu.gt;

import java.util.ArrayList;
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
    private static Object parseExpression(String expression) {
        if (expression.startsWith("(")) {
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
            System.out.println(list);
            return list;
        } else {
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
}

