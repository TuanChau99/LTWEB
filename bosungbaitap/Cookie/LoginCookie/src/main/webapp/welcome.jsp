<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
    <h2>Welcome, <%= request.getParameter("username") != null ? request.getParameter("username") : "User" %>!</h2>
    <p>Bạn đã login thành công và thông tin của bạn có thể được lưu bằng Cookie <3</p>
    <a href="login">Quay lại Login</a>
</body>
</html>
