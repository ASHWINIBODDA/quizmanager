package com.quiz.database;

public class Queries {
	
	Queries() {
		
	}

	public static final String QUESTIONCREATETABLE = "create table  IF NOT EXISTS questions( ID int NOT NULL AUTO_INCREMENT, description varchar(255) NOT NULL, topic varchar(255) NOT NULL, level varchar(20) NOT NULL, PRIMARY KEY (ID))";

	public static final String OPTIONSCREATETABLE = "create table  IF NOT EXISTS choice( ID int NOT NULL AUTO_INCREMENT, valid bit, questionId int  NOT NULL,PRIMARY KEY (ID),value varchar(255) )";//FOREIGN KEY (questionId) REFERENCES questions(ID)

	public static final String STUDENTCREATETABLE = "create table  IF NOT EXISTS student( ID int NOT NULL AUTO_INCREMENT, name varchar(255) ,studentId varchar(255), PRIMARY KEY (ID))";

	public static final String USERCREATETABLE = "create table  IF NOT EXISTS user( ID int NOT NULL AUTO_INCREMENT, name varchar(255) ,password varchar(255), PRIMARY KEY (ID))";

	public static final String INSERTINTOQUESTIONS = "insert into questions(description,topic,level) values(?,?,?)";

	public static final String GETQUESTION = "select que.ID, que.description, que.topic, que.level, opt.ID as optionId, opt.value  from questions que join choice opt on opt.questionId = question.id where ";

	public static final String QUESTIONBYID = "id=?";

	public static final String QUESTIONBYTOPIC = "topic=?";

	public static final String INSERTINTOOPTIONS = "insert into choice(valid,questionId, value) values(?,?,?)";

	public static final String INSERTINTOSTUDENT = "insert into student(name, studentId) values(?,?)";

	public static final String GETSTUDENT = "select ID, name, studentId from student where ";

	public static final String STUDENTBYID = "id=?";

	public static final String STUDENTBYSTUDENTID = "studentId=?";

	public static final String GETALLTOPICS = "select distinct(topic) from questions ";

	public static final String GETALLQUESTIONSBYTOPIC = "select Id, description, topic, level from questions where topic=?";
	
	public static final String GETALLQUESTIONS = "select Id, description, topic, level from questions";

	public static final String GETCHOICEBYQUESTIONID = "Select ID, valid, value, questionId from choice where questionId=?";

	public static final String GETQUESTIONSBYTOPICANDLEVEL = "select Id, description, topic, level from questions where topic=? and level=?";

	public static final String GETANWERS = "SELECT * FROM CHOICE WHERE questionid = ? AND valid = true";

	public static final String UPDATEQUESTION = "UPDATE QUESTIONS SET DESCRIPTION = '?'  WHERE DESCRIPTION = '?';";

	public static final String UPDATEQUESTIONBYID = "UPDATE questions SET DESCRIPTION = ?  WHERE ID = ?";

	public static final String INSERTINTOUSER = "insert into user(name,password) values(?,?)";

	public static final String FINDUSER = "select * from user where name=? and password=?";
	
	public static final String DELETEQUESTION = "delete questions where id = ?";

	public static final String DELETECHOICE = "delete choice where questionId = ?";

}