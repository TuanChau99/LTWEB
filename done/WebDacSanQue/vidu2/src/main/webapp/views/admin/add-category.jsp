<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<style>
    /* Sử dụng class riêng để tránh xung đột, căn giữa bằng margin auto */
    .add-category-box {
        width: 100%;
        max-width: 480px;
        margin: 20px auto; 
        background: #fff;
        padding: 40px;
        border-radius: 15px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.08);
    }

    .add-category-box h2 {
        text-align: center;
        margin-bottom: 30px;
        color: var(--color-dark); /* Màu xám đậm từ Decorator */
        font-weight: 600;
        font-size: 1.6rem;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 12px;
    }

    .form-group { 
        margin-bottom: 25px; 
        text-align: left;
    }

    .form-group label {
        display: block;
        font-weight: 600;
        margin-bottom: 10px;
        color: #444;
        font-size: 0.95rem;
    }

    /* Style cho các ô input đồng bộ với trang Edit */
    .form-control {
        width: 100%;
        padding: 12px 15px;
        border: 1px solid #ddd;
        border-radius: 8px;
        font-size: 14px;
        transition: all 0.3s ease;
        box-sizing: border-box;
    }

    .form-control:focus {
        border-color: var(--main-color); /* Xanh Teal */
        outline: none;
        box-shadow: 0 0 0 3px rgba(34, 186, 160, 0.1);
    }

    /* Nút bấm đồng bộ màu Xanh Teal hệ thống */
    .btn-submit {
        width: 100%;
        padding: 14px;
        background: var(--main-color);
        color: #fff;
        font-size: 16px;
        font-weight: 600;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s ease;
        margin-top: 10px;
    }

    .btn-submit:hover { 
        background: #1da88f; 
        transform: translateY(-2px);
        box-shadow: 0 5px 15px rgba(34, 186, 160, 0.3);
    }

    .back-link { 
        text-align: center; 
        margin-top: 25px; 
    }
    
    .back-link a { 
        text-decoration: none; 
        color: #888; 
        font-size: 14px;
        transition: color 0.2s;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 5px;
    }

    .back-link a:hover { 
        color: var(--main-color);
        text-decoration: underline; 
    }
</style>

<div class="add-category-box">
    <h2>
        <span class="las la-plus-circle" style="color: var(--main-color)"></span> 
        Thêm mới danh mục
    </h2>
    
    <form method="post" action="${pageContext.request.contextPath}/admin/category/add"
          enctype="multipart/form-data">
        
        <div class="form-group">
            <label for="cateName">Tên danh mục</label>
            <input type="text" id="cateName" name="name" class="form-control" 
                   placeholder="Nhập tên danh mục (VD: Đặc sản Miền Tây)" required />
        </div>

        <div class="form-group">
            <label for="cateIcon">Icon đại diện</label>
            <input type="file" id="cateIcon" name="icon" class="form-control" accept="image/*" />
            <small style="color: #999; display: block; margin-top: 5px;">
                Nên sử dụng ảnh định dạng .png hoặc .jpg, kích thước vuông.
            </small>
        </div>

        <button type="submit" class="btn-submit">
            <i class="las la-save"></i> Xác nhận thêm mới
        </button>
    </form>

    <div class="back-link">
        <a href="${pageContext.request.contextPath}/admin/category/list">
            <span class="las la-arrow-left"></span> Quay lại danh sách
        </a>
    </div>
</div>