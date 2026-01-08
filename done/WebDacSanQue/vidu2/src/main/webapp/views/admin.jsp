<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%-- 
    Dashboard Template sử dụng CSS và HTML gốc (tông Xanh Teal) 
    File này đã được tối ưu hóa cho Backend E-commerce.
--%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Dashboard Quản Trị Hệ Thống</title>

<%-- 1. Nhúng Font Icon (Sử dụng Line Awesome) --%>
<link rel="stylesheet"
	href="https://maxst.icons8.com/vue-static/landings/line-awesome/line-awesome/1.3.0/css/line-awesome.min.css">

<%-- 2. Nhúng Font Chữ (Merriweather Sans) --%>
<link
	href="https://fonts.googleapis.com/css2?family=Merriweather+Sans:wght@300;400;500;600&display=swap"
	rel="stylesheet">

<%-- 3. CSS GỐC (Tông Xanh Teal) --%>
<style>
:root {
	--main-color: #22BAA0; /* Xanh Teal */
	--color-dark: #34425A; /* Xám Xanh Đậm */
	--text-grey: #B0B0B0; /* Xám Nhạt */
}

* {
	margin: 0;
	padding: 0;
	text-decoration: none;
	list-style-type: none;
	box-sizing: border-box;
	/* Đảm bảo font 'Merriweather Sans' được sử dụng */
	font-family: 'Merriweather Sans', sans-serif;
}

/* ----------------------------- SIDEBAR ------------------------------ */
#menu-toggle {
	display: none;
}

.sidebar {
	position: fixed;
	height: 100%;
	width: 165px;
	left: 0;
	bottom: 0;
	top: 0;
	z-index: 100;
	background: var(--color-dark);
	transition: left 300ms;
}

.side-header {
	box-shadow: 0px 5px 5px -5px rgb(0 0 0/ 10%);
	background: var(--main-color);
	height: 60px;
	display: flex;
	justify-content: center;
	align-items: center;
}

.side-header h3, .side-header span {
	color: #fff;
	font-weight: 400;
	font-size: 1.2rem;
}

.side-content {
	height: calc(100vh - 60px);
	overflow: auto;
}

/* Scrollbar styles */
.side-content::-webkit-scrollbar {
	width: 5px;
}

.side-content::-webkit-scrollbar-track {
	box-shadow: inset 0 0 5px rgb(0 0 0/ 10%);
	border-radius: 10px;
}

.side-content::-webkit-scrollbar-thumb {
	background: #555;
	border-radius: 10px;
}

.side-content::-webkit-scrollbar-thumb:hover {
	background: #333;
}

.profile {
	text-align: center;
	padding: 2rem 0rem;
}

/* ⭐⭐⭐ Đã Sửa: Thêm background-size và background-repeat cho ảnh nền ⭐⭐⭐ */
.bg-img {
	background-repeat: no-repeat;
	background-size: cover; /* Đảm bảo ảnh phủ kín div */
	background-position: center; /* Căn giữa ảnh */
	border-radius: 50%;
}

.profile-img {
	height: 80px;
	width: 80px;
	display: inline-block;
	margin: 0 auto .5rem auto;
	border: 3px solid #899DC1;
}

.profile h4 {
	color: #fff;
	font-weight: 500;
}

.profile small {
	color: #899DC1;
	font-weight: 600;
	font-size: 0.75rem;
}

.side-menu ul {
	text-align: center;
	padding-bottom: 20px; /* Thêm padding cuối menu */
}

.side-menu a {
	display: block;
	padding: 1.2rem 0rem;
	transition: background 200ms;
}

/* Menu Phân quyền */
.side-menu .menu-heading {
	color: #899DC1;
	font-size: 0.7rem;
	font-weight: 600;
	text-transform: uppercase;
	padding: 1rem 0;
	margin-top: 10px;
}

.side-menu a:hover {
	background: #2B384E;
}

.side-menu a.active {
	background: #2B384E;
	border-left: 3px solid var(--main-color); /* Highlight item active */
}

.side-menu a.active span, .side-menu a.active small {
	color: #fff;
}

