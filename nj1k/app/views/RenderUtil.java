package views;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Messages;

public class RenderUtil {

	private static Logger logger = LoggerFactory.getLogger(RenderUtil.class);
	
	public static String formatAscentDate(Timestamp t) {
		if (t == null) {
			return Messages.get("unknown");
		}
		else {
			return new SimpleDateFormat("MM/dd/yyyy").format(t);
		}
	}
	
	public static String formatPercent(float f) {
		return NumberFormat.getPercentInstance().format(f);
	}
	
	public static String renderBoolean(boolean b) {
		return b ? Messages.get("true"): Messages.get("false");
	}
	
	public static String formatText(String text) {
		if (text != null) {
			return text.replaceAll("\\n", "<br/>");
		}
		
		return "";
	}
	
//	public static Html unescape(String html) {
//		
//		logger.trace("After: \n{}", html);
//
//		html = Jsoup.clean(html, Whitelist.basic());
//		
//		logger.trace("After: \n{}", html);
//		
//		return new Html(html);
//	}
}
