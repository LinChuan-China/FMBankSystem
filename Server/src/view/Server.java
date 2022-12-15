package view;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    private static final int port = 9090; // 后续放到配置里，算了 就这一个 没必要
    private static ServerSocket ss;
    private static ExecutorService executorService;

    static {
        try {
            // 初始化线程池
            executorService = new ThreadPoolExecutor(3, 3, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue(10));
            System.out.println("线程池创建成功！");
            // 初始化ss
            ss = new ServerSocket(port);
            System.out.println("服务器启动成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("服务器启动失败！");
        }
    }


    public static void run() {
        while (true) {
            Socket socket = null;
            // 新连接建立线程处理
            try {
                socket = ss.accept();
                System.out.println("新连接建立！");
//                System.out.println("新连接建立" + socket.getInetAddress() + ":" + socket.getPort());
                executorService.execute(new Channel(socket));
            } catch (IOException e) {
                System.out.println("有新连接请求但连接未建立成功！");
            }
        }
    }
}
