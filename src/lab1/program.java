package lab1;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/*
* Знайти ті слова які мають найбільшу кількість різних літер,
* тобто літери, що повторюються у слові, не враховувати.
* */

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
            System.out.printf("File: %s\n\tSize: %s\n", file.getName(), Files.size(file.toPath()));

            var words = getWordsWithMostUniqueLetters(file);

            System.out.println("Words with max unique letters count:");
            for (String word: words) {
                System.out.print(word + ' ');
            }
        } catch (IOException e) {
            System.out.println("An exception has occurred =(\nClosing program");
        }
    }

    private static ArrayList<String> getWordsWithMostUniqueLetters(File inputFile) throws FileNotFoundException {
        var resultWords = new ArrayList<String>();
        var maxLettersCount = 0;

        var input = new Scanner(inputFile);
        while (input.hasNext()) {
            var word = input.next();

            var currentLettersCount = countUniqueLetters(word);

            if (currentLettersCount < maxLettersCount) {
                continue;
            }

            if (currentLettersCount > maxLettersCount) {
                resultWords.clear();
                maxLettersCount = currentLettersCount;
            }

            resultWords.add(word + '(' + maxLettersCount + ')');
        }

        return resultWords;
    }

    private static int countUniqueLetters(String word) {
        var letterSet = new HashSet<Character>();
        for(int i = 0; i < word.length(); i++)
        {
            var letter = Character.toLowerCase(word.charAt(i));
            if (('a' <= letter && letter <= 'z')) letterSet.add(letter);
        }

        return letterSet.size();
    }
}
