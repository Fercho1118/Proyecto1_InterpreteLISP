package uvg.edu.gt;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    public static Object parse(String expression) throws Exception {
        expression = expression.trim();
        if (!isBalanced(expression)) {
            throw new Exception("Unbalanced parentheses in expression: " + expression);
        }
        return parseExpression(expression);
    }

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
            return list;
        } else {
            return parseToken(expression);
        }
    }

    private static Object parseToken(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            return token; 
        }
    }
}

