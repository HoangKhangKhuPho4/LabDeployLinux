package DAO;

import Model.Keys;
import Model.User;
import db.JDBIConector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KeysDAO implements DAOInterface<Keys>{
    @Override
    public List<Keys> selectAll() {
        List<Keys> ketQua = JDBIConector.me().withHandle((handle ->
        {
            List<Keys> users = new ArrayList<>();
            handle.createQuery("SELECT id, user_id, public_key, create_time, end_time FROM public_keys")
                    .map((rs, ctx) -> {
                        int id = rs.getInt("id");
                        String user_id = rs.getString("user_id");
                        String public_key = rs.getString("public_key");
                        Date create_time = rs.getDate("create_time");
                        Date end_time = rs.getDate("end_time");

                        User user = new UserDAO().selectById(new User(user_id, null, null, null, null,
                                null, null, null, null,null));
                        Keys keys = new Keys(id, user, public_key, create_time, end_time);
                        users.add(keys);

                        return null;
                    }).list();
            return users;
        }));
        return ketQua;
    }

    @Override
    public Keys selectById(Keys keys) {
        try {
            return JDBIConector.me().withHandle(handle ->
                    handle.createQuery("SELECT id, user_id, public_key, create_time, end_time FROM public_keys WHERE id=?")
                            .bind(0, keys.getId())
                            .map((rs, ctx) -> {
                                int id = rs.getInt("id");
                                String user_id = rs.getString("user_id");
                                String public_key = rs.getString("public_key");
                                Date create_time = rs.getDate("create_time");
                                Date end_time = rs.getDate("end_time");

                                User user = new UserDAO().selectById(new User(user_id, null, null, null, null, null, null, null, null, null));
                                return new Keys(id, user, public_key, create_time, end_time);
                            })
                            .findFirst()
                            .orElse(null)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int insert(Keys keys) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "INSERT INTO public_keys (id, user_id, public_key, create_time, end_time) " +
                    " VALUES (?,?,?,?,?)";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, keys.getId());
            if (keys.getUserId() != null) {
                st.setString(2, keys.getUserId().getId());
            } else {
                st.setNull(2, Types.VARCHAR);
            }
            st.setString(3, keys.getKeys());
            st.setDate(4, keys.getCreateTime());
            st.setDate(5, keys.getEndTime());


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
    public int delete(Keys keys) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "DELETE from public_keys "+
                    " WHERE id=?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, keys.getId());

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
    public int update(Keys keys) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "UPDATE public_keys "+
                    " SET " +
                    " user_id=?"+","+
                    " public_key=?"+","+
                    " create_time=?"+","+
                    " end_time=?"+
                    " WHERE id=?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, keys.getUserId().getId());
            st.setString(2, keys.getKeys());
            st.setDate(3, keys.getCreateTime());
            st.setDate(4, keys.getEndTime());


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

    public static void main(String[] args) {
        KeysDAO dao = new KeysDAO();
        System.out.println(dao.selectAll());
    }

}