.side-menu a span {
	display: block;
	text-align: center;
	font-size: 1.7rem;
}

.side-menu a span, .side-menu a small {
	color: #899DC1;
}

/* Toggles for Sidebar Collapse */
#menu-toggle:checked ~ .sidebar {
	width: 60px;
}

#menu-toggle:checked ~ .sidebar .side-header span {
	display: none;
}

#menu-toggle:checked ~ .sidebar .side-menu .menu-heading {
	display: none;
}

#menu-toggle:checked ~ .main-content {
	margin-left: 60px;
	width: calc(100% - 60px);
}

#menu-toggle:checked ~ .main-content header {
	left: 60px;
}

#menu-toggle:checked ~ .sidebar .profile, #menu-toggle:checked ~
	.sidebar .side-menu a small {
	display: none;
}

#menu-toggle:checked ~ .sidebar .side-menu a span {
	font-size: 1.3rem;
}

/* ---------------------------- MAIN CONTENT & HEADER ------------------------ */
.main-content {
	margin-left: 165px;
	width: calc(100% - 165px);
	transition: margin-left 300ms;
}

header {
	position: fixed;
	right: 0;
	top: 0;
	left: 165px;
	z-index: 100;
	height: 60px;
	box-shadow: 0px 5px 5px -5px rgb(0 0 0/ 10%);
	background: #fff;
	transition: left 300ms;
}

.header-content, .header-menu {
	display: flex;
	align-items: center;
}

.header-content {
	justify-content: space-between;
	padding: 0rem 1rem;
}

.header-content label:first-child span {
	font-size: 1.3rem;
}

.header-content label {
	cursor: pointer;
}

.header-menu {
	justify-content: flex-end;
	padding-top: .5rem;
}

.header-menu label, .header-menu .notify-icon {
	margin-right: 2rem;
	position: relative;
}

.header-menu label span, .notify-icon span:first-child {
	font-size: 1.3rem;
}

.notify-icon span:last-child {
	position: absolute;
	background: var(--main-color);
	height: 16px;
	width: 16px;
	display: flex;
	justify-content: center;
	align-items: center;
	border-radius: 50%;
	right: -5px;
	top: -5px;
	color: #fff;
	font-size: .8rem;
	font-weight: 500;
}

.user {
	display: flex;
	align-items: center;
}

.user div, .client-img {
	height: 40px;
	width: 40px;
	margin-right: 1rem;
}

/* ⭐ Đảm bảo ảnh nhỏ trên header cũng có border radius ⭐ */
.user div {
	border-radius: 50%;
	border: 1px solid #ddd;
}

.user span:last-child {
	display: inline-block;
	margin-left: .3rem;
	font-size: .8rem;
}

main {
	margin-top: 60px;
}

/* ------------------------ PAGE HEADER & CONTENT ---------------------- */
.page-header {
	padding: 1.3rem 1rem;
	background: #E9edf2;
	border-bottom: 1px solid #dee2e8;
}

.page-header h1, .page-header small {
	color: #74767d;
}

.page-content {
	padding: 1.3rem 1rem;
	background: #f1f4f9;
}

/* --------------------------- ANALYTICS CARDS ------------------------ */
.analytics {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	grid-gap: 2rem;
	margin-top: .5rem;
	margin-bottom: 2rem;
}

.card {
	box-shadow: 0px 5px 5px -5px rgb(0 0 0/ 10%);
	background: #fff;
	padding: 1rem;
	border-radius: 3px;
}

