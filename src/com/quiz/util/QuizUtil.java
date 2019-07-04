package com.quiz.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class QuizUtil {
	
	QuizUtil() {
		
		
	}
	
	static Logger log = Logger.getLogger("QuizUtil.class");
	
	public static boolean getBoolean(String yn) {
		return yn.equalsIgnoreCase("Y");
	}

	public static int getGeneratedId(ResultSet resultSet) throws SQLException {
		try (ResultSet generatedKeys = resultSet) {
			if (generatedKeys.next()) {
				log.info("Question added successfully...");
				return (generatedKeys.getInt(1));
			} else {
				throw new SQLException("Creating user failed, no ID obtained.");
			}
		} catch (Exception e) {
			log.error(e);
			return 0;
		} finally {
				resultSet.close();
		}

	}
}
