<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="footer mt-5">
    <div class="footer-top py-5">
        <div class="container">
            <div class="row gy-4">

                <div class="col-lg-4 col-md-6">
                    <h4 class="footer-logo">
                        <i class="fa-solid fa-seedling"></i> Đặc Sản Quê
                    </h4>
                    <p>
                        Đặc Sản Quê là cầu nối giúp bà con giới thiệu và quảng bá những nông sản, đặc sản tinh hoa của quê hương đến mọi miền đất nước, đảm bảo chất lượng và hương vị truyền thống.
                    </p>
                </div>

                <div class="col-lg-4 col-md-6">
                    <h5 class="fw-bold mb-3 text-uppercase">Liên kết nhanh</h5>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/home"><i class="fa fa-chevron-right me-2"></i>Trang chủ</a></li>
                        <li><a href="${pageContext.request.contextPath}/product"><i class="fa fa-chevron-right me-2"></i>Đặc Sản</a></li>
                        <li><a href="${pageContext.request.contextPath}/about"><i class="fa fa-chevron-right me-2"></i>Giới thiệu về Quê</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact"><i class="fa fa-chevron-right me-2"></i>Liên hệ</a></li>
                        <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fa fa-chevron-right me-2"></i>Trang quản trị</a></li>
                    </ul>
                </div>

                <div class="col-lg-4 col-md-6">
                    <h5 class="fw-bold mb-3 text-uppercase">Thông tin liên hệ</h5>
                    <ul class="list-unstyled">
                        <li><i class="fa fa-map-marker-alt me-2"></i> Địa chỉ Trang trại/Cơ sở sản xuất mẫu</li>
                        <li><i class="fa fa-phone me-2"></i> (+84) 777.550.500</li>
                        <li><i class="fa fa-envelope me-2"></i> hotro@dacsanque.vn</li>
                        <li><i class="fa fa-clock me-2"></i> Hỗ trợ: 8h00 - 17h00 (T2 - T7)</li>
                    </ul>

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

    <div class="footer-bottom text-center py-3">
        <p class="mb-0">
            © 2025 Đặc Sản Quê. Phát triển bởi <strong>Trần Châu Tuấn - MSSV-24TX810052</strong>.
        </p>
    </div>
</footer>

<style>
    /* Màu sắc chủ đạo: Nâu Đất */
    .footer {
        /* Tông màu ấm áp, đồng bộ với Header */
        background: linear-gradient(90deg, #a0522d, #cd853f);
        color: white;
        font-size: 0.95rem;
    }

    .footer a {
        color: white;
        text-decoration: none;
        transition: color 0.3s;
    }

    .footer a:hover {
        color: #fff3e0; /* Màu Vàng Kem Sáng */
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
        color: #a0522d; /* Màu Nâu Đất */
    }

    .footer-bottom {
        background-color: rgba(0, 0, 0, 0.1);
        font-size: 0.9rem;
    }
</style>