.card-head {
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.card-head h2 {
	color: #333;
	font-size: 1.8rem;
	font-weight: 500;
}

.card-head span {
	font-size: 3.2rem;
	color: var(--text-grey);
}

.card-progress small {
	color: #777;
	font-size: .8rem;
	font-weight: 600;
}

.card-indicator {
	margin: .7rem 0rem;
	height: 10px;
	border-radius: 4px;
	background: #e9edf2;
	overflow: hidden;
}

.indicator {
	height: 10px;
	border-radius: 4px;
}

/* Màu indicator chuẩn của template gốc */
.indicator.one {
	background: #22baa0;
} /* Doanh thu */
.indicator.two {
	background: #11a8c3;
} /* Đơn hàng */
.indicator.three {
	background: #f6d433;
} /* Sản phẩm */
.indicator.four {
	background: #f25656;
} /* Khách hàng mới */

/* --------------------------- RECORDS TABLE ------------------------ */
.records {
	box-shadow: 0px 5px 5px -5px rgb(0 0 0/ 10%);
	background: #fff;
	border-radius: 3px;
}

.record-header {
	padding: 1rem;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

.add, .browse {
	display: flex;
	align-items: center;
}

.add span {
	display: inline-block;
	margin-right: .6rem;
	font-size: .9rem;
	color: #666;
}

input, button, select {
	outline: none;
}

.add select, .browse input, .browse select {
	height: 35px;
	border: 1px solid #b0b0b0;
	border-radius: 3px;
	display: inline-block;
	width: 75px;
	padding: 0rem .5rem;
	margin-right: .8rem;
	color: #666;
}

.add button {
	background: var(--main-color);
	color: #fff;
	height: 37px;
	border-radius: 4px;
	padding: 0rem 1rem;
	border: none;
	font-weight: 600;
	cursor: pointer;
}

.browse button {
	background: var(--color-dark);
	color: #fff;
	height: 37px;
	border-radius: 4px;
	padding: 0rem 1rem;
	border: none;
	font-weight: 600;
	cursor: pointer;
}

.browse input {
	width: 150px;
}

.browse select {
	width: 100px;
}

.table-responsive {
	width: 100%;
	overflow: auto;
}

table {
	border-collapse: collapse;
	width: 100%;
}

table thead tr {
	background: #e9edf2;
}

table thead th {
	padding: 1rem 0rem;
	text-align: left;
	color: #444;
	font-size: .9rem;
}

table thead th:first-child {
	padding-left: 1rem;
}

table tbody td {
	padding: 1rem 0rem;
	color: #444;
}

table tbody td:first-child {
	padding-left: 1rem;
	color: var(--main-color);
	font-weight: 600;
	font-size: .9rem;
}

table tbody tr {
	border-bottom: 1px solid #dee2e8;
}

table tbody tr:last-child {
	border-bottom: none;
} /* Loại bỏ border cuối cùng */
.client {
	display: flex;
	align-items: center;
}

.client-img {
	margin-right: .5rem;
	border: 2px solid #b0b0b0;
	height: 45px;
	width: 45px;
}

.client-info h4 {
	color: #555;
	font-size: .95rem;
}

.client-info small {
	color: #777;
}

.actions span {
	display: inline-block;
	font-size: 1.5rem;
	margin-right: .5rem;
	cursor: pointer;
	color: #555;
}

.actions span:hover {
	color: var(--main-color);
}

.paid {
	display: inline-block;
	text-align: center;
	font-weight: 600;
	color: var(--main-color);
	background: #e5f8ed;
	padding: .5rem 1rem;
	border-radius: 20px;
	font-size: .8rem;
}

/* Màu trạng thái đặc biệt */
.status-pending {
	background: #fff5e5 !important;
	color: #f6d433 !important;
}

.status-cancelled {
	background: #fdf2f2 !important;
	color: #f25656 !important;
}

/* Khu vực Tồn kho/Reports */
.records h2 {
	padding-bottom: 10px;
	font-size: 1.3rem;
	color: #444;
}

/* ---------------------- MEDIA QUERIES (RESPONSIVE) ------------------ */
@media only screen and (max-width: 1200px) {
	.analytics {
		grid-template-columns: repeat(2, 1fr);
	}
	.dual-content {
		grid-template-columns: 100% !important;
	}
}

@media only screen and (max-width: 768px) {
	.analytics {
		grid-template-columns: 100%;
	}
	.dual-content {
		grid-template-columns: 100% !important;
	}
	.sidebar {
		left: -165px;
		z-index: 90;
	}
	header {
		left: 0;
		width: 100%;
	}
	.main-content {
		margin-left: 0;
		width: 100%;
	}

	/* Logic mở/đóng sidebar trên mobile */
	#menu-toggle:checked ~ .sidebar {
		left: 0;
	}
	#menu-toggle:checked ~ .sidebar {
		width: 165px;
	}
	#menu-toggle:checked ~ .sidebar .side-header span {
		display: inline-block;
	}
	#menu-toggle:checked ~ .sidebar .profile, #menu-toggle:checked ~
		.sidebar .side-menu a small {
		display: block;
	}
	#menu-toggle:checked ~ .sidebar .side-menu a span {
		font-size: 1.7rem;
	}
	#menu-toggle:checked ~ .main-content header {
		left: 0px;
	}
	table {
		width: 900px;
	} /* Đảm bảo bảng cuộn ngang trên mobile */
}
</style>
</head>
<body>

	<input type="checkbox" id="menu-toggle">

	<%-- ---------------------------------------------------------------- --%>
	<%-- ----------------------------- SIDEBAR (ĐÃ CẬP NHẬT) -------------- --%>
	<%-- ---------------------------------------------------------------- --%>
	<div class="sidebar">
		<div class="side-header">
			<h3>
				Admin <span>CMS</span>
			</h3>
		</div>

		<div class="side-content">
			<div class="profile">
				<c:choose>
					<%-- Kiểm tra nếu đã đăng nhập (đối tượng account tồn tại trong session) --%>
					<c:when test="${not empty sessionScope.account}">

						<%-- 1. Tạo URL ảnh thông qua ImageServlet giống trang List --%>
						<c:url value="/image?fname=${sessionScope.account.avatar}"
							var="adminImgUrl" />

						<%-- 2. Hiển thị ảnh: Nếu avatar trống thì dùng ảnh mặc định, ngược lại dùng adminImgUrl --%>
						<div class="profile-img bg-img"
							style="background-image: url('${empty sessionScope.account.avatar ? 'https://i.postimg.cc/1z9R3Yxg/admin-profile.png' : adminImgUrl}');">
						</div>

						<%-- 3. Hiển thị tên và vai trò động từ Session --%>
						<h4>${sessionScope.account.fullname}</h4>
						<small> <c:choose>
								<c:when test="${sessionScope.account.roleid == 1}">Quản trị viên</c:when>
								<c:otherwise>Người dùng</c:otherwise>
							</c:choose>
						</small>

					</c:when>
					<c:otherwise>
						<%-- Trường hợp dự phòng nếu session trống --%>
						<div class="profile-img bg-img"
							style="background-image: url('https://i.postimg.cc/1z9R3Yxg/admin-profile.png');"></div>
						<h4>Khách</h4>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="side-menu">
				<ul>
					<li><a href="${pageContext.request.contextPath}/admin"
						class="active"> <span class="las la-home"></span> <small>Dashboard</small>
					</a></li>

					<div class="menu-heading">QUẢN LÝ THÔNG TIN</div>

					<li><a
						href="${pageContext.request.contextPath}/admin/manageProduct">
							<span class="las la-box"></span> <small>Sản phẩm (CRUD)</small>
					</a></li>
					<li><a
						href="${pageContext.request.contextPath}/admin/recycle-bin"
						class="${uri.contains('recycle-bin') ? 'active' : ''}"> <span
							class="las la-trash-restore"></span> <small>Thùng rác</small>
					</a></li>
					<li>
    <a href="${pageContext.request.contextPath}/admin/category/list"
       class="${uri.contains('category/list') ? 'active' : ''}"> 
        <span class="las la-list-ul"></span> <small>Danh mục</small>
    </a>
