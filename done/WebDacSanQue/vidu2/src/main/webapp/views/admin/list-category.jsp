<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<style>
    /* Khối bao ngoài bảng - Tự động nằm giữa nhờ Decorator Flexbox */
    .list-container {
        width: 100%;
        max-width: 1000px;
        background: #fff;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.05);
        margin: 20px auto;
    }

    .list-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 25px;
        border-bottom: 2px solid #f1f4f9;
        padding-bottom: 15px;
    }

    .list-header h2 {
        color: var(--color-dark);
        font-size: 1.5rem;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    /* Nút thêm mới - Đồng bộ màu Xanh Teal */
    .btn-add {
        background: var(--main-color);
        color: white;
        padding: 10px 20px;
        border-radius: 8px;
        font-weight: 600;
        transition: 0.3s;
    }
    .btn-add:hover { background: #1da88f; color: white; transform: translateY(-2px); }

    /* Custom Table */
    .custom-table {
        width: 100%;
        border-collapse: collapse;
    }
    .custom-table th {
        background: #f8fafd;
        color: #74767d;
        font-weight: 600;
        text-transform: uppercase;
        font-size: 0.85rem;
        padding: 15px;
        text-align: center;
    }
    .custom-table td {
        padding: 15px;
        text-align: center;
        border-bottom: 1px solid #f1f4f9;
        vertical-align: middle;
    }
    .custom-table tr:hover { background: #fcfdfe; }

    /* Badge & Image */
    .cate-id { color: var(--main-color); font-weight: bold; }
    .cate-icon {
        width: 50px;
        height: 35px;
        object-fit: contain;
        border-radius: 4px;
        background: #f9f9f9;
        padding: 2px;
    }

    /* Action Buttons */
    .btn-edit { background: #67c23a; color: white; padding: 5px 12px; border-radius: 4px; margin-right: 5px; }
    .btn-delete { background: #f56c6c; color: white; padding: 5px 12px; border-radius: 4px; }
    .btn-edit:hover, .btn-delete:hover { opacity: 0.8; color: white; }
</style>

<div class="list-container">
    <div class="list-header">
        <h2><span class="las la-folder-open" style="color: #f6d433"></span> Danh sách Category</h2>
        <a href="<c:url value='/admin/category/add'/>" class="btn-add">
            <span class="las la-plus"></span> Thêm mới Category
        </a>
    </div>

    <table class="custom-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên danh mục</th>
                <th>Icon</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="c" items="${listCate}">
                <tr>
                    <td class="cate-id">#${c.id}</td>
                    <td style="font-weight: 500; color: #444;">${c.name}</td>
                    <td>
                        <img src="<c:url value='/image?fname=${c.icon}'/>" class="cate-icon" alt="${c.name}" />
                    </td>
                    <td>
                        <a href="<c:url value='/admin/category/edit?id=${c.id}'/>" class="btn-edit">
                            <i class="las la-edit"></i> Sửa
                        </a>
                        <a href="<c:url value='/admin/category/delete?id=${c.id}'/>" class="btn-delete"
                           onclick="return confirm('Xóa danh mục này có thể ảnh hưởng đến sản phẩm. Bạn chắc chắn chứ?');">
                            <i class="las la-trash"></i> Xóa
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>