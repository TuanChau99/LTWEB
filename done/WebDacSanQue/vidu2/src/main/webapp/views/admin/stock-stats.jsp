<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <title>Thống kê tồn kho</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
    /* 1. KHÔI PHỤC ĐỊNH DẠNG CHUẨN CHO SIDEBAR (Phòng trường hợp bị ghi đè) */
    .sidebar .profile h4 {
        font-size: 1.1rem !important;
    	font-family: 'Merriweather Sans', sans-serif !important;
    	/* Chỉnh margin-bottom về 2px hoặc 0px để thu hẹp khoảng cách với chữ bên dưới */
    	margin: 0.5rem 0 2px 0 !important; 
    	font-weight: 500 !important;
    	color: #fff !important;
    	text-transform: none !important;
    	line-height: 1.5 !important; /* Đảm bảo chiều cao dòng không quá lớn */
    }
    .sidebar .profile small {
        font-size: 0.75rem !important;
    	color: #899DC1 !important;
    	display: block !important; /* Đưa xuống dòng mới */
 	    line-height: 1 !important;
    }</style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <h2 class="mb-4 text-teal"><i class="fa fa-bar-chart"></i> BÁO CÁO KHO HÀNG</h2>
    
    <div class="row mb-4">
        <div class="col-md-6">
            <div class="card p-3 shadow-sm border-0">
                <h5>Tổng sản phẩm trong kho: <b class="text-primary">${totalStock}</b></h5>
                <h5>Tổng giá trị vốn: <b class="text-danger"><fmt:formatNumber value="${totalValue}"/> VNĐ</b></h5>
            </div>
        </div>
        <div class="col-md-6">
            <canvas id="stockChart" style="max-height: 200px;"></canvas>
        </div>
    </div>

    <div class="card shadow-sm border-0">
        <div class="card-header bg-danger text-white">SẢN PHẨM CẦN NHẬP THÊM (STOCK < 10)</div>
        <div class="card-body">
            <table class="table">
                <thead><tr><th>ID</th><th>Tên</th><th>Tồn hiện tại</th></tr></thead>
                <tbody>
                    <c:forEach items="${lowStockList}" var="l">
                        <tr>
                            <td>#${l.id}</td>
                            <td>${l.name}</td>
                            <td class="text-danger font-weight-bold">${l.stock}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
// Vẽ biểu đồ top 5 sản phẩm tồn nhiều nhất
const ctx = document.getElementById('stockChart').getContext('2d');
new Chart(ctx, {
    type: 'pie',
    data: {
        labels: [<c:forEach items="${allProduct}" var="p" end="4">'${p.name}',</c:forEach>],
        datasets: [{
            data: [<c:forEach items="${allProduct}" var="p" end="4">${p.stock},</c:forEach>],
            backgroundColor: ['#22BAA0', '#34425A', '#F39C12', '#E74C3C', '#9B59B6']
        }]
    }
});
</script>
</body>
</html>