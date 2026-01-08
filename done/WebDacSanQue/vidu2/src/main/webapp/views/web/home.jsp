<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<style>
    :root {
        --primary-color: #198754; /* Màu xanh lá chuẩn */
        --bg-light: #f8f9fa;
        --text-dark: #333;
    }

    body { background-color: var(--bg-light); font-family: 'Segoe UI', Roboto, sans-serif; }

    /* Banner Carousel */
    .hero-slider {
        border-radius: 20px;
        overflow: hidden;
        box-shadow: 0 10px 30px rgba(0,0,0,0.1);
        margin-bottom: 40px;
    }
    .carousel-item img { height: 450px; object-fit: cover; }

    /* Category Icons */
    .cat-item {
        text-align: center;
        padding: 20px;
        background: #fff;
        border-radius: 15px;
        transition: 0.3s ease;
        border: 1px solid transparent;
    }
    .cat-item:hover {
        transform: translateY(-5px);
        border-color: var(--primary-color);
        box-shadow: 0 5px 15px rgba(0,0,0,0.05);
    }
    .cat-icon {
        width: 65px; height: 65px;
        background: #f0fdf4;
        color: var(--primary-color);
        border-radius: 50%;
        display: flex;
        align-items: center; justify-content: center;
        margin: 0 auto 12px;
        font-size: 26px;
    }

    /* Section Styling */
    .section-box {
        background: #fff;
        padding: 35px;
        border-radius: 25px;
        margin-bottom: 40px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.02);
    }
    .section-title-vip {
        font-weight: 800;
        letter-spacing: 1px;
        border-left: 6px solid var(--primary-color);
        padding-left: 15px;
        color: var(--text-dark);
    }

    /* Product Card */
    .p-card-vip {
        border: none;
        border-radius: 18px;
        transition: 0.4s;
        overflow: hidden;
        background: #fff;
    }
    .p-card-vip:hover {
        transform: translateY(-10px);
        box-shadow: 0 15px 30px rgba(0,0,0,0.1) !important;
    }
    .p-img-wrapper {
        position: relative;
        padding: 15px;
        background: #fdfdfd;
    }
    .p-img-wrapper img {
        aspect-ratio: 1/1;
        object-fit: cover;
        border-radius: 12px;
    }
    .badge-location {
        position: absolute;
        top: 20px; right: 20px;
        background: rgba(255,255,255,0.9);
        padding: 4px 12px;
        border-radius: 20px;
        font-size: 11px;
        font-weight: 700;
        color: var(--primary-color);
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        z-index: 2;
    }

    .btn-buy {
        background-color: var(--primary-color);
        color: white;
        border: none;
        border-radius: 12px;
        font-weight: 600;
        padding: 10px;
        transition: 0.3s;
    }
    .btn-buy:hover {
        background-color: #157347;
        color: white;
        transform: scale(1.02);
    }
</style>

