<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<style>
    /* BỘ MÀU SỬA LẠI CHO ĐỒNG BỘ WEB CỦA BẠN */
    :root {
        --web-brown: #a0522d;      /* Màu nâu tiêu đề chính */
        --web-accent: #cd853f;     /* Màu nâu nhạt/cam cho tiêu đề phụ */
        --web-green: #5cb85c;      /* Màu xanh nút bấm đúng chuẩn web */
        --web-text: #333333;       /* Màu chữ nội dung dễ đọc */
        --web-bg: #fdfaf7;         /* Màu nền kem đồng bộ */
    }

    /* ĐỔI FONT CHỮ SANG HỆ THỐNG HIỆN ĐẠI */
    body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: var(--web-text); background-color: #fff; }

    /* BỐ CỤC CHÍNH GIỮ NGUYÊN */
    .detail-container { padding: 40px 0; }
    
    .product-gallery {
        position: sticky;
        top: 100px;
        background: var(--web-bg); /* Đổi sang màu kem */
        border-radius: 30px;
        padding: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        min-height: 450px;
        border: 1px solid #f1f1f1;
    }
    
    .product-gallery img {
        max-height: 400px;
        object-fit: contain;
        filter: drop-shadow(0 15px 30px rgba(0,0,0,0.08));
        transition: transform 0.8s cubic-bezier(0.2, 0, 0, 1);
    }
    .product-gallery:hover img { transform: scale(1.05); }

    /* THÔNG TIN SẢN PHẨM - ĐỔI MÀU CHỮ */
    .product-category {
        text-transform: uppercase;
        letter-spacing: 1.5px;
        font-weight: 700;
        font-size: 0.8rem;
        color: var(--web-accent); /* Đổi sang màu cam nhấn */
        display: block;
        margin-bottom: 10px;
    }

    .product-name { font-size: 2.8rem; font-weight: 800; color: var(--web-brown); line-height: 1.2; margin-bottom: 15px; }

    .price-text { font-size: 2.2rem; font-weight: 700; color: #c01508; letter-spacing: -0.5px; margin-bottom: 25px; }

    /* NÚT BẤM MÀU XANH THEO WEB */
    .btn-vip-add {
        background: var(--web-green);
        color: #fff;
        border: none;
        padding: 18px 40px;
        border-radius: 50px; /* Bo tròn hơn giống nút web */
        font-weight: 700;
        font-size: 1rem;
        transition: all 0.3s;
        width: 100%;
        margin-bottom: 30px;
        text-transform: uppercase;
    }
    .btn-vip-add:hover { background: #4cae4c; transform: translateY(-2px); color: #fff; box-shadow: 0 8px 20px rgba(92, 184, 92, 0.3); }

    /* TABS GIỮ NGUYÊN KIỂU APPLE NHƯNG ĐỔI MÀU ACTIVE */
    .nav-tabs-vip {
        background: #f1f1f1;
        padding: 5px;
        border-radius: 15px;
        border: none;
        display: inline-flex;
        margin-bottom: 30px;
    }
    .nav-tabs-vip .nav-link {
        border: none !important;
        color: #888;
        font-weight: 700;
        padding: 10px 30px;
        border-radius: 12px !important;
        font-size: 0.9rem;
    }
    .nav-tabs-vip .nav-link.active {
        background: #fff !important;
        color: var(--web-brown) !important;
        box-shadow: 0 4px 10px rgba(0,0,0,0.05);
    }

    /* CARD SẢN PHẨM GỢI Ý */
    .luxury-card {
        border: 1px solid #eee !important;
        border-radius: 20px;
        background: #fff;
        padding: 15px;
        transition: 0.4s;
    }
    .luxury-card:hover { transform: translateY(-8px); box-shadow: 0 15px 30px rgba(160, 82, 45, 0.1) !important; }
    
    .luxury-card .img-box {
        background: var(--web-bg);
        border-radius: 15px;
        aspect-ratio: 1/1;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12px;
    }
    
    .luxury-card h6 { font-weight: 700; color: var(--web-text); font-size: 1rem; }
    .luxury-card .related-price { color: #c01508; font-weight: 700; }

</style>

<div class="container detail-container">
    <nav class="mb-4">
        <ol class="breadcrumb bg-transparent p-0 small">
            <li class="breadcrumb-item"><a href="home" class="text-decoration-none" style="color: var(--web-accent);">Trang chủ</a></li>
            <li class="breadcrumb-item active fw-bold">${product.name}</li>
        </ol>
    </nav>

    <div class="row g-5">
        <div class="col-lg-6">
            <div class="product-gallery shadow-sm">
                <img src="${product.image}" class="img-fluid" alt="${product.name}">
            </div>
        </div>

        <div class="col-lg-6 ps-lg-5 d-flex flex-column justify-content-center">
            <span class="product-category">Đặc sản ${product.title}</span>
            <h1 class="product-name">${product.name}</h1>
            
            <div class="price-text">
                <fmt:formatNumber value="${product.price}" pattern="#,###"/> <small>VNĐ</small>
            </div>

            <p class="text-muted fs-5 mb-4 lh-base">
                Sản phẩm đại diện cho tinh hoa vùng đất <strong>${product.title}</strong>, được chế biến theo phương pháp truyền thống nhằm giữ trọn hương vị đặc trưng.
            </p>

            <a href="cart?action=add&id=${product.id}&success=add" class="btn btn-vip-add shadow">
                <i class="fa fa-cart-plus me-2"></i> Thêm vào giỏ hàng ngay
            </a>

            <div class="mt-2 py-4 border-top">
                <div class="row g-4">
                    <div class="col-6">
                        <span class="text-muted small d-block mb-1">Vùng miền</span>
                        <span class="fw-bold" style="color: var(--web-brown);">${product.title}</span>
                    </div>
                    <div class="col-6">
                        <span class="text-muted small d-block mb-1">Mã định danh</span>
                        <span class="fw-bold">#SP-${product.id}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="mt-5 pt-4 text-center">
        <ul class="nav nav-tabs-vip" id="vipTab" role="tablist">
            <li class="nav-item">
                <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#desc-pane" type="button">Chi tiết sản phẩm</button>
            </li>
            <li class="nav-item">
                <button class="nav-link" data-bs-toggle="tab" data-bs-target="#spec-pane" type="button">Thông số & Bảo quản</button>
            </li>
        </ul>

        <div class="tab-content text-start mt-4" id="vipTabContent">
            <div class="tab-pane fade show active mx-auto p-4 rounded-4 bg-white border" id="desc-pane" style="max-width: 950px;">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h4 class="fw-bold mb-3" style="color: var(--web-brown);">Câu chuyện sản phẩm</h4>
                        <p class="fs-6 text-muted lh-lg">${product.description}</p>
                    </div>
                    <div class="col-md-4 text-center">
                        <div class="p-4 rounded-4 border-dashed border-2" style="border: 2px dashed #dec9b1; background: var(--web-bg);">
                            <i class="fa fa-award fs-1 mb-3" style="color: var(--web-accent);"></i>
                            <h6 class="fw-bold">Chất lượng loại 1</h6>
                            <p class="small text-muted mb-0">Cam kết không chất bảo quản, an toàn tuyệt đối cho sức khỏe.</p>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="tab-pane fade" id="spec-pane">
                <div class="mx-auto bg-white p-4 rounded-4 border" style="max-width: 700px;">
                    <table class="table table-borderless m-0">
                        <tr class="border-bottom">
                            <td class="py-3 text-muted">Vùng nguyên liệu</td>
                            <td class="py-3 text-end fw-bold">${product.title}</td>
                        </tr>
                        <tr class="border-bottom">
                            <td class="py-3 text-muted">Ngày đóng gói</td>
                            <td class="py-3 text-end fw-bold">Xem trên bao bì</td>
                        </tr>
                        <tr>
                            <td class="py-3 text-muted">Hướng dẫn bảo quản</td>
                            <td class="py-3 text-end fw-bold text-brown">Ngăn mát tủ lạnh</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<c:if test="${not empty relatedProducts}">
    <div class="py-5" style="background-color: var(--web-bg); margin-top: 60px;">
        <div class="container py-4">
            <h3 class="fw-bold mb-5 text-center" style="color: var(--web-brown);">Có thể bạn cũng thích</h3>
            <div class="row g-4">
                <c:forEach items="${relatedProducts}" var="p" end="3">
                    <div class="col-6 col-md-3">
                        <div class="luxury-card shadow-sm">
                            <a href="product-detail?id=${p.id}" class="text-decoration-none text-dark">
                                <div class="img-box">
                                    <img src="${p.image}" class="img-fluid" style="max-height: 180px;">
                                </div>
                                <h6 class="text-truncate">${p.name}</h6>
                                <div class="related-price"><fmt:formatNumber value="${p.price}" pattern="#,###"/> đ</div>
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</c:if>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
</script>