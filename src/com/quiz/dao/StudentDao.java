package com.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.quiz.database.DatabaseConnection;
import com.quiz.database.Queries;
import com.quiz.pojos.Student;

public class StudentDao {
	
	Logger log = Logger.getLogger("StudentDao.class");
	
	Connection connection = null;

	public StudentDao(Connection connection) {
		this.connection = connection;
	}

	public StudentDao() {
		this.connection = DatabaseConnection.getConnection();
	}

	public int insertStudent(Student student) throws SQLException {

		PreparedStatement statement = null;
		try {

			statement = connection.prepareStatement(Queries.INSERTINTOSTUDENT, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, student.getName());
			statement.setString(2, student.getStudentId());
			statement.executeUpdate();
			
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	student.setId(generatedKeys.getInt(1));
	            }
	            else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	     }

		} catch (SQLException e) {
			log.error(e);
		} 
		finally {
	        try {
	            if (statement != null) {
	            	statement.close();
	            }

	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            log.error(e);
	        }
	    }
		return student.getId();
	}
	
	public Student getStudent(int id, String studentId) throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Student student = null;
		
		try {

			if(studentId ==null) {
				statement = connection.prepareStatement(Queries.GETSTUDENT + Queries.STUDENTBYID);
				statement.setInt(1, id);
			}else {
				statement = connection.prepareStatement(Queries.GETSTUDENT + Queries.STUDENTBYSTUDENTID);
				statement.setString(1, studentId);
			}
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				student = new Student();
				student.setId(resultSet.getInt("ID"));
				student.setName(resultSet.getString("name"));
				student.setStudentId(resultSet.getString("studentId"));
			}

			
		} catch (SQLException e) {
			log.error(e);
		} finally {
	        try {
	            if (statement != null) {
	                statement.close();
	            } 
	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            log.error(e);
	        }
		
		try {
            if (resultSet != null) {
            	resultSet.close();
            } 
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            log.error(e);
        }
		}
		return student;
		}
}
