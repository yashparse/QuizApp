import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class QuizApp {
    private static class Question {
        String questionText;
        String[] options;
        int correctOption;

        Question(String questionText, String[] options, int correctOption) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
        }
    }

    private static Question[] questions = new Question[] {
        new Question("What is the capital of France?", new String[]{"1) Berlin", "2) London", "3) Paris", "4) Madrid"}, 3),
        new Question("Which planet is known as the Red Planet?", new String[]{"1) Earth", "2) Mars", "3) Jupiter", "4) Venus"}, 2),
        new Question("Who wrote 'Romeo and Juliet'?", new String[]{"1) Charles Dickens", "2) William Shakespeare", "3) Mark Twain", "4) Jane Austen"}, 2),
        new Question("What is the largest ocean on Earth?", new String[]{"1) Atlantic Ocean", "2) Indian Ocean", "3) Arctic Ocean", "4) Pacific Ocean"}, 4)
    };

    private static int score = 0;
    private static Scanner scanner = new Scanner(System.in);
    private static boolean answered = false;

    public static void main(String[] args) {
        System.out.println("Welcome to the Quiz! You have 15 seconds to answer each question.\n");

        for (int i = 0; i < questions.length; i++) {
            askQuestion(i);
        }

        showResults();
    }

    private static void askQuestion(int index) {
        Question q = questions[index];
        System.out.println("Question " + (index + 1) + ": " + q.questionText);
        for (String option : q.options) {
            System.out.println(option);
        }

        answered = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                if (!answered) {
                    System.out.println("\nTime's up! Moving to the next question.\n");
                    answered = true;
                    synchronized(scanner) {
                        scanner.notify();
                    }
                }
            }
        }, 15000);  // 15 seconds timer

        int userChoice = -1;
        synchronized(scanner) {
            while (!answered) {
                System.out.print("Enter your answer (1-4): ");
                if (scanner.hasNextInt()) {
                    userChoice = scanner.nextInt();
                    if (userChoice >= 1 && userChoice <= 4) {
                        answered = true;
                    } else {
                        System.out.println("Please choose a valid option between 1 and 4.");
                    }
                } else {
                    scanner.next(); // consume invalid input
                    System.out.println("Invalid input. Please enter a number.");
                }
            }
            scanner.notify();
        }

        timer.cancel();

        if (userChoice == q.correctOption) {
            System.out.println("Correct!\n");
            score++;
        } else if (userChoice != -1) {
            System.out.println("Incorrect. The right answer was option " + q.correctOption + ".\n");
        }
    }

    private static void showResults() {
        System.out.println("Quiz Completed!");
        System.out.println("Your score: " + score + " out of " + questions.length);

        System.out.println("\nSummary:");
        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];
            System.out.println("Q" + (i + 1) + ": " + q.questionText);
            System.out.println("Correct Answer: " + q.options[q.correctOption - 1]);
            System.out.println();
        }

        System.out.println("Thanks for playing!");
    }
}
