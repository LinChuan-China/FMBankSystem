package service.interfaces;


import po.User;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

// service层是对dao的进一步封装，负责逻辑处理，所以参数不是user而是具体的值
public interface IUserService {
    /**
     * 开户
     */
    long addUser(String user_name, String passwd, String user_id, String phone_num, String sex, Date birthday, float balance) throws SQLException;

    /**
     * 销户
     */
    int deleteUser(long id) throws SQLException;

    /**
     * 查询余额
     */
    float checkBalance(long id) throws SQLException;

    /**
     * 修改账户余额，用于转账，存钱，取钱
     */
    int modifyBalance(long id, float num) throws SQLException;

    /**
     * 修改账户名
     */
    int modifyName(long id, String name) throws SQLException;
    /**
     * 修改账户密码
     */
    int modifyPasswd(long id, String passwd) throws SQLException;
    /**
     * 修改账户的电话号码
     */
    int modifyPhoneNum(long id, String phoneNum) throws SQLException;
    /**
     * 修改账户的出生日期
     */
    int modifyBirthday(long id, Date birthday) throws SQLException;

    /**
     * 转账
     *
     * @param id1 付款方
     * @param id2 收款方
     */
    void transfer(long id1, long id2, float num) throws SQLException;

    /**
     * 显示所有储户信息
     */
    List<User> getAllUser() throws SQLException;

    /**
     * 判断卡号是否存在
     */
    public boolean cardIdExisted(long id) throws SQLException;

    /**
     * 判断身份证号是否存在
     */
    public boolean userIdExisted(String userId) throws SQLException;

    /**
     * 判断电话号码是否存在
     */
    public boolean phoneNumExisted(String phoneNum) throws SQLException;

    /**
     * 通过card_id查询
     */
    User getUserByCardId(long id) throws SQLException;


//
//    /**
//     * 通过user_id查询
//     */
//    User getUserByUserId(String id) throws SQLException;
//
//    /**
//     * 通过user_name查询
//     */
//    User getUserByName(String name) throws SQLException;
//
//    /**
//     * 通过phone_num查询
//     */
//    User getUserByPhoneNum(String phoneNum) throws SQLException;
}
