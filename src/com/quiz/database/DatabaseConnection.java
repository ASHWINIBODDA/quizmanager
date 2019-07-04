
package com.quiz.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.quiz.pojos.Choice;

public class DatabaseConnection {
     static String driName="org.h2.Driver";
     static String urlName="jdbc:h2:tcp://localhost/~/test";
     static String userName="sa";
     static String password="";
     static Connection connection=null;
     
     static Logger log = Logger.getLogger("DatabaseConnection.class");

     
     
     public static Connection getConnection()
     {
    	 
    	 try {
    		 Class.forName(driName);
    		 connection=DriverManager.getConnection(urlName,userName,password);
    	 } catch (Exception e) {
    		 log.error(e);
    		 log.info("Database connection failed...");	
    	 }
    	 return connection;
     }
     
     public static void questionCreateTable() {
    	 PreparedStatement statement = null;
    	 try {
			statement = connection.prepareStatement(Queries.QUESTIONCREATETABLE);
			statement.execute();
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
	    }
    	 
     } 
     
     public static void studentCreateTable() {
    	 PreparedStatement statement = null;
    	 try {
			statement = connection.prepareStatement(Queries.STUDENTCREATETABLE);
			statement.execute();
			statement = connection.prepareStatement(Queries.INSERTINTOUSER, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1,"admin");
			statement.setString(2,"admin");
			statement.executeUpdate();
		} catch (SQLException e) {
			log.error(e);
		} finally {
	        try {
	            if (statement!= null) {
	            	statement.close();
	            }

	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            log.error(e);
	        }
	    }
    	 
     }
     
     public static void userCreateTable() {
    	 PreparedStatement statement = null;
    	 try {
		statement = connection.prepareStatement(Queries.USERCREATETABLE);
			statement.execute();
		} catch (SQLException e) {
			log.error(e);
		} finally {
	        try {
	            if (statement!= null) {
	            	statement.close();
	            }

	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            log.error(e);
	        }
	    }
    	 
     }
     
    public static void  optionsCreateTable() {
    	
    	PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(Queries.OPTIONSCREATETABLE);
			statement.execute();
		} catch (SQLException e) {
			log.error(e);
		} finally {
	        try {
	            if (statement!= null) {
	            	statement.close();
	            }

	            connection.setAutoCommit(true);
	        } catch (SQLException e) {
	            log.error(e);
	        }
	    }
    	
    }
    
    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) throws SQLException {
    	
    	if(resultSet != null)
    		resultSet.close();
    	
    	if(preparedStatement != null)
    		preparedStatement.close();
    	
    	if(connection != null)
    		connection.close();
    }
    
       public int insertOption(Choice options) {
    	
    	PreparedStatement statement = null;
    	try {
    		
			statement = connection.prepareStatement(Queries.INSERTINTOOPTIONS);
			statement.setInt(1, options.getId());
			statement.setInt(2, options.getQuestionId());
			statement.setString(3, options.getValue());
			statement.executeUpdate();
			
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
	    }
    	
    	
    	return 0;
    	
    }    
}
