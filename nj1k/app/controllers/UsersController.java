package controllers;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import models.AscentEntity;
import models.FinisherEntity;
import models.UserEntity;
import models.UserEntityAggregate;
import play.Configuration;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import actions.ETagAction;

import com.google.common.net.MediaType;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import flexjson.transformer.Transformer;

public class UsersController extends Controller {

	private static final JSONSerializer serializer;

	static {
		serializer = new JSONSerializer();
		Transformer transformer = new DateTransformer(Configuration.root().getString("render.date.format"));
		serializer.include("id", "ascent_date", "mountain.id", "mountain.name");
		serializer.exclude("*");
		serializer.transform(transformer, Timestamp.class);
	}

	public static Result users() {
		List<FinisherEntity> finishers = FinisherEntity.findFinishers();
		List<UserEntityAggregate> aspirants = UserEntityAggregate.findAll();
		aspirants.removeAll(finishers);

		return ok(views.html.users.render(finishers, aspirants));
	}

	public static Result user(Long id) {
		UserEntityAggregate user = UserEntityAggregate.find(id);

		if (user == null) {
			return notFound();
		}

		Map<Date, List<AscentEntity>> result = AscentEntity.findAscentsByClimberGroupByDateAndClimber(id, 0, 10).stream()
				.collect(Collectors.groupingBy(a -> a.ascent_date, () -> new TreeMap<Date, List<AscentEntity>>(Comparator.reverseOrder()), Collectors.toList()));
		return ok(views.html.user.render(user, result));
	}

	public static Result userAscents(Long id, Integer page, Integer num) {
		List<AscentEntity> ascents = AscentEntity.findAscentsByClimberGroupByDateAndClimber(id, page, num);

		if (ascents.isEmpty()) {
			return noContent();
		}

		Map<Date, List<AscentEntity>> result = ascents.stream().collect(
				Collectors.groupingBy(a -> a.ascent_date, () -> new TreeMap<Date, List<AscentEntity>>(Comparator.reverseOrder()), Collectors.toList()));
		if (request().accepts(MediaType.HTML_UTF_8.type())) {
			return ok(views.html.userAscentPanel.render(id, result, page, num));
		} else {
			return ok(serializer.serialize(ascents));
		}
	}
	
	public static Result userAscentsByDate(long id, long date) {
		List<AscentEntity> ascents = AscentEntity.findAscentsByUserIdAndDate(id, new Date(date));

		if (ascents.isEmpty()) {
			return noContent();
		}
		
		if (request().accepts(MediaType.HTML_UTF_8.type())) {
			return ok(views.html.ascents.render(ascents));
		} else {
			return ok(serializer.serialize(ascents));
		}
	}

	@With(ETagAction.class)
	public static Result userImage(Long id) {
		UserEntity user = UserEntity.findImage(id);

		if (user.pic == null) {
			return notFound();
		}
		return ok(user.pic).as(MediaType.ANY_IMAGE_TYPE.type());
	}

	@With(ETagAction.class)
	public static Result userThumbnail(Long id) {
		UserEntity user = UserEntity.findThumbnail(id);

		if (user.thumbnail != null) {
			return ok(user.thumbnail).as(MediaType.ANY_IMAGE_TYPE.type());
		} else if (user.pic != null) {
			return ok(user.pic).as(MediaType.ANY_IMAGE_TYPE.type());
		}

		return notFound();
	}
}
