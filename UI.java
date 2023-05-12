import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI {
    // Declare static variables to be accessed by all methods in the class.
    static String current_word;
    static boolean guess_made = true;

    // method that creates a pop-up window with a given title, message and duration.
    private static void createPopUpWindow(String title, String text, int ms) {
        // Create a new JFrame object for the pop-up window
        JFrame popupFrame = new JFrame(title);

        // Set the size and location of the pop-up window
        popupFrame.setSize(300, 150);
        popupFrame.setLocationRelativeTo(null);

        // Create a JPanel object to hold the components of the pop-up window
        JPanel popupPanel = new JPanel();

        // Create a JLabel object with the pop-up message
        JLabel popupLabel = new JLabel(text);

        // Add the label to the panel
        popupPanel.add(popupLabel);

        // Add the panel to the frame
        popupFrame.add(popupPanel);

        // Set the default close operation of the pop-up window to dispose the frame
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make the pop-up window visible
        popupFrame.setVisible(true);

        // Create a timer to close the pop-up window
        Timer timer = new Timer(ms, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popupFrame.dispose();
            }
        });

        // Start the timer
        timer.start();

    }

    // private method that centers a JLabel horizontally within its container.
    private static void center_label(JLabel jlabel) {
        // Set the maximum size of the label to the maximum width of its container, and the preferred height of the label.
        jlabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, jlabel.getPreferredSize().height));

        // Set the horizontal alignment of the label to CENTER.
        jlabel.setHorizontalAlignment(JLabel.CENTER);
    }

    // method to create the main program window.
    public static void create_window(WordManager manager) {
        // Create a new JFrame object for the main window.
        JFrame frame = new JFrame("Duolingo 2.0");

        // Set the size and location of the main window.
        frame.setSize(320, 200);
        frame.setLocationRelativeTo(null);

        // Set the default close operation of the frame to exit the application
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a new JPanel object to hold the components of the main window.
        JPanel panel = new JPanel();

        // Create a "next word" JButton object and set its position and size.
        JButton next_word_button = new JButton("next word");
        next_word_button.setBounds(90, 40, 120, 30);

        // Create a "?" JButton object for the help menu and set its position and size.
        JButton help_button = new JButton("?");
        help_button.setBounds(255, 0, 50, 30);

        // Create a new JTextArea object for displaying messages.
        JTextArea textArea = new JTextArea("");
        textArea.setEditable(false);

        // Create a new JTextField object for entering user input.
        JTextField textField = new JTextField();
        textField.setBounds(50, 100, 200, 30);

        // Add the components to the main window.
        frame.getContentPane().add(textField);
        frame.getContentPane().add(help_button);
        frame.getContentPane().add(next_word_button);

        // Add the text area to the panel.
        panel.add(textArea);

        // Add the panel to the frame
        frame.getContentPane().add(panel);

        // Make the frame visible
        frame.setVisible(true);

        // Set the action listener for the "next word" button.
        next_word_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // If guess has been made for the current word, reset the text field and message area and set guess_made flag to false.
                if (guess_made) {
                    textField.setText("");
                    textField.grabFocus();
                    textArea.setText("");
                    guess_made = false;

                    // Get a random word from the WordManager_V2 object and display it in a pop-up window for a duration proportional to the current difficulty level.
                    current_word = manager.getRandomWord();
                    createPopUpWindow("next_word", current_word, 3000 / manager.getCurrent_difficulty_lvl());
                } else {
                    // Display a warning message in a pop-up window if a guess hasn't been made yet.
                    createPopUpWindow("Achtung!", "check your guess first by pressing 'Enter'", 3000);
                }
            }
        });

        // Set the action listener for the help button.
        help_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Create a new JFrame object for the help menu.
                JFrame popupFrame = new JFrame("FAQ");

                // Set the size and location of the help menu.
                popupFrame.setSize(240, 150);
                popupFrame.setLocationRelativeTo(null);

                // Create a new JTextArea object with frequently asked questions and set its properties.
                JTextArea text = new JTextArea("""
                        1)The test never ends
                        2)To get next word, press 'next word' then try to repeat it in the text field below
                        3) check your spelling by pressing 'Enter'
                        Good Luck!""");
                text.setEditable(false);
                text.setLineWrap(true);
                text.setWrapStyleWord(true);

                // Add the text to the content pane.
                popupFrame.getContentPane().add(text);

                // Set the default close operation of the pop-up window to dispose the frame
                popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Make the pop-up window visible
                popupFrame.setVisible(true);
            }
        });

        // Create a KeyListener for the textField to handle Enter key press events
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // Check if the Enter key was pressed and a guess has not already been made
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !guess_made) {
                    // Move focus to the next word button
                    next_word_button.grabFocus();
                    guess_made = true;
                    String input = textField.getText();
                    boolean isCorrect = input.equals(current_word);
                    // Change the score of the current word in the WordManager
                    manager.changeWordScore(current_word, isCorrect);
                    if (isCorrect) {
                        textArea.setText("correct!");
                    } else {
                        textArea.setText("""
                         Wrong!
                         the correct spelling is: """ + current_word);
                        help_button.repaint();
                    }
                }
            }
        });
    }
    public static void allLearned(WordManager manager){
        // Create a pop-up window and set size and location
        JFrame popupFrame = new JFrame("congratulations!");
        popupFrame.setSize(240, 150);
        popupFrame.setLocationRelativeTo(null);

        // Create a reset button
        JButton reset_button = new JButton("reset");

        // Create a panel for the reset button
        JPanel buttonPopupPanel = new JPanel();

        // Create a panel for text labels
        JPanel textPopupPanel = new JPanel();

        // Set the layout of the text panel
        textPopupPanel.setLayout(new BoxLayout(textPopupPanel, BoxLayout.Y_AXIS));

        // Create labels with the message
        JLabel label1 = new JLabel("You have learned English language!");
        JLabel label2 = new JLabel("Now it's time for simplified Chinese!");
        JLabel label3 = new JLabel("(or you can just reset all words)");

        //center the text in the labels
        center_label(label1);
        center_label(label2);
        center_label(label3);

        // Add the labels to the text panel
        textPopupPanel.add(label1);
        textPopupPanel.add(label2);
        textPopupPanel.add(label3);

        // Add the reset button to the button panel
        buttonPopupPanel.add(reset_button);

        // Add an action listener to the reset button
        reset_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset word scores
                manager.resetWordScore();
                // Close the pop-up window
                popupFrame.dispose();
            }
        });

        // Set the layout of the pop-up window
        popupFrame.setLayout(new GridLayout(2,1));

        // Add the panels to the window
        popupFrame.add(textPopupPanel);
        popupFrame.add(buttonPopupPanel);

        // Set the default close operation of the pop-up window to dispose the frame
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make the pop-up window visible
        popupFrame.setVisible(true);

    }

}