<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:forEach var="list" items="${listcate}">
		<p>${list.name}</p>
		<br>
		<p>${list.status}</p>
		<br>
		<p>${list.images}</p>
	</c:forEach>
</body>
</html>