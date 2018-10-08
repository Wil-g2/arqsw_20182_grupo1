package com.terra.pp.singleton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static DateUtil instance;
	
	private DateUtil() {
		System.out.println("Criando DateUtil...");
	}
	
	public  static DateUtil getInstance() {
		if (instance == null) {
			instance = new DateUtil();
		}
		return instance;
	}
	
	/* ANTES */
//	public Date strToDate(String str, String format){
//		DateFormat df = new SimpleDateFormat(format);
//		df.setLenient(false);
//		try {
//			return df.parse(str);
//		} catch (ParseException e) {
//			return null;
//		}
//		
//	}
	
	public Date strToDate(String str, String format){
		DateFormat df = new SimpleDateFormat(format);
		df.setLenient(false);
		try {
			return df.parse(str);
		} catch (ParseException e) {
			return null;
		}
		
	}
	
	
}