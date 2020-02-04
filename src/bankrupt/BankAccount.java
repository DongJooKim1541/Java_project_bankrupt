package bankrupt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BankAccount extends User {
	static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    static Statement stmt=null;	//statement 객체
    static PreparedStatement pstm = null;  // SQL 문을 나타내는 객체
    static ResultSet rs = null;  // 쿼리문을 날린것에 대한 반환값을 담을 객체
    static Scanner key = new Scanner(System.in);
    private String createAccountTime;
    private String accountNumber;
    private int money;
	Time t = new Time();
    public BankAccount(){
    	super();
    	createAccountTime=null;
    	accountNumber=null;
    	money=0;
    }
    public BankAccount(String inpId, String inpPasswd, String inpName, String inpCountry, String inpCreateAccountTime, String inpAccountNumber, int inpMoney){
    	super(inpId, inpPasswd, inpName, inpCountry);
    	createAccountTime=inpCreateAccountTime;
    	accountNumber=inpAccountNumber;
    	money=inpMoney;
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
    //전체 계좌 조회
  	public void FindAllBankAccount(String inputId) throws SQLException{
  		DbConnect();
    	String quary="select * from BankAccount where userId = '"+inputId+"'";
      	pstm=conn.prepareStatement(quary);
      	rs=pstm.executeQuery();
      	System.out.println("UserId"+"\t\t"+"CreateAccountTime"+"\t"+"AccountNumber"+"\t\t"+"Money"); 
      	System.out.println("=============================================================================");
      	while(rs.next()){
      		id=rs.getString(1);
      		createAccountTime=rs.getString(2);
      		accountNumber=rs.getString(3);
      		money=rs.getInt(4);
      		String result=id+"\t\t"+createAccountTime+"\t"+accountNumber+"\t\t"+money;
      		System.out.println(result);
      	}
      	DBDisConnect();
  	}
  	//계좌 생성
  	public void MakeBankAccount(String inputId) throws SQLException{
  		DbConnect();
  		accountNumber = String.valueOf((int)(Math.random()*9999))+"-"+String.valueOf((int)(Math.random()*999))+"-"+String.valueOf((int)(Math.random()*999999));
  		System.out.print("계좌번호:"+accountNumber+"\n");
  		String curTime=t.GetTime();
  		String sql="insert into BankAccount values('"+inputId+"', '"+curTime+"', '"+accountNumber +"', "+money+")";
  		stmt.executeUpdate(sql);
  		stmt.executeUpdate("commit");
  		System.out.println(inputId+"님의 계좌 생성이 완료되었습니다.");
  		DBDisConnect();
  	}
  	//계좌 삭제
  	public void DeleteBankAccount(String inputId) throws SQLException {
  		DbConnect();
  		String findAccountNumber=null;
  		System.out.print("삭제하고 싶은 계좌의 Account Number: ");
  		accountNumber=key.next();
  		String findAccountNumSql="select AccountNumber from BankAccount where AccountNumber = '"+accountNumber+"'";
  		try {
			rs=stmt.executeQuery(findAccountNumSql);
			while(rs.next()){
	  			findAccountNumber =rs.getString("AccountNumber");
			}
		} 
  		catch (SQLException e) {
		}
  		if(findAccountNumber!=null){
  			String deleteSql="delete from BankAccount where UserId ='"+inputId+"' AND AccountNumber = '"+accountNumber+"'";
  	  		stmt.executeUpdate(deleteSql);
  	  		stmt.executeUpdate("commit");
  	  		System.out.println("계좌 삭제를 완료하였습니다.");
  		}
  		else{
  			System.out.println("유효하지 않은 계좌번호 입력입니다!");
  		}
  		DBDisConnect();
  	}
  	//이체(검사해야함)
  	public void TransferMoney(String inpId) throws SQLException{
  		DbConnect();
  		String findAccountNumber1=null, findAccountNumber2=null;
  		System.out.print("출금계좌번호: ");
  		String accountNum1=key.next();
  		
  		String findAccountNumSql1="select AccountNumber from BankAccount where AccountNumber = '"+accountNum1+"'";
  		try {
			rs=stmt.executeQuery(findAccountNumSql1);
			while(rs.next()){
	  			findAccountNumber1 =rs.getString("AccountNumber");
			}
		} 
  		catch (SQLException e) {
		}
  		if(findAccountNumber1!=null){
  			String getMoneySql="select Money from BankAccount where UserId = '"+inpId+"' AND accountNumber= '"+accountNum1+"'";
  	  		pstm=conn.prepareStatement(getMoneySql);
  	      	rs=pstm.executeQuery();
  	      	while(rs.next()){
  	      		money = rs.getInt("Money");
  	      	}
  	      	System.out.print("입금계좌번호: ");
    		String accountNum2=key.next();
  	      	String findAccountNumSql2="select AccountNumber from BankAccount where AccountNumber = '"+accountNum2+"'";
  	      		try {
  	      			rs=stmt.executeQuery(findAccountNumSql2);
  	      			while(rs.next()){
  	      				findAccountNumber2 =rs.getString("AccountNumber");
  	      			}
  	      		} 
    		catch (SQLException e) {
    		}
  	      	if(findAccountNumber2!=null){
  	      		System.out.print("액수: ");
  	      		int sendMoney = key.nextInt();
  	    		System.out.print("받는 예금주명: ");
  	    		String name = key.next();
  	    		
	  	      	String getUserIdSql="select Id from BankUser where Name = '"+name+"'";
	  	      	pstm=conn.prepareStatement(getUserIdSql);
	  	      	rs=pstm.executeQuery();
	  	      	while(rs.next()){
	  	      		id=rs.getString("Id");
	  	      	}
	  	      	if(id!=null){
	  	      		if(money>=sendMoney){
		  	      		String sendMoneySql="update BankAccount set Money = Money - "+sendMoney+" where UserId = '"+inpId+"' AND accountNumber= '"+accountNum1+"'";
		  	      		String receiveMoneySql="update BankAccount set Money = Money + "+sendMoney+" where UserId = '"+id+"' AND accountNumber= '"+accountNum2+"'";
		  	      		stmt.executeUpdate(sendMoneySql);
		  	      		stmt.executeUpdate(receiveMoneySql);
		  	  			stmt.executeUpdate("commit");
		  	  			System.out.println("송금이 완료되었습니다.");
		  	      	}
		  	      	else{
		  	      		System.out.println("잔액이 부족합니다.");
		  	      	}	
	  	      	}
	  	      	else{
	  	      		System.out.println("유효하지 않은 예금주입니다!");
	  	      	}
  	      	}
  	      	else{
  	      		System.out.println("유효하지 않은 계좌번호 입력입니다!");
  	      	}
  		}
  		else{
  			System.out.println("유효하지 않은 계좌번호 입력입니다!");
  		}
  		DBDisConnect();
  	}
  	//입금
  	public void Deposit(String inpId) throws SQLException{
  		DbConnect();
  		String findAccountNumber=null;
  		System.out.print("입금 계좌: ");
  		String depositAccountNum = key.next();
  		String findAccountNumSql="select AccountNumber from BankAccount where AccountNumber = '"+depositAccountNum+"'";
  		try {
			rs=stmt.executeQuery(findAccountNumSql);
			while(rs.next()){
	  			findAccountNumber =rs.getString("AccountNumber");
			}
		} 
  		catch (SQLException e) {
		}
  		if(findAccountNumber!=null){
  			System.out.print("입금액: ");
  	  		int money = key.nextInt();
  			String depositSql="update BankAccount set Money = Money + "+money+" where AccountNumber = '"+depositAccountNum+"'";
  	  		stmt.executeUpdate(depositSql);
  	  		stmt.executeUpdate("commit");
  	  		System.out.println("입금이 완료되었습니다.");
  		}
  		else{
  			System.out.println("유효하지 않은 계좌번호 입력입니다!");
  		}
  		DBDisConnect();
  	}
  	//출금
  	public void Withdraw(String inpId) throws SQLException{
  		DbConnect();
  		String findAccountNumber=null;
  		System.out.print("출금 계좌: ");
  		String withdrawAccountNum = key.next();
  		
  		String findAccountNumSql="select AccountNumber from BankAccount where AccountNumber = '"+withdrawAccountNum+"'";
  		try {
			rs=stmt.executeQuery(findAccountNumSql);
			while(rs.next()){
	  			findAccountNumber =rs.getString("AccountNumber");
			}
		} 
  		catch (SQLException e) {
		}
  		if(findAccountNumber!=null){
  			System.out.print("출금액: ");
  	  		int sendMoney = key.nextInt();
  	  		String getMoneySql="select Money from BankAccount where UserId = '"+inpId+"' AND accountNumber= '"+withdrawAccountNum+"'";
  	  		pstm=conn.prepareStatement(getMoneySql);
  	      	rs=pstm.executeQuery();
  	      	while(rs.next()){
  	      		money = rs.getInt("Money");
  	      	}
  	      	if(money>=sendMoney){
  	      		String withdrawSql="update BankAccount set Money = Money - "+sendMoney+" where AccountNumber = '"+withdrawAccountNum+"'";
  	      		stmt.executeUpdate(withdrawSql);
  	  			stmt.executeUpdate("commit");
  	  			System.out.println("출금이 완료되었습니다.");
  	      	}
  	      	else{
  	      		System.out.println("잔액이 부족합니다.");
  	      	}
  		}
  		else{
  			System.out.println("유효하지 않은 계좌번호 입력입니다!");
  		}
  		DBDisConnect();
  	}
  	//환전
  	public void Exchange(String inpId) throws SQLException{
  		DbConnect();
  		double Rate1=0.00;
  		double Rate2=0.00;
  		String findCountry1=null, findCountry2=null, findAccountNumber=null;
  		System.out.print("환전 대상 국가: ");
  		String country1=key.next();
  		String findCountrySql1="select Country from F_Money where Country = '"+country1+"'";
  		try {
			rs=stmt.executeQuery(findCountrySql1);
			while(rs.next()){
				findCountry1 =rs.getString("Country");
			}
		} 
  		catch (SQLException e) {
		}
  		if(findCountry1!=null){
  			String exchangeSql1="select Rate from F_Money where Country = '"+country1+"'";
  	  		pstm=conn.prepareStatement(exchangeSql1);
  	      	rs=pstm.executeQuery();
  	      	while(rs.next()){
  	      		Rate1 = rs.getDouble("Rate");
  	      	}
  	      	System.out.print("환전 국가: ");
    		String country2=key.next();
    		String findCountrySql2="select Country from F_Money where Country = '"+country2+"'";
      		try {
    			rs=stmt.executeQuery(findCountrySql2);
    			while(rs.next()){
    				findCountry2 =rs.getString("Country");
    			}
    		} 
      		catch (SQLException e) {
    		}
      		if(findCountry2!=null){
      	  		String exchangeSql2="select Rate from F_Money where Country = '"+country2+"'";
      	  		pstm=conn.prepareStatement(exchangeSql2);
      	      	rs=pstm.executeQuery();
      	      	while(rs.next()){
      	      		Rate2 = rs.getDouble("Rate");
      	      	}
      	      	double exchangeRatio = Rate2/Rate1;
      	      	System.out.print("출금계좌번호: ");
      	      	String withdrawAccountNum=key.next();
      	      	String findAccountNumSql="select AccountNumber from BankAccount where AccountNumber = '"+withdrawAccountNum+"'";
        		try {
        			rs=stmt.executeQuery(findAccountNumSql);
        			while(rs.next()){
        				findAccountNumber =rs.getString("AccountNumber");
        			}
        		} 
        		catch (SQLException e) {
        		}
      	      	if(findAccountNumber!=null){
	      	      	System.out.print("수령 액수: ");
	      	      	int receiveMoney = key.nextInt();
	      	      	String getMoneySql="select Money from BankAccount where UserId = '"+inpId+"' AND accountNumber= '"+withdrawAccountNum+"'";
	      	  		pstm=conn.prepareStatement(getMoneySql);
	      	      	rs=pstm.executeQuery();
	      	      	while(rs.next()){
	      	      		money = rs.getInt("Money");
	      	      	}
	      	      	if(money>=receiveMoney*exchangeRatio){
	      	      		String withdrawSql="update BankAccount set Money = Money - "+receiveMoney*exchangeRatio+" where AccountNumber = '"+withdrawAccountNum+"'";
	      	      		stmt.executeUpdate(withdrawSql);
	      	  			stmt.executeUpdate("commit");
	      	  			System.out.println("출금이 완료되었습니다.");
	      	  			System.out.println("수령한 금액은 "+receiveMoney+country2+" 입니다.");
	      	      	}
	      	      	else{
	      	      		System.out.println("잔액이 부족합니다.");
	      	      	}
      	      	}
      	      	else{
      	      		System.out.println("유효하지 않은 계좌번호 입력입니다!");
      	      	}
      		}
      		else{
      			System.out.println("유효하지 않은 국가 입력입니다!");
      		}
  		}
  		else{
  			System.out.println("유효하지 않은 국가 입력입니다!");
  		}
  		DBDisConnect();
  	}
}