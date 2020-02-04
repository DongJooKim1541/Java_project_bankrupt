package bankrupt;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RelationTable {
	static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    static Statement stmt=null;	//statement 객체
  //DB연결
  	private void DbConnect() throws SQLException{
  		conn = DBConnection.getConnection();
          stmt=conn.createStatement();
  	}
  //DB연결 해제
  	private void DBDisConnect(){
  		try { 	
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
	public void MakeTable() throws SQLException{
		DbConnect();
		String table1= "CREATE TABLE BankUser ("+
   		 		"Id			VARCHAR(20)	NOT NULL,"+
   		 		"AdminId	VARCHAR(20),"+
   		 		"Passwd			VARCHAR(20),"+
   		 		"Name			VARCHAR(20),"+
   		 		"Country		VARCHAR(20),"+
   		 		"PRIMARY KEY(Id),"+
   		 		"FOREIGN KEY(Country) REFERENCES F_Money(Country),"+
   		 		"FOREIGN KEY(AdminId) REFERENCES Admin(Id))";
		String table2= "CREATE TABLE BankAccount ("+
	 			"UserId	VARCHAR(20)	NOT NULL,"+
				"CreateAccountTime VARCHAR(40) NOT NULL,"+
				"AccountNumber	VARCHAR(40) NOT NULL,"+
	 			"Money	INT,"+
	 			"PRIMARY KEY(UserId, CreateAccountTime, AccountNumber),"+
	 			"FOREIGN KEY (UserId) REFERENCES BankUser(Id))";
		String table3= "CREATE TABLE Admin ("+
 				"Id		VARCHAR(20)	NOT NULL,"+
 				"Passwd			VARCHAR(20),"+
 				"Name			VARCHAR(20),"+
 				"PRIMARY KEY(Id))";
		String table4= "CREATE TABLE F_Money ("+
				"Country		VARCHAR(20) NOT NULL,"+
				"Rate			NUMBER,"+
				"PRIMARY KEY(Country))";
		stmt.executeUpdate(table4);
		stmt.executeUpdate(table3);
		stmt.executeUpdate(table1);
		stmt.executeUpdate(table2);
		
		String insert1="insert into F_Money values('KOR',1.00)";
		String insert2="insert into F_Money values('USA',1087.00)";
		String insert3="insert into F_Money values('JPN',967.30)";
		String insert4="insert into F_Money values('EU',1292.33)";
		String insert5="insert into F_Money values('CHN',164.48)";
		String insert6="insert into F_Money values('ENG',1467.45)";
		String insert7="insert into F_Money values('AUS',823.35)";
		stmt.executeUpdate(insert1);
		stmt.executeUpdate(insert2);
		stmt.executeUpdate(insert3);
		stmt.executeUpdate(insert4);
		stmt.executeUpdate(insert5);
		stmt.executeUpdate(insert6);
		stmt.executeUpdate(insert7);
		stmt.executeUpdate("commit");
		DBDisConnect();
	}
}