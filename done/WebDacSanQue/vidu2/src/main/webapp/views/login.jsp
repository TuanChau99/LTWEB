<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Đăng nhập</title>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

<style>
    body {
        background-color: #f8f9fa;
        padding-top: 100px; /* tránh navbar fixed-top đè lên */
    }
    .login-container {
        max-width: 400px;
        margin: 60px auto;
        padding: 25px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0px 4px 10px rgba(0,0,0,0.1);
    }
    .login-container h3 {
        text-align: center;
        margin-bottom: 20px;
        font-weight: 600;
    }
    .input-group-text {
        background-color: #f1f1f1;
    }
    .btn-primary {
        width: 100%;
    }
    .register-link {
        text-align: center;
        margin-top: 15px;
    }
</style>
</head>
<body>

<div class="login-container">
    <h3>Đăng nhập</h3>

    <c:if test="${alert != null}">
        <div class="alert alert-danger text-center">${alert}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <!-- Username -->
        <div class="input-group mb-3">
            <span class="input-group-text"><i class="fa fa-user"></i></span>
            <input type="text" class="form-control" name="username" placeholder="Tài khoản" required>
        </div>

        <!-- Password -->
        <div class="input-group mb-3">
            <span class="input-group-text"><i class="fa fa-lock"></i></span>
            <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
        </div>

        <!-- Submit -->
        <button type="submit" class="btn btn-primary">
            <i class="fa fa-sign-in-alt"></i> Đăng nhập
        </button>

        <!-- Register link -->
        <div class="register-link">
            Chưa có tài khoản? 
            <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </div>
    </form>
</div>

</body>
</html>
