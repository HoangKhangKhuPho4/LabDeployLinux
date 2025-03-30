package dao;
import db.JDBIConnector;
import model.OrderDetails;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import java.util.List;
import java.util.Optional;

public interface IOrderDetailtsDAO extends DAO<OrderDetails> {
    // Insert order item
    default long insert(Handle handle, OrderDetails orderdetails) {
        String sql = "INSERT INTO order_item (orderId, productId, price, discount, quantity, createdAt, updatedAt) " +
                "VALUES (:orderId, :productId, :price, :discount, :quantity, :createdAt, :updatedAt)";
        Update update = handle.createUpdate(sql)
                .bind("orderId", orderdetails.getOrder().getId())
                .bind("productId", orderdetails.getProduct().getId())
                .bind("price", orderdetails.getProduct().getPrice())
                .bind("discount", orderdetails.getDiscount())
                .bind("quantity", orderdetails.getQuantity())
                .bind("OrderDate", orderdetails.getOrder().getOrderDate())
                .bind("DeliveryDate", orderdetails.getOrder().getDeliveryDate());
        update.execute();

        // Retrieve generated ID
        return handle.createQuery("SELECT LAST_INSERT_ID()")
                .mapTo(Long.class)
                .findOnly();
    }

    // Update order item
    default void update(Handle handle, OrderDetails orderdetails) {
        String sql = "UPDATE order_item SET orderId = :orderId, productId = :productId, price = :price, " +
                "discount = :discount, quantity = :quantity, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id";
        Update update = handle.createUpdate(sql)
                .bind("orderId", orderdetails.getOrder().getId())
                .bind("productId", orderdetails.getProduct().getId())
                .bind("price", orderdetails.getProduct().getPrice())
                .bind("discount", orderdetails.getDiscount())
                .bind("quantity", orderdetails.getQuantity())
                .bind("OrderDate", orderdetails.getOrder().getOrderDate())
                .bind("DeliveryDate", orderdetails.getOrder().getDeliveryDate());
        update.execute();
    }

    // Delete order item by ID
    default void delete(Handle handle, long id) {
        String sql = "DELETE FROM order_item WHERE id = :id";
        handle.createUpdate(sql)
                .bind("id", id)
                .execute();
    }

    // Get order item by ID
    default Optional<OrderDetails> getById(Handle handle, long id) {
        String sql = "SELECT * FROM order_item WHERE id = :id";
        Query query = handle.createQuery(sql)
                .bind("id", id);
        return Optional.ofNullable(query.mapToBean(OrderDetails.class).findOnly());
    }

    // Get all order items
    default List<OrderDetails> getAll(Handle handle) {
        String sql = "SELECT * FROM order_item";
        Query query = handle.createQuery(sql);
        return query.mapToBean(OrderDetails.class).list();
    }

    // Get a part of order items
    default List<OrderDetails> getPart(Handle handle, int limit, int offset) {
        String sql = "SELECT * FROM order_item LIMIT :limit OFFSET :offset";
        Query query = handle.createQuery(sql)
                .bind("limit", limit)
                .bind("offset", offset);
        return query.mapToBean(OrderDetails.class).list();
    }

    // Get ordered part of order items with ordering
    default List<OrderDetails> getOrderedPart(Handle handle, int limit, int offset, String orderBy, String orderDir) {
        String sql = String.format("SELECT * FROM order_item ORDER BY %s %s LIMIT :limit OFFSET :offset", orderBy, orderDir);
        Query query = handle.createQuery(sql)
                .bind("limit", limit)
                .bind("offset", offset);
        return query.mapToBean(OrderDetails.class).list();
    }

    // Bulk insert order items
    default void bulkInsert(List<OrderDetails> orderdetails) {
        JDBIConnector.getConnect().withHandle(handle -> {
            // Tạo một đối tượng PreparedBatch
            String sql = "INSERT INTO order_item (orderId, productId, price, discount, quantity, createdAt, updatedAt) " +
                    "VALUES (:orderId, :productId, :price, :discount, :quantity, :createdAt, :updatedAt)";

            // Sử dụng PreparedBatch để thực hiện batch
            var batch = handle.prepareBatch(sql);

            // Thêm các câu lệnh vào batch
            for (OrderDetails orderDetail : orderdetails) {
                batch.bind("orderId", orderDetail.getOrder().getId())
                        .bind("orderId", orderDetail.getOrder().getId())
                        .bind("productId", orderDetail.getProduct().getId())
                        .bind("price", orderDetail.getProduct().getPrice())
                        .bind("discount", orderDetail.getDiscount())
                        .bind("quantity", orderDetail.getQuantity())
                        .bind("OrderDate", orderDetail.getOrder().getOrderDate())
                        .bind("DeliveryDate", orderDetail.getOrder().getDeliveryDate());
                         batch.add();  // Thêm câu lệnh vào batch
            }

            // Thực thi batch
            batch.execute();  // Thực thi tất cả các câu lệnh trong batch
            return null;
        });
    }
    // Get product names by order ID
    default List<String> getProductNamesByOrderId(Handle handle, long orderId) {
        String sql = "SELECT name FROM product p JOIN order_item o ON p.id = o.productId WHERE o.orderId = :orderId";
        Query query = handle.createQuery(sql)
                .bind("orderId", orderId);
        return query.mapTo(String.class).list();
    }

    // Get order items by order ID
    default List<OrderDetails> getByOrderId(Handle handle, long orderId) {
        String sql = "SELECT * FROM order_item WHERE orderId = :orderId";
        Query query = handle.createQuery(sql)
                .bind("orderId", orderId);
        return query.mapToBean(OrderDetails.class).list();
    }
}
