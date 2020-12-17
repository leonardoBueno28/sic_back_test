package com.sic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ObjectBuilder {
	public static Integer getNewIntegerValue(String item, String format) {
		return Integer.parseInt(item);
	}
	
	public static Double getNewDoubleValue(String item, String format) {
		return Double.parseDouble(item);
	}
	
	public static Boolean getNewBooleanValue(String item, String format) {
		return Boolean.parseBoolean(item);
	}
	
	public static Character getNewCharacterValue(String item, String format) {
		return new Character(item.charAt(0));
	}
	
	public static Object getNewFloatValue(String item, String format) {
		// TODO Auto-generated method stub
		return new Float(item);
	}

	public static Date getNewDateValue(String item, String format) {
		if(format.isEmpty()) {
			format = "yyyy-MM-dd";
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = formatter.parse(item);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static LocalDate getNewLocalDateValue(String item, String format) {
		if(format.isEmpty()) {
			format = "yyyy-MM-dd";
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDate.parse(item, formatter);
	}
	
	public static LocalDateTime getNewLocalDateTimeValue(String item, String format) {
		if(format.isEmpty()) {
			format = "yyyy-MM-dd HH:mm:ss";
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(item, formatter);
	}
}
