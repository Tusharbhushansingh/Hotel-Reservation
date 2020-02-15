package com.mindtree.hotelreservation.util;

import java.util.Date;

public class StringToDateConversion {

	@SuppressWarnings("deprecation")
	public static Date getDayMonthYearFromStringDate(String checkInDateString) {
		
		String[] stringTokens = checkInDateString.split("-");
		int[] dateTokens = new int[stringTokens.length];
		for (int i = 0; i < stringTokens.length; i++) {
			dateTokens[i] = Integer.parseInt(stringTokens[i]);
		}
		Date date = new Date(dateTokens[0], dateTokens[1], dateTokens[2]);

		return date;
	}

}
