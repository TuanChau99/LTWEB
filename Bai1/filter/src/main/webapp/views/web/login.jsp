<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        h3 {
            text-align: center;
            margin-bottom: 20px;
            font-weight: 500; /* Giảm độ đậm */
            color: #333;
        }
        .btn-primary {
            background-color: #0d6efd;
            border: none;
        }
        .btn-primary:hover {
            background-color: #0b5ed7;
        }
        .register-link {
            text-align: center;
            margin-top: 15px;
            font-size: 0.95rem;
        }
        .register-link a {
            color: #0d6efd;
            text-decoration: none;
            font-weight: 500;
        }
        .register-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container mt-5">
    <div class="card p-4" style="max-width: 400px; margin: auto;">
        <h3>Đăng nhập hệ thống</h3>

        <!-- Thông báo lỗi -->
        <c:if test="${not empty alert}">
            <div class="alert alert-danger text-center">${alert}</div>
        </c:if>

        <!-- Form đăng nhập -->
        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="mb-3">
                <label class="form-label">Tài khoản</label>
                <input type="text" name="username" class="form-control" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Mật khẩu</label>
                <input type="password" name="password" class="form-control" required>
            </div>

            <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
        </form>

        <!-- Liên kết đăng ký -->
        <div class="register-link">
            Nếu bạn chưa có tài khoản?
            <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </div>
    </div>
</div>

</body>
</html>
