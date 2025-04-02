package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import db.JDBIConnector;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.IUserService;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements IUserService {

    @Override
    public boolean login(String username, String password) {
        IUserDao userDao = new UserDaoImpl();
        User user = userDao.getUserByUserName(username);
        if(user == null) {
            return false;
        }
        // So sánh mật khẩu nhập với mật khẩu đã băm trong DB
        return BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public String register(User user) {
        // Băm mật khẩu trước khi lưu
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        IUserDao userDao = new UserDaoImpl();
        boolean result = userDao.register(user);
        return result ? "registered" : null;
    }

    @Override
    public boolean isUsernameExists(String username) {
        IUserDao userDao = new UserDaoImpl();
        return userDao.isUserNameExists(username);
    }

    @Override
    public String getIdByUsername(String username) {
        // TODO: triển khai
        return "";
    }

    @Override
    public User getByUsername(String username) {
        IUserDao userDao = new UserDaoImpl();
        return userDao.getUserByUserName(username);
    }

    @Override
    public User getById(Integer id) {
        // TODO: triển khai
        return null;
    }

    @Override
    public boolean isEmailExists(String email) {
        IUserDao userDao = new UserDaoImpl();
        return userDao.isEmailExists(email);
    }

    @Override
    public void resetPass(String email, String password) {
        try {
            // Băm mật khẩu mới trước khi update vào DB
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            int rowsAffected = JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createUpdate("UPDATE users SET password = ?, updated_at = ? WHERE email = ?")
                        .bind(0, hashedPassword)
                        .bind(1, new Timestamp(System.currentTimeMillis()))
                        .bind(2, email)
                        .execute();
            });
            if (rowsAffected == 0) {
                System.out.println("Reset pass update failed for email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }
    @Override
    public void update(User user) {
        try {
            JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createUpdate("UPDATE users SET password = ?, updated_at = ? WHERE id = ?")
                        .bind(0, user.getPassword())
                        .bind(1, java.sql.Timestamp.valueOf(user.getUpdatedAt()))
                        .bind(2, user.getId())
                        .execute();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(User user, String role) {

    }

    @Override
    public void addGoogleUser(User newUser) {

    }

    @Override
    public String createId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isUserLocked(String username) {
        return false;
    }
}