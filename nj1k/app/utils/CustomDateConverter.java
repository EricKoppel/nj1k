package utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.format.Formatters;

public class CustomDateConverter extends Formatters.SimpleFormatter<Timestamp>{

	private static final Logger logger = LoggerFactory.getLogger(CustomDateConverter.class);
	private String dateFormat;
	
	public CustomDateConverter(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
	public Timestamp parse(String dateStr, Locale locale) throws ParseException {
		
		Timestamp ts = null;
		
		logger.debug("Parsing date " + dateStr);
		
		try {
			
			DateFormat df = new SimpleDateFormat(dateFormat);
			Date d = df.parse(dateStr);
			
			ts = new Timestamp(d.getTime());
			logger.debug("Parsed timestamp {}", ts.toString());
		}
		catch (Exception e) {
			logger.error("Exception", e);
		}
	
		
		return ts;
	}

	@Override
	public String print(Timestamp timestamp, Locale locale) {
		DateFormat d = DateFormat.getDateInstance();
		return d.format(timestamp);
	}
}
