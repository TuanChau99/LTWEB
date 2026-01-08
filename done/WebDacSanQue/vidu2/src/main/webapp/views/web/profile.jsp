<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<style>
    body { background-color: #f8f9fa; }
    .profile-container { margin-top: 30px; margin-bottom: 50px; }
    .card-profile { border: none; border-radius: 15px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); background: #fff; }
    .profile-sidebar { background: #fff; padding: 25px; border-radius: 15px; text-align: center; border: none; }
    .avatar-lg { width: 100px; height: 100px; border-radius: 50%; object-fit: cover; border: 3px solid #198754; }
    .nav-pills .nav-link { color: #495057; font-weight: 500; border-radius: 10px; margin-bottom: 8px; transition: 0.3s; }
    .nav-pills .nav-link:hover { background: #e8f5e9; color: #198754; }
    .nav-pills .nav-link.active { background-color: #198754; color: white; }
    .form-label { font-weight: 600; color: #555; }
    .form-control { border-radius: 10px; padding: 10px; border: 1px solid #dee2e6; }
    .btn-update { background: #198754; color: white; border-radius: 10px; padding: 12px 35px; border: none; font-weight: 600; transition: 0.3s; }
    .btn-update:hover { background: #146c43; transform: translateY(-2px); }
    .badge { padding: 8px 12px; border-radius: 8px; }
</style>

<div class="container profile-container">
    <div class="row g-4">
        <div class="col-lg-4">
            <div class="profile-sidebar shadow-sm">
                <c:choose>
        <%-- TRƯỜNG HỢP 1: Nếu có tên file ảnh đã upload --%>
        <c:when test="${not empty sessionScope.account.avatar && sessionScope.account.avatar != 'default-user.png'}">
            <c:url value="/image?fname=${sessionScope.account.avatar}" var="imgUrl"/>
            <img src="${imgUrl}" class="avatar-lg mb-3 shadow" 
                 onerror="this.src='https://ui-avatars.com/api/?name=${sessionScope.account.fullname}&background=198754&color=fff'">
        </c:when>

        <%-- TRƯỜNG HỢP 2: Nếu là ảnh mặc định hệ thống --%>
        <c:when test="${sessionScope.account.avatar == 'default-user.png'}">
            <img src="<c:url value='/assets/img/default-user.png'/>" class="avatar-lg mb-3 shadow">
        </c:when>

        <%-- TRƯỜNG HỢP 3: Nếu NULL hoặc trống --%>
        <c:otherwise>
            <img src="https://ui-avatars.com/api/?name=${sessionScope.account.fullname}&background=198754&color=fff" 
                 class="avatar-lg mb-3 shadow">
        </c:otherwise>
    </c:choose>
    <h4 class="fw-bold">${sessionScope.account.fullname}</h4>
    <p class="text-muted mb-4">Thành viên từ <fmt:formatDate value="${sessionScope.account.createddate}" pattern="MM/yyyy"/></p>
                <div class="nav flex-column nav-pills">
                    <a class="nav-link active" href="#"><i class="fa fa-user me-2"></i> Hồ sơ cá nhân</a>
                    <a class="nav-link" href="${pageContext.request.contextPath}/cart"><i class="fa fa-shopping-cart me-2"></i> Giỏ hàng</a>
                    <a class="nav-link text-danger mt-3" href="logout"><i class="fa fa-sign-out-alt me-2"></i> Đăng xuất</a>
                </div>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="card card-profile p-4 shadow-sm mb-4">
                <h4 class="mb-4 fw-bold text-success border-bottom pb-2">Thông tin tài khoản</h4>
                
                <c:if test="${not empty message}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fa fa-check-circle me-2"></i> ${message}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <form action="profile" method="post">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label small text-uppercase">Tên đăng nhập</label>
                            <input type="text" class="form-control bg-light" value="${sessionScope.account.username}" disabled>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small text-uppercase">Họ và tên</label>
                            <input type="text" name="fullname" class="form-control" value="${sessionScope.account.fullname}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small text-uppercase">Địa chỉ Email</label>
                            <input type="email" name="email" class="form-control" value="${sessionScope.account.email}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label small text-uppercase">Số điện thoại</label>
                            <input type="text" name="phone" class="form-control" value="${sessionScope.account.phone}">
                        </div>
                        <div class="col-12">
                            <label class="form-label small text-uppercase">Địa chỉ nhận hàng</label>
                            <textarea name="address" class="form-control" rows="2">${sessionScope.account.address}</textarea>
                        </div>
                        <div class="col-12 text-end mt-4">
                            <button type="submit" class="btn-update shadow-sm">
                                <i class="fa fa-save me-2"></i> Lưu thay đổi
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <div class="card card-profile p-4 shadow-sm">
                <h4 class="mb-4 fw-bold text-success border-bottom pb-2"><i class="fa fa-history me-2"></i>Lịch sử đơn hàng</h4>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead class="table-light">
                            <tr class="small text-uppercase">
                                <th>Mã đơn</th>
                                <th>Ngày đặt</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="o" items="${listOrders}">
                                <tr class="align-middle">
                                    <td class="fw-bold text-primary">#${o.id}</td>
                                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td class="fw-bold text-danger">
                                        <fmt:formatNumber value="${o.totalPrice}" pattern="#,###"/> VNĐ
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${o.status == 0}"><span class="badge bg-warning text-dark">Chờ duyệt</span></c:when>
                                            <c:when test="${o.status == 1}"><span class="badge bg-info">Đang giao</span></c:when>
                                            <c:when test="${o.status == 2}"><span class="badge bg-success">Đã giao</span></c:when>
                                            <c:otherwise><span class="badge bg-danger">Đã hủy</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/order-detail?id=${o.id}" 
   class="btn btn-sm btn-light border rounded-pill px-3">
    Chi tiết
</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty listOrders}">
                                <tr>
                                    <td colspan="5" class="text-center py-5 text-muted">
                                        <i class="fa fa-box-open d-block mb-2 fs-2"></i>
                                        Bạn chưa có đơn hàng nào.
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>