package view;

import po.User;
import service.interfaces.IUserService;
import service.impls.UserServiceImpl;
import util.CommonUtils;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

public class Channel implements Runnable {
    private final Socket socket;
    // 字节流是最通用的，所以选择了字节流而不是字符流
    private OutputStream os;
    private InputStream is;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private IUserService userService;


    public Channel(Socket _socket) {
        this.socket = _socket;
        try {
            os = socket.getOutputStream();
            is = socket.getInputStream();
            dis = new DataInputStream(_socket.getInputStream());
            dos = new DataOutputStream(_socket.getOutputStream());
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            userService = new UserServiceImpl();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void sendTrue() throws IOException {
        dos.writeBoolean(true);
        dos.flush();
    }

    private void sendFalse(String msg) throws IOException {
        dos.writeBoolean(false);
        dos.writeUTF(msg);
        dos.flush();
    }


    /**
     * 有效性检验，是否存在，暂不考虑格式
     * username，phone_num，user_id
     */

    private void addUser() {
        try {
            // 获取参数
//            ObjectInputStream ois = new ObjectInputStream(is);
            User user = (User) ois.readObject();
            // 有效性验证，考虑存在问题，没有考虑格式
            if (userService.userIdExisted(user.getUser_id())) {
                sendFalse("身份证号已被使用！");
            } else if (userService.phoneNumExisted(user.getPhone_num())) {
                sendFalse("电话号码已被使用！");
            } else {
                sendTrue();
                long carId = userService.addUser(user.getUsername(), user.getPasswd(), user.getUser_id(), user.getPhone_num(), user.getSex(), user.getBirthday(), 2000);
                // 返回卡号
                dos.writeLong(carId);
            }
            dos.flush();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 验证客户端传来卡号与密码，向客户端返回user对象
    private void login() {
        try {
            // 读取客户端的卡号密码
            long id = dis.readLong();
            String passwd = dis.readUTF();
            // 用卡号向数据库查询并确认密码
            User user = userService.getUserByCardId(id);
            if (user == null) {
                sendFalse("卡号不存在！");
            } else if (!user.getPasswd().equals(passwd)) {
                sendFalse("密码不正确！");
            } else {
                sendTrue();
                // 遮掩密码
                user.setPasswd(null);
//                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos = new ObjectOutputStream(os);
                oos.writeObject(user);
                oos.flush();
                System.out.println(user);
            }
            dos.flush();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新用户信息
     */
    private void updateUser() {
        try {
            // 1. 获取参数
            long id = dis.readLong();
            String attr = dis.readUTF();
            switch (attr) {
                case "passwd":
                    String passwd = dis.readUTF();
                    userService.modifyPasswd(id, passwd);
                    break;
                case "phoneNum":
                    String phoneNum = dis.readUTF();
                    if (userService.phoneNumExisted(phoneNum)) {
                        sendFalse("该号码已被使用！");
                        return;
                    } else {
                        userService.modifyPhoneNum(id, phoneNum);
                    }
                    break;
                case "birthday":
                    ObjectInputStream ois = new ObjectInputStream(is);
                    Date birthday = (Date) ois.readObject();
                    userService.modifyBirthday(id, birthday);
                    break;
                case "username":
                    String name = dis.readUTF();
                    userService.modifyName(id, name);
                    break;
            }
            sendTrue();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                sendFalse("操作失败，请重试！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void transfer() {
        try {
            long id = dis.readLong();
            long id2 = dis.readLong();
            float num = dis.readFloat();
            if (!userService.cardIdExisted(id) || !userService.cardIdExisted(id2)) {
                sendFalse("卡号不存在！");
            } else {
                if (num > userService.getUserByCardId(id).getBalance()) {
                    sendFalse("余额不足！");
                } else {
                    userService.transfer(id, id2, num);
                    dos.writeBoolean(true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                sendFalse("操作失败，请重试！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveMoney() {
        try {
            long id = dis.readLong();
            float num = dis.readFloat();
            userService.modifyBalance(id, num);
            dos.writeBoolean(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            try {
                sendFalse("操作失败，请重试！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void drawMoney() {
        try {
            long id = dis.readLong();
            float num = dis.readFloat();
            if (num > userService.getUserByCardId(id).getBalance()) {
                sendFalse("余额不足！");
            } else {
                userService.modifyBalance(id, -num);
                dos.writeBoolean(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            try {
                sendFalse("操作失败，请重试！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void deleteUser() {
        try {
            long id = dis.readLong();
            userService.deleteUser(id);
            dos.writeBoolean(true);
        } catch (SQLException e) {
            try {
                sendFalse("操作失败，请重试！");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        try {
            String msg;
            while (true) {
                msg = dis.readUTF();
                System.out.println(msg);
                switch (msg) {
                    case "addUser":
                        addUser();
                        break;
                    case "login":
                        login();
                        break;
                    case "updateUser":
                        updateUser();
                        break;
                    case "transfer":
                        transfer();
                        break;
                    case "saveMoney":
                        saveMoney();
                        break;
                    case "drawMoney":
                        drawMoney();
                        break;
                    case "deleteUser":
                        deleteUser();
                        break;
                    case "exit":
                        System.out.println("连接关闭！");
                        return;
                    default:
                        System.out.println("Unknown input");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            CommonUtils.close(socket, dos, dis, oos, ois);
            CommonUtils.close(socket, dos, dis);
            // 函数内使用的ois，oos要不要显示关闭
        }
    }

}
