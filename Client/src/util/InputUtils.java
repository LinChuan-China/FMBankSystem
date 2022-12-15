package util;

import po.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

// 仅检查格式错误
public class InputUtils {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 该方法提示并等待，直到用户按回车键后返回。
     */
    public static void readReturn() {
        System.out.print("按回车键继续...");
        scanner.nextLine();
//        System.out.println("\n");
    }

    public static String getChoose() {
        return scanner.nextLine();
    }

    /**
     * 循环读取输入流获得一个合法的金额
     */
    public static float getNum() {
        float num;
        String input;
        for (; ; ) {
            System.out.print("请输入金额：");
            input = scanner.nextLine();
            try {
                num = Float.parseFloat(input);
                if (num < 0) {
                    System.out.println("请不要输入负数！");
                } else
                    break;
            } catch (NumberFormatException e) {
                System.out.println("请输入数字!");
            }
        }
        return num;
    }

    /**
     * 循环读取输入流获得一个合法的卡号
     */
    public static long getCardId() {
        String input;
        long id;
        for (; ; ) {
            System.out.print("请输入卡号：");
            input = scanner.nextLine();
            if (input.length() == User.C_ID_LENGTH) {
                try {
                    id = Long.parseLong(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("卡号仅由数字构成！");
                }
            } else {
                System.out.println("卡号应有" + User.C_ID_LENGTH + "位！");
            }
        }
        return id;
    }


    /**
     * 循环read输入流获得一个合法的身份证号
     */
    public static String getUserId() {
        String input;
        for (; ; ) {
            System.out.print("请输入身份证号：");
            input = scanner.nextLine();
            if (input.length() == User.U_ID_LENGTH) {
                try {
                    Long.parseLong(input);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("身份证号不应出现数字以外的字符！");
                }
            } else {
                System.out.println("身份证号" + User.U_ID_LENGTH + "位！");
            }
        }
        return input;
    }

    /**
     * 循环read输入流获得一个合法的用户密码
     */
    public static String getPassword() {
        String input;
        String input2;
        for (; ; ) {
            System.out.print("请输入密码：");
            input = scanner.nextLine();
            if (input.length() >= User.MIN_PASSWD_LENGTH) {
                break;
            } else {
                System.out.println("密码长度至少为" + User.MIN_PASSWD_LENGTH + "！");
            }
        }
        return input;
    }


    /**
     * 循环read输入流获得一个合法的电话号码
     */
    public static String getPhoneNum() {
        String phoneNum;
        for (; ; ) {
            System.out.print("请输入电话号码：");
            try {
                phoneNum = scanner.nextLine();
                if (phoneNum.length() == 11)
                    break;
                else
                    System.out.println("电话号码应为11位！");
            } catch (Exception e) {
                System.out.println("请不要输入数字以外的符号！");
            }
        }
        return phoneNum;
    }

    /**
     * 循环read输入流获得一个合法的姓名
     */
    public static String getUserName() {
        String name;
        for (; ; ) {
            System.out.print("请输入姓名：");
            name = scanner.nextLine();
            if (name.length() <= User.MAX_NAME_LENGTH)
                break;
            else
                System.out.println("姓名最长为10位！");
        }
        return name;
    }

    /**
     * 循环read输入流获得一个合法的用户性别
     */
    public static String getSex() {
        String input;
        String sex;
        for (; ; ) {
            System.out.print("请输入用户性别（男性：M，女性：F)：");
            input = scanner.nextLine();
            if (input.equals("M") || input.equals("m")) {
                sex = "M";
                break;
            } else if (input.equals("F") || input.equals("f")) {
                sex = "F";
                break;
            } else {
                System.out.println("请输入M或F！");
            }
        }
        return sex;
    }

    /**
     * 循环read输入流获得一个合法的生日
     */
    public static Date getBirthday() {
        String input;
        SimpleDateFormat sdf;
        Date date;
        for (; ; ) {
            System.out.print("请输入生日：（格式 2000-01-01）");
            input = scanner.nextLine();
            try {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(input);
                break;
            } catch (ParseException e) {
                System.out.println("请规范日期格式！");
            }
        }
        return date;
    }

    public static String getXlsPath() {
        String input;
        System.out.print("请输入xls文件路径：");
        input = scanner.nextLine();
        return input;
    }

}
