<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<title>Nhập thông tin</title>

<!-- Bootstrap CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">

<style>
    body {
        background-color: #f8f9fa;
    }
    .info-container {
        max-width: 450px;
        margin: 60px auto;
        padding: 25px;
        background: #fff;
        border-radius: 10px;
        box-shadow: 0px 4px 10px rgba(0,0,0,0.1);
    }
    .info-container h3 {
        text-align: center;
        margin-bottom: 20px;
        font-weight: 600;
    }
    .input-group-text {
        background-color: #f1f1f1;
    }
    .btn-primary {
        width: 100%;
    }
</style>
</head>
<body>
<div class="info-container">
    
    <h3><i class="fa fa-plane-departure"></i> Các Tour Du Lịch Nổi Bật</h3>

    <div class="row g-4">
        <div class="col-md-4">
            <div class="card tour-card">
                <img src="https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=900&q=80" alt="Phú Quốc">
                <div class="card-body">
                    <h5 class="card-title">Tour Phú Quốc 3N2Đ</h5>
                    <p class="card-text text-muted">Khám phá đảo ngọc Phú Quốc, tận hưởng biển xanh và hải sản tươi ngon.</p>
                    <a href="#" class="btn btn-primary btn-sm"><i class="fa fa-info-circle"></i> Xem chi tiết</a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card tour-card">
                <img src="https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=900&q=80" alt="Hà Giang">
                <div class="card-body">
                    <h5 class="card-title">Tour Hà Giang 4N3Đ</h5>
                    <p class="card-text text-muted">Hành trình chinh phục đèo Mã Pí Lèng, ngắm cao nguyên đá Đồng Văn hùng vĩ.</p>
                    <a href="#" class="btn btn-primary btn-sm"><i class="fa fa-info-circle"></i> Xem chi tiết</a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card tour-card">
                <img src="https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=900&q=80" alt="Đà Nẵng - Hội An">
                <div class="card-body">
                    <h5 class="card-title">Tour Đà Nẵng - Hội An 3N2Đ</h5>
                    <p class="card-text text-muted">Tham quan phố cổ Hội An, check-in Cầu Rồng và biển Mỹ Khê.</p>
                    <a href="#" class="btn btn-primary btn-sm"><i class="fa fa-info-circle"></i> Xem chi tiết</a>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/tour" class="btn btn-success btn-lg">
            <i class="fa fa-map-marked-alt"></i> Xem thêm các tour khác
        </a>
    </div>
</div>

</body>
</html>
