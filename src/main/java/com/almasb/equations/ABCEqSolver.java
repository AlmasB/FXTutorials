package com.almasb.equations;

/**
 * ax + b = c
 * ax = c - b
 * x = (c-b) / a
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class ABCEqSolver extends EqSolver {

    public ABCEqSolver() {
        super("[0-9]+x [\\+\\-] [0-9]+ = [0-9]+");
    }

    @Override
    public String solve(String input) {

        String[] tokens = input.split(" ");

        String op = tokens[1];

        int a = Integer.parseInt(tokens[0].substring(0, tokens[0].length() - 1));
        int b = Integer.parseInt(tokens[2]);
        int c = Integer.parseInt(tokens[4]);

        if (op.equals("-")) {
            b = -b;
        }

        int result = c - b;

        String output = result + "/" + a;

        if (result % a == 0) {
            output = String.valueOf(result / a);
        }

        return output;
    }
}
