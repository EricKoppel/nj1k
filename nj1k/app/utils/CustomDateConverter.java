package utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.data.format.Formatters;

public class CustomDateConverter extends Formatters.SimpleFormatter<Timestamp>{

	private static final Logger logger = LoggerFactory.getLogger(CustomDateConverter.class);
	private static final ThreadLocal<SimpleDateFormat> df = new ThreadLocal<SimpleDateFormat>() {
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat(Configuration.root().getString("date.format"));
		}
	};

	@Override
	public Timestamp parse(String dateStr, Locale locale) throws ParseException {
		
		Timestamp ts = null;
		
		logger.debug("Parsing date {}", dateStr);
		
		try {
			ts = new Timestamp(df.get().parse(dateStr).getTime());
			logger.debug("Parsed timestamp {}", ts.toString());
		}
		catch (Exception e) {
			logger.error("Exception", e);
		}
	
		return ts;
	}

	@Override
	public String print(Timestamp timestamp, Locale locale) {
		return df.get().format(timestamp);
	}
}
