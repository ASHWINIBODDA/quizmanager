package com.quiz.pojos;

import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.quiz.dao.StudentDao;

public class Student {
	static Logger log = Logger.getLogger("Student.class");
	int id;
	String name;
	String studentId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	
	public static Student collectStudent() {
		Scanner in = null;
		Student student = null;
		StudentDao studentDao = new StudentDao();		
		log.info("Enter Student Id");
		boolean readStudentID = true;
		String studentId = "";
		do {
			try {
				in = new Scanner(System.in);
				studentId = in.nextLine();
				Integer.parseInt(studentId);
				readStudentID = false;
			}
			catch(Exception e) {
				log.info("Invalid input, Please try again...");
			}
		}while(readStudentID);
		
		log.info("Authenticating.......");
		try {
			student = studentDao.getStudent(0, studentId);
			if(student == null) {
				student = new Student();
				student.setStudentId(studentId);
				
				log.info("Could not find student with studentId="+studentId);
				log.info("Enter name..");
				student.setName(in.nextLine());
				
				student.setId(studentDao.insertStudent(student));
			}
		} catch (SQLException e) {
			log.error(e);
		}
		
		return student;
	}
	
}
