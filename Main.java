import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Gson gson = new Gson();


        // СЪЗДАВАНЕ НА words.json, АКО НЕ СЪЩЕСТВУВА

        File file = new File("words.json");

        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                Map<String, List<String>> words = new LinkedHashMap<>();
                words.put("EASY", Arrays.asList("java", "code", "game", "ai", "data"));
                words.put("MEDIUM", Arrays.asList("computer", "developer", "mouse", "compiler", "keyboard"));
                words.put("HARD", Arrays.asList("programming", "architecture", "recursion", "variable", "inheritance"));
                words.put("INSANE", Arrays.asList(
                        "object oriented programming",
                        "design patterns",
                        "machine learning",
                        "artificial intelligence",
                        "software engineering",
                        "internet of things"
                ));
                gson.toJson(words, writer);
            } catch (Exception e) {
                System.out.println("Error creating the file!");
                return;
            }
        }


        // ЧЕТЕНЕ НА JSON ФАЙЛА

        Map<String, List<String>> wordsByDifficulty;

        try {
            FileReader reader = new FileReader("words.json");

            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            wordsByDifficulty = gson.fromJson(reader, type);

        } catch (Exception e) {
            System.out.println("Error reading the file!");
            return;
        }


        // ИЗБОР НА ТРУДНОСТ

        System.out.println("Choose difficulty: easy, medium, hard, insane");
        String difficulty = scanner.nextLine().toUpperCase();

        if (!wordsByDifficulty.containsKey(difficulty)) {
            System.out.println("Invalid difficulty!");
            return;
        }


        //  СЛУЧАЙНА ДУМА

        List<String> words = wordsByDifficulty.get(difficulty);
        Random random = new Random();
        String wordToGuess = words.get(random.nextInt(words.size()));


        // СКРИТАТА ДУМА

        char[] guessedWord = new char[wordToGuess.length()];

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == ' ') {
                guessedWord[i] = ' ';
            } else {
                guessedWord[i] = '_';
            }
        }

        int attempts = 6;
        Set<Character> usedLetters = new HashSet<>();



        while (attempts > 0) {

            System.out.println("\nWord: " + String.valueOf(guessedWord));
            System.out.println("Attempts left: " + attempts);
            System.out.println("Letters used: " + usedLetters);

            System.out.print("Enter a letter: ");
            String input = scanner.nextLine().toLowerCase();

            if (input.length() != 1) {
                System.out.println("Please, enter only one letter!");
                continue;
            }

            char letter = input.charAt(0);

            if (usedLetters.contains(letter)) {
                System.out.println("This letter is already used!");
                continue;
            }

            usedLetters.add(letter);
            boolean found = false;

            //дали буквата съществува в думата
            for (int i = 0; i < wordToGuess.length(); i++) {
                if (wordToGuess.charAt(i) == letter) {
                    guessedWord[i] = letter;
                    found = true;
                }
            }

            if (!found) {
                attempts--;
                System.out.println("Wrong letter!");
            }

            if (String.valueOf(guessedWord).equals(wordToGuess)) {
                System.out.println("\nCongratulations!");
                System.out.println("The word is: " + wordToGuess);
                return;
            }
        }


        System.out.println("\nFAIL!");
        System.out.println("The word was: " + wordToGuess);
    }
}
