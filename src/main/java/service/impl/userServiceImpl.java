package service.impl;

import DAO.IRoleDao;
import DAO.IUserDao;
import DAO.impl.roleDaoImpl;
import DAO.impl.userDaoImpl;
import Model.Role;
import Model.User;
import service.IUserService;

import java.util.List;
import java.util.UUID;

public class userServiceImpl implements IUserService {
    private IUserDao DAO = new userDaoImpl();
    private IRoleDao roleDao = new roleDaoImpl();

    @Override
    public boolean login(String username, String password) {
        User user = new User();
        user.setUser_name(username);
        user.setPassword(password);
        return DAO.login(user);
    }

    @Override
    public String register(User user) {
        user.setId(createId());
        User userNew = DAO.register(user);
        return userNew == null ? null : userNew.getId();
    }

    @Override
    public boolean isUsernameExists(String username) {
        return DAO.isUsernameExists(username);
    }

    @Override
    public String getIdByUsername(String username) {
        return DAO.getIdByUsername(username);
    }

    @Override
    public User getByUsername(String username) {
        return DAO.getByUsername(username);
    }

    @Override
    public User getById(String id) {
        return DAO.getById(id);
    }

    @Override
    public boolean isEmailExists(String email) {
        return DAO.isEmailExists(email);
    }

    @Override
    public void resetPass(String email, String password) {
        DAO.resetPass(email, password);
    }

    @Override
    public List<User> findAll() {
        return DAO.findAll();
    }

    @Override
    public void deleteById(String id) {
        DAO.deleteById(id);
    }

    @Override
    public void update(User user) {
        DAO.update(user);
    }

    @Override
    public void add(User user, String role) {
        user.setRole_idStr(roleDao.getByName(role).getId());
        user.setId(createId());
        DAO.add(user);
    }

    @Override
    public void addGoogleUser(User user) {
        DAO.addGoogleUser(user);
    }

    public String createId() {
        // Tạo ID dựa trên UUID
        String newId = "u_" + UUID.randomUUID().toString();

        if (DAO.isIdExists(newId)) {
            return createId(); // Gọi lại nếu ID đã tồn tại
        }

        return newId;
    }


}
