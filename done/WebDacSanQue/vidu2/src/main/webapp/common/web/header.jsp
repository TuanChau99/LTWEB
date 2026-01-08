<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặc Sản Quê Hương</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>

    <style>
    /* Khi có class .show thì ép menu phải hiện ra bằng mọi giá */
.dropdown-menu.show {
    display: block !important;
    opacity: 1 !important;
    visibility: visible !important;
}
        /* Màu sắc chủ đạo: Nâu Đất (Earth Brown) */
        .navbar {
            background: linear-gradient(90deg, #a0522d, #cd853f); 
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
            color: #fff3e0 !important;
        }
        
        /* HIỆU ỨNG RUNG GIỎ HÀNG */
        @keyframes cartShake {
            0% { transform: scale(1); }
            25% { transform: scale(1.2) rotate(15deg); }
            50% { transform: scale(1.2) rotate(-15deg); }
            75% { transform: scale(1.2) rotate(10deg); }
            100% { transform: scale(1); }
        }
        .cart-animate {
            display: inline-block;
            animation: cartShake 0.5s ease-in-out;
        }

        /* CUSTOM SWEETALERT TOAST */
        .colored-toast.swal2-icon-success {
            background-color: #a5dc86 !important;
        }
        .swal2-popup.swal2-toast {
            padding: 10px 15px !important;
            border-radius: 12px !important;
        }

        .btn-auth { border-radius: 20px; font-weight: 500; transition: all 0.3s ease; }
        .btn-login { background: white; color: #a0522d; }
        .btn-register { border: 1px solid white; color: white; }
        .btn-register:hover { background: white; color: #a0522d; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg fixed-top shadow">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
            <i class="fa-solid fa-seedling"></i> Đặc Sản Quê
        </a>

        <button class="navbar-toggler bg-light" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mx-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/product">Đặc Sản</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/about">Giới thiệu</a></li>
                <c:if test="${sessionScope.account != null && sessionScope.account.roleid == 1}">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin">Trang quản trị</a></li>
                </c:if>
            </ul>

            <ul class="navbar-nav align-items-center">
                <c:set var="cart" value="${sessionScope.cart}"/>
                <li class="nav-item me-3">
                    <a class="nav-link text-white position-relative" href="${pageContext.request.contextPath}/cart" id="cart-nav-link">
                        <i class="fa fa-shopping-cart fa-lg"></i>
                        <c:if test="${not empty cart.items && cart.items.size() > 0}">
                            <span id="cart-item-count" class="badge bg-danger rounded-pill position-absolute top-0 start-100 translate-middle" style="font-size: 0.6em;">
                                ${cart.items.size()}
                            </span>
                        </c:if>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <li class="nav-item dropdown me-2">
                            <a class="nav-link dropdown-toggle text-white d-flex align-items-center" href="javascript:void(0)" id="userDropdownLink">
                                <i class="fa fa-user"></i> ${sessionScope.account.fullname}
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow" id="userDropdownContent">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">Hồ sơ cá nhân</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/manageOrder">Đơn hàng</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="fa fa-sign-out-alt"></i> Đăng xuất</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/login" class="btn btn-light btn-sm mx-1 btn-auth btn-login px-3">Đăng nhập</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/register" class="btn btn-outline-light btn-sm btn-auth btn-register px-3">Đăng ký</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<div style="margin-top: 100px;"></div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    
    // --- 1. XỬ LÝ THÊM VÀO GIỎ HÀNG THÀNH CÔNG (TOAST VIP) ---
    if (urlParams.get('success') === 'add') {
        // Hiệu ứng rung giỏ hàng trên navbar
        const cartIcon = $('#cart-nav-link');
        cartIcon.addClass('cart-animate');
        setTimeout(() => cartIcon.removeClass('cart-animate'), 600);

        // Hiển thị thông báo Toast góc phải mượt mà
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            showClass: { popup: 'animate__animated animate__fadeInRight' },
            hideClass: { popup: 'animate__animated animate__fadeOutRight' }
        });

        Toast.fire({
            icon: 'success',
            title: '<span style="font-size:15px">Đã thêm vào giỏ hàng thành công!</span>',
            html: '<a href="${pageContext.request.contextPath}/cart" class="btn btn-sm btn-warning mt-1 w-100" style="font-size:12px; font-weight:bold">XEM GIỎ HÀNG NGAY</a>'
        });

        // Xóa tham số URL
        if (window.history.replaceState) {
            const newUrl = window.location.pathname + window.location.search.replace(/([&?])success=add/, '');
            window.history.replaceState({}, document.title, newUrl);
        }
    }

    // --- 2. XỬ LÝ THANH TOÁN THÀNH CÔNG ---
    if (urlParams.get('orderStatus') === 'success') {
        Swal.fire({
            title: '🎉 Thanh toán thành công!',
            text: 'Cảm ơn bạn đã tin dùng Đặc Sản Quê. Đơn hàng đang được xử lý!',
            icon: 'success',
            confirmButtonText: 'Tiếp tục mua sắm',
            confirmButtonColor: '#a0522d',
            backdrop: `rgba(0,0,123,0.2) url("https://sweetalert2.github.io/images/nyan-cat.gif") left top no-repeat`
        });

        if (window.history.replaceState) {
            const newUrl = window.location.pathname;
            window.history.replaceState({}, document.title, newUrl);
        }
    }
});
</script>
<script>
//Bắt sự kiện click vào tên người dùng
$('#userDropdownLink').on('click', function(e) {
    e.preventDefault();
    e.stopPropagation(); // Ngăn chặn sự kiện nổi bọt
    $('#userDropdownContent').toggleClass('show'); // Tự thêm/xóa class show
});

// Nếu click ra ngoài menu thì tự động đóng lại cho chuyên nghiệp
$(document).on('click', function (e) {
    if (!$(e.target).closest('#userDropdownLink').length) {
        $('#userDropdownContent').removeClass('show');
    }
});</script>

</body>
</html>