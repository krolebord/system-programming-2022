package lab2;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class program {
    public static void main(String[] args) {
        var fileChooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
        int result = fileChooser.showDialog(null, "Choose test file");

        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("No file - No output!!1!");
            return;
        }

        var file = fileChooser.getSelectedFile();

        try {
            var nfa = NFA.createFromTxt(file);
            System.out.println(file.getName());
            System.out.println();

            var reachableStates = nfa.getReachableStates();

            System.out.println("Reachable states:");
            System.out.println(reachableStates);
            System.out.println();

            System.out.println("Enter w0");
            var scanner = new Scanner(System.in);
            var w0 = scanner.next();

            var afterW0 = nfa.processWordFromStates(w0, reachableStates);

            var reachableFromAfterW0 = nfa.getReachableFromStates(afterW0);
            reachableFromAfterW0.retainAll(nfa.finalStates);
            System.out.println(reachableFromAfterW0.isEmpty() ? "w1w0w2 does not exist" : "w1w0w2 exists");
        } catch (IOException e) {
            System.out.println("An exception has occurred =(\nClosing program");
        }
    }
}
