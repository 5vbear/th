/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Descriptions
 * 
 * @version 2013-8-24
 * @author PSET
 * @since JDK1.6
 * 
 */
public class DateUtil {
	public static final String DATE_TYPE_DAY = "day";
	public static final String DATE_TYPE_WEEK = "week";
	public static final String DATE_TYPE_MONTH = "month";
	public static final String DATE_TYPE_YEAR = "year";

	public static String getStartTime( String time, String dateType ) throws ParseException {
		String inputDateFormat = "yyyy-MM-dd";// yyyyMMdd 20130821
		String outputDateFormat = "yyyyMMdd";
		SimpleDateFormat isdf = new SimpleDateFormat( inputDateFormat );
		Calendar ca = Calendar.getInstance();
		String startDate = null;
		if ( DATE_TYPE_DAY.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
		}
		if ( DATE_TYPE_WEEK.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.set( Calendar.DAY_OF_WEEK, 2 );
		}
		else if ( DATE_TYPE_MONTH.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.set( Calendar.DAY_OF_MONTH, 1 );
		}
		else if ( DATE_TYPE_YEAR.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.set( Calendar.DAY_OF_YEAR, 1 );

		}
		SimpleDateFormat osdf = new SimpleDateFormat( outputDateFormat );

		startDate = osdf.format( ca.getTime() );

		return startDate;
	}

	public static String getEndTime( String time, String dateType ) throws ParseException {
		String inputDateFormat = "yyyy-MM-dd";
		String outputDateFormat = "yyyyMMdd";
		SimpleDateFormat isdf = new SimpleDateFormat( inputDateFormat );
		Calendar ca = Calendar.getInstance();
		String endDate = null;
		if ( DATE_TYPE_DAY.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.add( Calendar.DATE, 1 );// 结束日期增加一天
		}
		else if ( DATE_TYPE_WEEK.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.set( Calendar.DAY_OF_WEEK, 2 );
			ca.add( Calendar.DAY_OF_WEEK, 7 );
		}
		else if ( DATE_TYPE_MONTH.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.set( Calendar.DAY_OF_MONTH, 1 );
			ca.add( Calendar.MONTH, 1 );
		}
		else if ( DATE_TYPE_YEAR.equals( dateType ) ) {
			ca.setTime( isdf.parse( time ) );
			ca.set( Calendar.DAY_OF_YEAR, 1 );
			ca.add( Calendar.YEAR, 1 );
		}
		SimpleDateFormat osdf = new SimpleDateFormat( outputDateFormat );

		endDate = osdf.format( ca.getTime() );

		return endDate;
	}

	/**
	 * 取得当前日期
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrentDate( String format ) {
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		return sdf.format( new Date() );
	}

	/**
	 * 取得昨天日期
	 * 
	 * @param format
	 * @return
	 */
	public static String getYesterdayDate( String format ) {
		SimpleDateFormat sdf = new SimpleDateFormat( format );
		Date today = new Date();
		Date yesterday = new Date( today.getTime() - 86400000L );
		return sdf.format( yesterday );

	}

	/**
	 * 日期增加一天
	 * 
	 * @param time
	 * @return
	 */
	public static String addOneDay( String time ) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Calendar cd = Calendar.getInstance();
		cd.setTime( sdf.parse( time ) );
		cd.add( Calendar.DATE, 1 );// 增加一天

		return sdf.format( cd.getTime() );

	}

	/**
	 * 取得日期的时间
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static long getDateTime( String time, String pattern ) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat( pattern );
		Date date = sdf.parse( time );
		return date.getTime();

	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 * @throws Exception
	 */
	public static String formatDate( String time, String inPattern, String outPattern ) throws Exception {
		if ( null == time ) {
			return null;
		}
		SimpleDateFormat inSdf = new SimpleDateFormat( inPattern );
		SimpleDateFormat outSdf = new SimpleDateFormat( outPattern );
		Date date = inSdf.parse( time );
		return outSdf.format( date.getTime() );

	}

	/**
	 * 设置新日期
	 * 
	 * @param time
	 * @return
	 */
	public static String setNewDay( String time, String pattern, int calendar_field, int amount ) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat( pattern );
		Calendar cd = Calendar.getInstance();
		cd.setTime( sdf.parse( time ) );
		cd.add( calendar_field, amount );

		return sdf.format( cd.getTime() );

	}
}
