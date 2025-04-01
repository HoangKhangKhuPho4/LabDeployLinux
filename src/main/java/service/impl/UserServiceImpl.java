package service.impl;

import dao.IUserDao;
import dao.impl.UserDaoImpl;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.IUserService;

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
        // TODO: triển khai
        return false;
    }

    @Override
    public void resetPass(String email, String password) {
        // TODO: triển khai
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