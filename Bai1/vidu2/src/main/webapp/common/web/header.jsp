<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TravelTour</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        .navbar {
            background: linear-gradient(90deg, #007bff, #00c6ff);
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        .navbar-brand {
            font-weight: 700;
            color: white !important;
            font-size: 1.6rem;
            letter-spacing: 1px;
        }
        .navbar-nav .nav-link {
            color: white !important;
            font-weight: 500;
            margin: 0 8px;
            transition: 0.3s;
        }
        .navbar-nav .nav-link:hover {
            color: #ffe082 !important;
        }
        .btn-auth {
            border-radius: 20px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        .btn-login {
            background: white;
            color: #0d6efd;
        }
        .btn-register {
            border: 1px solid white;
            color: white;
        }
        .btn-register:hover {
            background: white;
            color: #0d6efd;
        }
        .dropdown-menu a:hover {
            background-color: #f8f9fa;
        }
        .btn-logout {
            border: 1px solid #ff4d4d;
            color: white;
            background-color: #ff4d4d;
        }
        .btn-logout:hover {
            background-color: #e60000;
            color: #fff;
        }
    </style>
</head>
<body>

<!-- ======= Header/Navbar ======= -->
<nav class="navbar navbar-expand-lg fixed-top">
    <div class="container">
        <!-- Logo -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
            <i class="fa-solid fa-plane-departure"></i> TravelTour
        </a>

        <!-- Toggle for mobile -->
        <button class="navbar-toggler bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Menu -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/tour">Tour du lịch</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/about">Giới thiệu</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/contact">Liên hệ</a>
                </li>
            </ul>

            <!-- Tài khoản -->
            <ul class="navbar-nav align-items-center">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <!-- Hiển thị tên người dùng -->
                        <li class="nav-item dropdown me-2">
                            <a class="nav-link dropdown-toggle text-white" href="#" id="userDropdown"
                               role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fa fa-user"></i> ${sessionScope.account.fullname}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                <li>
                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                        Hồ sơ cá nhân
                                    </a>
                                </li>
                                <li><hr class="dropdown-divider"></li>
                                <li>
                                    <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                        <i class="fa fa-sign-out-alt"></i> Đăng xuất
                                    </a>
                                </li>
                            </ul>
                        </li>

                        <!-- Nút Đăng xuất riêng -->
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/logout"
                               class="btn btn-logout btn-sm btn-auth">
                                <i class="fa fa-sign-out-alt"></i> Đăng xuất
                            </a>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/login"
                               class="btn btn-light btn-sm mx-1 btn-auth btn-login">
                                <i class="fa fa-sign-in-alt"></i> Đăng nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="${pageContext.request.contextPath}/register"
                               class="btn btn-outline-light btn-sm btn-auth btn-register">
                                <i class="fa fa-user-plus"></i> Đăng ký
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Khoảng cách để tránh che nội dung -->
<div style="margin-top: 80px;"></div>

<!-- Bootstrap JS (có cả Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
