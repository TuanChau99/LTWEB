<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<style>
    .form-container {
        width: 100%;
        max-width: 700px;
        background: #fff;
        padding: 30px;
        border-radius: 12px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.1);
        margin: 30px auto;
    }
    .form-header { border-bottom: 2px solid #f4f7f6; margin-bottom: 25px; padding-bottom: 10px; }
    .form-group { margin-bottom: 20px; }
    .form-group label { display: block; margin-bottom: 8px; font-weight: 600; color: #444; }
    .form-control {
        width: 100%;
        padding: 12px;
        border: 1px solid #ddd;
        border-radius: 8px;
        box-sizing: border-box;
    }
    .btn-submit {
        background: var(--main-color);
        color: white;
        padding: 12px 25px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        font-weight: 600;
        width: 100%;
    }
    .preview-img { width: 100px; height: 100px; border-radius: 50%; object-fit: cover; margin-top: 10px; border: 2px solid #eee; }
</style>

<div class="form-container">
    <div class="form-header">
        <h2><i class="las la-user-plus"></i> Thêm thành viên mới</h2>
    </div>
    
    <form action="<c:url value='/admin/user/add'/>" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label>Tên đăng nhập (Username)</label>
            <input type="text" name="username" class="form-control" required placeholder="Nhập username...">
        </div>
        
        <div class="form-group">
            <label>Mật khẩu</label>
            <input type="password" name="password" class="form-control" required placeholder="Nhập mật khẩu...">
        </div>

        <div class="form-group">
            <label>Họ và tên</label>
            <input type="text" name="fullname" class="form-control" required placeholder="Nhập họ tên...">
        </div>

        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" class="form-control" required placeholder="example@gmail.com">
        </div>

        <div class="form-group">
            <label>Số điện thoại</label>
            <input type="text" name="phone" class="form-control" placeholder="Nhập số điện thoại...">
        </div>

        <div class="form-group">
            <label>Vai trò (Role)</label>
            <select name="roleid" class="form-control">
                <option value="5">Người dùng (User)</option>
                <option value="1">Quản trị viên (Admin)</option>
            </select>
        </div>

        <div class="form-group">
            <label>Ảnh đại diện</label>
            <input type="file" name="avatar" class="form-control" onchange="previewImage(this)">
            <img id="preview" src="https://i.postimg.cc/1z9R3Yxg/admin-profile.png" class="preview-img">
        </div>

        <button type="submit" class="btn-submit">Lưu thông tin</button>
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