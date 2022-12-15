package dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


// 提供给dao的工具，前期是自己编写的，后期转为使用apache的DBUtils
// 考虑到事务的存在，conn不能写在dao层
public abstract class BaseDAO<T> {
    private final QueryRunner queryRunner = new QueryRunner();
    // 定义一个变量接收泛型的类型
    private final Class<T> tClass;

    // 匿名代码块，实现类被实例化时调用，初始化tClass
    {
        // this.getClass获取this指向的类
        // this指向的是调用者，而抽象类不会实例化，所以this只会指向实现类
        // getGenericSuperclass()用来获取当前类的父类的类型
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        // ParameterizedType表示的是带泛型的类型，强转
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        // 获取具体的泛型类型 getActualTypeArguments获取具体的泛型的类型
        // 这个方法会返回一个Type的数组，第一个元素就是调用类
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        tClass = (Class<T>) actualTypeArguments[0];
    }

    /**
     * 通用的增删改操作，查操作有返回值，所以另写
     */
    public int update(Connection conn, String sql, Object... args) throws SQLException {
        return queryRunner.update(conn, sql, args);
    }

    /**
     * 查询单个对象
     */
    public T getBean(Connection conn, String sql, Object... params) throws SQLException {
        return queryRunner.query(conn, sql, new BeanHandler<T>(tClass), params);
    }

    /**
     * 查询多个对象
     */
    public List<T> getBeanList(Connection conn, String sql, Object... params) throws SQLException {
        return queryRunner.query(conn, sql, new BeanListHandler<T>(tClass), params);
    }

    /**
     * 获取一个单一值的方法，专门用来执行像 select count(*)...这样的sql语句
     */
    public Object getValue(Connection conn, String sql, Object... params) throws SQLException {
        // 调用queryRunner的query方法获取一个单一的值
        return queryRunner.query(conn, sql, new ScalarHandler<>(), params);

    }


// *****************************************************************************************
//    // 通用的增删改操作(version2.0)
//    // 考虑数据库事务，在事务操作全部结束前不关闭conn，所以conn从外部创建传入，也由外部的事务的处理函数关闭
//    public void update(Connection conn, String sql, Object... args) {
//        PreparedStatement ps = null;
//        try {
//            // 2.
//            ps = conn.prepareStatement(sql);
//            // 3.
//            for (int i = 0; i < args.length; i++) {
//                ps.setObject(i + 1, args[i]);
//            }
//            // 4.
//            ps.execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 5.
//            JDBCUtils.closeConnection(null, ps);
//        }
//    }

//    // 查询返回多条记录
//    public List<T> getForList(Connection conn, String sql, Object... args) {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            ps = conn.prepareStatement(sql);
//            for (int i = 0; i < args.length; i++) {
//                ps.setObject(i + 1, args[i]);
//            }
//            rs = ps.executeQuery();
//            // 通过结果集的元数据获取列数
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int col = rsmd.getColumnCount();
//            ArrayList<T> list = new ArrayList<>();
//            while (rs.next()) {
//                T t = tClass.newInstance();
//                // 遍历记录的每个字段
//                for (int i = 0; i < col; i++) {
//                    // 获取字段值
//                    Object value = rs.getObject(i + 1);
//
//                    // 获取字段名
//                    String name = rsmd.getColumnName(i + 1);
//
//                    // 通过反射给列赋值
//                    Field field = t.getClass().getDeclaredField(name);
//                    field.setAccessible(true);
//                    field.set(t, value);
//                }
//                list.add(t);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            JDBCUtils.closeConnection(null, ps, rs);
//        }
//        return null;
//    }

//    // 查询返回一条记录
//    public T getInstance(String sql, Object... args) {
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            conn = JDBCUtils.getConnection();
//            ps = conn.prepareStatement(sql);
//            for (int i = 0; i < args.length; i++) {
//                ps.setObject(i + 1, args[i]);
//            }
//            rs = ps.executeQuery();
//            // 通过结果集的元数据获取列数
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int col = rsmd.getColumnCount();
//            if (rs.next()) {
//                T t = tClass.newInstance();
//                // 遍历记录的每个字段
//                for (int i = 0; i < col; i++) {
//                    // 获取字段值
//                    Object value = rs.getObject(i + 1);
//
//                    // 获取字段名
//                    String name = rsmd.getColumnName(i + 1);
//
//                    // 通过反射给列赋值
//                    Field field = t.getClass().getDeclaredField(name);
//                    field.setAccessible(true);
//                    field.set(t, value);
//                }
//                return t;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            JDBCUtils.closeConnection(conn, ps, null);
//        }
//        return null;
//    }
//
//    // 特殊值的查询
//    // 这个E这个泛型怎么用，研究一下
//    public <E> E getValue(Connection conn, String sql, Object... arg) {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            ps = conn.prepareStatement(sql);
//            for (int i = 0; i < arg.length; i++) {
//                ps.setObject(i + 1, arg[i]);
//            }
//            rs = ps.executeQuery();
//
//            if (rs.next()) {
//                return (E) rs.getObject(1);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            JDBCUtils.closeConnection(null, ps, rs);
//        }
//        return null;
//    }
}
