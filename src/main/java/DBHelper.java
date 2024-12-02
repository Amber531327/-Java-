import java.sql.*;

public class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore"; // 修改为你的数据库URL
    private static final String USER = "root";  // 数据库用户名
    private static final String PASSWORD = "123456";  // 数据库密码

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
