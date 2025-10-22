<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

<style>
    .topbar {
        background-color: #fff;
        border-bottom: 1px solid #eaeaea;
        padding: 10px 20px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }
    .topbar a {
        text-decoration: none;
        font-weight: 500;
        color: #333;
    }
    .topbar a:hover {
        color: #0d6efd;
    }
    .search-btn {
        font-size: 18px;
        color: #666;
        cursor: pointer;
    }
    .search-btn:hover {
        color: #0d6efd;
    }
</style>
</head>
<body>

<h1 class="text-center mt-2">Header của Admin</h1>

<div class="topbar d-flex justify-content-end align-items-center">
    <c:choose>
        <c:when test="${sessionScope.account == null}">
            <!-- Khi chưa đăng nhập -->
            <div>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary btn-sm me-2">
                    <i class="fa fa-sign-in-alt"></i> Đăng nhập
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline-success btn-sm me-2">
                    <i class="fa fa-user-plus"></i> Đăng ký
                </a>
                <i class="fa fa-search search-btn"></i>
            </div>
        </c:when>

        <c:otherwise>
            <!-- Khi đã đăng nhập -->
            <div class="d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/member/myaccount" class="text-decoration-none text-dark me-3">
                    <i class="fa fa-user"></i> ${sessionScope.account.fullname}
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm me-3">
                    <i class="fa fa-sign-out-alt"></i> Đăng xuất
                </a>
                <i class="fa fa-search search-btn"></i>
            </div>
        </c:otherwise>
    </c:choose>
</div>
