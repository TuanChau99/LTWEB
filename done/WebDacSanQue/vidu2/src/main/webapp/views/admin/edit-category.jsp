<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    /* Khối container chuyên biệt cho Form sửa */
    .edit-category-box {
        background: #fff;
        border-radius: 15px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.08);
        width: 100%;
        max-width: 480px;
        padding: 40px;
        margin: 20px auto; /* Căn giữa form trong vùng main */
    }

    .edit-category-box h2 {
        color: var(--color-dark);
        margin-bottom: 30px;
        font-size: 1.6rem;
        font-weight: 600;
        text-align: center;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .form-group {
        margin-bottom: 20px;
        text-align: left;
    }

    .form-group label {
        display: block;
        font-weight: 600;
        margin-bottom: 8px;
        color: #444;
    }

    .form-control {
        width: 100%;
        padding: 12px;
        border-radius: 8px;
        border: 1px solid #ddd;
        font-size: 14px;
        transition: border-color 0.3s;
    }

    .form-control:focus {
        border-color: var(--main-color);
        outline: none;
        box-shadow: 0 0 0 3px rgba(34, 186, 160, 0.1);
    }

    /* Hiển thị icon hiện tại */
    .current-icon-wrapper {
        background: #f8f9fa;
        padding: 15px;
        border-radius: 8px;
        text-align: center;
        margin-bottom: 20px;
        border: 1px dashed #ccc;
    }

    .current-icon-wrapper img {
        max-width: 80px;
        border-radius: 4px;
        display: block;
        margin: 10px auto 0;
    }

    /* Nút bấm đồng bộ màu Teal */
    .btn-update {
        width: 100%;
        background-color: var(--main-color);
        color: white;
        border: none;
        border-radius: 8px;
        padding: 14px;
        font-size: 16px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .btn-update:hover {
        background-color: #1da88f;
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(34, 186, 160, 0.3);
    }

    .back-link {
        display: block;
        text-align: center;
        margin-top: 20px;
        font-size: 14px;
        color: #888;
        text-decoration: none;
        transition: color 0.2s;
    }

    .back-link:hover {
        color: var(--main-color);
        text-decoration: underline;
    }
</style>

<div class="edit-category-box">
    <h2><span class="las la-edit" style="color: var(--main-color)"></span> Sửa danh mục</h2>
    
    <form method="post" enctype="multipart/form-data" 
          action="${pageContext.request.contextPath}/admin/category/edit">
        
        <input type="hidden" name="id" value="${cate.id}" />

        <div class="form-group">
            <label for="name">Tên danh mục</label>
            <input type="text" id="name" name="name" class="form-control" value="${cate.name}" required />
        </div>

        <div class="current-icon-wrapper">
            <label style="font-size: 0.9rem; color: #666;">Icon hiện tại</label>
            <img src="<c:url value='/image?fname=${cate.icon}'/>" alt="${cate.name}" />
        </div>

        <div class="form-group">
            <label for="icon">Thay đổi Icon mới</label>
            <input type="file" id="icon" name="icon" class="form-control" />
            <small style="color: #999; font-size: 0.8rem;">(Để trống nếu không muốn thay đổi)</small>
        </div>

        <button type="submit" class="btn-update">Cập nhật ngay</button>

        <a href="<c:url value='/admin/category/list'/>" class="back-link">
            <span class="las la-arrow-left"></span> Quay lại danh sách
        </a>
    </form>
</div>