package br.com.tagme.gwt.commons.utils.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class StringUtils {
	private static final String	charsWithAccentuation		= "ÁÉÍÓÚÃÕÂÊÎÔÛÀÈÌÒÙÜÇ_áéíóúãõâêîôûàèìòùüç";
	private static final String	charsWithoutAccentuation	= "AEIOUAOAEIOUAEIOUUC_aeiouaoaeiouaeiouuc";

	private static final RegExp	regArgExp					= RegExp.compile("\\{([0-9]+)\\}", "i");
	private static final RegExp	regHtmlExp					= RegExp.compile("(((<)|(<\\/))(a|b|br|font|img|i|li|p|span|textformat|u)([\\s\\w]+=\"[^\"]+\")*>)|(<br\\s\\/?>)", "i");
	
	public static String removeSpecialChars(String text) {
		StringBuffer source = new StringBuffer(text);

		for (int i = 0; i < source.length(); i++) {
			char sourceChar = source.charAt(i);

			for (int j = 0; j < charsWithAccentuation.length(); j++) {
				char charWithAcc = charsWithAccentuation.charAt(j);

				if (sourceChar == charWithAcc) {
					char charToReplace = charsWithoutAccentuation.charAt(j);

					source.deleteCharAt(i);
					source.insert(i, charToReplace);

					break;
				}
			}
		}

		return source.toString();
	}

	public static String getEmptyAsNull(String s) {
		return ((s == null) || (s.trim().length() == 0)) ? null : s.trim();
	}

	public static String stringZero(String value, int width) {
		return stringZero(value, width, false);
	}

	public static String stringZero(String value, int width, boolean insertLeft) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < width; i++) {
			sb.append("0");
		}

		if (insertLeft) {
			sb.append(value);
		} else {
			sb.insert(0, value);
		}

		return sb.toString();
	}

	public static String getNullAsEmpty(String value) {
		return value == null ? "" : value;
	}

	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		} else {
			return value.trim().isEmpty();
		}
	}

	public static String toHexRgbColor(String color) {
		color = color.toLowerCase().replace("0x", "");
		return "#" + StringUtils.stringZero(color, 6 - color.length(), true);
	}

	public static String stringSubstitutor(String txt, Object... args) {
		MatchResult m = null; 
		
		while((m = regArgExp.exec(txt)) != null){
			String toReplace = m.getGroup(0);
			int argIndex = Integer.parseInt(m.getGroup(1));
			txt = txt.replace(toReplace, String.valueOf(args[argIndex]));
		}
		
		return txt;
	}
	
	public static String generateUID() {
		return Document.get().createUniqueId().replace("-", "");
	}
	
	public static String removeSpecialHtmlChars(String source){
		MatchResult m = null;
		
		while((m = regHtmlExp.exec(source)) != null){			
			//int start = m.getIndex();
			//int end = m.getGroup(0).length();
			source = source.replace(m.getGroup(1), "");
		}
		
		return source;
	}
}
