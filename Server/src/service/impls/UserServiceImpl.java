package service.impls;

import dao.interfaces.IUserDAO;
import dao.impls.UserDAOImpl;
import po.User;
import service.interfaces.IUserService;
import util.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements IUserService {
    private static IUserDAO dao = new UserDAOImpl();
    private Connection conn = null;

    public UserServiceImpl() throws SQLException {
        conn = JDBCUtils.getConnection();
        System.out.println(conn);
    }

    @Override
    public long addUser(String user_name, String user_password, String user_id, String phone_num, String sex, Date birthday, float balance) throws SQLException {
        User user = new User(0, user_name, user_password, user_id, phone_num, sex, birthday, balance);
        dao.insertUser(conn, user);
        user = dao.queryUserByUserId(conn, user);
        return user.getCard_id();
    }

    @Override
    public int deleteUser(long id) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        return dao.deleteUserByCardId(conn, user);
    }

    @Override
    public float checkBalance(long id) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        User res = dao.queryUserByCardId(conn, user);
        return res.getBalance();
    }

    @Override
    public int modifyBalance(long id, float num) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        user.setBalance(num);
        return dao.modifyBalance(conn, user);
    }

    @Override
    public int modifyName(long id, String name) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        user.setUsername(name);
        return dao.modifyUsername(conn, user);
    }

    @Override
    public int modifyPasswd(long id, String passwd) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        user.setPasswd(passwd);
        return dao.modifyPassword(conn, user);
    }

    @Override
    public int modifyPhoneNum(long id, String phoneNum) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        user.setPhone_num(phoneNum);
        return dao.modifyPhoneNum(conn, user);
    }

    @Override
    public int modifyBirthday(long id, Date birthday) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        user.setBirthday(birthday);
        return dao.modifyBirthday(conn, user);
    }

    // 考虑事务，在一个conn中完成
    @Override
    public void transfer(long id1, long id2, float num) throws SQLException {
        try {
            // 将conn的自动提交取消
            conn.setAutoCommit(false);
            // user1减，user2加
            User user = new User();
            user.setCard_id(id1);
            user.setBalance(-num);
            dao.modifyBalance(conn, user);

            User user2 = new User();
            user2.setCard_id(id2);
            user2.setBalance(num);
            dao.modifyBalance(conn, user2);
            conn.commit();
        }
        catch (SQLException e) {
            System.out.println("转账失败！");
            try {
                conn.rollback();
            } catch (SQLException e1) {
                System.out.println("数据回滚失败！");
            }
            throw new SQLException(e);
        }
        finally {
            try {
                // 设置回自动提交
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("自动提交设置失败！");
            }
        }
    }

    @Override
    public List<User> getAllUser() throws SQLException {
        return dao.queryUsers(conn);
    }

    @Override
    public User getUserByCardId(long id) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        return dao.queryUserByCardId(conn, user);
    }
//
//    @Override
//    public User getUserByUserId(String id) throws SQLException {
//        User user = new User();
//        user.setUser_id(id);
//        return dao.queryUserByUserId(conn, user);
//    }
//
//    @Override
//    public User getUserByName(String name) throws SQLException {
//        User user = new User();
//        user.setUsername(name);
//        return dao.queryUserByUserName(conn, user);
//    }
//
//    @Override
//    public User getUserByPhoneNum(String phoneNum) throws SQLException {
//        User user = new User();
//        user.setPhone_num(phoneNum);
//        return dao.queryUserByPhoneNum(conn, user);
//    }

    @Override
    public boolean cardIdExisted(long id) throws SQLException {
        User user = new User();
        user.setCard_id(id);
        user = dao.queryUserByCardId(conn, user);
        return user != null;
    }

    @Override
    public boolean userIdExisted(String userId) throws SQLException {
        User user = new User();
        user.setUser_id(userId);
        user = dao.queryUserByUserId(conn, user);
        return user != null;

    }

    @Override
    public boolean phoneNumExisted(String phoneNum) throws SQLException {
        User user = new User();
        user.setPhone_num(phoneNum);
        user = dao.queryUserByPhoneNum(conn, user);
        return user != null;

    }
}
