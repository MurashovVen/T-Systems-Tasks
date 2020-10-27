package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

public class Calculator {
    private final LinkedList<Double> numList = new LinkedList<>();
    private final LinkedList<Character> opList = new LinkedList<>();
    private final HashMap<Character, BinaryOperator<Double>> opMap = new HashMap<>();
    {
        opMap.put('/', (x, y) -> x/y );
        opMap.put('*', (x, y) -> x*y );
        opMap.put('-', (x, y) -> x-y );
        opMap.put('+', Double::sum);
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if(statement == null || statement.isEmpty()  || incorrectSymbols(statement)) {
            return null;
        }

        char[] chars = statement.toCharArray();

        try {
            //Shunting-yard algorithm
            for(int i = 0; i < chars.length; i++) {
                char currChar = chars[i];

                if (currChar == '(') {
                    opList.push(currChar);

                } else if (currChar == ')') {
                    while (!opList.isEmpty() && opList.peek() != '(') {
                        doNextOperation();
                    }

                    //Missing opening parenthesis
                    if(opList.isEmpty()) {
                        return null ;
                    } else {
                        opList.pop();
                    }
                } else if (isPartOfNum(currChar)) {
                    StringBuilder numStr = new StringBuilder(Character.toString(currChar));

                    while(i+1 < chars.length && isPartOfNum(chars[i+1])) {
                        numStr.append(chars[++i]);
                    }

                    numList.push(Double.parseDouble(numStr.toString()));
                } else {
                    if (!opList.isEmpty() && getPrecedence(opList.peek()) >= getPrecedence(currChar)) {
                        doNextOperation();
                    }

                    opList.push(currChar);
                }
            }
            if(opList.contains('(')) {
                return null;
            }

            while (numList.size() >= 2 && !opList.isEmpty()){
                doNextOperation();
            }

            //Missing closing parenthesis
            if(!opList.isEmpty()) {
                return null;
            }

        } catch (Exception ex) {
            return null;
        }

        return new DecimalFormat("#.####").format(numList.pop());
    }

    /**
     * @return true if character is digit or '.' (dot)
     */
    private boolean isPartOfNum(char character){
        return Character.isDigit(character) || character =='.' ;
    }

    /**
     * @return int value defining precedence
     */
    private int getPrecedence(Character operation){
        return operation == '('? -1 : (operation == '-' || operation == '+'? 0 : 1);
    }

    /**
     * Computation.
     *
     * @throws ArithmeticException if the operation cannot be performed
     */
    private void doNextOperation() throws ArithmeticException{
        double b = numList.pop();
        double a = numList.pop();
        char opChar = opList.pop();

        if (opChar == '/' && b == 0) {
            throw new ArithmeticException("Division by zero");
        }

        BinaryOperator<Double> operation = opMap.get(opChar);
        numList.push(operation.apply(a,b));
    }

    /**
     * Filter numbers, operations, parentheses, consecutive duplicates of non-numbers.
     *
     * @return true if statement is incorrect.
     */
    private boolean incorrectSymbols(String statement){
        int[] chars = statement.chars().toArray();

        return IntStream.range(0 , chars.length).
                filter(i -> {
                    int x = chars[i];
                    return !((x >= 48 && x <= 57) || (x >= 40 && x <= 43) || (x >= 45 && x<=47))
                            || (i < chars.length - 1 && chars[i] == chars[i+1] && (x <= 48 || x >= 57));
                })
                .count() != 0;
    }
}