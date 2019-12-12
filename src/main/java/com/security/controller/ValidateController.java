package com.security.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.connection.DbConnection;

@Controller
public class ValidateController
{
	


	@RequestMapping(value = "validatingotp.html", method = RequestMethod.POST)
	public String validateOtp(@RequestParam("fileid") int fileid,@RequestParam("otp") String otp) throws SQLException  {
		//ModelAndView mc = new ModelAndView();
		Connection con=DbConnection.getConnection();
		PreparedStatement ps=con.prepareStatement("select time from otp where fileid=? and otp=?");
		ps.setInt(1, fileid);
		ps.setString(2, otp);
		ResultSet rs = ps.executeQuery();
		
		if(rs.next())
		{
			//st.executeUpdate("insert into fileaccesslogs(id,username,fileid,Time) values ('"+id+"','"+username+"','"+fileid+"','"+Time+"')");	
			String generatedTimeString = rs.getString("time");
			LocalDateTime generatedTime = LocalDateTime.parse(generatedTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime now = LocalDateTime.now();
			if(ChronoUnit.MINUTES.between(generatedTime, now)>30)
			{
				return "redirect:/Expiredotp.jsp";
			}
			return "redirect:/download.html?fileid="+fileid;
		}else
		{
			//st.executeUpdate("insert into fileaccesslogs(id,username,fileid,Time) values ('"+id+"','"+username+"','"+fileid+"','"+Time+"')");	
			//mc.setViewName("Invalidotp.jsp");
			return "redirect:/Invalidotp.jsp";
		}
	
	}
	
}
