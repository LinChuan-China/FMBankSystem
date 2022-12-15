import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.junit.Test;
import po.User;
import util.InputUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
    private InetSocketAddress isa;
    private Socket socket;
    // 一个线程只操作一个user
    private User user;
    private OutputStream os;
    private InputStream is;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final String ioErrWarning = "传输异常";

    public Client() {
        try {
            // ip和port都被写到了配置文件中
//            Properties pros = new Properties();
//            InputStream pis = ClassLoader.getSystemClassLoader().getResourceAsStream("property/socket.properties");
//            pros.load(pis);
//            isa = new InetSocketAddress(pros.getProperty("ip"), Integer.parseInt(pros.getProperty("port")));
//            socket = new Socket(isa.getAddress(), isa.getPort());
            Socket socket = new Socket("127.0.0.1", 9090);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            dos = new DataOutputStream(os);
            dis = new DataInputStream(is);
            oos = new ObjectOutputStream(os);
            ois = new ObjectInputStream(is);
            System.out.println("服务器连接成功！");
        } catch (IOException e) {
            System.out.println("服务器连接失败！");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Client().enterMainMenu();
    }

    /**
     * 向服务器传递执行的将要执行的操作
     */
    private void submit(String option) throws IOException {
        dos.writeUTF(option);
        dos.flush();
    }

    private void addUserByInput() {
        String username = InputUtils.getUserName();
        String passwd = InputUtils.getPassword();
        String user_id = InputUtils.getUserId();
        String phone_num = InputUtils.getPhoneNum();
        String sex = InputUtils.getSex();
        Date birthday = InputUtils.getBirthday();
        addUser(username, passwd, user_id, phone_num, sex, birthday);
    }


    /**
     * 开户功能
     */
    private void addUser(String username, String passwd, String user_id, String phone_num, String sex, Date birthday) {
        try {
            // 1. 提交操作名
            submit("addUser");
            // 2. 提交操作参数
            user = new User(0, username, passwd, user_id, phone_num, sex, birthday, 0);
//            oos = new ObjectOutputStream(oos);
            oos.writeObject(user);
            oos.flush();
            // 3. 判断是否正确执行
//            dis = new DataInputStream(is);
            if (dis.readBoolean()) {
                System.out.println("开户成功！");
                System.out.println("您的卡号为：" + dis.readLong());
            } else {
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            System.out.println("对象流创建失败！");
            e.printStackTrace();
        }
        InputUtils.readReturn();
    }

    /**
     * xls批量开户
     */
    private void addUserByXls() {
        Workbook book = null;
        int i = 1;
        try {
            String url = InputUtils.getXlsPath();
            File f = new File(url);
            book = Workbook.getWorkbook(f);
            // 获取Excel第一个选项卡对象
            Sheet sheet = book.getSheet(0);
            int rows = sheet.getRows();// 取到表格的行数
            for (; i < sheet.getRows(); i++) {
                String username = sheet.getCell(1, i).getContents();
                String passwd = sheet.getCell(2, i).getContents();
                String user_id = sheet.getCell(3, i).getContents();
                String phone_num = sheet.getCell(4, i).getContents();
                String sex = sheet.getCell(5, i).getContents();
                String date = sheet.getCell(6, i).getContents();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date birthday = sdf.parse(date);
                addUser(username, passwd, user_id, phone_num, sex, birthday);
            }
            System.out.println("导入完毕！");
        } catch (IOException e) {
            System.out.println("文件导入错误！请检查路径或文件是否为xls");
        } catch (BiffException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            System.out.println("第" + (i + 1) + "行的日期格式有误，请遵循yyyy-MM-dd格式！");
        } finally {
            if (book != null) {
                book.close();
            }
        }
    }

    /**
     * 登录功能，向服务器验证卡号密码，获取user对象作为客户端的操作数据
     */
    private void login() {
        try {
            // 1. 提交操作名
            submit("login");
            // 2. 提交操作参数
            long id = InputUtils.getCardId();
            String passwd = InputUtils.getPassword();
            dos.writeLong(id);
            dos.writeUTF(passwd);
            dos.flush();
            // 3. 判断是否正确执行
            if (dis.readBoolean()) {
//                ObjectInputStream ois = new ObjectInputStream(is);
                ois = new ObjectInputStream(is);
                user = (User) ois.readObject();
                System.out.println("登陆成功！");
                System.out.println("尊敬的" + user.getUsername() + "您好，欢迎您使用飞马银行系统！");
//                System.out.println(user);
                enterUserMenu();
            } else {
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ioErrWarning);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void showUserInfo() {
        String sex;
        if (user.getSex().equals("M"))
            sex = "男";
        else
            sex = "女";
        System.out.println("******************用户信息**********************");
        System.out.println("            卡号：   " + user.getCard_id());
        System.out.println("            姓名：   " + user.getUsername());
        System.out.println("            身份证号：" + user.getUser_id());
        System.out.println("            电话号码：" + user.getPhone_num());
        System.out.println("            性别：   " + sex);
        System.out.println("            出生日期：" + user.getBirthday());
        System.out.println("            余额：   " + user.getBalance());
        System.out.println("**********************************************");
        InputUtils.readReturn();
    }

    // 修改用户信息
    private void updateUser(String attr, Object value) {
        String value1 = null;
        Date birthday = null;
        try {
            submit("updateUser");
            dos.writeLong(user.getCard_id());
            dos.writeUTF(attr);
            switch (attr) {
                case "passwd":
                case "phoneNum":
                case "sex":
                case "username":
                    value1 = (String) value;
                    dos.writeUTF(value1);
                    dos.flush();
                    break;
                case "birthday":
                    birthday = (Date) value;
                    oos.writeObject(birthday);
                    oos.flush();
                    break;
            }
            // 接收执行结果
            if (dis.readBoolean()) {
                System.out.println("修改成功！");
                switch (attr) {
                    case "passwd":
                        break;
                    case "phoneNum":
                        user.setPhone_num(value1);
                    case "sex":
                        user.setSex(value1);
                    case "username":
                        user.setUsername(value1);
                        break;
                    case "birthday":
                        user.setBirthday(birthday);
                        break;
                }
            } else {
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            System.out.println(ioErrWarning);
        }
        InputUtils.readReturn();
    }

    private void modifyUserInfo() {
        String choose;
        boolean loopFlag = true;
        while (loopFlag) {
            System.out.println("******************修改账户信息********************");
            System.out.println("            0-返回用户菜单");
            System.out.println("            1-修改密码");
            System.out.println("            2-修改电话号码");
            System.out.println("            3-修改性别");
            System.out.println("            4-修改生日");
            System.out.println("            5-修改姓名");
            System.out.println("***********************************************");
            System.out.print("请选择您要进行的操作：");
            choose = InputUtils.getChoose();
            switch (choose) {
                case "0":
                    loopFlag = false;
                    break;
                case "1":
                    updateUser("passwd", InputUtils.getPassword());
                    InputUtils.readReturn();
                    break;
                case "2":
                    updateUser("phoneNum", InputUtils.getPhoneNum());
                    InputUtils.readReturn();
                    break;
                case "3":
                    updateUser("sex", InputUtils.getSex());
                    InputUtils.readReturn();
                    break;
                case "4":
                    updateUser("birthday", InputUtils.getBirthday());
                    InputUtils.readReturn();
                    break;
                case "5":
                    updateUser("username", InputUtils.getUserName());
                    InputUtils.readReturn();
                    break;
                default:
                    System.out.println("请注意输入格式！");
                    InputUtils.readReturn();
            }
        }
    }

    private void saveMoney() {
        float num = InputUtils.getNum();
        try {
            submit("saveMoney");
            dos.writeLong(user.getCard_id());
            dos.writeFloat(num);
            dos.flush();
            if (dis.readBoolean()) {
                user.setBalance(user.getBalance() + num);
                System.out.println("存钱成功！您当前余额为 " + user.getBalance());
            } else {
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            System.out.println(ioErrWarning);
        }
        InputUtils.readReturn();
    }

    private void drawMoney() {
        float num = InputUtils.getNum();
        // 有效性检验
        while (num > user.getBalance()) {
            System.out.println("不好意思，您没有那么多钱！");
            num = InputUtils.getNum();
        }
        try {
            submit("drawMoney");
            dos.writeLong(user.getCard_id());
            dos.writeFloat(num);
            dos.flush();
            if (dis.readBoolean()) {
                user.setBalance(user.getBalance() - num);
                System.out.println("取钱成功！您当前余额为 " + user.getBalance());
            } else {
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            System.out.println(ioErrWarning);
        }
        InputUtils.readReturn();
    }

    private void transfer() {
        float num = InputUtils.getNum();
        while (num > user.getBalance()) {
            System.out.println("不好意思，您没有那么多钱！");
            num = InputUtils.getNum();
        }
        long id = InputUtils.getCardId();
        while (id == user.getCard_id()) {
            System.out.println("请不要输入自己的卡号！");
            id = InputUtils.getCardId();
        }
        try {
            submit("transfer");
            dos.writeLong(user.getCard_id());
            dos.writeLong(id);
            dos.writeFloat(num);
            dos.flush();
            if (dis.readBoolean()) {
                user.setBalance(user.getBalance() - num);
                System.out.println("转账成功！您当前余额为 " + user.getBalance());
            } else {
                System.out.println(dis.readUTF());
            }
            InputUtils.readReturn();
        } catch (IOException e) {
            System.out.println(ioErrWarning);
        }
    }

    private void deleteUser() {
        try {
            submit("deleteUser");
            dos.writeLong(user.getCard_id());
            dos.flush();
            if (dis.readBoolean()) {
                System.out.println("用户已删除！");
            } else {
                System.out.println(dis.readUTF());
            }
        } catch (IOException e) {
            System.out.println(ioErrWarning);
        }
        InputUtils.readReturn();
    }


    public void enterUserMenu() {
        String choose;
        boolean loopFlag = true;
        while (loopFlag) {
            System.out.println("******************用户菜单**********************");
            System.out.println("            0-返回主菜单");
            System.out.println("            1-查看账户信息");
            System.out.println("            2-修改账户信息");
            System.out.println("            3-存款");
            System.out.println("            4-取款");
            System.out.println("            5-转账");
            System.out.println("            6-销户");
            System.out.println("***********************************************");
            System.out.print("请选择您要进行的操作：");
            choose = InputUtils.getChoose();
            switch (choose) {
                case "1":
                    showUserInfo();
                    break;
                case "2":
                    modifyUserInfo();
                    break;
                case "3":
                    saveMoney();
                    break;
                case "4":
                    drawMoney();
                    break;
                case "5":
                    transfer();
                    break;
                case "6":
                    deleteUser();
                case "0":
                    loopFlag = false;
                    break;
                default:
                    System.out.println("请注意输入格式！");
                    InputUtils.readReturn();
            }
        }
    }

    /**
     *
     */
    public void exportData() {
        System.out.println("信息已生成文件！");
    }

    public void generateReport() {
        System.out.println("年终报告已生成！");
    }


    private void exit() {
        try {
            submit("exit");
        } catch (IOException e) {
            System.out.println(ioErrWarning);
        }
    }

    public void enterMainMenu() {
        String choose;
        while (true) {
            System.out.println("******************主菜单**********************");
            System.out.println("            0-退出");
            System.out.println("            1-开户");
            System.out.println("            2-登录");
            System.out.println("            3-批量开户");
            System.out.println("            4-导出所有用户");
            System.out.println("            5-生成年终报告");
            System.out.println("*********************************************");
            System.out.print("请选择您要进行的操作：");
            choose = InputUtils.getChoose();
            switch (choose) {
                case "1":
                    addUserByInput();
                    break;
                case "2":
                    login();
                    break;
                case "3":
                    addUserByXls();
                    break;
                case "4":
                    exportData();
                    break;
                case "5":
                    generateReport();
                    break;
                case "0":
                    exit();
                    return;
                default:
                    System.out.println("请注意输入格式！");
                    InputUtils.readReturn();
            }
        }
    }
}
