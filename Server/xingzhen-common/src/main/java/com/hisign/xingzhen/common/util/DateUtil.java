package com.hisign.xingzhen.common.util;

import com.hisign.xingzhen.common.constant.Constants;
import com.hisign.xingzhen.common.constant.DateStyle;
import org.apache.commons.lang.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 * 
 */
public class DateUtil {
	/**
	 * 日期格式
	 */
	public final static String datePattern = "yyyy-MM-dd";
	/**
	 * 时间格式
	 */
	public static String timePattern = "HH:mm:ss";
	/**
	 * 日期时间格式
	 */
	public static String datetimePattern = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 中文日期格式
	 */
	public final static String datePattern_CN = "yyyy年M月d日";
	/**
	 * 中文日期时间格式 精确到分
	 */
	public final static String datetimePattern_CN = "yyyy年M月d日H时m分";

	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

	private static final Object object = new Object();

	/**
	 * 返回默认的日期格式 (yyyy-MM-dd)
	 * 
	 * @return 日期类型为string类型
	 */
	public static String getDatePattern() {
		return datePattern;
	}

	/**
	 * 将日期格式转成（yyyy-MM-dd）
	 * 
	 * @param aDate
	 *            date类型的日期
	 * @return string类型的日期
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate != null) {
			df = new SimpleDateFormat(datePattern);
			returnValue = df.format(aDate);
		}
		return returnValue;
	}

	/**
	 * 将一个string类型的日期转成date类型的日期
	 * 
	 * @param aMask
	 *            日期的格式（如：yyyy-MM-dd）
	 * @param strDate
	 *            string类型的日期
	 * @return date类型的日期
	 * @see SimpleDateFormat
	 * @throws ParseException
	 *             转换异常
	 */
	public static final Date convertStringToDate(String format, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		if(StringUtils.isBlank(strDate)){
			return date;
		}
		df = new SimpleDateFormat(format);
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return date;
	}

	/**
	 * 将一个string类型的日期转成yyyy-MM-dd HH:mm:ss格式date类型的日期
	 * 
	 * @param strDate
	 *            string类型的日期
	 * @return date类型的日期
	 * @see SimpleDateFormat
	 * @throws ParseException
	 *             转换异常
	 */
	public static final Date convertStringToDateTime(String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		if(StringUtils.isBlank(strDate)){
			return date;
		}
		df = new SimpleDateFormat(datetimePattern);
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}

