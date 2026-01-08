<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Quản Trị Hệ Thống</title>
    <link rel="stylesheet" href="https://maxst.icons8.com/vue-static/landings/line-awesome/line-awesome/1.3.0/css/line-awesome.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Merriweather+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">

    <style>
        :root {
            --main-color: #22BAA0; 
            --color-dark: #34425A; 
            --text-grey: #B0B0B0; 
        }

        * {
            margin: 0;
            padding: 0;
            text-decoration: none;
            list-style-type: none;
            box-sizing: border-box;
            font-family: 'Merriweather Sans', sans-serif;
        }

        #menu-toggle { display: none; }

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

        .profile {
            text-align: center;
            padding: 2rem 0rem;
        }

        .bg-img {
            background-repeat: no-repeat;
            background-size: cover;
            background-position: center;
            border-radius: 50%;
        }

        .profile-img {
            height: 80px;
            width: 80px;
            display: inline-block;
            margin: 0 auto .5rem auto;
            border: 3px solid #899DC1;
        }

        .profile h4 { color: #fff; font-weight: 500; }
        .profile small { color: #899DC1; font-weight: 600; font-size: 0.75rem; }

        .side-menu ul { text-align: center; padding-bottom: 20px; }
        .side-menu a {
            display: block;
            padding: 1.2rem 0rem;
            transition: background 200ms;
        }

        .side-menu .menu-heading {
            color: #899DC1;
            font-size: 0.7rem;
            font-weight: 600;
            text-transform: uppercase;
            padding: 1rem 0;
            margin-top: 10px;
        }

        .side-menu a:hover { background: #2B384E; }

        /* ⭐ TỰ ĐỘNG BÔI ĐẬM KHI CÓ CLASS ACTIVE ⭐ */
        .side-menu a.active {
            background: #2B384E;
            border-left: 3px solid var(--main-color); 
        }

        .side-menu a.active span, .side-menu a.active small { color: #fff; }
        .side-menu a span { display: block; text-align: center; font-size: 1.7rem; color: #899DC1; }
        .side-menu a small { color: #899DC1; }

        /* Các phần CSS còn lại giữ nguyên như file bạn gửi... */
    </style>
</head>
<body>

    <input type="checkbox" id="menu-toggle">

    <div class="sidebar">
        <div class="side-header">
            <h3>Admin <span>CMS</span></h3>
        </div>

        <div class="side-content">
            <div class="profile">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <c:url value="/image?fname=${sessionScope.account.avatar}" var="adminImgUrl" />
                        <div class="profile-img bg-img"
                            style="background-image: url('${empty sessionScope.account.avatar ? 'https://i.postimg.cc/1z9R3Yxg/admin-profile.png' : adminImgUrl}');">
                        </div>
                        <h4>${sessionScope.account.fullname}</h4>
                        <small>
                            <c:choose>
                                <c:when test="${sessionScope.account.roleid == 1}">Quản trị viên</c:when>
                                <c:otherwise>Người dùng</c:otherwise>
                            </c:choose>
                        </small>
                    </c:when>
                    <c:otherwise>
                        <div class="profile-img bg-img" style="background-image: url('https://i.postimg.cc/1z9R3Yxg/admin-profile.png');"></div>
                        <h4>Khách</h4>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="side-menu">
                <ul>
                    <%-- ⭐ Lấy URL hiện tại để so sánh ⭐ --%>
                    <c:set var="uri" value="${requestScope['jakarta.servlet.forward.request_uri']}" />

                    <li>
                        <a href="${pageContext.request.contextPath}/admin" 
                           class="${uri.endsWith('/admin') ? 'active' : ''}"> 
                            <span class="las la-home"></span> 
                            <small>Dashboard</small>
                        </a>
                    </li>

                    <div class="menu-heading">QUẢN LÝ THÔNG TIN</div>

                    <li>
                        <a href="${pageContext.request.contextPath}/admin/manageProduct"
                           class="${uri.contains('manageProduct') ? 'active' : ''}"> 
                            <span class="las la-box"></span> 
                            <small>Sản phẩm (CRUD)</small>
                        </a>
                    </li>
                    <li>
    <a href="${pageContext.request.contextPath}/admin/recycle-bin"
    class="${uri.contains('recycle-bin') ? 'active' : ''}">
        <span class="las la-trash-restore"></span>
        <small>Thùng rác</small>
    </a>
</li>
<li>
    <a href="${pageContext.request.contextPath}/admin/category/list"
       class="${uri.contains('category/list') ? 'active' : ''}"> 
        <span class="las la-list-ul"></span> <small>Danh mục</small>
    </a>
</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/manageOrder"
                           class="${uri.contains('manageOrder') ? 'active' : ''}">
                            <span class="las la-shopping-bag"></span> 
                            <small>Đơn hàng (CRUD)</small>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/customers"
                           class="${uri.contains('customers') ? 'active' : ''}"> 
                            <span class="las la-user-tag"></span> 
                            <small>Khách hàng (CRUD)</small>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/user/list"
                           class="${uri.contains('user/list') ? 'active' : ''}"> 
                            <span class="las la-users-cog"></span> 
                            <small>Thành viên/Roles</small>
                        </a>
                    </li>

                    <div class="menu-heading">BÁO CÁO & THỐNG KÊ</div>

                    <li>
                        <a href="${pageContext.request.contextPath}/admin/reports/sales"
                           class="${uri.contains('reports/sales') ? 'active' : ''}">
                            <span class="las la-chart-bar"></span> 
                            <small>Thống kê Doanh thu</small>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/stockStats"
                           class="${uri.contains('stockStats') ? 'active' : ''}">
                            <span class="las la-warehouse"></span> 
                            <small>Thống kê Tồn kho</small>
                        </a>
                    </li>

                    <div class="menu-heading">HỆ THỐNG</div>
                    <li>
                        <a href="${pageContext.request.contextPath}/logout"> 
                            <span class="las la-power-off"></span> 
                            <small>Đăng xuất</small>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>