package bankrupt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Admin {
	static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    static Statement stmt=null;	//statement 객체
    static PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
    static ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
    static ResultSet rs2 = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
    private String name;
    private String id;
    private String passwd;
    //생성자들
    public Admin(){
    	name=null;
    	id=null;
    	passwd=null;
    }
    public Admin(String inpName, String inpId, String inpPasswd){
    	name=inpName;
    	id=inpId;
    	passwd=inpPasswd;
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
  	//관리자 정보 생성
  	public void MakeAdmin() throws SQLException{
  		DbConnect();
  		stmt.executeUpdate("insert into Admin values ('"+id+"','"+passwd+"','"+name+"')");
  		stmt.executeUpdate("commit");
  		DBDisConnect();
  	}
  	//Id 질의
  	public String getSelectId() throws SQLException{
  		DbConnect();
  		String sql="select Id from admin";
  		String retId = null;
  		rs=stmt.executeQuery(sql);
  		while(rs.next()){
  			retId=rs.getString("Id");
		}
  		DBDisConnect();
  		return retId;
  	}
	//Passwd 질의
  	public String getSelectPasswd() throws SQLException{
  		DbConnect();
  		String sql="select Passwd from admin";
  		String retPasswd = null;
  		rs=stmt.executeQuery(sql);
  		while(rs.next()){
  			retPasswd=rs.getString("Passwd");
		}
  		DBDisConnect();
  		return retPasswd;
  	}
  	//User정보 열람/찾기
  	public void FindUserInfo() throws SQLException{
  		DbConnect();
  		String quary="select * from Bankuser";
    	pstm=conn.prepareStatement(quary);
    	rs=pstm.executeQuery();
    	System.out.println("Id"+"\t"+"AdminId"+"\t\t"+"Passwd"+"\t\t"+"Name"+"\t"+"Country");
    	System.out.println("=======================================================");
    	while(rs.next()){
    		String id=rs.getString(1);
    		String adminId=rs.getString(2);
    		String passwd=rs.getString(3);
    		String name=rs.getString(4);
    		String country=rs.getString(5);
    		String result=id+"\t"+adminId+"\t\t"+passwd+"\t\t"+name+"\t"+country;
    		System.out.println(result);
    	}
    	DBDisConnect();
  	}
  	//User 1명의 정보 삭제
  	public void OneUserDelete(String inpName) throws SQLException{
  		DbConnect();
  		String quary="select name from Bankuser where name = '"+inpName+"'";
    	pstm=conn.prepareStatement(quary);
    	rs=pstm.executeQuery();
    	while(rs.next()){
        	if(inpName.equals(rs.getString("Name"))){
        		String quary2="select id from Bankuser where name = '"+inpName+"'";
        	    pstm=conn.prepareStatement(quary2);
            	rs2=pstm.executeQuery();
            	while(rs2.next()){
            		System.out.println("delete bankuser");
            		String deleteSql="delete from bankAccount where userId = '"+rs2.getString(1)+"'";
            		stmt.executeUpdate(deleteSql);
            		stmt.executeUpdate("commit");
            	}
        		String deleteSql="delete from bankUser where Name = '"+inpName+"'";
        		stmt.executeUpdate(deleteSql);
        		stmt.executeUpdate("commit");
        		System.out.println("사용자 삭제 완료!");
        		DBDisConnect();
        		return;
        	}
    	}
    	System.out.println("존재하지 않는 사용자입니다!");
    	DBDisConnect();
  	}
  	//User 전체 정보 삭제
  	public void AllUserDelete() throws SQLException{
  		DbConnect();
  		String deleteSql1="delete from BankAccount";
    	stmt.executeUpdate(deleteSql1);
    	stmt.executeUpdate("commit");
  		String deleteSql2="delete from Bankuser";
    	stmt.executeUpdate(deleteSql2);
    	stmt.executeUpdate("commit");
    	System.out.println("사용자 전체 삭제 완료!");
    	DBDisConnect();
  	}
}	