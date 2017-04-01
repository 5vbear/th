package th.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class StringUtils {

	/**
	 * <p>
	 * Checks if a String is not empty (""), not null and not whitespace only.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isNotBlank(null)      = false
	 * StringUtils.isNotBlank("")        = false
	 * StringUtils.isNotBlank(" ")       = false
	 * StringUtils.isNotBlank("bob")     = true
	 * StringUtils.isNotBlank("  bob  ") = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not empty and not null and not
	 *         whitespace
	 * @since 2.0
	 */
	public static boolean isNotBlank(String str) {
		return !StringUtils.isBlank(str);
	}

	/**
	 * <p>
	 * Checks if a String is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static int getIntValue(String str) {
		if (isBlank(str)) {
			return 0;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static double getDoubleValue(String str) {
		if (isBlank(str)) {
			return 0d;
		}
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return 0d;
		}
	}

	public static float getFloatValue(String str) {
		if (isBlank(str)) {
			return 0f;
		}
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return 0f;
		}
	}
	
	public static Date getHHmmss(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date getYyyyMMddHHmmss(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getYyyyMMddHHmmssSSS(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		
		return formatter.format(date);
	}
	
	public static String getYyyyMMddHHmmss(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return formatter.format( date );
	}
	
	public static Date getYyyyMMdd(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Date getYyyyMMddHHmmssDate(String str) {
		if(isBlank(str)) return new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	public static String getCurrentYyyyMMddString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.format(new Date());
		} catch (Exception e) {
			return "";
		}
	}
	public static String getCurrent24HourString() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		try {
			return formatter.format(new Date());
		} catch (Exception e) {
			return "";
		}
	}
	public static String getCurrentMinuteString() {
		SimpleDateFormat formatter = new SimpleDateFormat("mm");
		try {
			return formatter.format(new Date());
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String transStr( String str ) {
        if( str == null || str.trim().equals( "null" ) ) {
            return "";
        }
        return str;
    }
}