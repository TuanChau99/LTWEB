<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Insert title here</title>

</head>

<body>

	<h1>${category.isEdit ? 'Edit Category' :'Add New Category'}</h1>



	<form action="/admin/categories/saveOrUpdate" method="post"
		enctype="multipart/form-data">


		<label for="categoryId" class="form-label">Category ID:</label> <input
			type="hidden" value="${category.isEdit}"> <input type="text"
			readonly="readonly" class="form-control"
			value="${category.categoryId}" id="categoryId" name="categoryId"
			aria-describedby="categoryIdid" placeholder="Category Id"> <label
			for="name">Category name:</label><br> <input type="text"
			id="name" name="name" value="${category.name}"><br> <br>

		<br>
		<button class="btn btn-primary" type="submit">
			<i class="fas fa-save"></i>

			<c:if test="${category.isEdit}">

				<span>Update</span>

			</c:if>

			<c:if test="${!category.isEdit}">

				<span>Save</span>

			</c:if>

		</button>

	</form>



</body>

</html>