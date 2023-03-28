// Import necessary libraries
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WordManager {
    private final List<List<String>> words_dataset = new ArrayList<>();
    private final int wordCount;

    // Create a Scanner object to read user input from the console
    Scanner scanner = new Scanner(System.in);

    // Constructor to initialize the WordManager object with an array of words
    public WordManager(String[] words) {
        wordCount = words.length;
        for (short i = 0; i < 5; i++) {
            // Add all the words to the second difficulty level
            if (i == 1){
                words_dataset.add(new ArrayList<>(List.of(words)));
            }
            // Add empty lists for the other difficulty levels
            else words_dataset.add(new ArrayList<>());
        }
    }

    // Method to select and display a random word from the WordManager's dataset
    // prompt the user to enter a guess
    // move the word to the appropriate difficulty level based on the user's response
    public void getWord(){
        // Initializing for a variable for selecting the difficulty level of the word
        short n = 0;

        // Track the total number of words seen so far
        int count = 0;

        // Iterate through the 5 difficulty levels
        for (short i = 0; i<5; i++){
            // Get the number of words in the current difficulty level
            int t = words_dataset.get(i).size();

            // Increment the total count of words seen so far
            count += t;

            // Check if a word should be selected from the current difficulty level
            if ((Math.random() < 0.5 & t != 0) | count == wordCount){
                // If so, set the current difficulty level index and exit the loop
                n=i;
                break;
            }
        }

        // Select a random word from the chosen difficulty level
        int m = (int) (Math.random() * words_dataset.get(n).size());
        String word = words_dataset.get(n).get(m);

        // Output the selected word to the console
        System.out.println(word);

        // Prompt the user to enter their guess for the word
        String inputString = scanner.nextLine();

        // Check if the user's guess matches the selected word and output the result to the console
        System.out.println("You entered: " + inputString + " it's " + inputString.equals(word));

        // Move the selected word to the appropriate difficulty level based on whether the user's guess was correct or not
        moveWord(n, m, inputString.equals(word));
    }

    // Method to move the selected word to the appropriate difficulty level based on whether the user's guess was correct or not
    private void moveWord(int n, int m, boolean isCorrect){
        String temp;
        if (isCorrect){
            // Move the word up one difficulty level if the user's guess was correct and the word is not already in the highest difficulty level
            if (n != 4){
                temp = words_dataset.get(n).remove(m);
                words_dataset.get(n+1).add(temp);
            }
        }
        else {
            // Move the word down one difficulty level if the user's guess was incorrect and the word is not already in the lowest difficulty level
            if (n != 0){
                temp = words_dataset.get(n).remove(m);
                words_dataset.get(n-1).add(temp);
            }
        }
    }

    // Method to check if all the words have been learned (i.e. are in the highest difficulty level)
    public Boolean allLearned(){
        return wordCount == words_dataset.get(4).size();
    }
    @Override
    public String toString() {
        return "WordManager{" +
                "words_dataset=" + words_dataset +
                '}';
    }
}
