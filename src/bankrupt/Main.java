package bankrupt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
public class Main {
	static Scanner key = new Scanner(System.in);
	static Connection conn = null; // DB연결된 상태(세션)을 담은 객체
    static Statement stmt=null;	//statement 객체
    public static void main(String args[]) {
    	
        try {
             conn = DBConnection.getConnection();
             stmt=conn.createStatement();
             
             MakeTable();	//테이블 및 관리자 계정 삽입
             Account();	//관리자 혹은 사용자 계정.
        } 
        catch (Exception e) {
        	System.out.println("유효하지 않은 입력입니다.");
        	System.out.println("프로그램을 종료합니다.");
        }
        finally {
        	DBDisConnect();
        }
    }
    public static void MakeTable() throws SQLException{
    	/*//table 생성
    	RelationTable r = new RelationTable();
        r.MakeTable();
        //관리자 정보 생성
        Admin ad=new Admin("이름","아이디","비밀번호");
        ad.MakeAdmin();*/
    }
    //초기 화면
    public static void Account() throws SQLException{
    	while(true){
    		System.out.println("================");
    		System.out.println("  [은행업무 시스템]");
    		System.out.println("----------------");
    		System.out.println("  1. 로그인");
    		System.out.println("  2. 회원가입");
    		System.out.println("  3. 회원탈퇴");
    		System.out.println("  4. 프로그램 종료");
    		System.out.println("================");
    		System.out.print("입력: ");
    		String input=key.next();
    		switch(input){
    			case "1":
    				AccessAccount();
    				break;
    			case "2":
    				MakeUser();
    				break;
    			case "3":
    				DeleteUser();
    				break;
    			case "4":
    				return;
    			default:
    				System.out.println("다시 입력해 주세요!");
    				break;
    		}
    	}
    }
    //로그인 화면
    public static void AccessAccount() throws SQLException{
    	while(true){
    		String inputId,inputPasswd;
        	boolean admin=false;
        	boolean user=false;
        	System.out.println("================");
        	System.out.println("   [로그인 화면]");
        	System.out.println("----------------");
        	System.out.print("   아이디: ");
        	inputId=key.next();
    		System.out.print("   비밀번호: ");
    		inputPasswd=key.next();
    		System.out.println("================");
    		//admin
    		Admin ad=new Admin();
    		if(inputId.equals(ad.getSelectId()) && inputPasswd.equals(ad.getSelectPasswd())){
    			admin=true;
    		}
    		//bankUser
    		User u = new User();
    		if(inputId.equals(u.getSelectId(inputId)) && inputPasswd.equals(u.getSelectPasswdd(inputPasswd))){
    			user=true;
    		}
    		if(admin==true){
    			Admin();
    			return;
    		}
    		else if(user==true){
    			User(inputId,inputPasswd);
    			return;
    		}
    		else{
    			System.out.println("사용자 정보가 존재하지 않습니다!");
    		}
    		if(admin==false && user==false){
    			System.out.println("1번을 입력하면 이전 페이지로 돌아갑니다.");
        		System.out.println("그 외의 키를 입력하면 로그인 시도를 합니다.");
        		System.out.print("입력: ");
        		int choice=key.nextInt();
        		switch(choice){
        			case 1:
        				return;
        			default:
        				break;
        		}
    		}
    	}
    }
    //관리자 모드
    public static void Admin() throws SQLException{
    	while(true){
    		System.out.println("================");
    		System.out.println("   [관리자 모드]");
    		System.out.println("----------------");
    		System.out.println("   1. 사용자 정보 조회");
    		System.out.println("   2. 사용자 삭제");
    		System.out.println("   3. 로그아웃");
    		System.out.println("   4. 프로그램 종료");
    		System.out.println("================");
    		System.out.print("입력: ");
    		String input=key.next();
    		switch(input){
    			case "1":
    				FindUserInfo();
    				break;
    			case "2":
    				DeleteUserInfo();
    				break;
    			case "3":
    				return;
    			case "4":
    				DBDisConnect();
    				System.exit(0);
    				break;
    			default:
    				System.out.println("다시 입력해 주세요!");
    				break;
    		}
    	}
    }
    public static void FindUserInfo() throws SQLException{
    	Admin ad=new Admin();
    	ad.FindUserInfo();
    	System.out.println("아무 키나 입력하면 이전 페이지로 이동합니다.");
    	System.out.print("입력: ");
    	String button=key.next();
    	if(button=="1"){
    		return;
    	}
    }
    public static void DeleteUserInfo() throws SQLException{
    	while(true){
    		System.out.println("================");
    		System.out.println(" [관리자 모드 - 삭제]");
    		System.out.println("----------------");
    		System.out.println(" 1. 단일 삭제");
    		System.out.println(" 2. 전체 삭제");
    		System.out.println(" 3. 이전 페이지");
    		System.out.println(" 4. 프로그램 종료");
    		System.out.println("================");
    		System.out.print("입력: ");
    		String input=key.next();
    		switch(input){
    			case "1":
    				One_UserDelete();
    				break;
    			case "2":
    				All_UserDelete();
    				break;
    			case "3":
    				return;
    			case "4":
    				DBDisConnect();
    				System.exit(0);
    				break;
    			default:
    				System.out.println("다시 입력해 주세요!");
    				break;
    		}
    	}
    }
    public static void One_UserDelete() throws SQLException{
    	System.out.print("삭제할 사용자 이름: ");
    	String name=key.next();
    	Admin ad=new Admin();
    	ad.OneUserDelete(name);
    	return;
    }
    public static void All_UserDelete() throws SQLException{
    	Admin ad=new Admin();
    	ad.AllUserDelete();
		return;
    }
    
