package com.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.quiz.database.DatabaseConnection;
import com.quiz.database.Queries;
import com.quiz.pojos.Choice;

public class OptionsDAO {

	Connection connection = null;
	private Logger log = Logger.getLogger("OptionsDAO.class");

	public OptionsDAO(Connection connection) {
		this.connection = connection;
	}

	public OptionsDAO() {
		this.connection = DatabaseConnection.getConnection();
	}

	public int insertOption(Choice option) throws SQLException {

		try (PreparedStatement statement = connection.prepareStatement(Queries.INSERTINTOOPTIONS, Statement.RETURN_GENERATED_KEYS)){

			statement.setInt(2, option.getQuestionId());
			statement.setBoolean(1, option.isValid());
			statement.setString(3, option.getValue());
			statement.executeUpdate();

		} catch (SQLException e) {
			log.error(e);
		}
		return 0;
	}

	public List<Choice> getByQuestionId(String questioId) {
		List<Choice> choices = new ArrayList<>();
		ResultSet resultSet =null;
		try (PreparedStatement statement = connection.prepareStatement(Queries.GETCHOICEBYQUESTIONID)) {
			statement.setString(1, questioId);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Choice choice = new Choice();
				choice.setId(resultSet.getInt("ID"));
				choice.setValid(resultSet.getBoolean("valid"));
				choice.setValue(resultSet.getString("value"));
				choices.add(choice);
			}

		} catch (Exception e) {
			log.error(e);
		} finally {
			try {
				if(resultSet !=null) {
					resultSet.close();
				}
			} catch (SQLException e) {
				log.error(e);
			}
		}

		return choices;
	}

	public boolean deleteChoice(int questionId) throws SQLException {

		try (PreparedStatement statement = connection.prepareStatement(Queries.DELETECHOICE)) {
			statement.setInt(1, questionId);
			statement.execute();
			log.info("Deleteddd........");
			return true;
		} catch (Exception e) {
			connection.commit();
			log.error(e);
			return false;
		}
	}

}