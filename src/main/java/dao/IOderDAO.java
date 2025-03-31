package dao;

import model.Order;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import java.util.List;
import java.util.Optional;

public interface IOderDAO extends DAO<Order> {
    // Insert order
    default long insert(Handle handle, Order order) {
        String sql = "INSERT INTO orders (userId, status, deliveryMethod, deliveryPrice, orderDate, deliveryDate) " +
                "VALUES (:userId, :status, :deliveryMethod, :deliveryPrice, :orderDate, :deliveryDate)";
        Update update = handle.createUpdate(sql)
                .bind("userId", order.getUser().getId())
                .bind("status", order.getStatus())
                .bind("deliveryMethod", order.getDeliveryMethod())
                .bind("deliveryPrice", order.getDeliveryPrice())
                .bind("oderDate", order.getOrderDate())
                .bind("deliveryDate", order.getDeliveryDate());

        update.execute();
        // Retrieve generated ID
        return handle.createQuery("SELECT LAST_INSERT_ID()")
                .mapTo(Long.class)
                .findOnly();
    }

    // Update order
    default void update(Handle handle, Order order) {
        String sql = "UPDATE orders SET userId = :userId, status = :status, deliveryMethod = :deliveryMethod, " +
                "deliveryPrice = :deliveryPrice, createdAt = :createdAt, updatedAt = :updatedAt WHERE id = :id";
        Update update = handle.createUpdate(sql)
                .bind("id", order.getId())
                .bind("userId", order.getUser().getId())
                .bind("status", order.getStatus())
                .bind("deliveryMethod", order.getDeliveryMethod())
                .bind("deliveryPrice", order.getDeliveryPrice())
                .bind("oderDate", order.getOrderDate())
                .bind("deliveryDate", order.getDeliveryDate());

        update.execute();
    }

    // Delete order by ID
    default void delete(Handle handle, long id) {
        String sql = "DELETE FROM orders WHERE id = :id";
        handle.createUpdate(sql)
                .bind("id", id)
                .execute();
    }

    // Get order by ID
    default Optional<Order> getById(Handle handle, long id) {
        String sql = "SELECT * FROM orders WHERE id = :id";
        Query query = handle.createQuery(sql)
                .bind("id", id);
        return Optional.ofNullable(query.mapToBean(Order.class).findOnly());
    }

    // Get all orders
    default List<Order> getAll(Handle handle) {
        String sql = "SELECT * FROM orders";
        Query query = handle.createQuery(sql);
        return query.mapToBean(Order.class).list();
    }

    // Get a part of orders
    default List<Order> getPart(Handle handle, int limit, int offset) {
        String sql = "SELECT * FROM orders LIMIT :limit OFFSET :offset";
        Query query = handle.createQuery(sql)
                .bind("limit", limit)
                .bind("offset", offset);
        return query.mapToBean(Order.class).list();
    }

    // Get ordered part of orders with ordering
    default List<Order> getOrderedPart(Handle handle, int limit, int offset, String orderBy, String orderDir) {
        String sql = String.format("SELECT * FROM orders ORDER BY %s %s LIMIT :limit OFFSET :offset", orderBy, orderDir);
        Query query = handle.createQuery(sql)
                .bind("limit", limit)
                .bind("offset", offset);
        return query.mapToBean(Order.class).list();
    }

    // Get ordered part of orders by user ID
    default List<Order> getOrderedPartByUserId(Handle handle, long userId, int limit, int offset) {
        String sql = "SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit OFFSET :offset";
        Query query = handle.createQuery(sql)
                .bind("userId", userId)
                .bind("limit", limit)
                .bind("offset", offset);
        return query.mapToBean(Order.class).list();
    }

    // Count orders by user ID
    default int countByUserId(Handle handle, long userId) {
        String sql = "SELECT COUNT(id) FROM orders WHERE userId = :userId";
        Query query = handle.createQuery(sql)
                .bind("userId", userId);
        return query.mapTo(int.class).findOnly();
    }

    // Cancel order
    default void cancelOrder(Handle handle, long id) {
        String sql = "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = :id";
        handle.createUpdate(sql)
                .bind("id", id)
                .execute();
    }

    // Count all orders
    default int count(Handle handle) {
        String sql = "SELECT COUNT(id) FROM orders";
        Query query = handle.createQuery(sql);
        return query.mapTo(int.class).findOnly();
    }

    // Confirm order
    default void confirm(Handle handle, long id) {
        String sql = "UPDATE orders SET status = 2, updatedAt = NOW() WHERE id = :id";
        handle.createUpdate(sql)
                .bind("id", id)
                .execute();
    }

    // Cancel order (same as cancelOrder)
    default void cancel(Handle handle, long id) {
        String sql = "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = :id";
        handle.createUpdate(sql)
                .bind("id", id)
                .execute();
    }

    // Reset order status
    default void reset(Handle handle, long id) {
        String sql = "UPDATE orders SET status = 1, updatedAt = NOW() WHERE id = :id";
        handle.createUpdate(sql)
                .bind("id", id)
                .execute();
    }
}