    //사용자 모드
    public static void User(String inpId, String inpPasswd) throws SQLException{
    	while(true){
    		System.out.println("================");
    		System.out.println("   [사용자 모드]");
    		System.out.println("----------------");
    		System.out.println("   1. 계좌");
    		System.out.println("   2. 이체");
    		System.out.println("   3. 입/출금");
    		System.out.println("   4. 환전");
    		System.out.println("   5. 로그아웃");
    		System.out.println("   6. 프로그램 종료"); 
    		System.out.println("================");
    		System.out.print("입력: ");
    		String input=key.next();
    		switch(input){
    			case "1":
    				BankAccount(inpId, inpPasswd);
    				break;
    			case "2":
    				TransferMoney(inpId);
    				break;
    			case "3":
    				Deposit_Withdraw(inpId);
    				break;
    			case "4":
    				Exchange(inpId);
    				break;
    			case "5":
    				return;
    			case "6":
    				DBDisConnect();
    				System.exit(0);
    				break;
    			default:
    				System.out.println("다시 입력해 주세요!");
    				break;
    		}
    	}
    }
    public static void BankAccount(String inpId, String inpPasswd) throws SQLException{
    	while(true){
    		System.out.println("================");
    		System.out.println(" [사용자 모드 - 계좌]");
    		System.out.println("----------------");
    		System.out.println(" 1. 전체 계좌 조회");
    		System.out.println(" 2. 계좌 생성");
    		System.out.println(" 3. 계좌 삭제");
    		System.out.println(" 4. 이전 페이지");
    		System.out.println(" 5. 프로그램 종료");
    		System.out.println("================");
    		System.out.print("입력: ");
    		String input=key.next();
    		switch(input){
    			case "1":
    				FindAllBankAccount(inpId);
    				break;
    			case "2":
    				MakeBankAccount(inpId);
    				break;
    			case "3":
    				DeleteBankAccount(inpId);
    				break;
    			case "4":
    				return;
    			case "5":
    				DBDisConnect();
    				System.exit(0);
    				break;
    			default:
    				System.out.println("다시 입력해 주세요!");
    				break;
    		}
    	}
    }
    public static void FindAllBankAccount(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).FindAllBankAccount(inpId);
    }
    public static void MakeBankAccount(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).MakeBankAccount(inpId);
    }
    public static void DeleteBankAccount(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).DeleteBankAccount(inpId);
    }
    public static void TransferMoney(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).TransferMoney(inpId);
    }
    public static void Deposit_Withdraw(String inpId) throws SQLException{
    	while(true){
    		System.out.println("=================");
    		System.out.println("[사용자 모드 - 입/출금]");
    		System.out.println("-----------------");
    		System.out.println("1. 입금");
    		System.out.println("2. 출금");
    		System.out.println("3. 이전 페이지");
    		System.out.println("4. 프로그램 종료"); 
    		System.out.println("=================");
    		System.out.print("입력: ");
    		String input=key.next();
    		switch(input){
    			case "1":
    				Deposit(inpId);
    				break;
    			case "2":
    				Withdraw(inpId);
    				break;
    			case "3":
    				return;
    			case "4":
    				DBDisConnect();
    				System.exit(0);
    				break;
    			default:
    				System.out.println("다시 입력해 주세요!");
    				break;
    		}
    	}
    }
    public static void Deposit(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).Deposit(inpId);
    }
    public static void Withdraw(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).Withdraw(inpId);
    }
    public static void Exchange(String inpId) throws SQLException{
    	User u = new BankAccount();
    	((bankrupt.BankAccount) u).Exchange(inpId);
    }
    //회원가입
    public static void MakeUser() throws SQLException {
    	String name,passwd,country,id;
    	System.out.println("================");
    	System.out.println("    [회원가입]");
    	System.out.println("----------------");
		System.out.print("이름: ");
		name=key.next();
		System.out.print("아이디: ");
		id=key.next();
		System.out.print("비밀번호: ");
		passwd=key.next();
		System.out.print("국적: ");
		country=key.next();
		System.out.println("================");
		User u = new User(id,passwd,name,country);
		u.MakeUser();
	}
    //회원탈퇴
    public static void DeleteUser() throws SQLException{
    	String id,passwd;
    	System.out.println("================");
    	System.out.println("    [회원탈퇴]");
    	System.out.println("----------------");
    	System.out.print("아이디: ");
		id=key.next();
		System.out.print("비밀번호: ");
		passwd=key.next();
		System.out.println("================");
		User u = new User();
		u.SetUserId(id);
		u.setUserPasswd(passwd);
		u.DeleteUser();
    }
    private static void DBDisConnect(){
  		try { 
            if ( stmt != null ){
            	stmt.close(); 
            }
            if ( conn != null ){
            	conn.close(); 
            }
            System.out.println("프로그램을 종료합니다.");
        }
        catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
  	}
}