package service;

import model.User;  // Chuyển về package model (chữ thường)
import java.util.List;

public interface IUserService {
    boolean login(String username, String password);
    String register(User user);
    boolean isUsernameExists(String username);
    String getIdByUsername(String username);
    User getByUsername(String username);
    User getById(Integer id); // Chỉnh lại theo id kiểu Integer cho phù hợp Model
    boolean isEmailExists(String email);
    void resetPass(String email, String password);
    List<User> findAll();
    void deleteById(Integer id); // đổi sang Integer cho thống nhất
    void update(User user);
    void add(User user, String role);

    void addGoogleUser(User newUser);

    String createId(); // Có thể bỏ nếu bạn đã dùng auto_increment
    boolean isUserLocked(String username);
}
