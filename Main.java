public class Main {
    public static void main(String[] args) {
        // Create an instance of the WordManager class with the provided command line arguments
        WordManager manager = new WordManager(args);
        // Print the current state of the WordManager object
        System.out.println(manager);
        // Keep playing the game until all the words have been learned
        while (!manager.allLearned()){
            // Get a random word and prompt the user to input the correct spelling
            manager.getWord();
            // Print the current state of the WordManager object
//            System.out.println(manager);
        }
    }
}
