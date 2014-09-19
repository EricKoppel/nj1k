package views;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.i18n.Messages;
import utils.TextUtils;

public class RenderUtil {

	private static Logger logger = LoggerFactory.getLogger(RenderUtil.class);
	
	private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
		public DateFormat initialValue() {
			return new SimpleDateFormat(Configuration.root().getString("render.date.format"));
		}
	};
	
	public static String formatAscentDate(Timestamp t) {
		if (t == null) {
			return Messages.get("unknown");
		}
		else {
			return df.get().format(t);
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
	
	public static String formatNews(String text) {
		return TextUtils.extractFirstSentence(text);
	}
}
