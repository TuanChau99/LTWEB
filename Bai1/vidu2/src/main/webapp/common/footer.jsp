<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- ======= Footer Section ======= -->
<footer class="footer mt-5">
    <div class="footer-top py-5">
        <div class="container">
            <div class="row gy-4">

                <!-- Cột 1: Logo và giới thiệu -->
                <div class="col-lg-4 col-md-6">
                    <h4 class="footer-logo">
                        <i class="fa-solid fa-plane-departure"></i> TravelTour
                    </h4>
                    <p>
                        TravelTour là nền tảng du lịch hàng đầu, giúp bạn khám phá những hành trình tuyệt vời khắp Việt Nam và thế giới.
                    </p>
                </div>

                <!-- Cột 2: Liên kết nhanh -->
                <div class="col-lg-4 col-md-6">
                    <h5 class="fw-bold mb-3 text-uppercase">Liên kết nhanh</h5>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/home"><i class="fa fa-chevron-right me-2"></i>Trang chủ</a></li>
                        <li><a href="${pageContext.request.contextPath}/tour"><i class="fa fa-chevron-right me-2"></i>Tour du lịch</a></li>
                        <li><a href="${pageContext.request.contextPath}/about"><i class="fa fa-chevron-right me-2"></i>Giới thiệu</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact"><i class="fa fa-chevron-right me-2"></i>Liên hệ</a></li>
                    </ul>
                </div>

                <!-- Cột 3: Liên hệ -->
                <div class="col-lg-4 col-md-6">
                    <h5 class="fw-bold mb-3 text-uppercase">Liên hệ</h5>
                    <ul class="list-unstyled">
                        <li><i class="fa fa-map-marker-alt me-2"></i> 123 Đường Nguyễn Huệ, Quận 1, TP.HCM</li>
                        <li><i class="fa fa-phone me-2"></i> (+84) 123 456 789</li>
                        <li><i class="fa fa-envelope me-2"></i> support@traveltour.vn</li>
                    </ul>

                    <!-- Mạng xã hội -->
                    <div class="mt-3">
                        <a href="#" class="social-link"><i class="fab fa-facebook-f"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-instagram"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-youtube"></i></a>
                        <a href="#" class="social-link"><i class="fab fa-twitter"></i></a>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!-- Dòng bản quyền -->
    <div class="footer-bottom text-center py-3">
        <p class="mb-0">© 2025 TravelTour. Thiết kế bởi <strong>Nhóm Du Lịch JSP</strong>.</p>
    </div>
</footer>

<!-- ======= Footer Styles ======= -->
<style>
    .footer {
        background: linear-gradient(90deg, #007bff, #00c6ff);
        color: white;
        font-size: 0.95rem;
    }

    .footer a {
        color: white;
        text-decoration: none;
        transition: color 0.3s;
    }

    .footer a:hover {
        color: #ffe082;
    }

    .footer-logo {
        font-weight: 700;
        font-size: 1.5rem;
        margin-bottom: 15px;
        color: white;
    }

    .social-link {
        display: inline-block;
        width: 35px;
        height: 35px;
        line-height: 35px;
        text-align: center;
        border-radius: 50%;
        background: rgba(255,255,255,0.2);
        color: white;
        margin-right: 10px;
        transition: 0.3s;
    }

    .social-link:hover {
        background: white;
        color: #0d6efd;
    }

    .footer-bottom {
        background-color: rgba(0, 0, 0, 0.1);
        font-size: 0.9rem;
    }
</style>
