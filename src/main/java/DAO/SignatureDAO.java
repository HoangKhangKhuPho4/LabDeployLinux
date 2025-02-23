package DAO;

import Model.Keys;
import Model.Signature;
import Model.User;
import db.JDBIConector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignatureDAO implements DAOInterface<Signature> {
    @Override
    public List<Signature> selectAll() {
        List<Signature> ketQua = JDBIConector.me().withHandle((handle ->
        {
            List<Signature> users = new ArrayList<>();
            handle.createQuery("SELECT id, signature ,user_id FROM signature")
                    .map((rs, ctx) -> {
                        int id = rs.getInt("id");
                        String user_id = rs.getString("user_id");
                        String signature = rs.getString("signature");

                        User user = new UserDAO().selectById(new User(user_id, null, null, null, null,
                                null, null, null, null,null));
                        Signature sig = new Signature(id, signature ,user);
                        users.add(sig);

                        return null;
                    }).list();
            return users;
        }));
        return ketQua;
    }

    @Override
    public Signature selectById(Signature signature) {
        try {
            return JDBIConector.me().withHandle(handle ->
                    handle.createQuery("SELECT id, signature, user_id FROM signature WHERE id=?")
                            .bind(0, signature.getId())
                            .map((rs, ctx) -> {
                                int id = rs.getInt("id");
                                String sig = rs.getString("signature");
                                String user_id = rs.getString("user_id");
                                User user = new UserDAO().selectById(new User(user_id, null, null, null, null, null, null, null, null, null));
                                return new Signature(id, sig, user);
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
    public int insert(Signature signature) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "INSERT INTO signature (id, signature, user_id) " +
                    " VALUES (?,?,?)";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, signature.getId());
            if (signature.getUser_id() != null) {
                st.setString(2, signature.getUser_id().getId());
            } else {
                st.setNull(2, Types.VARCHAR);
            }
            st.setString(3, signature.getSignature());

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
    public int delete(Signature signature) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "DELETE from signature "+
                    " WHERE id=?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, signature.getId());

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
    public int update(Signature signature) {
        int ketQua = 0;
        try {
            // Bước 1: tạo kết nối đến CSDL
            Connection con = JDBCUtil.getConnection();

            // Bước 2: tạo ra đối tượng statement
            String sql = "UPDATE signature "+
                    " SET " +
                    " user_id=?"+","+
                    " signature=?"+
                    " WHERE id=?";
            PreparedStatement st = con.prepareStatement(sql);

            st.setString(1, signature.getUser_id().getId());
            st.setString(2, signature.getSignature());

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
        SignatureDAO dao = new SignatureDAO();
        System.out.println(dao.selectAll());
    }
}
