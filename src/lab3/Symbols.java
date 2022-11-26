package lab3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Symbols {
    public static boolean isSpace(char c) {
        return c == ' '  || c == '\t';
    }

    public static boolean isNewLine(char c) {
        return c == '\n' || c == '\r';
    }

    public static boolean isIdentifierSymbol(char c) {
        return Symbols.isLetter(c)
                || c == '_'
                || c == '$';
    }

    public static boolean isLetter(char c) {
        var upper = Character.toUpperCase(c);
        return ('A' <= upper && upper <= 'Z');
    }

    public static boolean isNumeric(char c) {
        return '0' <= c && c <= '9';
    }

    public static List<HashSet<Character>> getOperatorSymbols(String[] operators) {
        var maxOperatorLength = Arrays.stream(operators)
                .mapToInt(String::length)
                .max();

        if (maxOperatorLength.isEmpty()) return new ArrayList<>();
        var max = maxOperatorLength.getAsInt();
        var symbolSets = new ArrayList<HashSet<Character>>(maxOperatorLength.getAsInt());
        for (int i = 0; i < max; ++i) {
            symbolSets.add(new HashSet<>());
        }

        for (var operator: operators) {
            for (int i = 0; i < operator.length(); ++i) {
                symbolSets.get(i).add(operator.charAt(i));
            }
        }

        return symbolSets;
    }
}
