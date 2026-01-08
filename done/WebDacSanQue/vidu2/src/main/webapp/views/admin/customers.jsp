<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản Lý Khách Hàng</title>
<link
	href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">

<style>
/* CSS FIX: Giữ tên Admin không bị to và đồng nhất khoảng cách */
.sidebar .profile h4 {
	font-size: 1.1rem !important;
	font-family: 'Merriweather Sans', sans-serif !important;
	margin: 0.5rem 0 2px 0 !important;
	font-weight: 500 !important;
	color: #fff !important;
}

.sidebar .profile small {
	font-size: 0.75rem !important;
	color: #899DC1 !important;
	display: block !important;
}

/* Giao diện Card nội dung chính */
.customer-card {
	background: #fff;
	border-radius: 15px;
	padding: 30px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
	margin-top: 20px;
}

.customer-card h2 {
	font-size: 1.5rem;
	color: #34425A;
	margin-bottom: 25px;
	display: flex;
	align-items: center;
}

.customer-card h2 i {
	color: #22BAA0;
	margin-right: 15px;
}

/* Style cho bảng */
.table thead th {
	border-top: none;
	color: #B0B0B0;
	font-weight: 400;
	font-size: 0.9rem;
	text-transform: none;
}

.img-avatar {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	object-fit: cover;
}

/* Hiệu ứng khi rê chuột vào số tiền */
.total-spent-link:hover {
	text-decoration: none;
	opacity: 0.8;
}
</style>
</head>
<body class="bg-light">

	<div class="container">
		<div class="customer-card">
			<div class="d-flex justify-content-between align-items-center mb-4">
				<h2>
					<i class="fa fa-address-book"></i> Danh sách Khách hàng
				</h2>
				<button class="btn btn-success shadow-sm"
					style="background-color: #22BAA0; border: none; border-radius: 8px;">
					<i class="fa fa-plus-circle"></i> Thêm khách hàng
				</button>
			</div>

			<div class="table-responsive">
				<table class="table">
					<thead>
						<tr>
							<th class="text-center">ID</th>
							<th>Avatar</th>
							<th>Thông tin tài khoản</th>
							<th>Điện thoại</th>
							<th class="text-center">Tổng chi tiêu</th>
							<th class="text-center">Hành động</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${listC}" var="c">
							<tr>
								<td class="text-center text-success font-weight-bold">#${c.id}</td>
								<td>
									<img src="${pageContext.request.contextPath}/image?fname=${c.avatar}"
										 class="img-avatar"
										 onerror="this.src='https://i.postimg.cc/1z9R3Yxg/admin-profile.png'">
								</td>
								<td>
									<div class="font-weight-bold">${c.fullname}</div> 
									<small class="text-muted">User: ${c.username} | Email: ${c.email}</small>
								</td>
								<td>${empty c.phone ? 'Chưa cập nhật' : c.phone}</td>
								
								<td class="text-center">
									<a href="${pageContext.request.contextPath}/admin/customer/orders?id=${c.id}" 
									   class="total-spent-link" title="Click để xem lịch sử mua hàng">
										<span class="badge badge-success" style="font-size: 0.9rem; padding: 8px 12px; background-color: #22BAA0; cursor: pointer;">
											<fmt:formatNumber value="${c.totalSpent}" type="number" /> VNĐ
										</span>
									</a>
								</td>

								<td class="text-center text-nowrap">
									<a href="edit?id=${c.id}" class="text-success mr-3"><i class="fa fa-edit fa-lg"></i></a> 
									
									<a href="${pageContext.request.contextPath}/admin/customer/delete?id=${c.id}"
									   class="text-danger"
									   onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng ${c.fullname} không?')">
										<i class="fa fa-trash fa-lg"></i>
									</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<div class="mt-4">
				<a href="${pageContext.request.contextPath}/admin"
					class="btn btn-outline-secondary"> <i
					class="fa fa-chevron-left"></i> Quay lại Dashboard
				</a>
			</div>
		</div>
	</div>

</body>
</html>