</li>
					<li><a
						href="${pageContext.request.contextPath}/admin/manageOrder"> <span
							class="las la-shopping-bag"></span> <small>Đơn hàng
								(CRUD)</small>
					</a></li>
					<li><a
						href="${pageContext.request.contextPath}/admin/customers"> <span
							class="las la-user-tag"></span> <small>Khách hàng (CRUD)</small>
					</a></li>
					<li><a
						href="${pageContext.request.contextPath}/admin/user/list"> <span
							class="las la-users-cog"></span> <small>Thành viên/Roles</small>
					</a></li>

					<div class="menu-heading">BÁO CÁO & THỐNG KÊ</div>

					<li><a
						href="${pageContext.request.contextPath}/admin/reports/sales">
							<span class="las la-chart-bar"></span> <small>Thống kê
								Doanh thu</small>
					</a></li>
					<li><a
						href="${pageContext.request.contextPath}/admin/stockStats"> <span
							class="las la-warehouse"></span> <small>Thống kê Tồn kho</small>
					</a></li>

					<div class="menu-heading">HỆ THỐNG</div>
					<li><a href="${pageContext.request.contextPath}/logout"> <span
							class="las la-power-off"></span> <small>Đăng xuất</small>
					</a></li>
				</ul>
			</div>
		</div>
	</div>

	<%-- ---------------------------------------------------------------- --%>
	<%-- --------------------------- MAIN CONTENT ----------------------- --%>
	<%-- ---------------------------------------------------------------- --%>
	<div class="main-content">

		<header>
			<div class="header-content">
				<label for="menu-toggle"> <span class="las la-bars"></span>
				</label>

				<div class="header-menu">
					<%-- Các icon tiện ích --%>
					<div class="notify-icon">
						<span class="las la-bell"></span> <span class="count">4</span>
					</div>

					<label> <span class="las la-envelope"></span>
					</label>

					<div class="user">
						<%-- Dùng ảnh từ Internet để test trước, nếu không bị văng nữa thì mới do file tĩnh --%>
						<div class="bg-img"
							style="background-image: url('https://i.postimg.cc/1z9R3Yxg/admin-profile.png');"></div>
						<span><span class="las la-angle-down"></span></span>
					</div>
				</div>
			</div>
		</header>

		<main>
			<div class="page-header">
				<h1>Dashboard</h1>
				<small>Trang chủ / Thống kê tổng quan</small>
			</div>

			<div class="page-content">

				<h2>📈 Chỉ số Kinh doanh (KPIs) Hôm nay</h2>
				<%-- Khu vực 1: ANALYTICS CARDS (Các chỉ số quan trọng) --%>
				<%-- **LƯU Ý:** Bạn cần đặt các biến JSP/JSTL (ví dụ: totalRevenue, newOrdersCount) vào request attribute từ Servlet/Controller --%>
				<div class="analytics">
					<%-- Thẻ 1: Doanh thu - Đã đúng biến ${totalRevenue} --%>
					<div class="card">
						<div class="card-head">
							<h2>
								<fmt:formatNumber value="${totalRevenue}" type="number"
									maxFractionDigits="0" />
								VNĐ
							</h2>
							<span class="las la-chart-line"></span>
						</div>
						<div class="card-progress">
							<small>Tổng Doanh thu (Tháng này)</small>
							<div class="card-indicator">
								<div class="indicator one" style="width: 100%"></div>
							</div>
						</div>
					</div>

					<%-- Thẻ 2: Đơn hàng chờ xử lý - THAY SỐ 24 --%>
					<div class="card">
						<div class="card-head">
							<h2>${pendingOrders}</h2>
							<%-- Sửa ở đây --%>
							<span class="las la-receipt"></span>
						</div>
						<div class="card-progress">
							<small>Đơn hàng chờ xử lý</small>
							<div class="card-indicator">
								<div class="indicator two" style="width: 40%"></div>
							</div>
						</div>
					</div>

					<%-- Thẻ 3: Tổng sản phẩm - THAY SỐ 85 --%>
					<div class="card">
						<div class="card-head">
							<h2>${totalProductsCount}</h2>
							<%-- Sửa ở đây --%>
							<span class="las la-box-open"></span>
						</div>
						<div class="card-progress">
							<small>Tổng loại Đặc sản đang bán</small>
							<div class="card-indicator">
								<div class="indicator three" style="width: 80%"></div>
							</div>
						</div>
					</div>

					<%-- Thẻ 4: Khách hàng mới - Bạn có thể dùng tạm totalProductsCount hoặc biến mới --%>
					<div class="card">
						<div class="card-head">
							<h2>${newCustomersCount}</h2>
							<span class="las la-user-plus"></span>
						</div>
						<div class="card-progress">
							<small>Khách hàng mới (30 ngày)</small>
							<div class="card-indicator">
								<div class="indicator four" style="width: 70%"></div>
							</div>
						</div>
					</div>
				</div>

				<%-- Khu vực 2: Bảng và Thống kê nhanh (chia 2 cột) --%>
				<div class="dual-content"
					style="display: grid; grid-template-columns: 65% 33%; grid-gap: 2rem; margin-top: 1.5rem;">

					<%-- KHU VỰC BẢNG LATEST ORDERS (Đơn hàng mới nhất) --%>
					<div class="records table-responsive">
						<h2>📋 Đơn hàng mới nhất</h2>

						<div class="record-header" style="padding: 1rem 0 1rem 0;">
							<div class="add">
								<span>Hiển thị</span>
								<%-- Form tự động gửi yêu cầu khi thay đổi số lượng --%>
								<form action="${pageContext.request.contextPath}/admin"
									method="get" style="display: inline;">
									<select name="entries" onchange="this.form.submit()"
										style="width: 60px;">
										<option value="5" ${selectedLimit == 5 ? 'selected' : ''}>5</option>
										<option value="10" ${selectedLimit == 10 ? 'selected' : ''}>10</option>
										<option value="20" ${selectedLimit == 20 ? 'selected' : ''}>20</option>
									</select>
								</form>
							</div>

							<div class="browse">
								<form action="${pageContext.request.contextPath}/admin"
									method="get" style="display: flex;">
									<%-- Giữ lại giá trị entries hiện tại khi tìm kiếm --%>
									<input type="hidden" name="entries" value="${selectedLimit}">

									<input type="search" name="search" value="${keyword}"
										placeholder="Tìm kiếm Đơn hàng">
								</form>
								<button
									onclick="window.location.href='${pageContext.request.contextPath}/admin/manageOrder'">
									Quản lý Đơn hàng</button>
							</div>
						</div>

						<div>
							<table width="100%">
								<thead>
									<tr>
										<th># ID</th>
										<th><span class="las la-sort"></span> KHÁCH HÀNG</th>
										<th><span class="las la-sort"></span> NGÀY ĐẶT</th>
										<th><span class="las la-sort"></span> TỔNG TIỀN</th>
										<th><span class="las la-sort"></span> TRẠNG THÁI</th>
										<th><span class="las la-sort"></span> XEM</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<%-- Trường hợp có dữ liệu --%>
										<c:when test="${not empty recentOrders}">
											<c:forEach items="${recentOrders}" var="order">
												<tr>
													<td>#${order.id}</td>
													<td>
														<div class="client-info">
															<h4>${order.user.fullname}</h4>
															<small>${order.user.email}</small>
														</div>
													</td>
													<td><fmt:formatDate value="${order.orderDate}"
															pattern="dd/MM/yyyy" /></td>
													<td><fmt:formatNumber value="${order.totalPrice}"
															type="number" pattern="#,###" /> VNĐ</td>
													<td><c:choose>
															<%-- Khớp với logic trang chi tiết đơn hàng của bạn --%>
															<c:when test="${order.status == 0}">
																<div class="paid status-pending">Chờ xử lý</div>
															</c:when>

															<c:when test="${order.status == 1}">
																<%-- Thêm style màu xanh dương cho Đang giao để dễ phân biệt --%>
																<div class="paid"
																	style="background: #e1f5fe; color: #03a9f4;">Đang
																	giao</div>
															</c:when>

															<c:when test="${order.status == 2}">
																<div class="paid">Hoàn thành</div>
															</c:when>

															<c:otherwise>
																<div class="paid status-cancelled">Đã hủy</div>
															</c:otherwise>
														</c:choose></td>
													<td class="actions"><a
														href="${pageContext.request.contextPath}/admin/orderDetail?oid=${order.id}">
															<span class="las la-eye" title="Xem chi tiết"></span>
													</a></td>
												</tr>
											</c:forEach>
										</c:when>

										<%-- Trường hợp rỗng (Do không có đơn hàng hoặc tìm kiếm không ra) --%>
										<c:otherwise>
											<tr>
												<td colspan="6" style="text-align: center; padding: 40px 0;">
													<div style="color: #999;">
														<span class="las la-search-minus"
															style="font-size: 3rem; color: #eee; display: block; margin-bottom: 10px;"></span>
														<p style="font-size: 1rem;">
															Không tìm thấy đơn hàng nào khớp với: <strong>"${keyword}"</strong>
														</p>
														<a href="${pageContext.request.contextPath}/admin"
															style="display: inline-block; margin-top: 15px; color: var(--main-color); text-decoration: underline;">
															Quay lại danh sách đầy đủ </a>
													</div>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</div>
					</div>

					<%-- KHU VỰC BÁO CÁO NHANH/HÀNG TỒN --%>
					<%-- KHU VỰC BÁO CÁO NHANH/HÀNG TỒN (ĐÃ CẬP NHẬT) --%>
					<div class="records" style="padding: 1.5rem;">
						<h2 style="font-size: 1.2rem; color: #444; margin-bottom: 1.2rem;">
							<i class="las la-exclamation-triangle" style="color: #f6d433;"></i>
							Tình trạng Tồn kho (Sắp hết)
						</h2>

						<div
							style="display: flex; flex-direction: column; align-items: center;">
							<div style="width: 200px; height: 200px; margin-bottom: 1.5rem;">
								<canvas id="dashboardStockChart"></canvas>
							</div>

							<div style="width: 100%; max-height: 150px; overflow-y: auto;">
								<c:forEach items="${lowStockList}" var="ls">
									<div
										style="display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #f1f4f9;">
										<span
											style="font-size: 0.9rem; color: #555; font-weight: 500;">${ls.name}</span>
										<span
											style="color: #f25656; font-weight: 700; font-size: 0.9rem;">${ls.stock}
											sp</span>
									</div>
								</c:forEach>
								<c:if test="${empty lowStockList}">
									<p
										style="text-align: center; color: #999; font-size: 0.9rem; padding: 10px;">🎉
										Kho hàng đang rất đầy đủ!</p>
								</c:if>
							</div>

							<div style="margin-top: 1.5rem; display: flex; gap: 10px;">
								<button onclick="window.location.href='stockStats'"
									style="background: #eee; color: #333; padding: 8px 15px; border-radius: 4px; border: 1px solid #ddd; cursor: pointer; font-size: 0.8rem;">
									Xem chi tiết</button>
								<button onclick="window.location.href='manageProduct'"
									style="background: var(--main-color); color: #fff; padding: 8px 15px; border-radius: 4px; border: none; cursor: pointer; font-size: 0.8rem;">
									Thêm sản phẩm</button>
							</div>
						</div>
					</div>

				</div>
			</div>

		</main>

	</div>
	<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
	<script>
    document.addEventListener("DOMContentLoaded", function() {
        const ctx = document.getElementById('dashboardStockChart').getContext('2d');
        
        const labels = [<c:forEach items="${lowStockList}" var="p" end="4">'${p.name}',</c:forEach>];
        const data = [<c:forEach items="${lowStockList}" var="p" end="4">${p.stock},</c:forEach>];

        new Chart(ctx, {
            type: 'doughnut', // Đổi sang Doughnut cho đẹp
            data: {
                labels: labels,
                datasets: [{
                    data: data,
                    backgroundColor: ['#22BAA0', '#34425A', '#F39C12', '#E74C3C', '#9B59B6', '#11a8c3'],
                    borderWidth: 2,
                    hoverOffset: 15
                }]
            },
            options: {
                cutout: '75%', // Làm vòng mỏng lại cho sang trọng
                plugins: {
                    legend: { display: false } // Ẩn chú thích để Dashboard thoáng hơn
                },
                maintainAspectRatio: false
            }
        });
    });
</script>
</body>
</html>