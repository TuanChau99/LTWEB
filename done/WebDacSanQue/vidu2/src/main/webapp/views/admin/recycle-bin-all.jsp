<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<div class="container-fluid" style="padding: 30px; background-color: #f4f7f6; min-height: 100vh;">
    
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 style="font-weight: 700; color: #2c3e50; margin: 0;">
                <i class="las la-trash-restore-alt" style="color: #e74c3c;"></i> Trung tâm Khôi phục
            </h2>
            <p class="text-muted" style="margin: 0;">Quản lý các mục đã xóa hoặc tạm ẩn khỏi hệ thống</p>
        </div>
        <div class="d-flex gap-2">
            <a href="${pageContext.request.contextPath}/admin/manageProduct" class="btn btn-outline-primary shadow-sm" style="border-radius: 10px; font-weight: 500;">
                <i class="las la-box"></i> Kho sản phẩm
            </a>
            <a href="${pageContext.request.contextPath}/admin/user/list" class="btn btn-outline-dark shadow-sm" style="border-radius: 10px; font-weight: 500;">
                <i class="las la-users"></i> Quản lý người dùng
            </a>
        </div>
    </div>

    <div class="card border-0 shadow-sm mb-5" style="border-radius: 15px; overflow: hidden;">
        <div class="card-header" style="background: linear-gradient(45deg, #2c3e50, #34495e); color: white; padding: 15px 25px;">
            <h5 class="mb-0"><i class="las la-boxes"></i> Sản phẩm đã xóa</h5>
        </div>
        <div class="card-body" style="padding: 25px;">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th style="width: 100px;" class="text-center">ID</th>
                            <th>Tên sản phẩm</th>
                            <th class="text-end">Giá bán</th>
                            <th class="text-center">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${deletedProducts}" var="p">
                            <tr>
                                <td class="text-center fw-bold text-muted">#${p.id}</td>
                                <td>
                                    <span style="font-weight: 600; color: #2c3e50;">${p.name}</span>
                                </td>
                                <td class="text-end fw-bold text-success">
                                    <fmt:formatNumber value="${p.price}" pattern="###,###"/> VNĐ
                                </td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/admin/product/restore?id=${p.id}" 
                                       class="btn btn-success btn-sm px-3 shadow-sm" style="border-radius: 20px;"
                                       onclick="return confirm('Khôi phục sản phẩm này về cửa hàng?')">
                                       <i class="las la-undo"></i> Khôi phục
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty deletedProducts}">
                            <tr>
                                <td colspan="4" class="text-center py-5 text-muted italic">
                                    <i class="las la-inbox fa-3x d-block mb-2"></i> Thùng rác sản phẩm trống
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px; overflow: hidden;">
        <div class="card-header" style="background: linear-gradient(45deg, #d35400, #e67e22); color: white; padding: 15px 25px;">
            <h5 class="mb-0"><i class="las la-user-shield"></i> Tài khoản đã khóa</h5>
        </div>
        <div class="card-body" style="padding: 25px;">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th style="width: 100px;" class="text-center">ID</th>
                            <th>Tài khoản</th>
                            <th>Họ và Tên</th>
                            <th>Email liên hệ</th>
                            <th class="text-center">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${deletedUsers}" var="u">
                            <tr>
                                <td class="text-center fw-bold text-muted">#${u.id}</td>
                                <td class="fw-bold text-primary">${u.username}</td>
                                <td>${u.fullname}</td>
                                <td class="text-muted">${u.email}</td>
                                <td class="text-center">
                                    <a href="${pageContext.request.contextPath}/admin/user/restore?id=${u.id}" 
                                       class="btn btn-warning btn-sm px-3 shadow-sm text-white" style="border-radius: 20px;"
                                       onclick="return confirm('Mở khóa cho tài khoản này?')">
                                       <i class="las la-unlock"></i> Mở khóa
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty deletedUsers}">
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted italic">
                                    <i class="las la-user-check fa-3x d-block mb-2"></i> Không có người dùng nào bị khóa
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>		