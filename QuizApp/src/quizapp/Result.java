
package quizapp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Result extends JFrame {
    private JLabel totalScoreLabel;
    private JTextArea correctAnswersArea;

    public Result(int totalScore, List<String> correctAnswers) {
        setTitle("Result");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        totalScoreLabel = new JLabel("Total Score: " + totalScore);
        totalScoreLabel.setFont(totalScoreLabel.getFont().deriveFont(20.0f));
        totalScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        correctAnswersArea = new JTextArea();
        correctAnswersArea.setFont(correctAnswersArea.getFont().deriveFont(15.0f));
        correctAnswersArea.setWrapStyleWord(true);
        correctAnswersArea.setLineWrap(true);

        for (String answer : correctAnswers) {
            correctAnswersArea.append(answer + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(correctAnswersArea);

        setLayout(new BorderLayout());
        add(totalScoreLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
    public static void main(String[] args) {
        List<String> Trial = new ArrayList<>();
        Trial.add("First");
        Trial.add("Second");
        new Result(10,Trial);
    }
}
