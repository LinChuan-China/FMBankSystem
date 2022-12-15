package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

// JDBC工具类
public class JDBCUtils {
    /*
     * 使用Druid数据库连接池技术
     * */
    // 使用静态变量实现单例
    private static final DataSource source;

    static {
        try {
            //加载配置文件
            Properties pros = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("property/druid.properties");
            pros.load(is);
            //初始化连接池对象
            source = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 从连接池中获取连接
    public static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public static void closeConnection(Connection conn) {
        DbUtils.closeQuietly(conn);
    }






    //******************************************************************************下面的都没用

    // 关闭资源，用于update，使用DBUtils后用不到了，ps和rs，queryRunner会自己调用自己关
    public static void closeConnection(Connection conn, PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 关闭资源重载，用于query，
    public static void closeConnection(Connection conn, PreparedStatement prestmt, ResultSet rs) {
        try {
            if (prestmt != null) {
                prestmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
