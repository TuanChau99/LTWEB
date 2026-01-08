<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<style>
    /* CSS RIÊNG CHO TRANG PRODUCT */
    .product-page-section { padding-bottom: 50px; }
    
    .category-card { border: none; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); overflow: hidden; }
    .category-header { background: #a0522d; color: white; font-weight: bold; text-transform: uppercase; padding: 15px; }
    
    .category-item-list { list-group-item; border: none; border-bottom: 1px solid #eee; transition: 0.3s; }
    .category-item-list:hover { background-color: #fff3e0 !important; padding-left: 25px; }
    .category-item-list a { color: #444; text-decoration: none; display: block; width: 100%; }
    .category-item-list.active-item { background: #cd853f !important; }
    .category-item-list.active-item a { color: white !important; font-weight: bold; }

    .filter-card { border: none; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
    .filter-header { background: #cd853f; color: white; font-weight: bold; text-transform: uppercase; padding: 15px; }

    /* Card sản phẩm style mới */
    .custom-product-card { 
        border: 1px solid #f1f1f1; 
        border-radius: 15px; 
        transition: all 0.3s ease; 
        height: 100%; 
        background: white;
        position: relative;
    }
    .custom-product-card:hover { 
        transform: translateY(-10px); 
        box-shadow: 0 10px 25px rgba(160, 82, 45, 0.15); 
    }
    .product-img-wrapper { height: 220px; overflow: hidden; border-radius: 15px 15px 0 0; }
    .product-img-wrapper img { width: 100%; height: 100%; object-fit: cover; }
    
    .product-title-link { 
        font-weight: 600; 
        color: #333; 
        text-decoration: none; 
        display: -webkit-box;
        -webkit-line-clamp: 1;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }
    .product-title-link:hover { color: #a0522d; }
    
    .product-price-tag { color: #c01508; font-weight: 700; font-size: 1.2rem; }
    
    .btn-add-cart { 
        background-color: #28a745; 
        color: white; 
        border-radius: 20px; 
        border: none; 
        padding: 8px 15px;
        transition: 0.3s;
    }
    .btn-add-cart:hover { background-color: #218838; color: white; transform: scale(1.05); }

    /* Last Product Sidebar */
    .last-product-box { border: 1px solid #eee; border-radius: 10px; padding: 10px; transition: 0.3s; }
    .last-product-box:hover { background: #fdf5e6; }
    .last-product-img { width: 100%; height: 160px; object-fit: cover; border-radius: 8px; }
    
    .hero-banner {
        background: #fdf5e6;
        padding: 60px 0;
        margin-bottom: 40px;
        border-bottom: 3px solid #a0522d;
    }
</style>

<div class="product-page-section">
    <div class="hero-banner text-center">
        <div class="container">
            <h1 class="display-6 fw-bold" style="color: #a0522d;">DANH SÁCH SẢN PHẨM</h1>
            <p class="text-muted">Khám phá tinh hoa đặc sản từ mọi miền quê Việt</p>
        </div>
    </div>

    <div class="container">
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="home" class="text-decoration-none" style="color: #cd853f;">Trang chủ</a></li>
                <li class="breadcrumb-item active">Sản phẩm</li>
            </ol>
        </nav>

        <div class="row">
            <div class="col-lg-3">
                <div class="category-card mb-4">
                    <div class="category-header"><i class="fa fa-list me-2"></i> Danh mục</div>
                    <div class="list-group list-group-flush">
                        <c:forEach items="${listCC}" var="c">
    <div class="category-item-list ${param.cid == c.cate_id ? 'active-item' : ''}">
        <a href="product?cid=${c.cate_id}&sort=${param.sort}&min_price=${param.min_price}&max_price=${param.max_price}" class="p-3 d-block">
            ${c.cate_name}
        </a>
    </div>
</c:forEach>
                    </div>
                </div>

                <div class="filter-card mb-4 shadow-sm">
                    <div class="filter-header"><i class="fa fa-filter me-2"></i> Lọc Nâng Cao</div>
                    <div class="card-body p-3">
                        <form action="product" method="get">
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Sắp xếp:</label>
                                <select name="sort" class="form-select form-select-sm border-secondary-subtle">
                                    <option value="default" ${param.sort == 'default' ? 'selected' : ''}>Mặc định</option>
                                    <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                                    <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                                    <option value="name_asc" ${param.sort == 'name_asc' ? 'selected' : ''}>Tên A-Z</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Khoảng giá (VNĐ):</label>
                                <div class="input-group input-group-sm mb-2">
                                    <input type="number" name="min_price" class="form-control" placeholder="Từ" value="${param.min_price}">
                                    <input type="number" name="max_price" class="form-control" placeholder="Đến" value="${param.max_price}">
                                </div>
                            </div>
                            <c:if test="${param.cid != null}"><input type="hidden" name="cid" value="${param.cid}" /></c:if>
                            <button type="submit" class="btn btn-sm w-100 text-white shadow-sm" style="background: #a0522d;">ÁP DỤNG LỌC</button>
                        </form>
                    </div>
                </div>

                <div class="card border-0 shadow-sm mb-4 overflow-hidden" style="border-radius: 12px;">
                    <div class="p-3 text-white fw-bold" style="background: #28a745;">MỚI NHẤT</div>
                    <div class="card-body p-2">
                        <c:if test="${not empty p}">
                            <div class="last-product-box">
                                <img src="${p.image}" class="last-product-img" alt="${p.name}">
                                <div class="mt-2">
                                    <h6 class="fw-bold text-truncate">${p.name}</h6>
                                    <p class="text-danger fw-bold mb-1">
                                        <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                    </p>
                                    <a href="product-detail?id=${p.id}" class="btn btn-outline-dark btn-sm w-100">Chi tiết</a>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>

            <div class="col-lg-9">
                <div class="row g-4" id="product-list-container">
                    <c:forEach items="${listP}" var="product">
    <div class="col-md-6 col-lg-4 mb-3">
        <div class="custom-product-card d-flex flex-column shadow-sm">
            <div class="product-img-wrapper">
                <img src="${product.image}" alt="${product.name}">
            </div>
            <div class="card-body p-3 flex-grow-1 d-flex flex-column">
                <h5 class="mb-2">
                    <a href="product-detail?id=${product.id}" class="product-title-link stretched-link">
                        ${product.name}
                    </a>
                </h5>
                <p class="small text-muted mb-3" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">
                    ${product.title}
                </p>
                <div class="mt-auto">
                    <div class="d-flex justify-content-between align-items-center position-relative" style="z-index: 2;">
                        <span class="product-price-tag">
                            <fmt:formatNumber value="${product.price}" pattern="#,###"/> đ
                        </span>
                        <a href="cart?action=add&id=${product.id}&success=add" class="btn-add-cart text-decoration-none">
                            <i class="fa fa-cart-plus"></i>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</c:forEach>
                </div>

                <c:if test="${endP > 1}">
                    <nav class="mt-5">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${tag == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="product?page=${tag - 1}&cid=${param.cid}&sort=${param.sort}&min_price=${param.min_price}&max_price=${param.max_price}">«</a>
                            </li>
                            <c:forEach begin="1" end="${endP}" var="i">
                                <li class="page-item ${tag == i ? 'active' : ''}">
                                    <a class="page-link ${tag == i ? '' : 'text-dark'}" 
                                       style="${tag == i ? 'background-color: #a0522d; border-color: #a0522d;' : ''}"
                                       href="product?page=${i}&cid=${param.cid}&sort=${param.sort}&min_price=${param.min_price}&max_price=${param.max_price}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${tag == endP ? 'disabled' : ''}">
                                <a class="page-link" href="product?page=${tag + 1}&cid=${param.cid}&sort=${param.sort}&min_price=${param.min_price}&max_price=${param.max_price}">»</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>

                <div class="text-center mt-4">
                    <button id="loadMoreBtn" class="btn btn-outline-secondary px-5 rounded-pill shadow-sm" onclick="loadMoreProducts()">
                        TẢI THÊM SẢN PHẨM
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    // Các biến dùng cho Ajax
    let loadMorePage = ${tag} + 1;
    const totalPages = ${endP};

    // Kiểm tra ẩn nút xem thêm khi hết trang
    if (loadMorePage > totalPages) {
        $("#loadMoreBtn").hide();
    }

    function loadMoreProducts() {
        if (loadMorePage > totalPages) return;
        
        // Lấy tham số hiện tại để giữ bộ lọc khi tải thêm
        const urlParams = new URLSearchParams(window.location.search);
        let cid = urlParams.get('cid') || "";
        let sort = urlParams.get('sort') || "default";
        let min_price = urlParams.get('min_price') || "";
        let max_price = urlParams.get('max_price') || "";

        $.ajax({
            url: "ajax-product-list",
            type: "GET",
            data: {
                page: loadMorePage,
                cid: cid,
                sort: sort,
                min_price: min_price,
                max_price: max_price,
                loadMore: "true"
            },
            success: function(response) {
                if(response.trim() !== "") {
                    $("#product-list-container").append(response);
                    loadMorePage++;
                    if (loadMorePage > totalPages) {
                        $("#loadMoreBtn").fadeOut();
                    }
                } else {
                    $("#loadMoreBtn").hide();
                }
            },
            error: function() {
                alert("Lỗi tải dữ liệu!");
            }
        });
    }
</script>