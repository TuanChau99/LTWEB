<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chi Tiết Đơn Hàng</title>
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
}
        .detail-card { background: #fff; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); overflow: hidden; margin-top: 30px; }
        .detail-header { background-color: #22BAA0; color: white; padding: 15px 25px; }
        .info-section { background-color: #f8f9fa; border-bottom: 1px solid #eee; padding: 20px 25px; }
        .info-label { color: #888; font-size: 0.75rem; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 5px; }
        .info-value { color: #34425A; font-weight: 700; font-size: 1rem; }
        .product-img { width: 65px; height: 65px; object-fit: cover; border-radius: 8px; border: 1px solid #eee; }
        .price-text { color: #E74C3C; font-weight: bold; }
        tfoot tr td { border-top: 2px solid #22BAA0 !important; vertical-align: middle !important; }
    </style>
</head>
<body class="bg-light">
    <div class="container pb-5">
        <div class="detail-card border-0">
            <div class="detail-header d-flex justify-content-between align-items-center">
                <h4 class="mb-0"><i class="fa fa-file-text-o"></i> CHI TIẾT ĐƠN HÀNG #${orderId}</h4>
                <span class="badge badge-light p-2">
                    Ngày đặt: <fmt:formatDate value="${orderInfo.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                </span>
            </div>
            
            <div class="info-section">
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
                ${empty orderInfo.phone ? 'Chưa cập nhật' : orderInfo.phone}
            </div>
        </div>
        <div class="col-md-4 text-right">
            <div class="info-label">Trạng thái</div>
            <div class="info-value">
                <c:choose>
                    <c:when test="${orderInfo.status == 2}"><span class="badge badge-success">Thành công</span></c:when>
                    <c:when test="${orderInfo.status == 3}"><span class="badge badge-danger">Đã hủy</span></c:when>
                    <c:otherwise><span class="badge badge-warning">Đang xử lý</span></c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    
    <div class="row mt-3 pt-3 border-top">
        <div class="col-md-12">
            <div class="info-label"><i class="fa fa-map-marker text-danger"></i> Địa chỉ giao hàng</div>
            <div class="info-value text-muted" style="font-weight: 500;">
                ${empty orderInfo.address ? 'Chưa cung cấp địa chỉ giao hàng' : orderInfo.address}
            </div>
        </div>
    </div>
</div>

            <div class="p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="thead-light">
                            <tr>
                                <th class="text-center">Ảnh</th>
                                <th>Sản phẩm</th>
                                <th class="text-center">Giá mua</th>
                                <th class="text-center">Số lượng</th>
                                <th class="text-right">Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="totalItems" value="0" />
                            <c:set var="totalMoney" value="0" />
                            <c:forEach items="${listD}" var="d">
                                <tr>
                                    <td class="text-center">
                                        <img src="${d.productImage}" class="product-img" alt="${d.productName}">
                                    </td>
                                    <td>
                                        <div class="font-weight-bold text-dark">${d.productName}</div>
                                        <small class="text-muted">ID Sản phẩm: #${d.productId}</small>
                                    </td>
                                    <td class="text-center"><fmt:formatNumber value="${d.price}" type="number"/> $</td>
                                    <td class="text-center">
                                        <span class="badge badge-light border px-3 py-2">${d.quantity}</span>
                                    </td>
                                    <td class="text-right price-text">
                                        <fmt:formatNumber value="${d.price * d.quantity}" type="number"/> $
                                    </td>
                                </tr>
                                <c:set var="totalItems" value="${totalItems + d.quantity}" />
                                <c:set var="totalMoney" value="${totalMoney + (d.price * d.quantity)}" />
                            </c:forEach>
                        </tbody>
                        <tfoot class="bg-light">
                            <tr>
                                <td colspan="3" class="text-right font-weight-bold">TỔNG CỘNG:</td>
                                <td class="text-center">
                                    <span class="font-weight-bold text-info">${totalItems}</span> món
                                </td>
                                <td class="text-right">
                                    <span class="text-danger font-weight-bold" style="font-size: 1.3rem;">
                                        <fmt:formatNumber value="${totalMoney}" type="number"/> $
                                    </span>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>

                <div class="mt-4 text-right">
                    <button onclick="window.history.back()" class="btn btn-secondary px-4 shadow-sm" style="background-color: #34425A; border: none;">
                        <i class="fa fa-arrow-left"></i> Quay lại
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>