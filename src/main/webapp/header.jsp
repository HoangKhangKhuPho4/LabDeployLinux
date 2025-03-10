<%--
  Created by IntelliJ IDEA.
  User: hadun
  Date: 11/12/2023
  Time: 10:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="Cart.Cart" %>
<%@ page import="utils.SessionUtil" %>
<%@ page import="service.impl.userServiceImpl" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Cart cart = (Cart) session.getAttribute("cart");
    if(cart == null) cart = new Cart();
%>
<html>
<head>
    <title>Header</title>
    <link rel="icon" type="image/png" href="./img/logo.png"/>
    <style>
        .autocomplete-suggestions {
            position: absolute;
            top: 100%;
            left: 0;
            right: 0;
            background-color: #ffffff;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            z-index: 10;
            max-height: 300px;
            overflow-y: auto;
            width: 100%;
            display: none;
            padding: 0;
            margin: 0;
            list-style: none;
        }

        .autocomplete-item {
            padding: 10px;
            cursor: pointer;
            font-size: 14px;
            color: #333;
            transition: background-color 0.2s ease, padding-left 0.2s ease;
        }

        .autocomplete-item:hover {
            background-color: #f0f0f0;
            padding-left: 10px;
        }

        .autocomplete-item:active {
            background-color: #e0e0e0;
        }
    </style>
</head>
<body>
<!-- HEADER -->
<header>
    <!-- TOP HEADER -->
    <div id="top-header">
        <div class="container">
            <ul class="header-links pull-left">
                <li><a href="#"><i class="fa fa-phone"></i> 0973206403</a></li>
                <li><a href="#"><i class="fa fa-envelope-o"></i> hadung6765@gmail.com</a></li>
                <li><a href="#"><i class="fa fa-map-marker"></i>Quận Thủ Đức - Tp.Hồ Chí Minh</a></li>
            </ul>
            <ul class="header-links pull-right">
                <c:if test="${sessionScope.user == null}">
                    <li><a href="dangky.jsp"><i class="fa fa-user-o"></i> Đăng kí</a></li>
                    <li><a href="dangnhap.jsp"><i class="fa fa-user-o"></i> Đăng nhập</a></li>
                </c:if>
                <c:if test="${sessionScope.user != null}">
                    <li><a><i class="fa fa-user-o"></i> <%= new userServiceImpl().getById(SessionUtil.getInstance().getKey((HttpServletRequest) request, "user").toString()).getName() %></a></li>
                    <li><a href="logout"><i class="fa fa-user-o"></i> Đăng xuất</a></li>
                </c:if>
            </ul>
        </div>
    </div>
    <!-- /TOP HEADER -->

    <!-- MAIN HEADER -->
    <div id="header">
        <!-- container -->
        <div class="container">
            <!-- row -->
            <div class="row">
                <!-- LOGO -->
                <div class="col-md-3">
                    <div class="header-logo">
                        <a href="#" class="logo">
                            <img src="img/logo.png" alt="">
                        </a>
                    </div>
                </div>
                <!-- /LOGO -->

                <!-- SEARCH BAR -->
                <div class="col-md-6">
                    <div class="header-search">
                        <form id="searchForm" action="search" method="get">
                            <input class="input" name="query" id="searchInput" placeholder="Tìm kiếm tại đây" autocomplete="off">
                            <input class="search-btn" type="submit" value="Tìm kiếm">
                        </form>

                        <!-- Dropdown cho autocomplete sẽ nằm ngay dưới ô tìm kiếm -->
                        <ul class="autocomplete-suggestions" id="suggestions" style="display:none; position:absolute; list-style:none; background-color:white; border:1px solid #ccc; width:100%; z-index:10;">
                            <!-- Gợi ý sẽ được chèn vào đây -->
                        </ul>
                    </div>
                </div>
                <!-- /SEARCH BAR -->

                <!-- ACCOUNT -->
                <div class="col-md-3 clearfix">
                    <div class="header-ctn">
                        <!-- Cart -->
                        <div class="dropdown">
                            <a class="dropdown-toggle" href="giohang.jsp">
                                <i class="fa fa-shopping-cart"></i>
                                <span>Giỏ Hàng</span>
                                <div class="qty"><%=cart.getTotal()%></div>  <!--Code + khi thêm vào giỏ hàng-->
                            </a>
                        </div>
                        <!-- /Cart -->

                        <!-- Menu Toogle -->
                        <div class="menu-toggle">
                            <a href="#">
                                <i class="fa fa-bars"></i>
                                <span>Menu</span>
                            </a>
                        </div>
                        <!-- /Menu Toogle -->
                    </div>
                </div>
                <!-- /ACCOUNT -->
            </div>
            <!-- row -->
        </div>
        <!-- container -->
    </div>
    <!-- /MAIN HEADER -->
</header>
<!-- /HEADER -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        // Gửi yêu cầu tìm kiếm khi người dùng nhập từ khóa vào
        $("#searchInput").on("input", function() {
            var query = $(this).val();  // Lấy giá trị từ ô tìm kiếm
            if (query.length > 0) {
                $.ajax({
                    url: "autocomplete",  // Gọi API autocomplete
                    method: "GET",
                    data: { query: query },  // Truyền từ khóa tìm kiếm vào API
                    success: function(response) {
                        if (response && response.length > 0) {
                            var suggestionList = "";
                            // Tạo danh sách các sản phẩm dưới dạng <li>
                            response.forEach(function(product) {
                                suggestionList += "<li class='autocomplete-item' data-id='" + product.id + "'>" + product.name + "</li>";
                            });
                            $("#suggestions").html(suggestionList).show();  // Hiển thị dropdown
                        } else {
                            $("#suggestions").hide();  // Ẩn dropdown nếu không có kết quả
                        }
                    },
                    error: function() {
                        $("#suggestions").hide();  // Ẩn dropdown nếu có lỗi
                    }
                });
            } else {
                $("#suggestions").hide();  // Ẩn dropdown nếu không có từ khóa tìm kiếm
            }
        });

        // Khi người dùng click vào một gợi ý trong dropdown
        $(document).on("click", ".autocomplete-item", function() {
            var productId = $(this).data("id");  // Lấy id sản phẩm đã click
            $("#searchInput").val($(this).text());  // Điền vào ô tìm kiếm
            $("#suggestions").hide();  // Ẩn danh sách gợi ý

            // Điều hướng tới trang danh mục sản phẩm với ID sản phẩm đã chọn
            window.location.href = "sanpham.jsp?id=" + productId;  // Điều hướng tới trang chi tiết sản phẩm
        });


        // Khi người dùng nhấn Enter trong ô tìm kiếm
        $("#searchForm").on("submit", function(event) {
            event.preventDefault();  // Ngừng gửi form
            var query = $("#searchInput").val();
            if (query.length > 0) {
                // Chuyển hướng tới trang danh mục sản phẩm với từ khóa tìm kiếm
                window.location.href = "sanpham.jsp?query=" + query;  // Điều hướng tới trang danh mục sản phẩm
            }
        });
    });
</script>

</body>
</html>
