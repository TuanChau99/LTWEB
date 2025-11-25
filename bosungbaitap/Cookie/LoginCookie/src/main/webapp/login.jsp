<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>Login Form</h2>
    <form action="login" method="post">
        Username: <input type="text" name="username" value="<%= request.getAttribute("savedUser") != null ? request.getAttribute("savedUser") : "" %>"><br><br>
        Password: <input type="password" name="password"><br><br>
        <input type="checkbox" name="remember" value="on"> Remember me <br><br>
        <input type="submit" value="Login">
    </form>
    <p style="color:red"><%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %></p>
</body>
</html>