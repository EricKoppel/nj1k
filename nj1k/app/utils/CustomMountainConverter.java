package utils;

import java.text.ParseException;
import java.util.Locale;

import models.MountainEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.format.Formatters;

public class CustomMountainConverter extends Formatters.SimpleFormatter<MountainEntity>{

	private static final Logger logger = LoggerFactory.getLogger(CustomMountainConverter.class);

	@Override
	public MountainEntity parse(String id, Locale locale) throws ParseException {
		return MountainEntity.find(Long.parseLong(id));
	}

	@Override
	public String print(MountainEntity mountain, Locale locale) {
		return mountain.id.toString();
	}
}
