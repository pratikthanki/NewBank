package newbank.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputValidator {
	//Returns null if date is invalid
	public static Date parseDate(String date, String format) {
		SimpleDateFormat parser = new SimpleDateFormat(format);
		Date d = null;
		
		if (date.length() == 0) return null;
	        
		try {
			d = parser.parse(date);
		} catch(ParseException e) {
			e.printStackTrace();
		}
		
		return d;
	}

	public static boolean isEmailAddressValid(String email) {
		if (email != null && email.length()< 3) 
			return false;
		
		return true;
	}
	//maxLength is not validate when it has a value of -1
	public static boolean validateTextLength(String address, int minLength, int maxLength) {
		if (address.length()< minLength || (maxLength != -1 && address.length()>maxLength)) 
			return false;
		
		return true;
	}
	public static boolean isNumeric(String number) {
		return !Double.isNaN(Double.parseDouble(number));
	}
}
