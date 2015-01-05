package br.com.tagme.gwt.commons.utils.client;

import com.google.gwt.i18n.client.NumberFormat;

public class NumberUtils {

	public static final NumberFormat	INTEGER_FORMATER		= NumberFormat.getFormat("#");
	public static final NumberFormat	CURRENCY_FORMATER	= NumberFormat.getFormat("#,##0.00");
	public static final NumberFormat	FLOAT_FORMATER		= NumberFormat.getFormat("#,###.##");
	
	public static String getCurrencyFormat(double value) {
		return CURRENCY_FORMATER.format(value);
	}
	
	public static String getFloatFormat(double value) {
		return FLOAT_FORMATER.format(value);
	}
	
	public static String getIntegerFormat(double value) {
		return INTEGER_FORMATER.format(value);
	}
	
	public static double getDouble(String value) {
		int indexV = value.indexOf(",");
		int indexP = value.indexOf(".");
		
		if(indexP > indexV) {
			value = value.replace(",", "");
		} else if(indexP < indexV) {
			value = value.replace(".", "@").replace(",", ".").replace("@", "");
		}
		
		return Double.valueOf(value);
	}
	
	public static double getDoubleOrZero(String value) {
		double result = 0;
		
		try {
			result = getDouble(value);
		} catch (Exception e) {
		}
		
		return result;
	}
}