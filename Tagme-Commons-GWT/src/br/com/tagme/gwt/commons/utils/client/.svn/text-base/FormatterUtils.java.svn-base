package br.com.tagme.gwt.commons.utils.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class FormatterUtils {

	private static final RegExp			DECIMAL_REGEX	= RegExp.compile("\\#([,\\.])?##0([,\\.][0]+)?");
	private static final DateTimeFormat	ddMMyyyy		= DateTimeFormat.getFormat("dd/MM/yyyy");
	private static final DateTimeFormat	ddMMyyyyHHmmSS	= DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");
	private static final DateTimeFormat	ddMMyyyyHHmm	= DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

	public static String formatDecimalByMask(String value, String mask) {
		double dblValue;

		try {
			dblValue = NumberUtils.getDouble(value);
		} catch (Exception e) {
			return value;
		}

		String[] splitedMask = mask.split(";");
		String negativeMask = null;

		if (splitedMask.length == 2) {
			mask = splitedMask[0];
			negativeMask = splitedMask[1].replace("n", mask);
		}

		MatchResult compiledMask = DECIMAL_REGEX.exec(mask);

		if (compiledMask != null) {
			String decimalMask = compiledMask.getGroup(0);
			int startDecimalMask = compiledMask.getIndex();
			int endDecimalMask = decimalMask.length();

			if (negativeMask != null && dblValue < 0) {
				decimalMask = negativeMask;
				dblValue = Math.abs(dblValue);
			}

			//Preciso trocar os separadores para o padrao americado para o formatter passar para o locale do brownser.
			int indexV = decimalMask.indexOf(",");
			int indexP = decimalMask.indexOf(".");
			
			if(indexV > indexP) {
				decimalMask = decimalMask.replace(",", "@").replace(".", ",").replace("@", ".");
			}else if(indexV == -1 && indexP != -1){
				/* Utilizado para os casos onde temos somente a separação de milhar e n�o temos casas decimais, e a mascara vem assim : #.##0
				 * e mudamos para #,##0 pois com o numberFormater faz a mudança de acordo com locale do browser, exemplos numéricos:
				 * Locale PT-BR : 345.000 /  1.200.000 
				 * Locale EN-US : 345,000 /  1,200,000
				 * Ou seja mesmo a mascara falando que o separador de milhar é virgula, ele converte automaticamente através do locale,
				 * sendo assim ele não aceita a mascara #.##0 , por isso temos que mudar. 
				 * */
				decimalMask = decimalMask.replace(".", ",");
			}
			
			NumberFormat numberFormatter;
			
			try {
				numberFormatter = NumberFormat.getFormat(decimalMask);
			} catch (Exception e) {
				return value;
			}

			String beforeDecimalMask = mask.substring(0, startDecimalMask);
			String afterDecimalMask = mask.substring(startDecimalMask + endDecimalMask);

			return beforeDecimalMask + numberFormatter.format(dblValue) + afterDecimalMask;
		}

		return value;
	}
	
	public static String formatCurrency(String value){
		Double valor = Double.valueOf(value);
		return "R$ " + (NumberFormat.getFormat("###,###,##0.00").format(valor)).replace(".", "@").replace(",", ".").replace("@", ",");
	}

	public static String formatDateByMask(Date date, String mask) {
		try {
			DateTimeFormat dtFormat = DateTimeFormat.getFormat(convertMaskDateFlexToJava(mask));
			return dtFormat.format(date);
		} catch (Exception e) {
			return ddMMyyyy.format(date);
		}
	}

	public static String formatDate(Date dateValue) {
		return ddMMyyyy.format(dateValue);
	}

	public static String formatDateTime(Date dateValue) {
		return ddMMyyyyHHmmSS.format(dateValue);
	}

	public static Date parseDate(String value) {
		try {
			return ddMMyyyy.parse(value);
		} catch (Exception e) {
		}

		try {
			return ddMMyyyyHHmmSS.parse(value);
		} catch (Exception e) {
		}

		return null;
	}

	public static String getTimeFormatter(String hour) {
		if (StringUtils.getEmptyAsNull(hour) == null) {
			return "";
		}

		if (hour.length() == 1) {
			hour = "00:0" + hour;
		} else if (hour.length() == 2) {
			hour = "00:" + hour;
		} else if (hour.length() == 3) {
			hour = "0" + hour.substring(0, 1) + ":" + hour.substring(1);
		} else if (hour.length() == 4) {
			hour = hour.substring(0, 2) + ":" + hour.substring(2);
		} else if (hour.length() > 4 && hour.indexOf(":") == -1) {
			hour = hour.substring(0, hour.length() - 2) + ":" + hour.substring(hour.length() - 2);
		}

		return hour;
	}

	public static String convertMaskDateFlexToJava(String mask) {
		if (StringUtils.getEmptyAsNull(mask) != null) {
			mask = mask.replace("EEEEE", "EEEE").replace("MMMMM", "MMMM").replace("D", "d").replace("Y", "y").replace("N", "m").replace("S", "s").replace("A", "a").replace("H", "k").replace("L", "h");
		}

		return mask;
	}
	
	public static String formatStringDate(String strDate){
		if(!StringUtils.isEmpty(strDate)){
			Date date = ddMMyyyyHHmm.parse(strDate);
			return ddMMyyyy.format(date);
		}else{
			return "";
		}
	}
	
	public static String formatOverflowText(String value, int size){
		if(value.length() <= size){
			return value;
		}else{
			return value.substring(0, size-1) + "...";
		}
	}
	
	public static String capitalize(String value){
		
		if(!StringUtils.isEmpty(value)){
		
			value = value.toLowerCase();
			
			String[] notCaptalizebleWords = new String[] {"de", "da", "do", "e", "no", "na"};
			
			String capitalizedValue = "";
			
			if(!"".equals(value) && value != null){
				
				String[] valueArr = value.split(" ");
				for(int i=0;i<valueArr.length;i++){
					
					boolean write = false;
					
					for(String notCaptalizebleWord : notCaptalizebleWords){
						if(valueArr[i].equals(notCaptalizebleWord)){
							capitalizedValue += notCaptalizebleWord;
							write = true;
						}
					}
					
					if(!write){
						String arrValue = valueArr[i];
						if(!StringUtils.isEmpty(arrValue)){
							capitalizedValue += Character.toUpperCase(valueArr[i].charAt(0)) + valueArr[i].substring(1);
							write = true;
						}else{
							return "";
						}
					}
					
					capitalizedValue += " ";
				}
				
			}
			return capitalizedValue.trim();
		}else{
			return "";
		}
	}
	
}
