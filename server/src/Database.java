/**
 * Created with IntelliJ IDEA.
 * User: Владимир
 * Date: 05.03.13
 * Time: 22:12
 * To change this template use File | Settings | File Templates.
 */

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database
{
    private Connection conn;

    public static Connection getConnection() throws Exception {
        Class.forName("org.gjt.mm.mysql.Driver").newInstance();

        // String url = "jdbc:mysql://localhost/commining";
        String url = "jdbc:mysql://localhost/youtube";
        String username = "root";
        String password = "pass";
        return DriverManager.getConnection(url, username, password);

    }

    public Database() throws Exception{
        conn = getConnection();
    }
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
