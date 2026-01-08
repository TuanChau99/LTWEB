<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Chi tiết đơn hàng</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <style>/* CSS FIX: Giữ tên Admin không bị to và đồng nhất khoảng cách */
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
}
        .detail-order-container { margin-top: 30px; margin-bottom: 50px; }
        .card { border-radius: 12px; overflow: hidden; border: none; box-shadow: 0 5px 15px rgba(0,0,0,0.08); }
        .card-header { background-color: #22BAA0 !important; color: white; padding: 15px 25px; }
        .card-header h4 { margin: 0; font-size: 1.2rem; font-weight: 600; text-transform: uppercase; }
        
        /* Phần thông tin khách hàng mới thêm */
        .info-bar { background-color: #fcfcfc; border-bottom: 1px solid #eee; padding: 20px 25px; }
        .info-label { color: #888; font-size: 0.7rem; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 3px; }
        .info-value { color: #34425A; font-weight: 700; font-size: 0.95rem; }
        
        .table th { background-color: #f8f9fa; text-align: center; vertical-align: middle; font-size: 14px; color: #34425A; }
        .table td { vertical-align: middle; text-align: center; font-size: 15px; }
        .img-product { width: 60px; height: 60px; object-fit: cover; border-radius: 5px; border: 1px solid #ddd; }
    </style>
</head>
<body class="bg-light">

    <div class="container detail-order-container">
        <div class="card shadow-sm">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h4><i class="fa fa-file-text-o"></i> CHI TIẾT ĐƠN HÀNG #${orderInfo.id}</h4>
                <span class="badge badge-light">
                    Ngày đặt: <fmt:formatDate value="${orderInfo.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                </span>
            </div>

            <div class="info-bar">
                <div class="row">
                    <div class="col-md-4 border-right">
                        <div class="info-label">Khách hàng</div>
                        <div class="info-value text-primary">
                            <i class="fa fa-user-circle"></i> ${orderInfo.userName}
                        </div>
                    </div>
                    <div class="col-md-4 border-right text-center">
                        <div class="info-label">Số điện thoại</div>
                        <div class="info-value">
                            <i class="fa fa-phone text-muted"></i> 
                            ${empty orderInfo.phone ? 'N/A' : orderInfo.phone}
                        </div>
                    </div>
                    <div class="col-md-4 text-right">
                        <div class="info-label">Trạng thái</div>
                        <div class="info-value">
                            <c:choose>
                                <c:when test="${orderInfo.status == 2}"><span class="badge badge-success">Hoàn thành</span></c:when>
                                <c:when test="${orderInfo.status == 3}"><span class="badge badge-danger">Đã hủy</span></c:when>
                                <c:when test="${orderInfo.status == 1}"><span class="badge badge-info">Đang giao</span></c:when>
                                <c:otherwise><span class="badge badge-warning">Đang xử lý</span></c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="row mt-3 pt-3 border-top">
                    <div class="col-12">
                        <div class="info-label"><i class="fa fa-map-marker text-danger"></i> Địa chỉ giao hàng</div>
                        <div class="info-value text-muted" style="font-weight: 500;">
                            ${empty orderInfo.address ? 'Chưa cung cấp địa chỉ' : orderInfo.address}
                        </div>
                    </div>
                </div>
            </div>

            <div class="card-body p-4">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover">
                        <thead>
                            <tr>
                                <th style="width: 100px;">Ảnh</th>
                                <th>Sản phẩm</th>
                                <th>Giá mua</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="totalItems" value="0" />
                            <c:forEach items="${listD}" var="d">
                                <tr>
                                    <td>
                                        <img src="${d.productImage}" class="img-product" alt="${d.productName}">
                                    </td>
                                    <td class="text-left">
                                        <strong>${d.productName}</strong><br>
                                        <small class="text-muted">ID: #${d.productId}</small>
                                    </td>
                                    <td><fmt:formatNumber value="${d.price}" type="number" pattern="#,###"/> VNĐ</td>
                                    <td><span class="badge badge-light border" style="font-size: 14px;">${d.quantity}</span></td>
                                    <td class="text-danger font-weight-bold">
                                        <fmt:formatNumber value="${d.price * d.quantity}" type="number" pattern="#,###"/> VNĐ
                                    </td>
                                </tr>
                                <c:set var="totalItems" value="${totalItems + d.quantity}" />
                            </c:forEach>
                        </tbody>
                        <tfoot class="bg-light">
                            <tr>
                                <td colspan="3" class="text-right font-weight-bold">TỔNG CỘNG:</td>
                                <td class="text-center font-weight-bold text-info">${totalItems} món</td>
                                <td class="text-right">
                                    <span class="text-danger font-weight-bold" style="font-size: 1.2rem;">
                                        <fmt:formatNumber value="${orderInfo.totalPrice}" type="number" pattern="#,###"/> VNĐ
                                    </span>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

                <div class="mt-4 d-flex justify-content-between align-items-center">
                    <p class="text-muted small">* Dữ liệu chi tiết dựa trên thời điểm đặt hàng.</p>
                    <a href="${pageContext.request.contextPath}/admin/manageOrder" class="btn btn-secondary shadow-sm" style="background-color: #34425A; border: none;">
                        <i class="fa fa-arrow-left"></i> Quay lại danh sách
                    </a>
                </div>
            </div>
        </div>
    </div>

</body>
</html>