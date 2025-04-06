<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1"> <!-- Phù hợp mọi loại màn hình -->
    <link rel="icon" href="./img/logo.png" type="image/x-icon"/>

    <title>Hồ Sơ Của Tôi</title>

    <!-- Google font -->
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700" rel="stylesheet">

    <!-- Bootstrap -->
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css"/>

    <!-- Slick -->
    <link type="text/css" rel="stylesheet" href="css/slick.css"/>
    <link type="text/css" rel="stylesheet" href="css/slick-theme.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" integrity="sha512-papN8Auqwsb5Jnu3hU0MGLuedlitO+lQ9B+8R4zbmlF+M8sN+IN0KjhWtmjSel6BxGR+N4e244MQ7vq6QQP2mw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link type="text/css" rel="stylesheet" href="css/nouislider.min.css"/>

    <!-- Font Awesome Icon -->
    <link rel="stylesheet" href="css/font-awesome.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <!-- stlylesheet -->
    <link type="text/css" rel="stylesheet" href="css/style.css"/>
    <link rel="icon" href="./img/logo.png" type="image/x-icon"/>
    <link rel="stylesheet" href="css/font-awesome.min.css"/>
    <!-- jQuery Plugins -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/slick.min.js"></script>
    <script src="js/nouislider.min.js"></script>
    <script src="js/jquery.zoom.min.js"></script>

    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <link rel="stylesheet" href="css/font-awesome.min.css"/>
    <!-- CSS cho phần Profile. Lưu ý: các style này chỉ áp dụng trong phần nội dung chính để không làm ảnh hưởng đến header, menu, footer -->
    <style>
        /* Container của profile được giới hạn với class .profile-container */
        .profile-container {
            display: flex;
            flex-wrap: wrap; /* Hỗ trợ màn hình nhỏ */
            gap: 20px;
            margin: 20px auto;
            max-width: 1200px;
            padding: 20px;
        }

        /* Sidebar Profile */
        .profile-container .sidebar {
            flex: 0 0 250px;
            background-color: #ffffff;
            padding: 20px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        .profile-container .sidebar:hover {
            transform: translateY(-3px);
        }
        .profile-container .sidebar .section-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 10px;
            color: #333;
            border-bottom: 1px solid #f0f0f0;
            padding-bottom: 5px;
            display: flex;
            align-items: center;
        }
        .profile-container .sidebar .section-title i {
            font-size: 1.2em;
            margin-right: 8px;
            color: #ff5722;
        }
        .profile-container .sidebar ul {
            list-style: none;
            padding: 0;
            margin: 0 0 20px;
        }
        .profile-container .sidebar li {
            margin-bottom: 10px;
        }
        .profile-container .sidebar a {
            display: flex;
            align-items: center;
            padding: 10px;
            color: #333;
            font-weight: bold;
            border-radius: 4px;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        .profile-container .sidebar a i {
            font-size: 1em;
            margin-right: 8px;
            color: #555;
        }
        .profile-container .sidebar a:hover {
            background-color: #f7f7f7;
        }
        .profile-container .sidebar a.active {
            background-color: #ff5722;
            color: #fff;
        }
        .profile-container .sidebar a.active i {
            color: #fff;
        }

        /* Content Profile */
        .profile-container .content {
            flex: 1;
            background-color: #fff;
            padding: 30px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: box-shadow 0.3s ease;
        }
        .profile-container .content:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        }

        /* Profile Header */
        .profile-container .profile-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
            border-bottom: 1px solid #f0f0f0;
            padding-bottom: 10px;
        }
        .profile-container .profile-info .profile-title {
            font-size: 20px;
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }
        .profile-container .profile-info .profile-subtitle {
            font-size: 14px;
            color: #666;
        }

        /* Form Styles */
        .profile-container .form-group {
            margin-bottom: 20px;
        }
        .profile-container .form-label {
            font-weight: bold;
            font-size: 14px;
            margin-bottom: 5px;
            color: #555;
            display: block;
        }
        .profile-container input[type="text"],
        .profile-container input[type="email"],
        .profile-container select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }

        /* Save Button */
        .profile-container .save-button {
            background-color: #ff5722;
            color: #fff;
            border: none;
            padding: 12px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .profile-container .save-button:hover {
            background-color: #e64a19;
        }

        /* Avatar Upload */
        .profile-container .avatar-upload {
            margin-top: 20px;
            text-align: center;
        }
        .profile-container .avatar-upload button {
            background-color: #6a5acd;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s;
        }
        .profile-container .avatar-upload button:hover {
            background-color: #5b4886;
        }

        /* Responsive adjustments: Sidebar full width trên màn hình nhỏ */
        @media (max-width: 768px) {
            .profile-container {
                flex-direction: column;
            }
            .profile-container .sidebar {
                width: 100%;
            }
        }
    </style>
</head>
<body>

<!-- HEADER -->
<jsp:include page="header.jsp"/>
<!-- /HEADER -->

<!-- MENU -->
<jsp:include page="menu.jsp"/>
<!-- /MENU -->

<!-- PHẦN NỘI DUNG CHÍNH của Profile -->
<div class="profile-container">
    <!-- Sidebar Profile -->
    <div class="sidebar">
        <div class="section-title"><i class="fas fa-address-card"></i> Tài Khoản</div>
        <ul>
            <li><a href="#" class="active"><i class="fas fa-user-circle"></i> Hồ Sơ</a></li>
            <li><a href="#"><i class="fas fa-university"></i> Ngân Hàng</a></li>
            <li><a href="#"><i class="fas fa-map-marker-alt"></i> Địa Chỉ</a></li>
            <li><a href="changePassword"><i class="fas fa-key"></i> Đổi Mật Khẩu</a></li>
            <li><a href="#"><i class="fas fa-bell"></i> Cài Đặt Thông Báo</a></li>
            <li><a href="#"><i class="fas fa-cog"></i> Những Thiết Lập Riêng Tư</a></li>
        </ul>
        <div class="section-title"><i class="fas fa-shopping-cart"></i> Đơn Hàng</div>
        <ul>
            <li><a href="#"><i class="fas fa-file-alt"></i> Đơn Mua</a></li>
            <li><a href="#"><i class="fas fa-gift"></i> Kho Voucher</a></li>
            <li><a href="#"><i class="fas fa-coins"></i> Shopee Xu</a></li>
            <li><a href="#"><i class="fas fa-star"></i> Siêu Hội Freeship</a></li>
        </ul>
        <div class="section-title"><i class="fas fa-ellipsis-h"></i> Khác</div>
        <ul>
            <li><a href="#"><i class="fas fa-question-circle"></i> Trợ Giúp</a></li>
            <li><a href="#"><i class="fas fa-sign-out-alt"></i> Đăng Xuất</a></li>
        </ul>
    </div>



    <!-- Content Profile -->
    <div class="content">
        <!-- Profile Header -->
        <div class="profile-header">
            <div class="profile-info">
                <h2 class="profile-title">Hồ Sơ Của Tôi</h2>
                <c:choose>
                    <c:when test="${user.twoFaEnabled}">
                        <p class="text-success" style="font-size:16px;">
                            Xác thực hai lớp (2FA) đã được bật.
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p class="text-warning" style="font-size:16px;">
                            Bạn chưa bật xác thực hai lớp (2FA). Vui lòng nhấn vào
                            <a href="changePassword" style="color:#ff5722; font-weight:bold;">Đổi Mật Khẩu</a> để kích hoạt.
                        </p>
                    </c:otherwise>
                </c:choose>
                <p class="profile-subtitle">Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
            </div>
            <div class="profile-avatar">
                <c:choose>
                    <c:when test="${not empty user.name}">
                        ${fn:substring(user.name, 0, 1)}
                    </c:when>
                    <c:otherwise>
                        U
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Form Edit Profile -->
        <form action="updateProfile" method="post">
            <div class="form-group">
                <label class="form-label">Tên đăng nhập</label>
                <input type="text" name="username" value="${user.username}" readonly>
                <small>Tên đăng nhập chỉ có thể thay đổi một lần.</small>
            </div>

            <div class="form-group">
                <label class="form-label">Tên</label>
                <input type="text" name="name" placeholder="Nhập tên của bạn" value="${user.name}">
            </div>

            <div class="form-group">
                <label class="form-label">Email</label>
                <input type="email" name="email" value="${user.email}" readonly>
                <a href="#" style="color: blue;">Thay Đổi</a>
            </div>

            <div class="form-group">
                <label class="form-label">Số điện thoại</label>
                <!-- Nếu có dữ liệu số điện thoại có thể hiển thị ở đây -->
            </div>

            <button type="submit" class="save-button">Lưu</button>
        </form>

        <!-- Avatar Upload -->
        <div class="avatar-upload">
            <button type="button">Chọn Ảnh</button>
            <p>Dung lượng file tối đa: 1 MB<br>Dạng file: JPEG, PNG</p>
        </div>
    </div>
</div>
<!-- /PHẦN NỘI DUNG CHÍNH -->

<!-- FOOTER -->
<jsp:include page="footer.jsp"/>
<!-- /FOOTER -->

<script src="js/main.js"></script>
</body>
</html>