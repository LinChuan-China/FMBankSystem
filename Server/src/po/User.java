package po;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    public static long serialVersionUID = 599293L;

    public static int C_ID_LENGTH = 10;
    public static int U_ID_LENGTH = 12;
    public static int MAX_NAME_LENGTH = 10;
    public static int MIN_PASSWD_LENGTH = 4;
    private long card_id;
    private String username;
    private String passwd;
    private String user_id;
    private String phone_num;
    private String sex;
    private Date birthday;
    private float balance;

    public User() {
    }

    public User(long card_id, String username, String passwd, String user_id, String phone_num, String sex, Date birthday, float balance) {
        this.card_id = card_id;
        this.username = username;
        this.passwd = passwd;
        this.user_id = user_id;
        this.phone_num = phone_num;
        this.sex = sex;
        this.birthday = birthday;
        this.balance = balance;
    }

    public long getCard_id() {
        return card_id;
    }

    public void setCard_id(long card_id) {
        this.card_id = card_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "card_id=" + card_id +
                ", username='" + username + '\'' +
//                ", passwd='" + passwd + '\'' +
                ", user_id='" + user_id + '\'' +
                ", phone_num='" + phone_num + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", balance=" + balance +
                '}';
    }
}
