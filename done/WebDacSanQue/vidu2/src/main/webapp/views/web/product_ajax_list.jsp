<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:choose>
    <c:when test="${not empty listP}">
        <c:forEach items="${listP}" var="product">
            <div class="col-md-6 col-lg-4 mb-3">
                <div class="custom-product-card d-flex flex-column position-relative shadow-sm">
                    
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
    </c:when>
</c:choose>