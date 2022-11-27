package lab3;

import java.util.*;

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


    public static class OperatorCharacters {
        private final Map<Character, OperatorCharacters> _map;

        public OperatorCharacters() {
            _map = new HashMap<Character, OperatorCharacters>();
        }

        public OperatorCharacters getOrSet(Character c) {
            var map = _map.getOrDefault(c, null);
            if (map != null) return map;

            map = new OperatorCharacters();
            _map.put(c, map);
            return map;
        }

        public OperatorCharacters get(Character c) {
            return _map.get(c);
        }

        public boolean containsKey(Character c) {
            return _map.containsKey(c);
        }
    }

    public static OperatorCharacters getOperatorsMap(String[] operators) {
        var result = new OperatorCharacters();

        for (var operator : operators) {
            var curr = result;
            for (int i = 0; i < operator.length(); ++i) {
                curr = curr.getOrSet(operator.charAt(i));
            }
        }

        return result;
    }
}
