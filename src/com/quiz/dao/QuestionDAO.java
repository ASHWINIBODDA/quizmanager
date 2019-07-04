package com.quiz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.quiz.database.DatabaseConnection;
import com.quiz.database.Queries;
import com.quiz.pojos.Choice;
import com.quiz.pojos.Question;

public class QuestionDAO {

	Logger log = Logger.getLogger("QuestionDAO.class");
	Connection connection = null;
	String level ="level";
	String description = "description";
	String TOPIC = "topic";

	public QuestionDAO(Connection connection) {
		this.connection = connection;
	}

	public QuestionDAO() {
		this.connection = DatabaseConnection.getConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public int insertQuestion(Question questions) throws SQLException {

		PreparedStatement statement = null;
		try {

			statement = connection.prepareStatement(Queries.INSERTINTOQUESTIONS, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, questions.getDescription());
			statement.setString(2, questions.getTopic());
			statement.setString(3, questions.getLevel());
			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					log.info("Question added successfully...");
					questions.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}

			List<Choice> options = questions.getOptions();
			OptionsDAO optionsDAO = new OptionsDAO(connection);
			for (Choice option : options) {
				option.setQuestionId(questions.getId());
				optionsDAO.insertOption(option);
			}

		} catch (SQLException e) {
			log.error(e);
		} finally {
			DatabaseConnection.close(connection, statement, null);

		}
		return 0;
	}

	public int getQuestion(int questionId, String topic) throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Question question = new Question();
		try {

			if (topic == null) {
				statement = connection.prepareStatement(Queries.GETQUESTION + Queries.QUESTIONBYID);
				statement.setInt(1, questionId);
			} else {
				statement = connection.prepareStatement(Queries.INSERTINTOQUESTIONS + Queries.QUESTIONBYTOPIC);
				statement.setString(1, topic);
			}

			resultSet = statement.executeQuery();

			
			question.setId(resultSet.getInt("ID"));
			question.setDescription(resultSet.getString(description));
			question.setTopic(resultSet.getString(TOPIC));
			question.setLevel(resultSet.getString(level));

			while (resultSet.next()) {
				Choice option = new Choice();
				option.setId(resultSet.getInt("optionId"));
				option.setValue(resultSet.getString("value"));
				question.setOptions(option);
			}

		} catch (SQLException e) {
			log.error(e);
		} finally {
			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return question.getId();
	}

	public Map<Integer, String> getTopics() throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Map<Integer, String> result = new HashMap<>();
		try {

			statement = connection.prepareStatement(Queries.GETALLTOPICS);
			resultSet = statement.executeQuery();

			int i = 0;
			while (resultSet.next()) {
				result.put(i++, resultSet.getString(TOPIC));
			}

		} catch (Exception e) {
			log.error(e);
		} finally {
			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return result;
	}

	public List<Question> getAllByTopic(String topic) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Question> questions = new ArrayList<>();
		try {
			if (topic.equalsIgnoreCase("all")) {
				statement = connection.prepareStatement(Queries.GETALLQUESTIONS);
			} else {
				statement = connection.prepareStatement(Queries.GETALLQUESTIONSBYTOPIC);
				statement.setString(1, topic);
			}
			resultSet = statement.executeQuery();

			OptionsDAO optionsDAO = new OptionsDAO(connection);
			while (resultSet.next()) {
				Question question = new Question();
				question.setId(resultSet.getInt("ID"));
				question.setDescription(resultSet.getString(description));
				question.setTopic(resultSet.getString(TOPIC));
				question.setLevel(resultSet.getString(level));
				question.setOptions(optionsDAO.getByQuestionId(String.valueOf(question.getId())));
				questions.add(question);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}

		return questions;
	}

	public List<Question> getQuestionsByTopicAndLevel(String topic, int level) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		List<Question> questions = new ArrayList<>();
		try {
			statement = connection.prepareStatement(Queries.GETQUESTIONSBYTOPICANDLEVEL);
			statement.setString(1, topic);
			statement.setInt(2, level);
			resultSet = statement.executeQuery();

			OptionsDAO optionsDAO = new OptionsDAO(connection);
			while (resultSet.next()) {
				Question question = new Question();
				question.setId(resultSet.getInt("ID"));
				question.setDescription(resultSet.getString("description"));
				question.setTopic(resultSet.getString(TOPIC));
				question.setLevel(resultSet.getString("level"));
				question.setOptions(optionsDAO.getByQuestionId(String.valueOf(question.getId())));
				questions.add(question);
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return questions;
	}

	public Choice getChoices(int questionId) {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = connection.prepareStatement(Queries.GETANWERS);
			statement.setInt(1, questionId);
			statement.toString();
			resultSet = statement.executeQuery();

			Choice choice = new Choice();
			while (resultSet.next()) {
				choice.setId(resultSet.getInt(1));
				choice.setValid(resultSet.getBoolean(2));
				choice.setQuestionId(resultSet.getInt(3));
				choice.setValue(resultSet.getString(4));

			}
			return choice;
		} catch (Exception e) {
			log.error(e);
		} finally {
			if(resultSet!=null) {
				try {
					resultSet.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e);
				}
			}
		}
		return null;
	}

	public void updateQuestionById(int id, String updatedQuestion) {
		try (PreparedStatement statement = connection.prepareStatement(Queries.UPDATEQUESTIONBYID)){
			statement.setString(1, updatedQuestion);
			statement.setInt(2, id);
			statement.toString();
			statement.executeUpdate();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * Update the question using description
	 * 
	 * @param presentQuestion
	 * @param updatedQuestion
	 */
	public void updateQuestion(String presentQuestion, String updatedQuestion) {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(Queries.UPDATEQUESTION);
			statement.setString(1, updatedQuestion);
			statement.setString(2, presentQuestion);
			statement.toString();
			statement.executeUpdate();
		} catch (Exception e) {
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
	    }
	}

	/**
	 * Deleting Question 1st deletig options and the question
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteQuery(int id) {

		PreparedStatement statement = null;
		try {
			if (new OptionsDAO(connection).deleteChoice(id)) {
				connection.commit();
				statement = connection.prepareStatement(Queries.DELETEQUESTION);
				statement.setInt(1, id);
				statement.toString();
				statement.execute();
				return true;
			}
		} catch (Exception e) {
			log.error(e);
		} finally {

			try {
				DatabaseConnection.close(connection, statement, null);
			} catch (SQLException e) {
				log.error(e);
			}

		}

		return false; 
	}

	/**
	 * first deleting existing options and then adding new option 
	 * @param question
	 * @return
	 */
	public boolean updateQuestion(Question question) {
		PreparedStatement statement = null;

		try {
			connection.setAutoCommit(false);
			OptionsDAO optionsDAO = new OptionsDAO(connection);
			
			optionsDAO.deleteChoice(question.getId());
			statement = connection.prepareStatement(Queries.UPDATEQUESTIONBYID);
			statement.setString(1, question.getDescription());
			statement.setInt(2, question.getId());

			for (Choice choice : question.getOptions()) {
				choice.setQuestionId(question.getId());
				optionsDAO.insertOption(choice);
			}
			
			statement.executeUpdate();
			connection.commit();
			return true;
			
		} catch (SQLException e) {
			log.error(e);
			return false;
		} finally {
	        try {
	            if (statement != null) {
	                statement.close();
	            }

	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            log.error(e);
	        }
	    }
	}

}