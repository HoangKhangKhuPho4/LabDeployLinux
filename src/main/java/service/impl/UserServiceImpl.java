package service.impl;

import dao.IRoleDao;
import dao.IUserDao;
import dao.impl.RoleDaoImpl;
import dao.impl.UserDaoImpl;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.IUserService;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements IUserService {


    @Override
    public boolean login(String username, String password) {
        return false;
    }

    @Override
    public String register(User user) {
        return "";
    }

    @Override
    public boolean isUsernameExists(String username) {
        return false;
    }

    @Override
    public String getIdByUsername(String username) {
        return "";
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User getById(Integer id) {
        return null;
    }

    @Override
    public boolean isEmailExists(String email) {
        return false;
    }

    @Override
    public void resetPass(String email, String password) {

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
        return "";
    }

    @Override
    public boolean isUserLocked(String username) {
        return false;
    }
}
