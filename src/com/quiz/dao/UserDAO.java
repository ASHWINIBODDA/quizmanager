package com.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.quiz.database.DatabaseConnection;
import com.quiz.database.Queries;
import com.quiz.pojos.User;

public class UserDAO {
	
	Logger log = Logger.getLogger("UserDAO.class");
	Connection connection = null;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}

	public UserDAO() {
		this.connection = DatabaseConnection.getConnection();
	}

	
	public User getUserByName(String name, String password) throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		User user = null;
		try {

			statement = connection.prepareStatement(Queries.FINDUSER);
			statement.setString(1, name);
			statement.setString(2, password);
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				user = new User();
				user.setId(resultSet.getInt("ID"));
				user.setName(name);
				user.setPassword(password);
			}
			
			return user;

		}catch(Exception e) {
			log.error(e);
		}finally {
			DatabaseConnection.close(connection, statement, resultSet);
		}
		return null;
	}

}
