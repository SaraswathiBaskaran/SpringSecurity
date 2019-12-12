package com.connection;
import java.sql.*;

public class DbConnection
{
	private static Connection conn;
	 static 
	 {   
	try {
		
		 Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdetails", "root", "");
	
		
		
		 
	} 
	catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	 }
	 public static Connection getConnection()
	 {
		 return conn;
	 }
     
/*	 String employeeid=request.getParameter("employeeid");
	 String employeename=request.getParameter("employeename");
	 String employerole=request.getParameter("employerole");
	 String vacationstartdate=request.getParameter("vacationstartdate");
	 String vacationenddate=request.getParameter("vacationenddate");
	 String teamleader=request.getParameter("teamleader");
	 String manager=request.getParameter("manager");
	 String requeststatus=request.getParameter("requeststatus");
	 String comment=request.getParameter("comment");
	*/ 
}
