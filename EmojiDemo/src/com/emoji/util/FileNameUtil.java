package com.emoji.util;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class FileNameUtil {

	public static String getName(String path) {
		String name = path.substring(path.lastIndexOf(File.separator) + 1);
		return name;
	}

	public static String getNameBySystemTime(String suffix) {
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		StringBuilder builder = new StringBuilder();
		builder.append(year);
		if (month < 10)
			builder.append("0").append(month);
		else
			builder.append(month);
		if (day < 10)
			builder.append("0").append(day);
		else
			builder.append(day);
		if (hour < 10)
			builder.append("0").append(hour);
		else
			builder.append(hour);
		if (minute < 10)
			builder.append("0").append(minute);
		else
			builder.append(minute);
		if (second < 10)
			builder.append("0").append(second);
		else
			builder.append(second);
		if (suffix.startsWith("."))
			builder.append(suffix);
		else
			builder.append(".").append(suffix);
		return builder.toString();
	}
}
