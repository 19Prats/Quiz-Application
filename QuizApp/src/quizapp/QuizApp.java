package quizapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp {
    private JFrame frame;
    private JPanel questionPanel;
    private JScrollPane scrollPane;
    private JButton submitButton;
    private List<String> selectedAnswers;
    private List<Question> questions; // Store the loaded questions
    ButtonGroup buttonGroup;
    private List<JRadioButton> optionButtons = new ArrayList<>();

    public QuizApp() {
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.setLayout(new BorderLayout());

        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(questionPanel);
        scrollPane.setPreferredSize(new Dimension(980, 800));

        frame.add(scrollPane, BorderLayout.CENTER);

        loadQuestionsFromDatabase(); // Load questions from the database
        createUIForQuestions();

        submitButton = new JButton("Submit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                selectedAnswers = new ArrayList<>();
                selectedAnswers = getSelectedAnswers();
                int score = calculateScore(questions,selectedAnswers);
                Result result = new Result(score,selectedAnswers);
            }
        });
        
        frame.add(buttonPanel, BorderLayout.SOUTH);

        setUIFont(new javax.swing.plaf.FontUIResource("Sans-serif", Font.PLAIN, 13));

        frame.setVisible(true);
    }
    
    int calculateScore(List<Question> questions, List<String> selectedAnswers) {
        int correctScore = 0;

        // Iterate through the questions and compare selected answers with correct answers
        try {
            for (int i = 0; i < questions.size(); i++) {
            String selectedAnswer = selectedAnswers.get(i).substring(10).trim();
            System.out.println(selectedAnswer);
            String correctAnswer = questions.get(i).getCorrectAnswer().trim();
            System.out.println(correctAnswer);

            if (correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                correctScore++;
                System.out.println("Score : "+correctScore);
            }
        }
        } catch (Exception e) {
            System.out.println("EXCEPTION IN calculateScore: "+e);
            return 0;
        }
        

    return correctScore;
    }

    private void loadQuestionsFromDatabase() {
        questions = new ArrayList<>(); // Initialize the list

        // Establish a database connection
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quiz_db", "root", "Pratik");
            String query = "SELECT * FROM quiz";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("no");
                String question = resultSet.getString("question");
                String option1 = resultSet.getString("option1");
                String option2 = resultSet.getString("option2");
                String option3 = resultSet.getString("option3");
                String option4 = resultSet.getString("option4");
                String correctAnswer = resultSet.getString("answer");

                // Create a Question object and add it to the list
                questions.add(new Question(id, question, option1, option2, option3, option4, correctAnswer));
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Shuffle the questions to get random 20 questions
        Collections.shuffle(questions);
        questions = questions.subList(0, 20);
    }

    private void createUIForQuestions() {
        for (int i = 0; i < questions.size(); i++) {
            JLabel questionLabel = new JLabel("Question " + (i + 1) + ": " + questions.get(i).getQuestion());
            questionLabel.setFont(questionLabel.getFont().deriveFont(13.0f));
            questionPanel.add(questionLabel);

            ButtonGroup buttonGroup = new ButtonGroup();
            for (int j = 1; j <= 4; j++) {
                JRadioButton optionButton = new JRadioButton("Option " + j + ": " + questions.get(i).getOption(j));
                optionButton.setFont(optionButton.getFont().deriveFont(13.0f));
                buttonGroup.add(optionButton);
                optionButtons.add(optionButton); // Keep track of the JRadioButton
                questionPanel.add(optionButton);
            }

            if (i < questions.size() - 1) {
                questionPanel.add(Box.createRigidArea(new Dimension(0, 50)));
            }
        }
    }
    
    private List<String> getSelectedAnswers() {
        List<String> selectedAnswerTexts = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            // ButtonModel selectedButtonModel = buttonGroup.getSelection(); // Remove this line

            // Iterate through the JRadioButton components for each question
            for (int j = 0; j < 4; j++) {
                JRadioButton optionButton = optionButtons.get(i * 4 + j);
                if (optionButton.isSelected()) {
                    // If the optionButton is selected, add its text content to the list
                    selectedAnswerTexts.add(optionButton.getText());
//                    System.out.println(selectedAnswerTexts);       //To test whether it prints selected
                    break; // Exit the loop since only one option can be selected
                }
            }
        }

        return selectedAnswerTexts;
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}

class Question {
    private int id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctAnswer;

    public Question(int id, String question, String option1, String option2, String option3, String option4, String correctAnswer) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption(int optionNumber) {
        switch (optionNumber) {
            case 1:
                return option1;
            case 2:
                return option2;
            case 3:
                return option3;
            case 4:
                return option4;
            default:
                return "";
        }
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
