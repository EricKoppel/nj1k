package views;

import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.i18n.Messages;
import utils.TextUtils;

public class RenderUtil {

	public static final Pattern urlPattern = Pattern.compile("(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\".,<>?«»“”‘’]))");
	public static final Pattern emailPattern = Pattern.compile("([A-Z0-9._%+-]+)@([A-Z0-9.-]+\\.[A-Z]{2,6})", Pattern.CASE_INSENSITIVE);
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
	
	public static String convertUrlToHyperlink(String text) throws MalformedURLException {
		Matcher m = urlPattern.matcher(text);
		
		if (m.find()) {
			return m.replaceAll("<a target=\"_blank\" href=\"" + m.group() + "\">" + m.group() + " <span class=\"glyphicon glyphicon-share-alt\"></span></a>");
		} else {
			return text;
		}
	}
	
	public static String obfuscateEmail(String text) {
		Matcher m = emailPattern.matcher(text);
		
		if (m.find()) {
			return m.replaceAll(m.group(1).replaceAll("\\.", "&#46;") + "<span style=\"display:none\">foo@bar.com</span>&#64;" + m.group(2).replaceAll("\\.", "&#46;"));
		} else {
			return text;
		}
	}
}
