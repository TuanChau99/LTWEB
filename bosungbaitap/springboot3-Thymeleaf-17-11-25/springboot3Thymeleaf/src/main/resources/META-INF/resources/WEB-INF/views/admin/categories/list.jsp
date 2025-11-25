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
	
	
	<h2><a href="/admin/categories/add">Add new category</a></h2>
	<!-- Hiển thông báo -->

	<c:if test="${message != null}">

		<div class="alert alert-primary" role="alert">

			<i>${message}</i>

		</div>

	</c:if>

	<!-- Hêt thông báo -->

	<table class="table table-striped table-responsive">

		<thead class="thead-inverse">

			<tr>

				<th>Category ID</th>

				<th>Category Name</th>

				<th>Action</th>

			</tr>

		</thead>

		<tbody>

			<c:forEach items="${categories}" var="category">

				<tr>

					<td scope="row">${category.categoryId}</td>

					<td>${category.name}</td>

					<td><a href="/admin/categories/edit/${category.categoryId}"
						class="btn btn-outline-warning">Edit</a> <a
						href="/admin/categories/delete/${category.categoryId}"
						class="btn btn-outline-danger"> Delete</a></td>

				</tr>

			</c:forEach>

		</tbody>



	</table>



</body>

</html>