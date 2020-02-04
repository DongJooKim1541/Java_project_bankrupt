package bankrupt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {
	static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    static Statement stmt=null;	//statement 객체
    static PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
    static ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
    static Scanner key = new Scanner(System.in);
    protected String id;
    protected String passwd;
    protected String name;
    protected String country;
	//생성자들
	public User(){
		id=null;
		passwd=null;
		name=null;
		country=null;
	}
	public User(String inpId, String inpPasswd, String inpName, String inpCountry){
		id=inpId;
		passwd=inpPasswd;
		name=inpName;
		country=inpCountry;
	}
	//DB연결
	private void DbConnect() throws SQLException{
		conn = DBConnection.getConnection();
        stmt=conn.createStatement();
	}
	//DB연결 해제
  	private void DBDisConnect(){
  		try {
            if ( rs != null ){
            	rs.close();
            }   
            if ( pstm != null ){
            	pstm.close();
            }   
            if ( stmt != null ){
            	stmt.close(); 
            }
            if ( conn != null ){
            	conn.close(); 
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
  	}
	//회원가입
	public void MakeUser() throws SQLException {
		DbConnect();
		int count1=0,count2=0;
		String countSql1="select count(*) from bankUser";
		rs=stmt.executeQuery(countSql1);
  		while(rs.next()){
  			count1 =rs.getInt(1);
		}
		try {
			stmt.executeUpdate("insert into BankUser values('"+id+"','ADMIN_ID','"+passwd+"','"+name+"','"+country+"')");
		} 
		catch (SQLException e) {
		}
		String countSql2="select count(*) from bankUser";
		rs=stmt.executeQuery(countSql2);
  		while(rs.next()){
  			count2 =rs.getInt(1);
		}
  		if(count1!=count2){
  			System.out.println("회원가입이 완료되었습니다.");
  			stmt.executeUpdate("commit");
  		}
  		else{
  			System.out.println("중복된 Id가 존재합니다!");
  		}
  		DBDisConnect();
	}
	//회원탈퇴
	public void DeleteUser() throws SQLException{
		DbConnect();
		String findId=null;
		String findPasswd=null;
		String sql1="select Id from bankUser where Id = '"+id+"'";
		try {
			rs=stmt.executeQuery(sql1);
	  		while(rs.next()){
	  			findId =rs.getString("Id");
			}
		} 
		catch (SQLException e) {
		}
  		if(findId!=null){
  			String sql2="select Passwd from bankUser where Passwd = '"+passwd+"'";
  			try {
  				rs=stmt.executeQuery(sql2);
  		  		while(rs.next()){
  		  		findPasswd =rs.getString("Passwd");
  				}
  			} 
  			catch (SQLException e) {
  			}
  			if(findPasswd!=null){
  				String userAccountSql="delete from bankUser where Id = '"+id+"'";
  	  			stmt.executeUpdate(userAccountSql);
  	  			System.out.println("회원탈퇴가 완료되었습니다.");
  	  			stmt.executeUpdate("commit");
  			}
  			else{
  				System.out.println("존재하지 않는 비밀번호입니다!");
  			}
  		}
  		else{
			System.out.println("존재하지 않는 Id입니다!");
  		}
  		DBDisConnect();
	}
	//Id 질의
	public String getSelectId(String inputId) throws SQLException{
		DbConnect();
		String selectUserIdSql="select Id from bankUser where Id = '"+inputId+"'";
		rs=stmt.executeQuery(selectUserIdSql);
		while(rs.next()){
			id=rs.getString("Id");
		}
		DBDisConnect();
		return id;
	}
	//Passwd 질의
	public String getSelectPasswdd(String inputPasswd) throws SQLException{
		DbConnect();
		String selectUserPasswdSql="select Passwd from bankUser where passwd= '"+inputPasswd+"'";
		rs=stmt.executeQuery(selectUserPasswdSql);
    	while(rs.next()){
    		passwd=rs.getString("Passwd");
    	}
    	DBDisConnect();
		return passwd;
	}
	//SetMethod
	public void SetUserId(String inpId){
		id=inpId;
	}
	public void setUserPasswd(String inpPasswd){
		passwd=inpPasswd;
	}
}