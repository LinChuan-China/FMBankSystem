package dao.impls;

import dao.BaseDAO;
import dao.interfaces.IUserDAO;
import po.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDAOImpl extends BaseDAO<User> implements IUserDAO {

    @Override
    public List<User> queryUsers(Connection conn) throws SQLException {
        String sql = "select * from users";
        return getBeanList(conn, sql);
    }

    @Override
    public User queryUserByCardId(Connection conn, User _user) throws SQLException {
        String sql = "select * from users where card_id = ?";
        return getBean(conn, sql, _user.getCard_id());
    }

    @Override
    public User queryUserByUserId(Connection conn, User _user) throws SQLException {
        String sql = "select * from users where user_id = ?";
        return getBean(conn, sql, _user.getUser_id());
    }

    @Override
    public User queryUserByUserName(Connection conn, User _user) throws SQLException {
        String sql = "select * from users where username = ?";
        return getBean(conn, sql, _user.getUsername());
    }

    @Override
    public User queryUserByPhoneNum(Connection conn, User _user) throws SQLException {
        String sql = "select * from users where phone_num = ?";
        return getBean(conn, sql, _user.getPhone_num());
    }

    @Override
    public int insertUser(Connection conn, User user) throws SQLException {
        String sql = "insert into users values(?,?,?,?,?,?,?,?)";
        return update(conn, sql, null, user.getUsername(), user.getPasswd(), user.getUser_id(), user.getPhone_num(), user.getSex(), user.getBirthday(), user.getBalance());
    }

    @Override
    public int updateUser(Connection conn, User user) throws SQLException {
        String sql = "update users set passwd = ? , phone_num = ? , sex = ? , birthday = ? , balance = ? where card_id = ?";
        return update(conn, sql, user.getPasswd(), user.getUser_id(), user.getPhone_num(), user.getSex(), user.getBirthday(), user.getBalance(), user.getCard_id());
    }

    @Override
    public int modifyUsername(Connection conn, User user) throws SQLException {
        String sql = "update users set username = ? where card_id = ?";
        return update(conn, sql, user.getUsername(), user.getCard_id());
    }

    @Override
    public int modifyPassword(Connection conn, User user) throws SQLException {
        String sql = "update users set passwd = ? where card_id = ?";
        return update(conn, sql, user.getPasswd(), user.getCard_id());
    }

    @Override
    public int modifyPhoneNum(Connection conn, User user) throws SQLException {
        String sql = "update users set phone_num = ? where card_id = ?";
        return  update(conn, sql, user.getPhone_num(), user.getCard_id());
    }


    @Override
    public int modifyBirthday(Connection conn, User user) throws SQLException {
        String sql = "update users set birthday = ? where card_id = ?";
        return update(conn, sql, user.getBirthday(), user.getCard_id());
    }

    @Override
    public int modifyBalance(Connection conn, User user) throws SQLException {
        String sql = "update users set balance = ? + balance where card_id = ?";
        return update(conn, sql, user.getBalance(), user.getCard_id());
    }

    @Override
    public int deleteUserByCardId(Connection conn, User user) throws SQLException {
        String sql = "delete from users where card_id = ?";
        return update(conn, sql, user.getCard_id());
    }

    @Override
    public int deleteUserByUserId(Connection conn, User user) throws SQLException {
        String sql = "delete from users where user_id = ?";
        return update(conn, sql, user.getUser_id());
    }
}
