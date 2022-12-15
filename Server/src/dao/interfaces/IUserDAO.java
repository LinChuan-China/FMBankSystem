package dao.interfaces;


import po.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// card_id，user_id，username不能修改
// 此处的id指的都是card_id
public interface IUserDAO {


    //************************ Query ******************************

    /**
     * 显示表中所有记录
     */
    List<User> queryUsers(Connection conn) throws SQLException;

    /**
     * 通过card_id查询
     */
    User queryUserByCardId(Connection conn, User user) throws SQLException;

    /**
     * 通过user_id查询
     */
    User queryUserByUserId(Connection conn, User user) throws SQLException;

    /**
     * 通过user_name查询
     */
    public User queryUserByUserName(Connection conn, User user) throws SQLException;

    /**
     * 通过phone_num查询
     */
    public User queryUserByPhoneNum(Connection conn, User user) throws SQLException;


    //************************* Update ********************************

    /**
     * 创建用户，返回卡号
     */
    int insertUser(Connection conn, User user) throws SQLException;

    /**
     * 更新用户信息
     */
    int updateUser(Connection conn, User user) throws SQLException;

    /**
     * 修改用户名
     */
    int modifyUsername(Connection conn, User user) throws SQLException;

    /**
     * 修改密码
     */
    int modifyPassword(Connection conn, User user) throws SQLException;

    /**
     * 修改手机号
     */
    int modifyPhoneNum(Connection conn, User user) throws SQLException;

    /**
     * 修改生日
     */
    int modifyBirthday(Connection conn, User user) throws SQLException;

    /**
     * 修改余额，在原有的数值上加！！！
     */
    int modifyBalance(Connection conn, User user) throws SQLException;

    /**
     * 通过card_id销户
     */
    int deleteUserByCardId(Connection conn, User user) throws SQLException;

    /**
     * 通过User_id销户
     */
    int deleteUserByUserId(Connection conn, User user) throws SQLException;


}