		return (date);
	}
	
	
	public static Date getNowDate() {
		try {
			SimpleDateFormat f = new SimpleDateFormat(datePattern);
			return f.parse(getNowDateStr());
		} catch (ParseException e) {
			return null;
		}
	}
	
	
	public static String getNowDateStr(){
		SimpleDateFormat f = new SimpleDateFormat(datePattern);
		return f.format(new Date());
	}

	/**
	 * 将一个date类型的日期转成HH:mm:ss格式string类型的日期
	 * 
	 * @param theTime
	 *            date类型的日期
	 * @return string类型的日期
	 * @throws ParseException
	 *             转换异常
	 */
	public static String getTimeNow(Date theTime) throws Exception {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * 返回一个格式为yyyy-MM-dd格式的Calendar
	 * 
	 * @return Calendar格式的类型
	 * @throws ParseException
	 *             转换异常
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datePattern);

		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * 自定义一个date类型日期的格式
	 * 
	 * @param aMask
	 *            日期的格式（如：yyyy-MM-dd）
	 * @param aDate
	 *            date类型的日期
	 * @return string类型的日期
	 * 
	 * @see SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate)
			throws Exception {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		} 
		return returnValue;
	}

	/**
	 * 将一个date类型的日期转成yyyy-MM-dd HH:mm:ss格式string类型的日期
	 * 
	 * @param aDate
	 *            date类型的日期
	 * @return string类型的日期
	 * @throws ParseException
	 *             转换异常
	 */
	public static final String getDateTime(Date aDate) throws Exception {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate != null) {
			df = new SimpleDateFormat(datetimePattern);
			returnValue = df.format(aDate);
		} 
		return returnValue;
	}

	/**
	 * 将一个date类型的日期转成yyyy-MM-dd格式string类型的日期
	 * 
	 * @param aDate
	 *            date类型的日期
	 * @return string类型的日期
	 * @throws ParseException
	 *             转换异常
	 */
	public static final String convertDateToString(Date aDate) throws Exception {
		return getDateTime(datePattern, aDate);
	}

	/**
	 * 自定义一个date类型日期的格式
	 * 
	 * @param pattern
	 *            日期的格式（如：yyyy-MM-dd）
	 * @return string类型的日期
	 * @see SimpleDateFormat
	 */
	public static final String convertDateToString(String pattern, Date aDate)
			throws Exception {
		return getDateTime(pattern, aDate);
	}

	/**
	 * 将一个String类型的日期转成yyyy-MM-dd格式Date类型的日期
	 * 
	 * @param strDate
	 * @return Date类型的日期
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;

		try {
			aDate = convertStringToDate(datePattern, strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return aDate;
	}

	/**
	 * 比较两个日期的大小 如果date1>date2 返回 1 如果date1=date2 返回 0 如果date1<date2 返回 -1
	 * 
	 * @param date1
	 *            日期1
	 * @param date2
	 *            日期2
	 * @return 比较结果1,0,-1
	 */
	public static int compareDate(Date date1, Date date2) throws Exception {
		String d1 = getDateTime(datePattern, date1);
		String d2 = getDateTime(datePattern, date2);

		if (d1 == null && d2 != null) {
			return -1;
		}else if (d1 != null && d2 == null) {
			return 1;
		}else if (d1 == null && d2 == null){
			return 0;
		}else {
			return d1.compareTo(d2);
		}
	}

	/**
	 * 将一个date类型的日期转成yyyy年M月d日H时m分格式string类型的日期
	 * 
	 * @param date
	 * @return string类型的日期
	 */
	public static String convertDatetimeToChineseString(Date date) {
		DateFormat df = new SimpleDateFormat(datetimePattern_CN);
		String strDate = df.format(date);
		return strDate;
	}

	/**
	 * 将一个date类型的日期转成yyyy年M月d日格式string类型的日期
	 * 
	 * @param date
	 * @return string类型的日期
	 */
	public static String convertDateToChineseString(Date date) {
		DateFormat df = new SimpleDateFormat(datePattern_CN);
		String strDate = df.format(date);
		return strDate;
	}

	/**
	 * 获取2个字符日期的月数差
	 * 
	 * @param p_startDate
	 * @param p_endDate
	 * @return 月数差（long型，相差几个月）
	 */
	public static long getMonthsOfTowDiffDate(String p_startDate,
			String p_endDate) throws ParseException {
		return getDaysOfTowDiffDate(p_startDate, p_endDate) / 30;
	}

	/**
	 * 获取指定日期的毫秒
	 * 
	 * @param p_date
	 *            Date型日期
	 * @return long型毫秒数
	 */
	public static long getMillisOfDate(Date p_date) {
		Calendar c = Calendar.getInstance();
		long longTime = 0L;
		if (null != p_date) {
			c.setTime(p_date);
			longTime = c.getTimeInMillis();
		}
		return longTime;

	}

	/**
	 * 字符型日期转化util.Date型日期
	 * 
	 * @Param:p_strDate 字符型日期
	 * @param p_format
	 *            格式:"yyyy-MM-dd" / "yyyy-MM-dd hh:mm:ss"
	 * @Return:java.util.Date型日期
	 * @Throws: ParseException
	 */
	public static Date toUtilDateFromStrDateByFormat(String p_strDate,
			String p_format) throws ParseException {
		Date l_date = null;
		DateFormat df = new SimpleDateFormat(p_format);
		if (p_strDate != null && (!"".equals(p_strDate)) && p_format != null
				&& (!"".equals(p_format))) {
			l_date = df.parse(p_strDate);
		}
		return l_date;
	}

	/**
	 * 获取2个字符日期的天数差
	 * 
	 * @param p_startDate
	 *            string类型
	 * @param p_endDate
	 *            string类型
	 * @return 天数差（long型，相差几天）
	 */
	public static long getDaysOfTowDiffDate(String p_startDate, String p_endDate)
			throws ParseException {

		Date l_startDate = toUtilDateFromStrDateByFormat(p_startDate,
				"yyyy-MM-dd");
		Date l_endDate = toUtilDateFromStrDateByFormat(p_endDate, "yyyy-MM-dd");
		long l_startTime = getMillisOfDate(l_startDate);
		long l_endTime = getMillisOfDate(l_endDate);
		long betweenDays = (long) ((l_endTime - l_startTime) / (1000 * 60 * 60 * 24));
		return betweenDays;
	}

	/**
	 * 将毫秒转换为Date类型
	 * 
	 * @param time
	 *            毫秒（long型）
	 * @return Date类型的时间
	 */
	public static Date getDate(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return c.getTime();
	}

	/**
	 * 获取SimpleDateFormat
	 * 
	 * @param pattern
	 *            日期格式
	 * @return SimpleDateFormat对象
	 * @throws RuntimeException
	 *             异常：非法日期格式
	 */
	private static SimpleDateFormat getDateFormat(String pattern)
			throws RuntimeException {
		SimpleDateFormat dateFormat = threadLocal.get();
		if (dateFormat == null) {
			synchronized (object) {
				if (dateFormat == null) {
					dateFormat = new SimpleDateFormat(pattern);
					dateFormat.setLenient(false);
					threadLocal.set(dateFormat);
				}
			}
		}
		dateFormat.applyPattern(pattern);
		return dateFormat;
	}

	/**
	 * 获取日期中的某数值。如获取月份
	 * 
	 * @param date
	 *            日期
	 * @param dateType
	 *            日期格式（如：MM，yyyy，dd）
	 * @return 数值(int型)
	 */
	private static int getInteger(Date date, int dateType) {
		int num = 0;
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
			num = calendar.get(dateType);
		}
		return num;
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 * 
	 * @param date
	 *            日期字符串
	 * @param dateType
	 *            类型
	 * @param amount
	 *            数值
	 * @return 计算后日期字符串
	 */
	private static String addInteger(String date, int dateType, int amount) {
		String dateString = null;
		DateStyle dateStyle = getDateStyle(date);
		if (dateStyle != null) {
			Date myDate = StringToDate(date, dateStyle);
			myDate = addInteger(myDate, dateType, amount);
			dateString = DateToString(myDate, dateStyle);
		}
		return dateString;
	}

	/**
	 * 增加日期中某类型的某数值。如增加日期
	 * 
	 * @param date
	 *            日期（date型）
	 * @param dateType
	 *            类型（如：MM，yyyy，dd）
	 * @param amount
	 *            数值（int型）
	 * @return 计算后日期
	 */
	private static Date addInteger(Date date, int dateType, int amount) {
		Date myDate = null;
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(dateType, amount);
			myDate = calendar.getTime();
		}
		return myDate;
	}

	/**
	 * 获取精确的日期
	 * 
	 * @param timestamps
	 *            时间long集合
	 * @return 日期
	 */
	private static Date getAccurateDate(List<Long> timestamps) {
		Date date = null;
		long timestamp = 0;
		Map<Long, long[]> map = new HashMap<Long, long[]>();
		List<Long> absoluteValues = new ArrayList<Long>();

		if (timestamps != null && timestamps.size() > 0) {
			if (timestamps.size() > 1) {
				for (int i = 0; i < timestamps.size(); i++) {
					for (int j = i + 1; j < timestamps.size(); j++) {
						long absoluteValue = Math.abs(timestamps.get(i)
								- timestamps.get(j));
						absoluteValues.add(absoluteValue);
						long[] timestampTmp = { timestamps.get(i),
								timestamps.get(j) };
						map.put(absoluteValue, timestampTmp);
					}
				}

				// 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
				// 因此不能将minAbsoluteValue取默认值0
				long minAbsoluteValue = -1;
				if (!absoluteValues.isEmpty()) {
					minAbsoluteValue = absoluteValues.get(0);
					for (int i = 1; i < absoluteValues.size(); i++) {
						if (minAbsoluteValue > absoluteValues.get(i)) {
							minAbsoluteValue = absoluteValues.get(i);
						}
					}
				}

				if (minAbsoluteValue != -1) {
					long[] timestampsLastTmp = map.get(minAbsoluteValue);

					long dateOne = timestampsLastTmp[0];
					long dateTwo = timestampsLastTmp[1];
					if (absoluteValues.size() > 1) {
						timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne
								: dateTwo;
					}
				}
			} else {
				timestamp = timestamps.get(0);
			}
		}

		if (timestamp != 0) {
			date = new Date(timestamp);
		}
		return date;
	}

	/**
	 * 判断字符串是否为日期字符串
	 * 
	 * @param date
	 *            日期字符串
	 * @return true 或者 false
	 */
	public static boolean isDate(String date) {
		boolean isDate = false;
		if (date != null) {
			if (getDateStyle(date) != null) {
				isDate = true;
			}
		}
		return isDate;
	}

	/**
	 * 获取日期字符串的日期风格。失敗返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 日期风格
	 */
	public static DateStyle getDateStyle(String date) {
		DateStyle dateStyle = null;
		Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
		List<Long> timestamps = new ArrayList<Long>();
		for (DateStyle style : DateStyle.values()) {
			if (style.isShowOnly()) {
				continue;
			}
			Date dateTmp = null;
			if (date != null) {
				try {
					ParsePosition pos = new ParsePosition(0);
					dateTmp = getDateFormat(style.getValue()).parse(date, pos);
					if (pos.getIndex() != date.length()) {
						dateTmp = null;
					}
				} catch (Exception e) {
				}
			}
			if (dateTmp != null) {
				timestamps.add(dateTmp.getTime());
				map.put(dateTmp.getTime(), style);
			}
		}
		Date accurateDate = getAccurateDate(timestamps);
		if (accurateDate != null) {
			dateStyle = map.get(accurateDate.getTime());
		}
		return dateStyle;
	}

	/**
	 * 将日期字符串转化为日期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 日期
	 */
	public static Date StringToDate(String date) {
		DateStyle dateStyle = getDateStyle(date);
		return StringToDate(date, dateStyle);
	}

	/**
	 * 将日期字符串转化为日期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param pattern
	 *            日期格式
	 * @return 日期
	 */
	public static Date StringToDate(String date, String pattern) {
		Date myDate = null;
		if (date != null) {
			try {
				myDate = getDateFormat(pattern).parse(date);
			} catch (Exception e) {
			}
		}
		return myDate;
	}

	/**
	 * 将日期字符串转化为日期。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param dateStyle
	 *            日期风格
	 * @return 日期
	 */
	public static Date StringToDate(String date, DateStyle dateStyle) {
		Date myDate = null;
		if (dateStyle != null) {
			myDate = StringToDate(date, dateStyle.getValue());
		}
		return myDate;
	}

	/**
	 * 将日期转化为日期字符串。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式（如：yyyy-MM-dd）
	 * @return 日期字符串
	 */
	public static String DateToString(Date date, String pattern) {
		String dateString = null;
		if (date != null) {
			try {
				dateString = getDateFormat(pattern).format(date);
			} catch (Exception e) {
			}
		}
		return dateString;
	}

	/**
	 * 将日期转化为日期字符串。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param dateStyle
	 *            日期风格
	 * @return 日期字符串
	 */
	public static String DateToString(Date date, DateStyle dateStyle) {
		String dateString = null;
		if (dateStyle != null) {
			dateString = DateToString(date, dateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param newPattern
	 *            新日期格式
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, String newPattern) {
		DateStyle oldDateStyle = getDateStyle(date);
		return StringToString(date, oldDateStyle, newPattern);
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param newDateStyle
	 *            新日期风格
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, DateStyle newDateStyle) {
		DateStyle oldDateStyle = getDateStyle(date);
		return StringToString(date, oldDateStyle, newDateStyle);
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddPattern
	 *            旧日期格式
	 * @param newPattern
	 *            新日期格式
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, String olddPattern,
			String newPattern) {
		return DateToString(StringToDate(date, olddPattern), newPattern);
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddDteStyle
	 *            旧日期风格
	 * @param newParttern
	 *            新日期格式
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, DateStyle olddDteStyle,
			String newParttern) {
		String dateString = null;
		if (olddDteStyle != null) {
			dateString = StringToString(date, olddDteStyle.getValue(),
					newParttern);
		}
		return dateString;
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddPattern
	 *            旧日期格式
	 * @param newDateStyle
	 *            新日期风格
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, String olddPattern,
			DateStyle newDateStyle) {
		String dateString = null;
		if (newDateStyle != null) {
			dateString = StringToString(date, olddPattern,
					newDateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * 将日期字符串转化为另一日期字符串。失败返回null。
	 * 
	 * @param date
	 *            旧日期字符串
	 * @param olddDteStyle
	 *            旧日期风格
	 * @param newDateStyle
	 *            新日期风格
	 * @return 新日期字符串
	 */
	public static String StringToString(String date, DateStyle olddDteStyle,
			DateStyle newDateStyle) {
		String dateString = null;
		if (olddDteStyle != null && newDateStyle != null) {
			dateString = StringToString(date, olddDteStyle.getValue(),
					newDateStyle.getValue());
		}
		return dateString;
	}

	/**
	 * 增加日期的年份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param yearAmount
	 *            增加数量(int)。可为负数
	 * @return 增加年份后的日期字符串
	 */
	public static String addYear(String date, int yearAmount) {
		return addInteger(date, Calendar.YEAR, yearAmount);
	}

	/**
	 * 增加日期的年份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param yearAmount
	 *            增加数量(int)。可为负数
	 * @return 增加年份后的日期
	 */
	public static Date addYear(Date date, int yearAmount) {
		return addInteger(date, Calendar.YEAR, yearAmount);
	}

	/**
	 * 增加日期的月份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param monthAmount
	 *            增加数量(int)。可为负数
	 * @return 增加月份后的日期字符串
	 */
	public static String addMonth(String date, int monthAmount) {
		return addInteger(date, Calendar.MONTH, monthAmount);
	}

	/**
	 * 增加日期的月份。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param monthAmount
	 *            增加数量(int)。可为负数
	 * @return 增加月份后的日期
	 */
	public static Date addMonth(Date date, int monthAmount) {
		return addInteger(date, Calendar.MONTH, monthAmount);
	}

	/**
	 * 增加日期的天数。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param dayAmount
	 *            增加数量(int)。可为负数
	 * @return 增加天数后的日期字符串
	 */
	public static String addDay(String date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 增加日期的天数。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param dayAmount
	 *            增加数量(int)。可为负数
	 * @return 增加天数后的日期
	 */
	public static Date addDay(Date date, int dayAmount) {
		return addInteger(date, Calendar.DATE, dayAmount);
	}

	/**
	 * 增加日期的小时。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param hourAmount
	 *            增加数量(int)。可为负数
	 * @return 增加小时后的日期字符串
	 */
	public static String addHour(String date, int hourAmount) {
		return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}

	/**
	 * 增加日期的小时。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param hourAmount
	 *            增加数量(int)。可为负数
	 * @return 增加小时后的日期
	 */
	public static Date addHour(Date date, int hourAmount) {
		return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}

	/**
	 * 增加日期的分钟。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param minuteAmount
	 *            增加数量(int)。可为负数
	 * @return 增加分钟后的日期字符串
	 */
	public static String addMinute(String date, int minuteAmount) {
		return addInteger(date, Calendar.MINUTE, minuteAmount);
	}

	/**
	 * 增加日期的分钟。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param minuteAmount
	 *            增加数量(int)。可为负数
	 * @return 增加分钟后的日期
	 */
	public static Date addMinute(Date date, int minuteAmount) {
		return addInteger(date, Calendar.MINUTE, minuteAmount);
	}

	/**
	 * 增加日期的秒钟。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @param secondAmount
	 *            增加数量(int)。可为负数
	 * @return 增加秒钟后的日期字符串
	 */
	public static String addSecond(String date, int secondAmount) {
		return addInteger(date, Calendar.SECOND, secondAmount);
	}

	/**
	 * 增加日期的秒钟。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @param secondAmount
	 *            增加数量(int)。可为负数
	 * @return 增加秒钟后的日期
	 */
	public static Date addSecond(Date date, int secondAmount) {
		return addInteger(date, Calendar.SECOND, secondAmount);
	}

	/**
	 * 获取日期的年份。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 年份(yyyy)
	 */
	public static int getYear(String date) {
		return getYear(StringToDate(date));
	}

	/**
	 * 获取日期的年份。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 年份(yyyy)
	 */
	public static int getYear(Date date) {
		return getInteger(date, Calendar.YEAR);
	}

	/**
	 * 获取日期的月份。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 月份(MM)
	 */
	public static int getMonth(String date) {
		return getMonth(StringToDate(date));
	}

	/**
	 * 获取日期的月份。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 月份(MM)
	 */
	public static int getMonth(Date date) {
		return getInteger(date, Calendar.MONTH) + 1;
	}

	/**
	 * 获取日期的天数。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 天(dd)
	 */
	public static int getDay(String date) {
		return getDay(StringToDate(date));
	}

	/**
	 * 获取日期的天数。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 天(dd)
	 */
	public static int getDay(Date date) {
		return getInteger(date, Calendar.DATE);
	}

	/**
	 * 获取日期的小时。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 小时
	 */
	public static int getHour(String date) {
		return getHour(StringToDate(date));
	}

	/**
	 * 获取日期的小时。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 小时
	 */
	public static int getHour(Date date) {
		return getInteger(date, Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取日期的分钟。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 分钟
	 */
	public static int getMinute(String date) {
		return getMinute(StringToDate(date));
	}

	/**
	 * 获取日期的分钟。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 分钟
	 */
	public static int getMinute(Date date) {
		return getInteger(date, Calendar.MINUTE);
	}

	/**
	 * 获取日期的秒钟。失败返回0。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 秒钟
	 */
	public static int getSecond(String date) {
		return getSecond(StringToDate(date));
	}

	/**
	 * 获取日期的秒钟。失败返回0。
	 * 
	 * @param date
	 *            日期
	 * @return 秒钟
	 */
	public static int getSecond(Date date) {
		return getInteger(date, Calendar.SECOND);
	}

	/**
	 * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 日期
	 */
	public static String getDate(String date) {
		return StringToString(date, DateStyle.YYYY_MM_DD);
	}

	/*
	 * /** 获取日期。默认yyyy-MM-dd格式。失败返回null。
	 * 
	 * @param date 日期
	 * 
	 * @return 日期
	 */
	// public static String getDate(Date date) {
	// return DateToString(date, DateStyle.YYYY_MM_DD);
	// }

	/**
	 * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
	 * 
	 * @param date
	 *            日期字符串
	 * @return 时间
	 */
	public static String getTime(String date) {
		return StringToString(date, DateStyle.HH_MM_SS);
	}

	/**
	 * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
	 * 
	 * @param date
	 *            日期
	 * @return 时间
	 */
	public static String getTime(Date date) {
		return DateToString(date, DateStyle.HH_MM_SS);
	}

	/**
	 * 获取两个日期相差的天数
	 * 
	 * @param date
	 *            日期字符串
	 * @param otherDate
	 *            另一个日期字符串
	 * @return 相差天数。如果失败则返回-1
	 */
	public static int getIntervalDays(String date, String otherDate) {
		return getIntervalDays(StringToDate(date), StringToDate(otherDate));
	}

	/**
	 * 相差天数
	 * 
	 * @param date
	 *            日期
	 * @param otherDate
	 *            另一个日期
	 * @return 相差天数。如果失败则返回-1
	 */
	public static int getIntervalDays(Date date, Date otherDate) {
		int num = -1;
		Date dateTmp = StringToDate(DateUtil.getDate(date),
				DateStyle.YYYY_MM_DD);
		Date otherDateTmp = StringToDate(DateUtil.getDate(otherDate),
				DateStyle.YYYY_MM_DD);
		if (dateTmp != null && otherDateTmp != null) {
			long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
			num = (int) (time / (24 * 60 * 60 * 1000));
		}
		return num;
	}

	/**
	 * 取得年龄大小
	 * 
	 * @param birthDay
	 *            出生日期
	 * @return int类型的年龄
	 * @throws Exception
	 */
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			throw new IllegalArgumentException("出生时间大于当前时间!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;// 注意此处，如果不加1的话计算结果是错误的
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthDay);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				} else {
				}
			} else {
				age--;
			}
		} else {
		}

		return age;
	}

	/**
	 * 取得系统当前时间
	 * 
	 * @return 系统当前时间
	 */
	public static Date getSystemTime() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getSystemDateTimeStamp() {

		Date date = Calendar.getInstance().getTime();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = sdf.format(date);
		try {
			return string2Time(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
	 * 
	 * @param dateString
	 *            需要转换为timestamp的字符串
	 * @return dataTime timestamp
	 */
	public static Timestamp string2Time(String dateString)
			throws ParseException {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String tsStr = dateString;
		try {
			ts = Timestamp.valueOf(tsStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ts;
	}
	
	/**
	 * 类名: DateUtil.java
	 * 描述: 根据时间返回时间区间，例如 月度  季度  年度
	 * 公司: 北京海鑫科金高科技股份有限公司
	 * 作者: 何建辉
	 * 版本: 
	 * 参数: @param dateType
	 * 参数: @return Date[] 开始时间  结束时间  
	 * 创建时间: 2017年5月8日
	 * 最后修改时间: 2017年5月8日
	 */
	@SuppressWarnings("deprecation")
	public static Date[] getDateSection(int dateType,Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		Date[] arr = new Date[2];
		
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		if (dateType== Constants.DateType.YEAR) {//年度
			calendar.set(Calendar.MONTH, 1);
			arr[0] = calendar.getTime();
			calendar.add(Calendar.YEAR, 1);
			arr[1] = calendar.getTime();
		}else if (dateType == Constants.DateType.QUARTER) {//季度 1-3  4-6 7-9 10-12
			int month = date.getMonth();
			calendar.set(Calendar.DATE, 1);
			if (month<4) {
				calendar.set(Calendar.MONTH, 1);
				arr[0] = calendar.getTime();
				calendar.set(Calendar.MONTH, 4);
				arr[1] = calendar.getTime();
			}else if (month<7) {
				calendar.set(Calendar.MONTH, 4);
				arr[0] = calendar.getTime();
				calendar.set(Calendar.MONTH, 7);
				arr[1] = calendar.getTime();
			}else if (month<10) {
				calendar.set(Calendar.MONTH, 7);
				arr[0] = calendar.getTime();
				calendar.set(Calendar.MONTH, 10);
				arr[1] = calendar.getTime();
			}else{
				calendar.set(Calendar.MONTH, 10);
				arr[0] = calendar.getTime();
				calendar.add(Calendar.YEAR, 1);
				calendar.set(Calendar.MONTH, 1);
				arr[1] = calendar.getTime();
			}
		}else {//月度
			calendar.set(Calendar.DATE, 1);
			arr[0] = calendar.getTime();
			calendar.add(Calendar.MONTH, 1);
			arr[1] = calendar.getTime();
		}
		
		return arr;
	}

}
