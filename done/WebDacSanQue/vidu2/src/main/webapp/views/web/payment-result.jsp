<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Kết quả giao dịch | Vip Pro Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    
    <style>
        :root {
            --success-color: #00b894;
            --fail-color: #ff7675;
        }

        body { 
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            display: flex; 
            align-items: center; 
            justify-content: center; 
            min-height: 100vh; 
            margin: 0;
            font-family: 'Segoe UI', system-ui, -apple-system, sans-serif;
        }

        .result-card { 
            background: #fff; 
            padding: 40px; 
            border-radius: 24px; 
            box-shadow: 0 20px 60px rgba(0,0,0,0.1); 
            max-width: 450px; /* Thu nhỏ chiều rộng card để nội dung cô đọng */
            width: 90%; 
            text-align: center;
            position: relative;
            z-index: 1;
        }

        .icon-box {
            width: 80px;
            height: 80px;
            margin: 0 auto 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            font-size: 40px;
        }

        .success-bg { background: rgba(0, 184, 148, 0.1); color: var(--success-color); }
        .fail-bg { background: rgba(255, 118, 117, 0.1); color: var(--fail-color); }

        h2 { font-weight: 800; color: #2d3436; font-size: 1.5rem; margin-bottom: 10px; }
        
        /* Cấu trúc mới để thông tin sát lại gần nhau */
        .order-info-box {
            background: #f8f9fa;
            border-radius: 16px;
            padding: 20px;
            margin: 25px 0;
            border: 1px solid #eee;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 10px;
        }

        .info-row:last-child { margin-bottom: 0; }

        .info-label { color: #636e72; font-size: 0.9rem; }
        .info-value { font-weight: 700; color: #2d3436; font-size: 1rem; }

        .btn-group-vipro { display: flex; gap: 12px; margin-top: 30px; }
        
        .btn-vipro {
            flex: 1;
            border-radius: 12px;
            padding: 12px;
            font-weight: 700;
            text-decoration: none;
            transition: all 0.2s;
            font-size: 0.85rem;
            text-transform: uppercase;
        }

        .btn-home { border: 2px solid #eee; color: #636e72; }
        .btn-order { background: #0d6efd; color: white; border: none; box-shadow: 0 4px 12px rgba(13, 110, 253, 0.2); }
        
        .btn-vipro:hover { transform: translateY(-2px); opacity: 0.9; color: inherit; }

        #countdown-text { font-size: 0.85rem; color: #b2bec3; margin-top: 25px; }
    </style>
</head>
<body>

    <div class="result-card animate__animated animate__zoomIn">
        <c:choose>
            <c:when test="${status == 'success'}">
                <div class="icon-box success-bg animate__animated animate__bounceIn">
                    <i class="fa-solid fa-check"></i>
                </div>
                <h2>Thanh toán thành công!</h2>
                <p class="text-muted small px-3">Cảm ơn bạn đã tin tưởng. Đơn hàng của bạn đang được hệ thống xử lý ngay bây giờ.</p>
                
                <div class="order-info-box animate__animated animate__fadeInUp">
                    <div class="info-row">
                        <span class="info-label">Mã đơn hàng:</span>
                        <span class="info-value text-primary">#${orderId}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Trạng thái:</span>
                        <span class="badge bg-success">Đã xác nhận</span>
                    </div>
                </div>
            </c:when>
            
            <c:otherwise>
                <div class="icon-box fail-bg animate__animated animate__shakeX">
                    <i class="fa-solid fa-xmark"></i>
                </div>
                <h2>Giao dịch thất bại</h2>
                <p class="text-muted small">${message}</p>
                
                <div class="order-info-box">
                    <p class="small text-danger mb-0">Vui lòng kiểm tra lại phương thức thanh toán.</p>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="btn-group-vipro">
            <a href="${pageContext.request.contextPath}/home" class="btn-vipro btn-home">
                <i class="fa-solid fa-house me-2"></i>Trang chủ
            </a>
            <a href="${pageContext.request.contextPath}/admin/manageOrder" class="btn-vipro btn-order">
                <i class="fa-solid fa-receipt me-2"></i>Đơn hàng
            </a>
        </div>

        <p id="countdown-text">
            Tự động chuyển hướng sau <span id="countdown" class="fw-bold text-dark">10</span> giây
        </p>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/canvas-confetti@1.6.0/dist/confetti.browser.min.js"></script>
    <script>
        // Hiệu ứng pháo hoa
        <c:if test="${status == 'success'}">
            confetti({ particleCount: 100, spread: 70, origin: { y: 0.6 } });
        </c:if>

        // Đếm ngược
        let seconds = 10;
        const countdownEl = document.getElementById("countdown");
        const timer = setInterval(() => {
            seconds--;
            countdownEl.textContent = seconds;
            if (seconds <= 0) {
                clearInterval(timer);
                window.location.href = "${pageContext.request.contextPath}/admin/manageOrder";
            }
        }, 1000);
    </script>
</body>
</html>