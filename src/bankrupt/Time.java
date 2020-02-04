package bankrupt;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time {
	public String GetTime(){
		DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date nowDate = new Date();
		String tempDate = sdFormat.format(nowDate);
		return tempDate;
	}
}