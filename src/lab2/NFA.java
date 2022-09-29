package lab2;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class NFA {
    Set<Character> alphabet;

    Set<Integer> states;

    Integer initialState;

    Set<Integer> finalStates;

    Map<Integer, Map<Character, Set<Integer>>> transitions;

    private NFA() {
        this.alphabet = new HashSet<>();
        this.states = new HashSet<>();
        this.initialState = 0;
        this.finalStates = new HashSet<>();
        this.transitions = new HashMap<>();
    }

    public NFA(Set<Character> alphabet, Set<Integer> states, Integer initialState, Set<Integer> finalStates, Map<Integer, Map<Character, Set<Integer>>> transitions) {
        this.alphabet = new HashSet<>(alphabet);
        this.states = new HashSet<>(states);
        this.initialState = initialState;
        this.finalStates = new HashSet<>(finalStates);
        this.transitions = new HashMap<>(transitions);

        for (Integer fromState : transitions.keySet()) {
            this.transitions.put(fromState, new HashMap<>());
            for (Character viaLetter : transitions.get(fromState).keySet()) {
                Set<Integer> toStates = transitions.get(fromState).get(viaLetter);
                this.transitions.get(fromState).put(viaLetter, new HashSet<>(toStates));
            }
        }
    }

    public static NFA createFromYaml(File file) throws FileNotFoundException {
        var nfa = new NFA();

        var inputStream = new FileInputStream(file);
        var parser = new Yaml(new Constructor(NfaProperties.class));
        NfaProperties properties = parser.load(inputStream);

        for (char c = 'a'; c <= 'z' && c < 'a' + properties.alphabetSize; c++) {
            nfa.alphabet.add(c);
        }

        nfa.initialState = properties.initialState;
        for (int i = 0; i < properties.statesCount; i++) {
            nfa.states.add(i);
        }

        nfa.finalStates.addAll(properties.finalStates);

        for (var state : nfa.states) {
            nfa.transitions.put(state, new HashMap<>());
        }

        for (var transitionProps : properties.transitions) {
            var toStates = nfa.transitions.get(transitionProps.from)
                    .computeIfAbsent(transitionProps.condition, k -> new HashSet<>());

            toStates.add(transitionProps.to);
        }

        return nfa;
    }

    public static NFA createFromTxt(File file) throws FileNotFoundException {
        var nfa = new NFA();

        var scanner = new Scanner(file);

        var alphabetSize = scanner.nextInt();
        for (char c = 'a'; c <= 'z' && c < 'a' + alphabetSize; c++) {
            nfa.alphabet.add(c);
        }

        var statesCount = scanner.nextInt();
        for (int i = 0; i < statesCount; i++) {
            nfa.states.add(i);
        }
        nfa.initialState = scanner.nextInt();

        var finalStatesCount = scanner.nextInt();
        for (int i = 0; i < finalStatesCount; i++) {
            nfa.finalStates.add(scanner.nextInt());
        }

        for (var state : nfa.states) {
            nfa.transitions.put(state, new HashMap<>());
        }

        while (scanner.hasNext()) {
            var from = scanner.nextInt();
            var cond = scanner.next().charAt(0);
            var to = scanner.nextInt();

            var toStates = nfa.transitions.get(from)
                    .computeIfAbsent(cond, k -> new HashSet<>());
            toStates.add(to);
        }

        return nfa;
    }


    public Set<Integer> getReachableStates() {
        return getReachableStates(initialState);
    }

    public Set<Integer> getReachableStates(int fromState) {
        var fromStates = new LinkedList<Integer>();
        fromStates.add(fromState);

        return getReachableFromStates(fromStates);
    }

    public Set<Integer> getReachableFromStates(Collection<Integer> states) {
        var fromStates = new LinkedList<>(states);

        var reachableStates = new HashSet<Integer>();

        while (!fromStates.isEmpty()) {
            var from = fromStates.poll();

            if (reachableStates.contains(from)) {
                continue;
            }

            reachableStates.add(from);
            var transition = transitions.get(from);
            for (var condition : transition.keySet()) {
                for (var toState : transition.get(condition)) {
                    if (!reachableStates.contains(toState)) {
                        fromStates.add(toState);
                    }
                }
            }
        }

        return reachableStates;
    }

    Set<Integer> processWordFromStates(String word, Set<Integer> fromStates) {
        var currStates = new HashSet<>(fromStates);
        for (var condition : word.toCharArray()) {
            var nextStates = new HashSet<Integer>();
            for (Integer from : currStates) {
                if (transitions.get(from).containsKey(condition)) {
                    nextStates.addAll(transitions.get(from).get(condition));
                }
            }
            currStates = new HashSet<>(nextStates);
        }
        return currStates;
    }
}
