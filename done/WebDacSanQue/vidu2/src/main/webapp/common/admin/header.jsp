<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
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
</body>
</html>