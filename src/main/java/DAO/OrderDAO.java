package DAO;

import Model.*;
import db.JDBIConector;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class OrderDAO implements DAOInterface<Order> {
    @Override
    public List<Order> selectAll() {
        List<Order> ketQua = JDBIConector.me().withHandle((handle ->
        {
            List<Order> orders = new ArrayList<>();

            handle.createQuery("SELECT id, user_id, delivery_address, order_status, payment_method, " +
                            "order_date, delivery_date, amount FROM orders")
                    .map((rs, ctx) -> {
                        String id = rs.getString("id");
                        String userId = rs.getString("user_id");
                        String deliveryAddress = rs.getString("delivery_address");
                        String orderStatus = rs.getString("order_status");
                        String paymentMethod = rs.getString("payment_method");
                        Date orderDate = rs.getDate("order_date");
                        Date deliveryDate = rs.getDate("delivery_date");
                        double amount = rs.getDouble("amount");

                        User user = new UserDAO().selectById(new User(userId, null, null, null, null,
                                null, null, null, null,null));

                        Order order = new Order(id, user, deliveryAddress, orderStatus, paymentMethod, orderDate, deliveryDate, amount);
                        orders.add(order);

                        return null;
                    })
                    .list();

            return orders;
        }));

        return ketQua;
    }

    public List<Order> selectAllById(String idUser) {
        List<Order> ketQua = JDBIConector.me().withHandle(handle -> {
            List<Order> orders = new ArrayList<>();

            handle.createQuery("SELECT id, user_id, delivery_address, order_status, payment_method, " +
                            "order_date, delivery_date, amount FROM orders WHERE user_id = :user_id")
                    .bind("user_id", idUser)
                    .map((rs, ctx) -> {
                        String id = rs.getString("id");
                        String userId = rs.getString("user_id");
                        String deliveryAddress = rs.getString("delivery_address");
                        String orderStatus = rs.getString("order_status");
                        String paymentMethod = rs.getString("payment_method");
                        Date orderDate = rs.getDate("order_date");
                        Date deliveryDate = rs.getDate("delivery_date");
                        double amount = rs.getDouble("amount");
                        User user = new UserDAO().selectById(new User(userId, null, null, null, null,
                                null, null, null, null, null));

                        Order order = new Order(id, user, deliveryAddress, orderStatus, paymentMethod, orderDate, deliveryDate, amount);
                        orders.add(order);

                        return null;
                    })
                    .list();

            return orders;
        });

        return ketQua;
    }

    @Override
    public Order selectById(Order orderP) {
        try {
            return JDBIConector.me().withHandle(handle ->
                    handle.createQuery("SELECT id, user_id, delivery_address, order_status," +
                                    " payment_method, order_date, delivery_date, amount FROM orders WHERE id=?")
                            .bind(0, orderP.getId())
                            .map((rs, ctx) -> {
                                String orderId = rs.getString("id");
                                String userId = rs.getString("user_id");
                                String address = rs.getString("delivery_address");
                                String orderStatus = rs.getString("order_status");
                                String paymentMethod = rs.getString("payment_method");
                                Date orderDate = rs.getDate("order_date");
                                Date deliveryDate = rs.getDate("delivery_date");
                                double amount = rs.getDouble("amount");

                                User user = new UserDAO().selectById(new User(userId, null, null, null, null, null, null, null, null, null));
                                return new Order(orderId, user, address, orderStatus, paymentMethod, orderDate, deliveryDate, amount);
                            })
                            .findFirst()
                            .orElse(null)
            );
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi để debug
            return null;
        }
    }

    public static Order getById(String id) {
        try {
            return JDBIConector.me().withHandle(handle ->
                    handle.createQuery("SELECT id, user_id, delivery_address, order_status," +
                                    " payment_method, order_date, delivery_date, amount FROM orders WHERE id=?")
                            .bind(0, id)
                            .map((rs, ctx) -> {
                                String orderId = rs.getString("id");
                                String userId = rs.getString("user_id");
                                String address = rs.getString("delivery_address");
                                String orderStatus = rs.getString("order_status");
                                String paymentMethod = rs.getString("payment_method");
                                Date orderDate = rs.getDate("order_date");
                                Date deliveryDate = rs.getDate("delivery_date");
                                double amount = rs.getDouble("amount");

                                User user = new UserDAO().selectById(new User(userId, null, null, null, null, null, null, null, null, null));
                                return new Order(orderId, user, address, orderStatus, paymentMethod, orderDate, deliveryDate, amount);
                            })
                            .findFirst()
                            .orElse(null)
            );
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi để debug
            return null;
        }
    }

    public int updateOrderStatus(String orderId, String status) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "UPDATE orders SET order_status = ? WHERE id = ?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, status);
            st.setString(2, orderId);

            // Bước 3: thực thi câu lệnh SQL
            ketQua = st.executeUpdate();

            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Có " + ketQua + " dòng bị thay đổi!");

            // Bước 5:
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ketQua;
    }

    @Override
    public int insert(Order order) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "INSERT INTO orders (id, user_id, delivery_address, order_status, payment_method, order_date, delivery_date, amount) " +
                    " VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, order.getId());
            if (order.getUser() != null) {
                st.setString(2, order.getUser().getId());
            } else {
                st.setNull(2, Types.VARCHAR);
            }
            st.setString(3, order.getAddress());
            st.setString(4, order.getStatus());
            st.setString(5, order.getPayMent());
            st.setDate(6, order.getOrderDate());
            st.setDate(7, order.getDeliveryDate());
            st.setDouble(8, order.getAmount());


            // Bước 3: thực thi câu lệnh SQL
            ketQua = st.executeUpdate();

            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Có " + ketQua + " dòng bị thay đổi!");

            // Bước 5:
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ketQua;
    }


    @Override
    public int delete(Order order) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "DELETE from orders "+
                    " WHERE id=?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, order.getId());

            // Bước 3: thực thi câu lệnh SQL
            System.out.println(sql);
            ketQua = st.executeUpdate();

            // Bước 4:
            System.out.println("Bạn đã thực thi: "+ sql);
            System.out.println("Có "+ ketQua+" dòng bị thay đổi!");

            // Bước 5:
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ketQua;
    }

    @Override
    public int update(Order order) {

        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "UPDATE orders "+
                    " SET " +
                    " user_id=?"+","+
                    " delivery_address=?"+","+
                    " order_status=?"+","+
                    " payment_method=?"+","+
                    " order_date=?"+","+
                    " delivery_date=?"+
                    " WHERE id=?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, order.getUser().getId());
            st.setString(2, order.getAddress());
            st.setString(3, order.getStatus());
            st.setString(4, order.getPayMent());
            st.setDate(5, order.getOrderDate());
            st.setDate(6, order.getDeliveryDate());
            st.setString(7, order.getId());


            // Bước 3: thực thi câu lệnh SQL

            System.out.println(sql);
            ketQua = st.executeUpdate();

            // Bước 4:
            System.out.println("Bạn đã thực thi: "+ sql);
            System.out.println("Có "+ ketQua+" dòng bị thay đổi!");

            // Bước 5:
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ketQua;
    }

    public int countOrdersInMonth(LocalDate localDate) {
        int count = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT COUNT(*) FROM orders WHERE MONTH(order_date) = ? AND YEAR(order_date) = ?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, localDate.getMonthValue());
            st.setInt(2, localDate.getYear());

            // Bước 3: thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Số lượng đơn hàng trong tháng: " + count);

            // Bước 5:
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    public int countCustomersWithOrdersInMonth(LocalDate localDate) {
        int count = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "SELECT COUNT(DISTINCT user_id) FROM orders WHERE MONTH(order_date) = ? AND YEAR(order_date) = ?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setInt(1, localDate.getMonthValue());
            st.setInt(2, localDate.getYear());

            // Bước 3: thực thi câu lệnh SQL
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

            // Bước 4:
            System.out.println("Bạn đã thực thi: " + sql);
            System.out.println("Số lượng khách hàng đã mua hàng trong tháng: " + count);

            // Bước 5:
            JDBCUtil.closeConnection(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static boolean updateOrderStatusWithoutAudit(String orderId, String newStatus) {
        String sql = "UPDATE orders SET order_status = ? WHERE id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setString(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getOrderIdByInfo(Order order) throws SQLException {
        String sql = "SELECT id FROM Orders " +
                "WHERE user_id = ? AND delivery_address = ? AND payment_method = ? " +
                "AND order_date = ? AND delivery_date = ? AND order_status = ? AND amount = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số cho truy vấn
            stmt.setString(1, order.getUser().getId());
            stmt.setString(2, order.getAddress());
            stmt.setString(3, order.getPayMent());
            stmt.setDate(4, new java.sql.Date(order.getOrderDate().getTime()));
            stmt.setDate(5, new java.sql.Date(order.getDeliveryDate().getTime()));
            stmt.setString(6, order.getStatus());
            stmt.setDouble(7, order.getAmount());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id");
                } else {
                    throw new SQLException("Order not found for the given details.");
                }
            }
        }
    }


    public static void updateOrderStatus(String orderId, boolean isVerified) throws SQLException {
        String sql = "UPDATE Orders SET status = ? WHERE id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isVerified);
            stmt.setString(2, orderId);
            stmt.executeUpdate();
        }
    }

    public static boolean isDataModified(String orderId) {
        String sql = "SELECT COUNT(*) FROM audit_log WHERE (table_name = 'order' OR table_name = 'order_details') AND record_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean restoreOrder(String orderId) {
        String selectAuditSql = "SELECT table_name, record_id, column_name, old_value " +
                "FROM audit_log WHERE (table_name = 'order' OR table_name = 'order_details') " +
                "AND record_id = ? ORDER BY action_date DESC";
        String restoreOrderSql = "UPDATE orders SET user_id = ?, delivery_address = ?, " +
                "order_status = ?, payment_method = ?, order_date = ?, " +
                "delivery_date = ?, amount = ? WHERE id = ?";
        String restoreOrderDetailsSql = "UPDATE order_details SET product_id = ?, quantity = ?, " +
                "price = ?, discount = ?, amount = ? WHERE id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectAuditSql);
             PreparedStatement restoreOrderStmt = conn.prepareStatement(restoreOrderSql);
             PreparedStatement restoreOrderDetailsStmt = conn.prepareStatement(restoreOrderDetailsSql)) {

            // Lưu giá trị khôi phục cho `orders`
            String userId = null, deliveryAddress = null, orderStatus = null, paymentMethod = null;
            Date orderDate = null, deliveryDate = null;
            Double orderAmount = null;

            // Lưu giá trị khôi phục cho `order_details`
            Map<String, Map<String, Object>> orderDetailsRestoreMap = new HashMap<>();

            selectStmt.setString(1, orderId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    String tableName = rs.getString("table_name");
                    String columnName = rs.getString("column_name");
                    String oldValue = rs.getString("old_value");

                    if ("order".equals(tableName)) {
                        // Khôi phục dữ liệu từ bảng `order`
                        switch (columnName) {
                            case "user_id": userId = oldValue; break;
                            case "delivery_address": deliveryAddress = oldValue; break;
                            case "order_status": orderStatus = oldValue; break;
                            case "payment_method": paymentMethod = oldValue; break;
                            case "order_date": orderDate = oldValue != null ? Date.valueOf(oldValue) : null; break;
                            case "delivery_date": deliveryDate = oldValue != null ? Date.valueOf(oldValue) : null; break;
                            case "amount": orderAmount = oldValue != null ? Double.valueOf(oldValue) : null; break;
                        }
                    } else if ("order_details".equals(tableName)) {
                        // Khôi phục dữ liệu từ bảng `order_details`
                        String detailId = rs.getString("record_id");
                        Map<String, Object> detailsMap = orderDetailsRestoreMap.getOrDefault(detailId, new HashMap<>());
                        switch (columnName) {
                            case "product_id": detailsMap.put("product_id", oldValue); break;
                            case "quantity": detailsMap.put("quantity", Integer.valueOf(oldValue)); break;
                            case "price": detailsMap.put("price", Double.valueOf(oldValue)); break;
                            case "discount": detailsMap.put("discount", Double.valueOf(oldValue)); break;
                            case "amount": detailsMap.put("amount", Double.valueOf(oldValue)); break;
                        }
                        orderDetailsRestoreMap.put(detailId, detailsMap);
                    }
                }
            }

            // Khôi phục dữ liệu bảng `order`
            restoreOrderStmt.setString(1, userId);
            restoreOrderStmt.setString(2, deliveryAddress);
            restoreOrderStmt.setString(3, orderStatus);
            restoreOrderStmt.setString(4, paymentMethod);
            restoreOrderStmt.setDate(5, orderDate);
            restoreOrderStmt.setDate(6, deliveryDate);
            restoreOrderStmt.setDouble(7, orderAmount);
            restoreOrderStmt.setString(8, orderId);
            int orderRowsAffected = restoreOrderStmt.executeUpdate();

            // Khôi phục dữ liệu bảng `order_details`
            for (Map.Entry<String, Map<String, Object>> entry : orderDetailsRestoreMap.entrySet()) {
                String detailId = entry.getKey();
                Map<String, Object> detailsMap = entry.getValue();

                restoreOrderDetailsStmt.setString(1, (String) detailsMap.get("product_id"));
                restoreOrderDetailsStmt.setInt(2, (Integer) detailsMap.get("quantity"));
                restoreOrderDetailsStmt.setDouble(3, (Double) detailsMap.get("price"));
                restoreOrderDetailsStmt.setDouble(4, (Double) detailsMap.get("discount"));
                restoreOrderDetailsStmt.setDouble(5, (Double) detailsMap.get("amount"));
                restoreOrderDetailsStmt.setString(6, detailId);
                restoreOrderDetailsStmt.addBatch();
            }
            int[] detailsRowsAffected = restoreOrderDetailsStmt.executeBatch();

            return orderRowsAffected > 0 || detailsRowsAffected.length > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }





    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();
        System.out.println(isDataModified("or_1"));
        //orderDAO.insert(new Order("or_10", UserDAO.getById("u_4"), "vv", "Chưa xác thực", "cc", null, null, 0));
    }
}