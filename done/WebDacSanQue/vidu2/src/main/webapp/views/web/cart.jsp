<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<style>
    /* CSS ĐỒNG BỘ */
    .cart-page-section { padding: 50px 0; background-color: #fdf5e6; min-height: 70vh; }
    .cart-card { border: none; border-radius: 15px; box-shadow: 0 5px 20px rgba(0,0,0,0.05); background: #fff; }
    .cart-header-title { color: #a0522d; font-weight: 800; text-transform: uppercase; letter-spacing: 1px; }
    .cart-img-wrapper { width: 80px; height: 80px; overflow: hidden; border-radius: 10px; border: 1px solid #eee; }
    .cart-img-wrapper img { width: 100%; height: 100%; object-fit: cover; }
    .product-name-link { color: #333; text-decoration: none; font-weight: 600; transition: 0.3s; }
    .product-name-link:hover { color: #a0522d; }
    .quantity-input { max-width: 80px; border-radius: 20px; text-align: center; border: 1px solid #ddd; }
    .cart-summary-box { background: #fff; padding: 25px; border-radius: 15px; border-top: 5px solid #a0522d; box-shadow: 0 5px 20px rgba(0,0,0,0.05); }

    .btn-checkout-custom {
        background: linear-gradient(135deg, #a0522d, #cd853f);
        color: white; border: none; border-radius: 30px; padding: 15px;
        font-weight: 700; transition: all 0.3s ease; text-transform: uppercase;
        box-shadow: 0 4px 15px rgba(160, 82, 45, 0.3);
    }
    .btn-checkout-custom:hover { transform: translateY(-2px); box-shadow: 0 6px 20px rgba(160, 82, 45, 0.4); color: white; }
    .btn-remove-item { color: #999; transition: 0.3s; text-decoration: none; font-size: 0.9rem; }
    .btn-remove-item:hover { color: #dc3545; }
</style>

<div class="cart-page-section">
    <div class="container">
        <div class="text-center mb-5">
            <h2 class="cart-header-title"><i class="fa fa-shopping-basket me-2"></i> Giỏ hàng của bạn</h2>
            <div style="width: 60px; height: 3px; background: #a0522d; margin: 10px auto;"></div>
        </div>

        <c:set var="cart" value="${sessionScope.cart}"/>

        <div class="row g-4">
            <div class="col-lg-8">
                <div class="cart-card p-4">
                    <h5 class="fw-bold mb-4">Sản phẩm (${not empty cart.items ? cart.items.size() : 0})</h5>

                    <c:if test="${empty cart.items || cart.items.size() == 0}">
                        <div class="text-center py-5">
                            <img src="https://cdn-icons-png.flaticon.com/512/11329/11329060.png" width="100" class="mb-3 opacity-50">
                            <p class="text-muted fs-5">Giỏ hàng của bạn đang trống!</p>
                            <a href="product" class="btn btn-outline-success rounded-pill px-4 mt-2">Đi mua sắm ngay</a>
                        </div>
                    </c:if>

                    <c:forEach var="entry" items="${cart.items}">
                        <c:set var="product" value="${entry.value}"/>
                        <div class="row align-items-center mb-3 pb-3 border-bottom">
                            <div class="col-md-6 col-7 d-flex align-items-center">
                                <div class="cart-img-wrapper me-3">
                                    <img src="${product.image}" alt="${product.name}">
                                </div>
                                <div>
                                    <a href="product-detail?id=${product.id}" class="product-name-link">
                                        <h6 class="mb-1">${product.name}</h6>
                                    </a>
                                    <small class="text-muted d-block text-truncate" style="max-width: 150px;">${product.title}</small>
                                </div>
                            </div>
                            <div class="col-md-2 d-none d-md-block text-center">
                                <span class="fw-bold"><fmt:formatNumber value="${product.price}" pattern="#,###"/> đ</span>
                            </div>
                            <div class="col-md-2 col-3 text-center">
                                <form action="cart" method="get">
                                    <input type="hidden" name="action" value="update"/>
                                    <input type="hidden" name="id" value="${product.id}"/>
                                    <input type="number" name="quantity" value="${product.quantity}" min="1" 
                                           class="form-control form-control-sm quantity-input mx-auto" onchange="this.form.submit()">
                                </form>
                            </div>
                            <div class="col-md-2 col-2 text-end">
                                <a href="cart?action=delete&id=${product.id}" class="btn-remove-item">
                                    <i class="fa fa-trash-can"></i> <span class="d-none d-md-inline">Xóa</span>
                                </a>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="mt-4">
                        <a href="product" class="text-decoration-none text-success fw-bold">
                            <i class="fa fa-chevron-left me-1"></i> Tiếp tục mua sắm
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="cart-summary-box">
                    <h5 class="fw-bold mb-4">Thông tin đơn hàng</h5>
                    
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Tạm tính</span>
                        <span class="fw-bold"><fmt:formatNumber value="${cart.totalPrice}" pattern="#,###"/> đ</span>
                    </div>
                    <div class="d-flex justify-content-between mb-4 pb-3 border-bottom text-success fw-bold">
                        <span>Phí vận chuyển</span>
                        <span>Miễn phí</span>
                    </div>

                    <div class="p-3 bg-light rounded-3 mb-4 border shadow-sm">
                        <h6 class="fw-bold mb-3"><i class="fa fa-map-marker-alt text-danger me-2"></i> Thông tin nhận hàng</h6>
                        <div class="mb-2">
                            <input type="text" id="receiverName" class="form-control form-control-sm" 
                                   placeholder="Họ tên người nhận" value="${sessionScope.account.fullname}" required>
                        </div>
                        <div class="mb-2">
                            <input type="text" id="receiverPhone" class="form-control form-control-sm" 
                                   placeholder="Số điện thoại" value="${sessionScope.account.phone}" required>
                        </div>
                        <div class="mb-2">
                            <textarea id="receiverAddress" class="form-control form-control-sm" 
                                      rows="2" placeholder="Địa chỉ chi tiết (Số nhà, đường...)" required></textarea>
                            <small class="text-danger d-none" id="addrError">Vui lòng nhập đầy đủ thông tin!</small>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <span class="fs-5 fw-bold">Tổng cộng</span>
                        <span class="fs-4 fw-bold text-danger"><fmt:formatNumber value="${cart.totalPrice}" pattern="#,###"/> đ</span>
                    </div>

                    <c:choose>
                        <c:when test="${not empty cart.items && cart.items.size() > 0}">
                            <button onclick="validateAndPay('VNPAY')" class="btn btn-checkout-custom w-100 mb-2">
                                <i class="fa fa-credit-card me-2"></i> Thanh toán VNPAY
                            </button>
                            <button onclick="validateAndPay('COD')" class="btn btn-outline-dark w-100 rounded-pill py-2 fw-bold">
                                <i class="fa fa-truck me-2"></i> Thanh toán COD
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-secondary w-100 rounded-pill opacity-50" disabled>Giỏ hàng trống</button>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="mt-4 p-3 border rounded-3 bg-white shadow-sm">
                    <p class="small text-muted mb-0">
                        <i class="fa fa-shield-halved text-success me-1"></i> 
                        Thanh toán an toàn với đa dạng phương thức. Cam kết bảo mật thông tin khách hàng.
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
    function validateAndPay(method) {
        const name = document.getElementById("receiverName").value.trim();
        const phone = document.getElementById("receiverPhone").value.trim();
        const address = document.getElementById("receiverAddress").value.trim();
        const errorEl = document.getElementById("addrError");

        if (!name || !phone || !address) {
            errorEl.classList.remove("d-none");
            return;
        }
        errorEl.classList.add("d-none");

        Swal.fire({
            title: 'Đang xử lý...',
            text: 'Vui lòng chờ trong giây lát',
            allowOutsideClick: false,
            didOpen: () => { Swal.showLoading() }
        });

        const fullInfo = encodeURIComponent(name + " | " + phone + " | " + address);
        window.location.href = "${pageContext.request.contextPath}/checkout?paymentMethod=" + method + "&address=" + fullInfo;
    }
</script>

<c:if test="${not empty sessionScope.errorStock}">
    <div id="errorData" style="display:none;">${sessionScope.errorStock}</div>
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            var msg = document.getElementById("errorData").textContent;
            Swal.fire({ title: 'Rất tiếc!', text: msg, icon: 'warning', confirmButtonColor: '#a0522d' });
        });
    </script>
    <c:remove var="errorStock" scope="session" />
</c:if>