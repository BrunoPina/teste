package br.com.tagme.gwt.commons.utils.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class DateUtils {
	
	private static final RegExp DATE_REGEX = RegExp.compile("(3[01]|[1-2]\\d|0[1-9]|[1-9])[^\\d]?(1[0-2]|0[1-9]|[1-9])[^\\d]?(\\d{4}|\\d{2})(?:\\s([1-24]{1,2}):([0-59]{1,2}):?([0-5][0-9]{0,2}))?");

	public static String getDateTimeFormat(Date value) {
		return FormatterUtils.formatDateTime(value);
	}
	
	public static String getDateFormat(Date value) {
		return FormatterUtils.formatDate(value);
	}
	
	public static Date strToDate(String value) {
		MatchResult result = DATE_REGEX.exec(value);
		Date date = null;
		
		if(result != null) {
			int dia = (int) NumberUtils.getDoubleOrZero(result.getGroup(1));
			int mes = (int) NumberUtils.getDoubleOrZero(result.getGroup(2));
			int ano = (int) NumberUtils.getDoubleOrZero(result.getGroup(3));
			
			if(ano < 100) {
				ano += ano < 30 ? 2000 : 1900;
			}
			
//			int hora = (int) (result.getGroupCount() > 4 ? NumberUtils.getDoubleOrZero(result.getGroup(4)) : 0);
//			int min = (int) (result.getGroupCount() > 4 ? NumberUtils.getDoubleOrZero(result.getGroup(5)) : 0);
			
			if( ! "pt_BR".equals(LocaleInfo.getCurrentLocale().getLocaleName())) {
				int mesEn_US = dia;
				dia = mes;
				mes = mesEn_US;
			}
			
			String dtStr = dia + "/" + mes + "/" + ano;
			
			try {
				date = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).parse(dtStr);
			} catch (Exception e) {
			}
		}
		
		return date;
	}
}
