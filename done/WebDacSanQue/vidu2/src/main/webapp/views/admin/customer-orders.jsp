<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử đơn hàng</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <style>
    /* CSS FIX: Giữ tên Admin không bị to và đồng nhất khoảng cách */
.sidebar .profile h4 {
	font-size: 1.1rem !important;
	font-family: 'Merriweather Sans', sans-serif !important;
	margin: 0.5rem 0 2px 0 !important;
	font-weight: 500 !important;
	color: #fff !important;
}

.sidebar .profile small {
	font-size: 0.75rem !important;
	color: #899DC1 !important;
	display: block !important;
}</style>
</head>
<body class="bg-light">
    <div class="container mt-5">
        <div class="card shadow border-0" style="border-radius: 15px;">
            <div class="card-header bg-white py-3">
                <h4 class="mb-0 text-primary"><i class="fa fa-shopping-bag"></i> Lịch sử đơn hàng</h4>
            </div>
            <div class="card-body">
                <table class="table table-hover">
                    <thead class="thead-light">
                        <tr>
                            <th>Mã đơn</th>
                            <th>Ngày đặt</th>
                            <th>Tổng tiền</th>
                            <th class="text-center">Trạng thái</th>
                            <th class="text-center">Chi tiết</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${listOrders}" var="o">
                            <tr>
                                <td>#${o.id}</td>
                                <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td class="font-weight-bold text-danger">
                                    <fmt:formatNumber value="${o.totalPrice}" type="number"/> VNĐ
                                </td>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${o.status == 2}"><span class="badge badge-success">Thành công</span></c:when>
                                        <c:when test="${o.status == 3}"><span class="badge badge-danger">Đã hủy</span></c:when>
                                        <c:otherwise><span class="badge badge-warning">Đang xử lý</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
    <a href="${pageContext.request.contextPath}/admin/customer/orderDetail?id=${o.id}" 
   class="btn btn-sm btn-outline-info">
    <i class="fa fa-eye"></i> Xem
</a>
</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listOrders}">
                            <tr><td colspan="5" class="text-center">Khách hàng này chưa có đơn hàng nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>
                <div class="mt-3">
                    <a href="${pageContext.request.contextPath}/admin/customers" class="btn btn-secondary">
                        <i class="fa fa-arrow-left"></i> Quay lại danh sách
                    </a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>