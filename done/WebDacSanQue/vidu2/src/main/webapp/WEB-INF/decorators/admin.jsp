<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title' default="Quản Trị Hệ Thống"/></title>

    <link rel="stylesheet" href="https://maxst.icons8.com/vue-static/landings/line-awesome/line-awesome/1.3.0/css/line-awesome.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Merriweather+Sans:wght@300;400;500;600&display=swap" rel="stylesheet">
    
    <style>
        :root { --main-color: #22BAA0; --color-dark: #34425A; --text-grey: #B0B0B0; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Merriweather Sans', sans-serif; text-decoration: none; list-style-type: none; }
        
        #menu-toggle { display: none; }

        /* Sidebar mặc định */
        .sidebar { position: fixed; height: 100%; width: 165px; left: 0; top: 0; z-index: 100; background: var(--color-dark); transition: width 300ms; }
        
        /* Nội dung chính */
        .main-content { margin-left: 165px; width: calc(100% - 165px); transition: margin-left 300ms; min-height: 100vh; background: #f1f4f9; display: flex; flex-direction: column; }
        
        /* Header cố định */
        header { position: fixed; top: 0; right: 0; left: 165px; height: 60px; background: #fff; z-index: 99; transition: left 300ms; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }

        /* Vùng đổ nội dung trang con */
        main { 
            margin-top: 60px; 
            padding: 20px; 
            display: flex; 
            justify-content: center; /* Căn giữa form trang con */
            align-items: flex-start; 
            min-height: calc(100vh - 60px); 
        }

        /* Logic đóng mở Sidebar */
        #menu-toggle:checked ~ .sidebar { width: 60px; }
        #menu-toggle:checked ~ .main-content { margin-left: 60px; width: calc(100% - 60px); }
        #menu-toggle:checked ~ .main-content header { left: 60px; }
        #menu-toggle:checked ~ .sidebar .profile, #menu-toggle:checked ~ .sidebar span:not(.las) { display: none; }
    </style>
    <sitemesh:write property="head"/>
</head>
<body>
    <input type="checkbox" id="menu-toggle">

    <%@ include file="/common/admin/left.jsp" %>

    <div class="main-content">
        <%@ include file="/common/admin/header.jsp" %>

        <main>
            <%-- Mọi trang con khi đổ vào đây sẽ tự động nằm giữa nhờ Flexbox ở trên --%>
            <sitemesh:write property="body"/>
        </main>
    </div>
</body>
</html>