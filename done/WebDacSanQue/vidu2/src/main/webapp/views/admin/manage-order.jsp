<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Đơn Hàng | Admin CMS</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
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
    }
        /* CSS Giao diện chung */
        body { background-color: #f4f7f6; font-family: 'Segoe UI', sans-serif; }
        
        .main-content-order { 
            margin: 30px auto; 
            background: #fff; 
            padding: 30px; 
            border-radius: 15px; 
            box-shadow: 0 10px 25px rgba(0,0,0,0.05); 
        }

        /* --- THANH LỌC TRẠNG THÁI (MỚI) --- */
        .filter-wrapper {
            display: flex;
            justify-content: center;
            gap: 12px;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
        }
        .filter-btn {
            border: 1px solid #ddd;
            background: #fff;
            padding: 8px 18px;
            border-radius: 25px;
            font-weight: 600;
            font-size: 14px;
            transition: all 0.3s;
            cursor: pointer;
            color: #555;
        }
        .filter-btn:hover { background: #f8f9fa; }
        .filter-btn.active {
            background: #007bff;
            color: #fff !important;
            border-color: #007bff;
            box-shadow: 0 4px 10px rgba(0,123,255,0.3);
        }
        .filter-btn span { margin-left: 5px; opacity: 0.7; font-size: 12px; }

        /* Định dạng Table */
        .table thead th { 
            background-color: #343a40; 
            color: white; 
            border: none; 
            text-transform: uppercase; 
            font-size: 13px;
            letter-spacing: 0.5px;
        }
        .order-row { transition: all 0.3s; }
        .order-row:hover { background-color: #fcfcfc; }

        /* Badge Trạng thái */
        .badge-status { 
            display: inline-flex;       
            align-items: center;        
            justify-content: center;     
            padding: 7px 15px;          
            border-radius: 20px; 
            font-size: 11px;            
            font-weight: 700;           
            min-width: 130px;           
            text-transform: uppercase;
        }
        .badge-status i { margin-right: 6px; }

        .bg-waiting   { background-color: #fff4e5; color: #ff9800; border: 1px solid #ffe0b2; }
        .bg-delivery  { background-color: #e3f2fd; color: #2196f3; border: 1px solid #bbdefb; }
        .bg-completed { background-color: #e8f5e9; color: #4caf50; border: 1px solid #c8e6c9; }
        .bg-cancelled { background-color: #ffebee; color: #f44336; border: 1px solid #ffcdd2; }
        
        .btn-action { border-radius: 50%; width: 32px; height: 32px; padding: 0; line-height: 32px; }
    </style>
</head>
<body>

<div class="container-fluid">
    <div class="main-content-order container">
        <h2 class="text-center mb-4 font-weight-bold" style="color: #2c3e50;">DANH SÁCH ĐƠN HÀNG</h2>
        
        <div class="filter-wrapper">
            <button class="filter-btn active" data-filter="all">Tất cả</button>
            <button class="filter-btn" data-filter="bg-waiting">
                <i class="fa fa-clock-o text-warning"></i> Chờ duyệt <span id="count-waiting">(0)</span>
            </button>
            <button class="filter-btn" data-filter="bg-delivery">
                <i class="fa fa-truck text-primary"></i> Đang giao <span id="count-delivery">(0)</span>
            </button>
            <button class="filter-btn" data-filter="bg-completed">
                <i class="fa fa-check-circle text-success"></i> Hoàn tất <span id="count-completed">(0)</span>
            </button>
            <button class="filter-btn" data-filter="bg-cancelled">
                <i class="fa fa-times-circle text-danger"></i> Đã hủy <span id="count-cancelled">(0)</span>
            </button>
        </div>

        <table class="table table-hover border-bottom" id="orderTable">
            <thead>
                <tr>
                    <th class="text-center">ID</th>
                    <th>Khách hàng</th>
                    <th>Ngày đặt</th>
                    <th>Tổng tiền</th>
                    <th class="text-center">Trạng thái</th>
                    <th class="text-center">Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listO}" var="o">
                    <tr class="order-row">
                        <td class="text-center font-weight-bold">#${o.id}</td>
                        <td><strong>${o.userName}</strong></td>
                        <td class="text-muted" style="font-size: 13px;">${o.orderDate}</td>
                        <td class="text-danger font-weight-bold">
                            <fmt:formatNumber value="${o.totalPrice}" pattern="#,###"/> VNĐ
                        </td>
                        <td class="text-center">
                            <c:choose>
                                <c:when test="${o.status == 0}">
                                    <span class="badge-status bg-waiting"><i class="fa fa-clock-o"></i> Chờ xử lý</span>
                                </c:when>
                                <c:when test="${o.status == 1}">
                                    <span class="badge-status bg-delivery"><i class="fa fa-truck"></i> Đang giao</span>
                                </c:when>
                                <c:when test="${o.status == 2}">
                                    <span class="badge-status bg-completed"><i class="fa fa-check-circle"></i> Hoàn thành</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge-status bg-cancelled"><i class="fa fa-times-circle"></i> Đã hủy</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="text-center">
                            <a href="orderDetail?oid=${o.id}" class="btn btn-sm btn-outline-primary btn-action" title="Xem chi tiết">
                                <i class="fa fa-eye"></i>
                            </a>
                            <c:if test="${o.status == 0}">
                                <button type="button" class="btn btn-sm btn-success btn-approve ml-1" data-id="${o.id}" title="Duyệt đơn">
                                    <i class="fa fa-check"></i>
                                </button>
                                <button type="button" class="btn btn-sm btn-danger btn-cancel ml-1" data-id="${o.id}" title="Hủy đơn">
                                    <i class="fa fa-times"></i>
                                </button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <div class="text-right mt-4">
            <a href="${pageContext.request.contextPath}/admin" class="btn btn-secondary rounded-pill px-4">
                <i class="fa fa-arrow-left"></i> Dashboard
            </a>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function() {
    // 1. Hàm cập nhật số lượng hiển thị trên nút lọc
    function updateCounters() {
        $("#count-waiting").text("(" + $(".badge-status.bg-waiting").length + ")");
        $("#count-delivery").text("(" + $(".badge-status.bg-delivery").length + ")");
        $("#count-completed").text("(" + $(".badge-status.bg-completed").length + ")");
        $("#count-cancelled").text("(" + $(".badge-status.bg-cancelled").length + ")");
    }
    updateCounters();

    // 2. Xử lý sự kiện click Nút Lọc
    $(".filter-btn").click(function() {
        var filter = $(this).attr("data-filter");
        $(".filter-btn").removeClass("active");
        $(this).addClass("active");

        if (filter === "all") {
            $(".order-row").show();
        } else {
            $(".order-row").hide();
            $(".order-row").each(function() {
                if ($(this).find(".badge-status").hasClass(filter)) {
                    $(this).show();
                }
            });
        }
    });

    // 3. AJAX Cập nhật trạng thái
    function updateStatus(orderId, newStatus, buttonElement) {
        var row = buttonElement.closest("tr");
        row.css("opacity", "0.5");
        
        $.ajax({
            url: "${pageContext.request.contextPath}/admin/updateStatus", 
            type: "GET",
            data: { oid: orderId, status: newStatus },
            success: function() {
                var badge = row.find(".badge-status");
                if (newStatus == 1) {
                    badge.html('<i class="fa fa-truck"></i> Đang giao').attr('class', 'badge-status bg-delivery');
                } else {
                    badge.html('<i class="fa fa-times-circle"></i> Đã hủy').attr('class', 'badge-status bg-cancelled');
                }

                // Xóa nút xử lý
                row.find(".btn-approve, .btn-cancel").remove();
                row.css("opacity", "1");
                
                // Cập nhật lại số lượng
                updateCounters();

                // Nếu đang lọc 'Chờ xử lý', đơn vừa duyệt sẽ ẩn đi luôn
                if($(".filter-btn.active").attr("data-filter") === "bg-waiting") {
                    row.fadeOut();
                }
            },
            error: function() {
                alert("Lỗi hệ thống!");
                row.css("opacity", "1");
            }
        });
    }

    $(document).on("click", ".btn-approve", function() {
        if (confirm("Duyệt đơn hàng này?")) updateStatus($(this).data("id"), 1, $(this));
    });

    $(document).on("click", ".btn-cancel", function() {
        if (confirm("Hủy đơn hàng này?")) updateStatus($(this).data("id"), 3, $(this));
    });
});
</script>
</body>
</html>