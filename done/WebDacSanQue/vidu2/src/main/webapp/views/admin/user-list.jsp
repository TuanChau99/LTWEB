<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<style>
    .user-container {
        width: 100%;
        max-width: 1200px;
        background: #fff;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.05);
        margin: 20px auto;
    }
    .user-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
        border-bottom: 2px solid #f1f4f9;
        padding-bottom: 15px;
    }
    .user-header h2 { color: var(--color-dark); display: flex; align-items: center; gap: 10px; }
    
    .btn-add { background: var(--main-color); color: white; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none; }
    
    .role-badge { background: #eef2ff; color: #4f46e5; padding: 5px 12px; border-radius: 20px; font-size: 0.8rem; font-weight: 600; }

    .user-table { width: 100%; border-collapse: collapse; }
    .user-table th { background: #f8fafd; color: #74767d; padding: 15px; text-align: left; }
    .user-table td { padding: 15px; border-bottom: 1px solid #f1f4f9; }
    
    .avatar-circle { width: 45px; height: 45px; border-radius: 50%; object-fit: cover; border: 2px solid var(--main-color); background: #eee; }
    .text-muted { color: #888; font-size: 0.85rem; }
</style>

<div class="user-container">
    <div class="user-header">
        <h2><span class="las la-users-cog" style="color: var(--main-color)"></span> Quản lý tài khoản</h2>
        <%-- Đổi link về user-add cho đồng bộ --%>
        <a href="<c:url value='/admin/user/add'/>" class="btn-add">+ Thêm thành viên</a>
    </div>

    <table class="user-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Avatar</th>
                <th>Thông tin tài khoản</th>
                <th>Điện thoại</th>
                <th>Vai trò</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="u" items="${listUser}">
                <tr>
                    <td><strong>#${u.id}</strong></td>
                    <td>
    <c:choose>
        <%-- TRƯỜNG HỢP 1: Nếu là ảnh mặc định hệ thống --%>
        <c:when test="${u.avatar == 'default-user.png'}">
            <img src="<c:url value='/assets/img/default-user.png'/>" 
                 class="img-circle" width="40" height="40">
        </c:when>

        <%-- TRƯƯỜNG HỢP 2: Nếu có tên file ảnh đã upload (ID #10, #11 trong ảnh của bạn) --%>
        <c:when test="${not empty u.avatar}">
            <c:url value="/image?fname=${u.avatar}" var="imgUrl"/>
            <img src="${imgUrl}" class="img-circle" width="40" height="40" 
                 onerror="this.src='https://ui-avatars.com/api/?name=${u.fullname}&background=random'">
        </c:when>

        <%-- TRƯỜNG HỢP 3: Nếu NULL hoàn toàn (Các ID #1 đến #7) --%>
        <c:otherwise>
            <img src="https://ui-avatars.com/api/?name=${u.fullname}&background=0D8ABC&color=fff" 
                 class="img-circle" width="40" height="40">
        </c:otherwise>
    </c:choose>
</td>
                    <td>
                        <%-- Đã sửa: dùng fullname thay vì fullName --%>
                        <strong>${u.fullname}</strong><br>
                        <small class="text-muted">User: ${u.username} | Email: ${u.email}</small>
                    </td>
                    <td>${empty u.phone ? 'Chưa cập nhật' : u.phone}</td>
                    <td>
                        <span class="role-badge">
                            <c:choose>
                                <c:when test="${u.roleid == 1}">Quản trị viên</c:when>
                                <c:otherwise>Người dùng (ID:${u.roleid})</c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/admin/user/edit?id=${u.id}" style="color: #67c23a; font-size: 1.2rem;"><i class="las la-edit"></i></a>
                        <a href="<c:url value='/admin/user/delete?id=${u.id}'/>" style="color: #f56c6c; margin-left: 15px; font-size: 1.2rem;" 
                           onclick="return confirm('Bạn có chắc muốn xóa người dùng này?')"><i class="las la-trash"></i></a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>