<div class="container mt-4">
    <div id="vipCarousel" class="carousel slide hero-slider" data-bs-ride="carousel">
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=2070" class="d-block w-100" alt="Banner 1">
                <div class="carousel-caption d-none d-md-block text-start mb-5">
                    <h1 class="display-3 fw-bold">Đặc Sản Việt Nam</h1>
                    <p class="fs-4">Hương vị quê hương - Gói trọn tâm tình</p>
                    <a href="product" class="btn btn-success btn-lg px-5 rounded-pill shadow">Khám phá ngay</a>
                </div>
            </div>
            <div class="carousel-item">
                <img src="https://images.unsplash.com/photo-1547592166-23ac45744acd?q=80&w=2071" class="d-block w-100" alt="Banner 2">
            </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#vipCarousel" data-bs-slide="prev">
            <span class="carousel-control-prev-icon"></span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#vipCarousel" data-bs-slide="next">
            <span class="carousel-control-next-icon"></span>
        </button>
    </div>

    <div class="row g-3 mb-5 justify-content-center">
        <c:forEach items="${listCC}" var="c">
            <div class="col-4 col-md-2">
                <a href="product?cid=${c.cateID}" class="text-decoration-none text-dark">
                    <div class="cat-item shadow-sm">
                        <div class="cat-icon"><i class="fa fa-utensils"></i></div>
                        <span class="fw-bold d-block small text-truncate">${c.cate_name}</span>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>

    <div class="section-box">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="section-title-vip text-uppercase">Đặc sản Miền Bắc</h3>
            <a href="product?cid=2" class="btn btn-link text-success text-decoration-none fw-bold">Xem tất cả <i class="fa fa-arrow-right"></i></a>
        </div>
        <div class="row g-4">
            <c:forEach var="p" items="${listP}">
                <c:if test="${p.cateID == 2}">
                    <div class="col-6 col-md-3">
                        <div class="card p-card-vip h-100 shadow-sm">
                            <div class="p-img-wrapper">
                                <span class="badge-location">Miền Bắc</span>
                                <img src="${p.image}" class="card-img-top" alt="${p.name}">
                                <a href="product-detail?id=${p.id}" class="stretched-link"></a>
                            </div>
                            <div class="card-body d-flex flex-column text-center pt-0">
                                <h6 class="fw-bold text-dark mb-2 text-truncate">${p.name}</h6>
                                <p class="text-danger fw-bold mb-3"><fmt:formatNumber value="${p.price}" pattern="#,###"/> đ</p>
                                <button onclick="location.href='cart?action=add&id=${p.id}'" class="btn btn-buy mt-auto shadow-sm">
                                    <i class="fa fa-shopping-cart me-2"></i>Thêm giỏ hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <div class="section-box">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="section-title-vip text-uppercase">Đặc sản Miền Trung</h3>
            <a href="product?cid=3" class="btn btn-link text-success text-decoration-none fw-bold">Xem tất cả <i class="fa fa-arrow-right"></i></a>
        </div>
        <div class="row g-4">
            <c:forEach var="p" items="${listP}">
                <c:if test="${p.cateID == 3}">
                    <div class="col-6 col-md-3">
                        <div class="card p-card-vip h-100 shadow-sm">
                            <div class="p-img-wrapper">
                                <span class="badge-location">Miền Trung</span>
                                <img src="${p.image}" class="card-img-top" alt="${p.name}">
                                <a href="product-detail?id=${p.id}" class="stretched-link"></a>
                            </div>
                            <div class="card-body d-flex flex-column text-center pt-0">
                                <h6 class="fw-bold text-dark mb-2 text-truncate">${p.name}</h6>
                                <p class="text-danger fw-bold mb-3"><fmt:formatNumber value="${p.price}" pattern="#,###"/> đ</p>
                                <button onclick="location.href='cart?action=add&id=${p.id}'" class="btn btn-buy mt-auto shadow-sm">
                                    <i class="fa fa-shopping-cart me-2"></i>Thêm giỏ hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <div class="section-box">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="section-title-vip text-uppercase">Đặc sản Miền Nam</h3>
            <a href="product?cid=5" class="btn btn-link text-success text-decoration-none fw-bold">Xem tất cả <i class="fa fa-arrow-right"></i></a>
        </div>
        <div class="row g-4">
            <c:forEach var="p" items="${listP}">
                <c:if test="${p.cateID == 5}">
                    <div class="col-6 col-md-3">
                        <div class="card p-card-vip h-100 shadow-sm">
                            <div class="p-img-wrapper">
                                <span class="badge-location">Miền Nam</span>
                                <img src="${p.image}" class="card-img-top" alt="${p.name}">
                                <a href="product-detail?id=${p.id}" class="stretched-link"></a>
                            </div>
                            <div class="card-body d-flex flex-column text-center pt-0">
                                <h6 class="fw-bold text-dark mb-2 text-truncate">${p.name}</h6>
                                <p class="text-danger fw-bold mb-3"><fmt:formatNumber value="${p.price}" pattern="#,###"/> đ</p>
                                <button onclick="location.href='cart?action=add&id=${p.id}'" class="btn btn-buy mt-auto shadow-sm">
                                    <i class="fa fa-shopping-cart me-2"></i>Thêm giỏ hàng
                                </button>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</div>