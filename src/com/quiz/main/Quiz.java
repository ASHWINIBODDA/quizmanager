package com.quiz.main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.quiz.dao.QuestionDAO;
import com.quiz.dao.UserDAO;
import com.quiz.database.DatabaseConnection;
import com.quiz.pojos.Choice;
import com.quiz.pojos.Question;
import com.quiz.pojos.Student;
import com.quiz.pojos.User;
import com.quiz.util.QuizUtil;

public class Quiz {

	static Logger log = Logger.getLogger("Quiz.class");
	
	static Connection connection = null;
    static Scanner in = new Scanner(System.in);
	static User user = null; 

	public static void main(String[] args) throws Exception {

		try {

			connection = DatabaseConnection.getConnection();
		} catch (Exception e) {
			try {
				connection.close();
			} catch (SQLException e1) {
				log.error(e);
			}
		}

		DatabaseConnection.questionCreateTable();
		DatabaseConnection.optionsCreateTable();
		DatabaseConnection.studentCreateTable();
		DatabaseConnection.userCreateTable();

		QuestionDAO questionDAO = new QuestionDAO();

		log.info("Do you want to continue as a student......?  Y/N");

		if (QuizUtil.getBoolean(in.nextLine())) {
			Student student = Student.collectStudent();
			log.info("Select a topic..");

			Map<Integer, String> result = new HashMap<>();
			try {
				result = questionDAO.getTopics();
				log.info(result.toString());
				Iterator<Integer> itr = result.keySet().iterator();
				while (itr.hasNext()) {
					int key = itr.next();
					log.info(key + ".  " + result.get(key));
				}
			} catch (SQLException e) {
				log.error(e);
			}

			log.info("Enter topic number");
			boolean readTopicNumber = true;
			int key = 0;
			do {
				try {
					key = Integer.parseInt(in.nextLine());
					readTopicNumber = false;
				} catch (Exception e) {
					log.info("Invalid input, Please try again...");
				}
			} while (readTopicNumber);

			log.info("Enter difficulty level (1. Easy 2. Medium 3. Hard) : ");
			boolean readlevel = true;
			int level = 0;
			do {
				try {
					level = Integer.parseInt(in.nextLine());
					readlevel = false;
				} catch (Exception e) {
					log.info("Invalid input, Please try again...");
				}
			} while (readlevel);
			List<Question> questions = questionDAO.getQuestionsByTopicAndLevel(result.get(key), level);

			int correctAnswers = 0;
			String difficultyLevel;
			if (!questions.isEmpty()) {
				int i = 0;
				do {
					Question question = questions.get(i++);
					log.info(question.getDescription());
					int j = 1;
					for (Choice choice : question.getOptions()) {
						log.info((j++) + ". " + choice.getValue());
					}
					log.info("Enter your option (option number): ");
					boolean readChoice = true;
					while (readChoice) {
						try {
							int userChoice = Integer.parseInt(in.nextLine());
							Choice choice = questionDAO.getChoices(question.getId());
							if (choice.getValue().equals(question.getOptions().get(userChoice - 1).getValue())) {
								correctAnswers++;
							}
							readChoice = false;
						} catch (Exception e) {
							log.info("Invalid option, Please try again...");
						}
					}

					difficultyLevel = question.getLevel();
				} while (i < questions.size());
				log.info("Student ID : " + student.getStudentId() + ", Student Name : " + student.getName()
						+ " Correct answers = " + correctAnswers + ", Total questions = " + questions.size()
						+ ", Topic = " + result.get(key) + ", Difficulty Level = " + difficultyLevel);
			} else {
				log.info("No questions available with selected options ");
			}
		} else {

			log.info("Enter name and password");

			String name = null;
			String password = null;

			do {
				log.info("name : ");
				name = in.nextLine();
				log.info("password : ");
				password = in.nextLine();
			} while (!checkUser(name, password));

			if (user != null) {

				log.info("****************************************************");
				log.info("*****************Welcome*************************");
				log.info("****************************************************");

				displayMenu();

			}
		}
		in.close();
	}

	public static boolean checkUser(String name, String password) {
		try {
			user = new UserDAO().getUserByName(name, password);
		} catch (SQLException e) {
			log.error(e);
		}

		if (user == null) {
			log.info("name or password enterd is wrong");
			log.info("To re-try press Y/N");
			return !QuizUtil.getBoolean(in.nextLine());
		} else {
			return true;
		}
	}

	static void displayMenu() throws SQLException {
		log.info("Menu");

		log.info("1. View All Question");
		log.info("2. Add Question");
		log.info("3. Delete Question");
		log.info("4. Update");
		log.info("5. Exit");

		log.info("Enter an Option");
		executeMenu(in.nextLine());
	}                                        

	static void executeMenu(String key) throws SQLException {
		switch (key) {
		case "1":
			displayQuestions();
			displayMenu();
			break;
		case "2":
			log.info("2");
			String selectOption="";
			do {
				new QuestionDAO().insertQuestion(Question.collectQuestion(in));
				log.info("Do you want to add one more question (Y/N) : ");
				selectOption=in.nextLine();
			} while (QuizUtil.getBoolean(selectOption));
			
			
			displayMenu();
			break;
		case "3":
			List<Question> questionList = displayQuestions();
			log.info("Enter question number to delete:");
			String key1 = in.nextLine();
			
			new QuestionDAO().deleteQuery(questionList.get(Integer.parseInt(key1)).getId());
			log.info("Deleted Successfully");
			displayMenu();
			break;
		case "4":
			List<Question> questionList1 =  displayQuestions();
			log.info("Enter question number which you want to edit");
			String key2 = in.nextLine();
			int questionId = Integer.parseInt(key2);
			
			Question updatedQuestionObject = Question.collectQuestion(in);
			updatedQuestionObject.setId(questionList1.get(questionId).getId());
			
			new QuestionDAO().updateQuestion(updatedQuestionObject);
			log.info("Updated Successfully");
			displayMenu();
			break;
		case "5":
			System.exit(0); 
			break;
		default:
			
			break;
		}
	}

	static List<Question> displayQuestions() {
		List<Question> questionList = new QuestionDAO().getAllByTopic("all");
		int index1 = 0;
		for (Question question : questionList) {
			log.info((index1++)+" " + "Topic - " + question.getTopic()+" - - " + question.getDescription());
		}

		return questionList;
	}
}