<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>



<html>
<head>
    <title>Danh sách Category</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f0f2f5;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 90%;
            max-width: 900px;
            margin: 40px auto;
            background: #fff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

        .action-bar {
            text-align: right;
            margin-bottom: 15px;
        }

        .btn {
            display: inline-block;
            padding: 10px 16px;
            background: #1877f2;
            color: #fff;
            text-decoration: none;
            border-radius: 6px;
            font-size: 14px;
            font-weight: bold;
            transition: background 0.3s;
        }
        .btn:hover {
            background: #0d65d9;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
        }

        th, td {
            padding: 12px;
            text-align: center;
            border-bottom: 1px solid #eee;
        }

        th {
            background: #1877f2;
            color: white;
            font-weight: bold;
        }

        tr:hover {
            background: #f9f9f9;
        }

        img {
            max-width: 50px;
            border-radius: 4px;
        }

        .action-links a {
            text-decoration: none;
            margin: 0 5px;
            padding: 6px 12px;
            border-radius: 4px;
            font-size: 13px;
            font-weight: bold;
            transition: 0.3s;
        }

        .edit-link {
            background: #42b72a;
            color: white;
        }
        .edit-link:hover {
            background: #2e8d1c;
        }

        .delete-link {
            background: #e41e3f;
            color: white;
        }
        .delete-link:hover {
            background: #b50d29;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>📂 Danh sách Category</h2>
        <div class="action-bar">
            <a href="<c:url value='/admin/category/add'/>" class="btn">+ Thêm mới Category</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên</th>
                    <th>Icon</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${listCate}">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.name}</td>
                        <td>
                            <img src="<c:url value='/image?fname=${c.icon}'/>" alt="${c.name}" />
                        </td>
                        <td class="action-links">
                            <a href="<c:url value='/admin/category/edit?id=${c.id}'/>" class="edit-link">Sửa</a>
                            <a href="<c:url value='/admin/category/delete?id=${c.id}'/>" class="delete-link"
                               onclick="return confirm('Bạn có chắc muốn xóa Category này không?');">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>
