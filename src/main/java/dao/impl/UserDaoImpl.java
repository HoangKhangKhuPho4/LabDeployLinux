package dao.impl;

import dao.IUserDao;
import db.JDBIConnector;
import model.User;

import java.sql.Timestamp;
import java.util.List;

public class UserDaoImpl implements IUserDao {
    private static final String SELECT_USER = "SELECT id, username, password, oauth_provider, oauth_uid, oauth_token, name, email, picture, role_id, created_at, updated_at, status, secret_key, twoFaEnabled FROM users";
    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

    @Override
    public boolean login(String username, String password) {
        return true;
    }

    @Override
    public boolean register(User user) {
        try {
            int rowsAffected = JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createUpdate("INSERT INTO users(username, password, oauth_provider, oauth_uid, oauth_token, name, email, picture, role_id, created_at, updated_at, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                        .bind(0, user.getUsername())
                        .bind(1, user.getPassword())
                        .bind(2, user.getOauthProvider())  // Có thể null
                        .bind(3, user.getOauthUid())       // Có thể null
                        .bind(4, user.getOauthToken())     // Có thể null
                        .bind(5, user.getName())
                        .bind(6, user.getEmail())
                        .bind(7, user.getPicture())  // Giá trị cho cột picture
                        .bind(8, user.getRoleId())
                        .bind(9, user.getCreatedAt())
                        .bind(10, user.getUpdatedAt())
                        .bind(11, user.getStatus())
                        .execute();
            });
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi để debug
            return false; // Trả về false nếu có lỗi
        }
    }

    @Override
    public boolean isUserNameExists(String username) {
        try {
            int count = JDBIConnector.getConnect().withHandle(handle ->
                    handle.createQuery("SELECT COUNT(*) FROM users WHERE username = :username")
                            .bind("username", username)
                            .mapTo(Integer.class)
                            .one()
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi để debug
            return false; // Trả về false nếu có lỗi
        }
    }

    @Override
    public String getIdByUserName(String username) {
        return "";
    }

    @Override
    public User getUserByUserName(String username) {
        try {
            return JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createQuery(SELECT_USER + " WHERE username = :username")
                        .bind("username", username)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null);
            });
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi để debug
            return null; // Trả về null nếu có lỗi
        }
    }

    @Override
    public User getUserByUserId(Integer userId) {
        try {
            return JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createQuery(SELECT_USER + " WHERE id = :userId")
                        .bind("userId", userId)
                        .mapToBean(User.class)
                        .findFirst()
                        .orElse(null);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        try {
            int count = JDBIConnector.getConnect().withHandle(handle ->
                    handle.createQuery("SELECT COUNT(*) FROM users WHERE email = :email")
                            .bind("email", email)
                            .mapTo(Integer.class)
                            .one()
            );
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi để debug
            return false; // Trả về false nếu có lỗi
        }
    }

    @Override
    public void resetPass(String email, String password) {
        // TODO: Triển khai reset mật khẩu
    }

    @Override
    public List<User> findAll() {
        try {
            return JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createQuery(SELECT_USER)
                        .mapToBean(User.class)
                        .list();
            });
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public void deleteById(Integer id) {
        try {
            JDBIConnector.getConnect().useHandle(handle ->
                    handle.createUpdate("UPDATE users SET status = 0 WHERE id = :id")
                            .bind("id", id)
                            .execute()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(User user) {
        try {
            // Nếu cần cập nhật thông tin 2FA (secret_key, twoFaEnabled), bạn có thể cập nhật thêm vào truy vấn UPDATE
            int rowsAffected = JDBIConnector.getConnect().withHandle(handle -> {
                return handle.createUpdate("UPDATE users SET username = ?, password = ?, name = ?, email = ?, updated_at = ?, role_id = ?, secret_key = ?, twoFaEnabled = ? WHERE id = ?")
                        .bind(0, user.getUsername())
                        .bind(1, user.getPassword())
                        .bind(2, user.getName())
                        .bind(3, user.getEmail())
                        .bind(4, user.getUpdatedAt())
                        .bind(5, user.getRoleId())
                        .bind(6, user.getSecretKey())
                        .bind(7, user.isTwoFaEnabled())
                        .bind(8, user.getId())
                        .execute();
            });
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User isUserExists(String oauthProvider, String oauthUid) {
        User user = JDBIConnector.getConnect().withHandle(handle -> {
            return handle.createQuery(SELECT_USER + " WHERE oauth_provider = :oauthProvider AND oauth_uid = :oauthUid")
                    .bind("oauthProvider", oauthProvider)
                    .bind("oauthUid", oauthUid)
                    .mapToBean(User.class)
                    .findFirst()
                    .orElse(null);
        });
        return user;
    }

    @Override
    public User getByOAuthUser(String oauthUid) {
        try {
            return JDBIConnector.getConnect().withHandle(handle ->
                    handle.createQuery(SELECT_USER + " WHERE oauth_uid = :oauthUid")
                            .bind("oauthUid", oauthUid)
                            .mapToBean(User.class)
                            .findFirst().orElse(null)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addGoogleUser(User user) {
        int rowsAffected = JDBIConnector.getConnect().withHandle(handle -> {
            // Cập nhật câu lệnh INSERT để thêm giá trị cho cột picture
            return handle.createUpdate("INSERT INTO users(username, password, oauth_provider, oauth_uid, oauth_token, name, email, picture, role_id, created_at, updated_at, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")
                    .bind(0, user.getUsername())
                    .bind(1, user.getPassword())
                    .bind(2, user.getOauthProvider())
                    .bind(3, user.getOauthUid())
                    .bind(4, user.getOauthToken())
                    .bind(5, user.getName())
                    .bind(6, user.getEmail())
                    .bind(7, user.getPicture())
                    .bind(8, user.getRoleId())
                    .bind(9, user.getCreatedAt())
                    .bind(10, user.getUpdatedAt())
                    .bind(11, user.getStatus())
                    .execute();
        });
        return rowsAffected > 0;
    }
}
