import java.sql.*;
import static java.lang.System.*;

public class WordManager {

    // Constants for minimum and maximum word difficulties
    private static final int MIN_DIFFICULTY = 1;
    private static final int MAX_DIFFICULTY = 5;

    // Connection object to communicate with the database
    private final Connection conn;

    // Total number of words in the word list
    private final int WORD_COUNT;

    // Difficulty level of the current word
    private int current_difficulty_lvl;

    // Getter method for current difficulty level
    public int getCurrent_difficulty_lvl() {
        return current_difficulty_lvl;
    }

    // Constructor method to initialize database connection and get word count
    public WordManager(String user, String password) {
        try {
            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vocabulary", user, password);
            out.println("connection successful");
        } catch (SQLException e) {
            // Print error message and throw exception if connection fails
            out.println("connection failed");
            throw new RuntimeException(e);
        }

        Statement stmt;
        ResultSet rs;
        try {
            // Create statement object and execute query to get word count
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery("SELECT count(*) FROM word_list");
            rs.next();
            WORD_COUNT = rs.getInt(1);
            out.println("word count=" + WORD_COUNT);
        } catch (SQLException e) {
            // Throw exception if query fails
            throw new RuntimeException(e);
        }
    }

    // Private method to get word distribution across different difficulty levels
    private int[] getWordDistribution() {
        // Initialize an array to hold word distribution
        int[] distribution = new int[MAX_DIFFICULTY];
        Statement stmt;
        ResultSet rs;
        try {
            // Iterate through different difficulty levels
            for (short i = MIN_DIFFICULTY; i <= MAX_DIFFICULTY; i++) {
                // Create statement object and execute query to get word count for current difficulty level
                stmt = this.conn.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(*) FROM word_list WHERE word_score = " + i + ";");
                rs.next();
                distribution[i - 1] = rs.getInt(1);
            }
        } catch (SQLException e) {
            // Throw exception if query fails
            throw new RuntimeException(e);
        }
        // Return word distribution array
        return distribution;
    }

    // Public method to get a random word from the word list
    public String getRandomWord() {
        // Initializing a variable for selecting the difficulty level of the word
        current_difficulty_lvl = 0;

        // Get word distribution array
        int[] distribution = this.getWordDistribution();

        // Initialize variable to track the total number of words seen so far
        int count = 0;

        // Initialise output variable
        String word;

        // Iterate through the difficulty levels
        for (short i = MIN_DIFFICULTY; i <= MAX_DIFFICULTY; i++) {
            // Get the number of words in the current difficulty level
            int t = distribution[i - 1];

            // Increment the total count of words seen so far
            count += t;

            // Check if a word should be selected from the current difficulty level
            if ((Math.random() < (float) t / WORD_COUNT & t != 0) | count == WORD_COUNT) {
                // If so, set the current difficulty level index and exit the loop
                current_difficulty_lvl = i;
                break;
            }
        }

        try {
            // Execute a query to select a word from the word_list table
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT word FROM word_list WHERE word_score = " + current_difficulty_lvl);

            // Generate a random number to select a random word from the selected difficulty level
            int randomWordId = (int) (Math.random() * distribution[current_difficulty_lvl - 1]);

            // Get the selected word from the ResultSet
            rs.absolute(randomWordId);
            word = rs.getString("word");

            // Close ResultSet and Statement objects
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            // Throw a runtime exception if the query fails
            throw new RuntimeException(e);
        }

        // Return the selected word
        return word;
    }

    // Public method to change the difficulty score of a given word
    public void changeWordScore(String word, boolean isCorrect) {
        Statement stmt;
        ResultSet rs;
        try {
            // Create statement object and execute query to get the current difficulty score of the word
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery("SELECT word_score FROM word_list WHERE word = '" + word + "'");
            rs.next();
            int wordScore = rs.getInt(1);

            // Increment or decrement the difficulty score based on whether the word was answered correctly
            if (isCorrect) {
                if (wordScore < MAX_DIFFICULTY) {
                    wordScore++;
                    stmt.executeUpdate("UPDATE word_list SET word_score = " + wordScore + " WHERE word = '" + word + "'");
                }
            } else {
                if (wordScore > MIN_DIFFICULTY) {
                    wordScore--;
                    stmt.executeUpdate("UPDATE word_list SET word_score = " + wordScore + " WHERE word = '" + word + "'");
                }
            }
        } catch (SQLException e) {
            // Throw a runtime exception if the query fails
            throw new RuntimeException(e);
        }

        // Check if all words in the word list have been learned
        if (this.getWordDistribution()[MAX_DIFFICULTY - 1] == WORD_COUNT) {
            UI.allLearned(this);
        }
    }

    // Public method to reset the difficulty score of all words in the word list to the default value
    public void resetWordScore() {
        Statement stmt;
        try {
            // Create statement object and execute query to update the word_score column of all rows in the word_list table to the default value
            stmt = this.conn.createStatement();
            stmt.executeUpdate("UPDATE word_list SET word_score = " + MIN_DIFFICULTY);
        } catch (SQLException e) {
            // Throw a runtime exception if the query fails
            throw new RuntimeException(e);
        }
    }
}
