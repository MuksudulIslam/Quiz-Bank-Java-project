import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class JavaQuizProject {
    private static String UserFileLocation = "./src/main/resources/users.json";
    private static String QuizFileLocation = "./src/main/resources/quiz.json";
    public static int NumQuesQuiz = 10;

    public static void main(String[] args) throws IOException, ParseException {

        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader(UserFileLocation));

        JSONObject adminUser = new JSONObject();
        adminUser.put("username", "admin");
        adminUser.put("password", "1234");
        adminUser.put("role", "admin");

        JSONObject studentUser = new JSONObject();
        studentUser.put("username", "salman");
        studentUser.put("password", "1234");
        studentUser.put("role", "student");

        userArray.add(adminUser);
        userArray.add(studentUser);

        FileWriter Writer = new FileWriter(UserFileLocation);
        Writer.write(userArray.toJSONString());
        Writer.flush();
        Writer.close();


        System.out.println("*********Welcome to The Quiz Bank*********");
        System.out.println("Login Now.......");
        System.out.println("Choose One Option for Login...");
        System.out.println("1. Login as Admin");
        System.out.println("2. Login as Student");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option) {

            case 1:
                Login(UserFileLocation, "admin");
                break;

            case 2:
                Login(UserFileLocation, "student");
                break;

            default:
                System.out.println("You Choose Invalid Choice for Login");
                break;

        }

    }

    public static void Login(String userFile, String role) throws IOException, ParseException {

        Scanner scanner = new Scanner(System.in);

        JSONParser parser = new JSONParser();
        JSONArray userArray = (JSONArray) parser.parse(new FileReader(UserFileLocation));

        System.out.println("Enter Your Username: ");
        String username = scanner.next();
        System.out.println("Enter Your Password: ");
        String password = scanner.next();

        boolean login = false;

        for (Object userObj : userArray) {
            JSONObject user = (JSONObject) userObj;

            String usernameStore = (String) user.get("username");
            String passStore = (String) user.get("password");
            String roleStore = (String) user.get("role");

            if (username.equals(usernameStore) && password.equals(passStore) && role.equals(roleStore)) {
                System.out.println("Login Successful as " + roleStore);
                login = true;

                if ("admin".equals(role)) {
                    System.out.println("Welcome " + usernameStore + "! " + "Please create new questions in the question bank.");
                    AdminMenu();

                } else if ("student".equals(role)) {
                    System.out.println("Welcome " + usernameStore + "!" + " to the Quiz.");
                    StudentMenu();
                    break;

                }
            }
        }

        if (!login) {
            System.out.println("Login Failed. Check Username, Password or Role information.. ");
        }
    }

    public static void AdminMenu() throws IOException, ParseException {

        char ch = 's';
        Scanner scanner = new Scanner(System.in);

        while (ch == 's' || ch == 'q') {

            if (ch == 's') {
                QuesAdding();

            } else if (ch == 'q') {
                break;

            }

            System.out.println("Do you Want to Add more Question Press [s] For Start.");
            scanner.nextLine();
            ch = scanner.nextLine().charAt(0);
        }

    }

    public static void QuesAdding() throws IOException, ParseException {

        Scanner scanner = new Scanner(System.in);

        JSONParser parser = new JSONParser();
        JSONArray quizArray = (JSONArray) parser.parse(new FileReader(QuizFileLocation));

        char ch = 's';
        int questionCount = 0;

        while (ch == 's' || ch == 'q') {

            if (ch == 's') {
                System.out.println("Input Your Question Here : ");
                String question = scanner.nextLine();
                JSONObject quizObj = new JSONObject();
                quizObj.put("question", question);

                for (int i = 1; i <= 4; i++) {
                    String quesOption = "Input Option " + i + " :";
                    System.out.println(quesOption);
                    String OptionInput = scanner.nextLine();
                    quizObj.put("option" + i, OptionInput);

                }

                System.out.println("What is the Answer Key : ");
                int answer = scanner.nextInt();
                scanner.nextLine();

                if (answer >= 1 && answer <= 4) {
                    quizObj.put("answerKey", answer);

                } else {
                    System.out.println("Invalid Answer Key ");
                    continue;
                }

                quizArray.add(quizObj);
                questionCount++;

                if (questionCount >= 30) {
                    System.out.println("30 Question Added. You can Press [q] to Quit");
                    String choice = scanner.next().trim().toLowerCase();
                    scanner.nextLine();

                    if (choice.equals("q")) {
                        ch = 'q';
                    }

                } else {

                    System.out.println("Saved Successfully! Do you want to Add More Question. press [s] to Start Press [q] to Quit ");
                    String choice = scanner.next().trim().toLowerCase();
                    scanner.nextLine();

                    if (choice.equals("q")) {
                        ch = 'q';

                    } else if (choice.equals("s")) {
                        ch = 's';

                    } else {
                        System.out.println("You Choose Invalid choice. ");

                    }
                }

            } else if (ch == 'q') {
                break;
            }

        }

        FileWriter Writer = new FileWriter(QuizFileLocation);
        Writer.write(quizArray.toJSONString());
        Writer.flush();
        Writer.close();

        System.out.println("Your Quiz Question is Saved in the Database.. ");
    }

    public static void StudentMenu() throws IOException, ParseException {

        int numOfQes = 10;
        System.out.println("We will throw you " + numOfQes + " Question. Each MCQ mark is 1 and No Negative marking.");
        System.out.println("Are You Ready? Press [s] for Start");

        Scanner scanner = new Scanner(System.in);
        String choice1 = scanner.next().trim().toLowerCase();

        if ("s".equals(choice1)) {

            int correctAns = 0;

            JSONParser parser = new JSONParser();
            JSONArray questionArr = (JSONArray) parser.parse(new FileReader(QuizFileLocation));

            for (int i = 0; i < numOfQes; i++) {

                int random = new Random().nextInt(questionArr.size());
                JSONObject quesObj = (JSONObject) questionArr.get(random);

                System.out.println("Question" + (i + 1) + " : " + quesObj.get("question"));

                for (int j = 1; j <= 4; j++) {
                    String quesOption = "option" + j;
                    System.out.println(j + ". " + quesObj.get(quesOption));
                }

                System.out.println("Enter Your Answer [Example : 1, 2, 3, 4] : ");
                int ansIndex = scanner.nextInt();
                scanner.nextLine();

                if (ansIndex >= 1 && ansIndex <= 4) {

                    int correctAnswerIndex = Integer.parseInt(quesObj.get("answerKey").toString());

                    if (ansIndex == correctAnswerIndex) {
                        correctAns++;
                    } else {
                        System.out.println("You Choose Incorrect answer. Skip This Question. Answer Next Question");
                    }

                } else {
                    System.out.println("You Choose Invalid answer. Skip This Question. Answer Next Question");
                }
            }

            DisplayResult(correctAns);

            System.out.println("Would You Like to Start Again? Press [s] for Start or [q] for Quit");
            String choice = scanner.next().trim().toLowerCase();

            if ("s".equals(choice)) {
                System.out.println("Thank your for Retry Quiz. ");
                StudentMenu();

            } else if ("q".equals(choice)) {
                System.out.println("Thank you See you soon");

            } else {
                System.out.println("You Choose Invalid Choice for Retry.");
            }

        } else {
            System.out.println("You Choose Invalid Choice for Start the Quiz.");
        }
    }

    public static void DisplayResult(int score) {

        if (score >= 8) {
            System.out.println("Excellent! You Have Got " + score + " Out of " + NumQuesQuiz);

        } else if (score >= 5) {
            System.out.println("Good. You Have Got " + score + " Out of " + NumQuesQuiz);

        } else if (score >= 2) {
            System.out.println("Very Poor. You Have Got " + score + " Out of " + NumQuesQuiz);

        } else {
            System.out.println("Very Sorry. You Have Failed. You Have Got " + score + " Out of " + NumQuesQuiz);
        }
    }

}
