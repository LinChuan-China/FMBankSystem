package util;

import java.io.Closeable;

public class CommonUtils {
    public static void close(Closeable... resources) {
        // Closeable是IO流中接口，"..."可变参数
        // IO流和Socket都实现了Closeable接口，可以直接用
        for (Closeable resource : resources) {
            try {
                // 只要是释放资源就要加入空判断
                if (resource != null) {
                    resource.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
