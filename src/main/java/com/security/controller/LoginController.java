package com.security.controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.io.*;
import com.connection.DbConnection;
import com.security.Information;



@Controller
public class LoginController
{
	@RequestMapping(value = "/success.html", method = RequestMethod.POST)
	public ModelAndView submitAdmissionForm(@RequestParam("username") String username,
			@RequestParam("password") String password) throws ClassNotFoundException, SQLException
	{

		Information s =new Information();
		String j = "client";
		String q = null;
		s.setUsername(username);
		s.setPassword(password);
//
//		Class.forName("com.mysql.jdbc.Driver");
//		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdetails", "root", "");
//		System.out.println("PostEstablishing a Db connection:" +con);
//		
//		//PreparedStatement ps = null;
//		Statement stmt = con.createStatement();
//		String sql = "SELECT * FROM users where username='" + username + "'";
//		ResultSet rs = stmt.executeQuery(sql);
//		while (rs.next())
//		{
//			System.out.println(rs.getString("username")+"  "+rs.getString("password"));  
//			q = rs.getString("password");
//			con.close();
//			break;
//		}
//
//		if (password.equals(q) && (password.equals(j))) 
//		{
//			ModelAndView mv = new ModelAndView();
//			mv.setViewName("Browse.jsp");
//			mv.addObject("headerMessage", "LoginSuccessfull for Admin");
//			mv.addObject("u1", s);
//
//			return mv;
//		}
//		else {
//			ModelAndView mc = new ModelAndView();
//			mc.setViewName("Invalid.jsp");
//			mc.addObject("login Not Successfull. PLease enter a valid username and password");
//			mc.addObject("headerMessage", "login Not Successfull. PLease enter a valid username and password");
//			return mc;
//		}
		
		String url = "ldap://localhost:10389";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid="+username+",uid=admin,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, password);

		try {
			DirContext ctx = new InitialDirContext(env);
			System.out.println("connected");
			System.out.println(ctx.getEnvironment());
			
			// do something useful with the context...

			ctx.close();
			
			ModelAndView mv = new ModelAndView();
			mv.setViewName("Browse.jsp");
			mv.addObject("headerMessage", "LoginSuccessfull for Admin");
			mv.addObject("u1", s);
			return mv;

		} catch (AuthenticationNotSupportedException ex) {
			System.out.println("The authentication is not supported by the server");
		} catch (AuthenticationException ex) {
			System.out.println("incorrect password or username");
			ModelAndView mc = new ModelAndView();
			mc.setViewName("Invalid.jsp");
			mc.addObject("login Not Successfully. PLease enter a valid username and password");
			mc.addObject("headerMessage", "login Not Successfull. PLease enter a valid username and password");
			return mc;
		} catch (NamingException ex) {
			System.out.println("error when trying to create the context");
		}
		return null;
		
	}

}