<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa Category</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            background-color: #f5f7fa;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 60vh;
            margin: 0;
        }

        .container {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
            width: 420px;
            padding: 30px 40px;
            text-align: center;
            top: 0; /* Giúp tránh bị đẩy xuống */
        }

        h2 {
            color: #333;
            margin-bottom: 25px;
            font-size: 20px;
            font-weight: 600;
        }

        label {
            display: block;
            text-align: left;
            font-weight: 500;
            margin-bottom: 6px;
            color: #333;
        }

        input[type="text"], 
        input[type="file"] {
            width: 100%;
            padding: 10px;
            border-radius: 6px;
            border: 1px solid #ccc;
            margin-bottom: 18px;
            font-size: 14px;
        }

        img {
            display: block;
            margin: 8px auto 15px;
            border-radius: 6px;
            border: 1px solid #eee;
        }

        button {
            width: 100%;
            background-color: #1677ff;
            color: white;
            border: none;
            border-radius: 6px;
            padding: 10px;
            font-size: 15px;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        button:hover {
            background-color: #0d5ee0;
        }

        .back-link {
            display: inline-block;
            margin-top: 15px;
            font-size: 14px;
            color: #1677ff;
            text-decoration: none;
        }

        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>✏️ Sửa danh mục</h2>
    <form method="post" enctype="multipart/form-data" 
          action="${pageContext.request.contextPath}/admin/category/edit">
        <input type="hidden" name="id" value="${cate.id}" />

        <label for="name">Tên danh mục</label>
        <input type="text" id="name" name="name" value="${cate.name}" required />

        <label>Icon hiện tại</label>
        <img src="<c:url value='/image?fname=${cate.icon}'/>" alt="${cate.name}" width="60" />

        <label for="icon">Icon mới (nếu muốn thay)</label>
        <input type="file" id="icon" name="icon" />

        <button type="submit">Cập nhật</button>

        <a href="<c:url value='/admin/category/list'/>" class="back-link">← Quay lại danh sách</a>
    </form>
</div>
</body>
</html>
