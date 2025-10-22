<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tạo tài khoản mới</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
        }
        .register-container {
            max-width: 400px;
            margin: 50px auto;
            padding: 25px;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0,0,0,0.1);
        }
        .register-container h3 {
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
        .login-link {
            text-align: center;
            margin-top: 15px;
        }
    </style>
</head>
<body>

    <div class="register-container">
        <h3>Tạo tài khoản mới</h3>

        <!-- Hiển thị thông báo nếu có -->
        <c:if test="${not empty alert}">
            <div class="alert alert-danger text-center">${alert}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            
            <!-- Username -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa fa-user"></i></span>
                <input type="text" class="form-control" name="username" placeholder="Tên đăng nhập" required>
            </div>

            <!-- Full Name -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa fa-id-card"></i></span>
                <input type="text" class="form-control" name="fullname" placeholder="Họ tên" required>
            </div>

            <!-- Email -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa fa-envelope"></i></span>
                <input type="email" class="form-control" name="email" placeholder="Nhập Email" required>
            </div>

            <!-- Phone -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa fa-phone"></i></span>
                <input type="text" class="form-control" name="phone" placeholder="Số điện thoại">
            </div>

            <!-- Password -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa fa-lock"></i></span>
                <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
            </div>

            <!-- Confirm Password -->
            <div class="input-group mb-3">
                <span class="input-group-text"><i class="fa fa-lock"></i></span>
                <input type="password" class="form-control" name="repassword" placeholder="Nhập lại mật khẩu" required>
            </div>

            <!-- Submit -->
            <button type="submit" class="btn btn-primary">Tạo tài khoản</button>

            <!-- Login link -->
            <div class="login-link">
                Nếu bạn đã có tài khoản? 
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
            </div>
        </form>
    </div>

</body>
</html>
