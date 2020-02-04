package bankrupt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 
public class DBConnection {
    public static Connection dbConn;
    public static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    public static Statement stmt=null;	//statement 객체
    public static PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
    public static ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
    public static Connection getConnection() {
    	Connection conn = null;
        try {
        		String user = "YOUR_DB_USER";
                String pw = "YOUR_DB_PASSWORD";
                String url = "jdbc:oracle:thin:@localhost:1521:YOUR_SID";
                
                Class.forName("oracle.jdbc.driver.OracleDriver");        
                conn = DriverManager.getConnection(url, user, pw);    
        } 
        catch (ClassNotFoundException ce) {
            System.out.println("DB 드라이버 로딩 실패 :"+ce.toString());
        } 
        catch (SQLException se) {
            System.out.println("DB 접속실패 : "+se.toString());
        } 
        catch (Exception e) {
            System.out.println("Unkonwn error");
            e.printStackTrace();
        }
        return conn;     
    }
}