package com.quiz.pojos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.quiz.util.QuizUtil;

public class Question {

	static Logger log = Logger.getLogger("Question.class");
	int id;
	String description;
	String topic;
	String level;
	List<Choice> options = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public List<Choice> getOptions() {
		return options;
	}

	public void setOptions(List<Choice> options) {
		this.options = options;
	}

	public void setOptions(Choice options) {
		this.options.add(options);
	}

	public static  Question collectQuestion(Scanner in) {
		Question question = new Question();

		log.info("Enter the topic");
		question.setTopic(in.nextLine());

		log.info("Enter dificulty Level (1,2,3) : ");
		question.setLevel(in.nextLine());
		
		log.info("Enter a Question");
		question.setDescription(in.nextLine());

		log.info("How many choices do u want to add ");
		boolean read = true;
		int choiceCount = 0;
		while(read) {
			try {
				choiceCount = Integer.parseInt(in.nextLine());
				read = false;
			}
			catch(NumberFormatException ex) {
				log.info("Invalid input, Please try again...");			
			}
		}
		boolean hasValue = false;
		for (int i = 0; i < choiceCount; i++) {
			Choice option1 = new Choice();
			log.info("Enter  choice");
			option1.setValue(in.nextLine());

			log.info("is valid choice Y/N");
			boolean isValid = QuizUtil.getBoolean(in.nextLine());
			option1.setValid(isValid);
			question.setOptions(option1);
			if(isValid) {
				hasValue = isValid;
			}
		}
		
		if(!hasValue) {
			log.info("please re-enter a qestion with valid ans");
			log.info("Terminating......");
			System.exit(200);
		}
		return question;
		
	}

}
