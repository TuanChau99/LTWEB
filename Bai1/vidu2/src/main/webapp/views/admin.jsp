<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- ===== MAIN CONTENT (Admin Dashboard) ===== -->
<div class="container my-5">
    <div class="card shadow-lg border-0">
        <div class="card-header bg-primary text-white text-center py-3">
            <h4>Chào mừng Quản trị viên!</h4>
        </div>
        <div class="card-body text-center py-5">
            <p class="fs-5 mb-4">
                Bạn đã đăng nhập với quyền <b>Quản trị viên</b>.<br>
                Vui lòng chọn chức năng quản lý bên dưới:
            </p>

            <div class="row justify-content-center">
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/tour" class="btn btn-outline-primary w-100 py-3">
                        <i class="bi bi-map"></i> Quản lý Tour
                    </a>
                </div>
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/khachhang" class="btn btn-outline-success w-100 py-3">
                        <i class="bi bi-people"></i> Quản lý Khách hàng
                    </a>
                </div>
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/nhanvien" class="btn btn-outline-info w-100 py-3">
                        <i class="bi bi-person-badge"></i> Quản lý Nhân viên
                    </a>
                </div>
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/dattour" class="btn btn-outline-warning w-100 py-3">
                        <i class="bi bi-card-checklist"></i> Quản lý Đặt tour
                    </a>
                </div>
                <div class="col-md-3 mb-3">
                    <a href="${pageContext.request.contextPath}/admin/thanhtoan" class="btn btn-outline-danger w-100 py-3">
                        <i class="bi bi-credit-card"></i> Quản lý Thanh toán
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
