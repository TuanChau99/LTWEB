<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<style>
    .form-container { 
        max-width: 600px; 
        margin: 30px auto; 
        padding: 30px; 
        background: #fff; 
        border-radius: 12px; 
        box-shadow: 0 4px 20px rgba(0,0,0,0.08); 
    }
    .form-header {
        margin-bottom: 25px;
        border-bottom: 1px solid #f0f0f0;
        padding-bottom: 15px;
    }
    .form-header h2 {
        margin: 0;
        font-size: 1.5rem;
        color: #333;
        display: flex;
        align-items: center;
        gap: 10px;
    }
    .form-group { 
        margin-bottom: 20px; /* Tăng khoảng cách giữa các nhóm nhập liệu */
    }
    .form-group label { 
        display: block; 
        font-weight: 600; 
        margin-bottom: 10px; /* Khoảng cách giữa chữ và ô input */
        color: #444;
        font-size: 0.95rem;
    }
    .form-control { 
        width: 100%; 
        padding: 12px; /* Tăng padding để ô nhập liệu to và dễ gõ hơn */
        border: 1px solid #e0e0e0; 
        border-radius: 6px; 
        box-sizing: border-box; 
        transition: border-color 0.3s;
        font-size: 1rem;
    }
    .form-control:focus {
        border-color: #22baa0;
        outline: none;
    }
    .form-control[readonly] {
        background-color: #f9f9f9;
        color: #777;
        cursor: not-allowed;
    }
    .preview-container {
        margin-top: 15px;
        text-align: center;
        display: flex;
        justify-content: center;
    }
    .preview-img { 
        width: 120px; 
        height: 120px; 
        object-fit: cover; 
        border-radius: 50%; 
        border: 3px solid #22baa0; 
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    .btn-submit { 
        width: 100%; 
        padding: 12px; 
        color: #fff; 
        background: #22baa0;
        border: none; 
        border-radius: 6px; 
        cursor: pointer; 
        font-size: 1rem; 
        font-weight: bold;
        transition: opacity 0.3s;
        margin-top: 10px;
    }
    .btn-submit:hover {
        opacity: 0.9;
    }
    .btn-back {
        display: block; 
        text-align: center; 
        margin-top: 15px; 
        color: #666; 
        text-decoration: none;
        font-size: 0.9rem;
    }
    .btn-back:hover {
        text-decoration: underline;
    }
</style>

<div class="form-container">
    <div class="form-header">
        <h2><i class="las la-user-edit"></i> Chỉnh sửa thành viên</h2>
    </div>
    
    <form action="${pageContext.request.contextPath}/admin/user/edit" method="post" enctype="multipart/form-data">
        <%-- ID ẩn để gửi về Servlet xử lý WHERE id=? --%>
        <input type="hidden" name="id" value="${user.id}">
        <input type="hidden" name="oldAvatar" value="${user.avatar}">

        <div class="form-group">
            <label>Tên đăng nhập (Không được sửa)</label>
            <input type="text" name="username" value="${user.username}" class="form-control" readonly>
        </div>

        <div class="form-group">
            <label>Họ và tên</label>
            <input type="text" name="fullname" value="${user.fullname}" class="form-control" required placeholder="Nhập họ và tên...">
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" value="${user.email}" class="form-control" required placeholder="example@gmail.com">
        </div>

        <div class="form-group">
            <label>Số điện thoại</label>
            <input type="text" name="phone" value="${user.phone}" class="form-control" placeholder="Nhập số điện thoại...">
        </div>
		
		<div class="form-group">
    <label>Địa chỉ</label>
    <input type="text" name="address" value="${user.address}" class="form-control" placeholder="Nhập địa chỉ thường trú...">
</div>

        <div class="form-group">
            <label>Vai trò (Role)</label>
            <select name="roleid" class="form-control">
                <option value="5" ${user.roleid == 5 ? 'selected' : ''}>Người dùng (User)</option>
                <option value="1" ${user.roleid == 1 ? 'selected' : ''}>Quản trị viên (Admin)</option>
            </select>
        </div>

        <div class="form-group">
            <label>Ảnh đại diện</label>
            <input type="file" name="avatar" class="form-control" onchange="previewImage(this)">
            
            <div class="preview-container">
                <c:url value="/image?fname=${user.avatar}" var="imgUrl"/>
                <%-- Hiển thị ảnh mặc định nếu user chưa có ảnh --%>
                <img id="preview" src="${empty user.avatar ? 'https://i.postimg.cc/1z9R3Yxg/admin-profile.png' : imgUrl}" class="preview-img">
            </div>
        </div>

        <button type="submit" class="btn-submit">Cập nhật thay đổi</button>
        <a href="${pageContext.request.contextPath}/admin/user/list" class="btn-back">Quay lại danh sách</a>
    </form>
</div>

<script>
    function previewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('preview').src = e.target.result;
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>