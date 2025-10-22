<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Thêm danh mục</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f0f2f5;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 100%;
            max-width: 400px;
            margin: 60px auto;
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

        .form-group {
            margin-bottom: 15px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 6px;
            color: #555;
        }

        input[type="text"],
        input[type="file"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
        }

        input[type="text"]:focus,
        input[type="file"]:focus {
            border-color: #1877f2;
            outline: none;
        }

        button {
            width: 100%;
            padding: 12px;
            background: #1877f2;
            color: #fff;
            font-size: 15px;
            font-weight: bold;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover {
            background: #0d65d9;
        }

        .back-link {
            text-align: center;
            margin-top: 15px;
        }

        .back-link a {
            text-decoration: none;
            color: #1877f2;
            font-size: 14px;
        }

        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<c:out value="${listCate}" />

    <div class="container">
        <h2>➕ Thêm mới danh mục</h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/category/add"
              enctype="multipart/form-data">
            
            <div class="form-group">
                <label>Tên danh mục</label>
                <input type="text" name="name" required />
            </div>

            <div class="form-group">
                <label>Icon</label>
                <input type="file" name="icon" />
            </div>

            <button type="submit">Thêm mới</button>
        </form>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/admin/category/list">← Quay lại danh sách</a>
        </div>
    </div>
</body>
</html>
