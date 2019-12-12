<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Otp Validation</title>
</head>
<body>

	<form action="validatingotp.html" method="post">
		<input type="hidden" value="<%= request.getParameter("fileid") %>" name="fileid" />
	<!-- 	<input type="hidden" value="<%= request.getParameter("id") %>" name="id" />
		<input type="hidden" value="<%= request.getParameter("username") %>" name="username" />
		<input type="hidden" value="<%= request.getParameter("Time") %>" name="Time" />  -->
		
		
		OTP: &nbsp; <input type="text" name="otp" /> <br> <input
			type="submit" name="submit" value="validate" />
	</form>
</body>
</html>