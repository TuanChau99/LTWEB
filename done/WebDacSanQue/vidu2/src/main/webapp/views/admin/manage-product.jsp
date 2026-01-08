<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Sản Phẩm</title>
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
        .manage-card { background: #fff; border-radius: 12px; box-shadow: 0 5px 15px rgba(0,0,0,0.08); margin-top: 20px; border: none; }
        .manage-header { background-color: #22BAA0 !important; color: white; padding: 18px 25px; border-radius: 12px 12px 0 0; }
        .product-img { width: 50px; height: 50px; object-fit: cover; border-radius: 5px; border: 1px solid #eee; }
        .table thead th { background-color: #f8f9fa; color: #34425A; font-size: 0.85rem; text-transform: uppercase; border: none; }
        .btn-add { background-color: #34425A; color: white; border: none; border-radius: 5px; transition: 0.3s; }
        .btn-add:hover { background-color: #22BAA0; color: white; }
        .stock-low { color: #E74C3C; font-weight: bold; }
        .stock-ok { color: #27AE60; font-weight: bold; }
        .action-btn { width: 32px; height: 32px; padding: 0; line-height: 32px; border-radius: 4px; }
    </style>
</head>
<body class="bg-light">
    <div class="container-fluid px-4 pb-5">
        <div class="manage-card">
            <div class="manage-header d-flex justify-content-between align-items-center">
                <h4 class="mb-0"><i class="fa fa-cubes"></i> QUẢN LÝ HÀNG HÓA</h4>
                <a href="${pageContext.request.contextPath}/admin/product/add" class="btn btn-dark">
   <i class="las la-plus-circle"></i> Thêm sản phẩm mới
</a>
            </div>
            
            <div class="p-4">
                <div class="table-responsive">
                    <table class="table table-hover align-middle border-bottom">
                        <thead>
                            <tr class="text-center">
                                <th>ID</th>
                                <th>Ảnh</th>
                                <th class="text-left">Tên sản phẩm</th>
                                <th>Danh mục</th>
                                <th>Giá bán</th>
                                <th>Tồn kho</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${listP}" var="p">
                                <tr class="text-center">
                                    <td>#${p.id}</td>
                                    <td><img src="${p.image}" class="product-img" alt="product"></td>
                                    <td class="text-left">
                                        <div class="font-weight-bold text-dark">${p.name}</div>
                                        <small class="text-muted text-truncate d-inline-block" style="max-width: 200px;">${p.title}</small>
                                    </td>
                                    <td>
                                        <span class="badge badge-light border px-2 py-1">ID: ${p.cateID}</span>
                                    </td>
                                    <td class="text-info font-weight-bold">
                                        <fmt:formatNumber value="${p.price}" type="number" pattern="#,###"/> VNĐ
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.stock < 10}">
                                                <span class="stock-low"><i class="fa fa-warning"></i> ${p.stock} (Sắp hết)</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="stock-ok">${p.stock}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}" class="btn btn-warning btn-sm">
   <i class="las la-edit"></i>
</a>
                                        <button onclick="confirmDelete(${p.id})" class="btn btn-danger action-btn" title="Xóa">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <c:if test="${empty listP}">
                    <div class="text-center py-5">
                        <i class="fa fa-dropbox fa-4x text-muted mb-3"></i>
                        <p class="text-muted">Chưa có sản phẩm nào trong kho hàng.</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <script>
        function confirmDelete(id) {
            if (confirm("Bạn có chắc chắn muốn xóa sản phẩm #" + id + " không?")) {
                window.location.href = "deleteProduct?pid=" + id;
            }
        }
    </script>
</body>
</html>