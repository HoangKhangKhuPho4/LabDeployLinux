    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    <!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hồ Sơ Của Tôi</title>
        <link rel="stylesheet" href="css/register.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
        <link rel="stylesheet" href="css/bootstrap.min.css"/>
        <style>
            /* General Styles */
            body { font-family: Arial, sans-serif; background-color: #f4f5f7; margin: 0; padding: 0; }
            .container { display: flex; max-width: 1200px; margin: 0 auto; padding: 20px; }
            /* Sidebar Styles */
            .sidebar { width: 250px; background-color: #fff; padding: 20px; border-right: 1px solid #e0e0e0; }
            .sidebar ul { list-style: none; padding: 0; margin: 0; }
            .sidebar li { margin-bottom: 15px; }
            .sidebar a { text-decoration: none; color: #333; font-weight: bold; display: block; padding: 10px; border-radius: 4px; transition: background-color 0.3s ease; }
            .sidebar a:hover { background-color: #f0f0f0; }
            .sidebar a.active { background-color: #ff5722; color: white; }
            .sidebar .section-title { font-size: 14px; color: #666; margin-bottom: 10px; }
            /* Content Styles */
            .content { flex: 1; padding: 20px; }
            .profile-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
            .profile-avatar { width: 100px; height: 100px; border-radius: 50%; background-color: #6a5acd; display: flex; justify-content: center; align-items: center; font-size: 36px; color: white; font-weight: bold; }
            .profile-info { flex: 1; }
            .profile-title { font-size: 18px; font-weight: bold; margin-bottom: 5px; }
            .profile-subtitle { color: #666; font-size: 14px; }
            .form-group { margin-bottom: 30px; }
            .form-label { display: block; margin-bottom: 10px; font-weight: bold; }
            input[type="text"],
            input[type="email"],
            select { width: 100%; padding: 12px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; }
            .gender-options { display: flex; align-items: center; gap: 20px; }
            .save-button { background-color: #ff5722; color: white; border: none; padding: 12px 20px; border-radius: 4px; cursor: pointer; font-size: 16px; }
            .save-button:hover { background-color: #e64a19; }
            .avatar-upload { text-align: center; margin-top: 20px; }
            .avatar-upload button { background-color: #6a5acd; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; font-size: 16px; }
            .avatar-upload button:hover { background-color: #5b4886; }
            /* Birthday Selectors */
            .birthday-selectors { display: flex; align-items: center; gap: 15px; }
            .birthday-selectors select { flex: 1; margin: 0; }
        </style>
    </head>
    <body>
    <div class="container">
        <!-- Sidebar -->
        <div class="sidebar">
            <div class="section-title">Tài Khoản</div>
            <ul>
                <li><a href="#" class="active"><i class="fas fa-user"></i> Hồ Sơ</a></li>
                <li><a href="#"><i class="fas fa-university"></i> Ngân Hàng</a></li>
                <li><a href="#"><i class="fas fa-map-marker-alt"></i> Địa Chỉ</a></li>
                <li><a href="changePassword"><i class="fas fa-key"></i> Đổi Mật Khẩu</a></li>                <li><a href="#"><i class="fas fa-bell"></i> Cài Đặt Thông Báo</a></li>
                <li><a href="#"><i class="fas fa-cog"></i> Những Thiết Lập Riêng Tư</a></li>
            </ul>
            <div class="section-title">Đơn Hàng</div>
            <ul>
                <li><a href="#"><i class="fas fa-file-alt"></i> Đơn Mua</a></li>
                <li><a href="#"><i class="fas fa-gift"></i> Kho Voucher</a></li>
                <li><a href="#"><i class="fas fa-coins"></i> Shopee Xu</a></li>
                <li><a href="#"><i class="fas fa-star"></i> Siêu Hội Freeship</a></li>
            </ul>
            <div class="section-title">Khác</div>
            <ul>
                <li><a href="#"><i class="fas fa-question-circle"></i> Trợ Giúp</a></li>
                <li><a href="#"><i class="fas fa-sign-out-alt"></i> Đăng Xuất</a></li>
            </ul>
        </div>

        <!-- Content -->
        <div class="content">
            <!-- Profile Header -->
            <div class="profile-header">
                <div class="profile-info">
                    <h2 class="profile-title">Hồ Sơ Của Tôi</h2>
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
                    <!-- Lấy từ DB qua trường user_name -->
                    <input type="text" name="username" value=" ${user.username}" readonly>
                    <small>Tên đăng nhập chỉ có thể thay đổi một lần.</small>
                </div>

                <div class="form-group">
                    <label class="form-label">Tên</label>
                    <input type="text" name="name" placeholder="Nhập tên của bạn" value="${user.name}">
                </div>

                <div class="form-group">
                    <label class="form-label">Email</label>
                    <!-- Email lấy từ DB -->
                    <input type="email" name="email" value="${user.email}" readonly>
                    <!-- Có thể cập nhật email qua chức năng khác -->
                    <a href="#" style="color: blue;">Thay Đổi</a>
                </div>

                <div class="form-group">
                    <label class="form-label">Số điện thoại</label>
                </div>


                <!-- Save Button -->
                <button type="submit" class="save-button">Lưu</button>
            </form>

            <!-- Avatar Upload -->
            <div class="avatar-upload">
                <button type="button">Chọn Ảnh</button>
                <p>Dung lượng file tối đa: 1 MB<br>Dạng file: JPEG, PNG</p>
            </div>
        </div>
    </div>

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    </body>
    </html>