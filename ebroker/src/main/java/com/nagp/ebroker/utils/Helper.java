package com.nagp.ebroker.utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Helper {
	private Helper() {

	}

	public static boolean checkTime(LocalDateTime currentDate) {
		DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
		int hourOfTheDay = currentDate.getHour();
		if (dayOfWeek.getValue() < 6 && (hourOfTheDay >= 9 && hourOfTheDay <= 17)) {
			return true;
		}
		return false;

	}
}
