package controllers;

import static play.data.Form.form;
import models.NewsEntity;
import models.NewsImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ImageUtil;
import utils.SecurityUtil;

public class NewsController extends Controller {

	private final static Form<NewsEntity> newsForm = form(NewsEntity.class);
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

	public static Result showNews(Long id) {
		NewsEntity news = NewsEntity.find(id);

		if (news == null) {
			return notFound();
		} else {
			return ok(views.html.news.render(news));
		}
	}

	public static Result delete(Long id) {
		if (!SecurityUtil.hasRole("admin")) {
			forbidden();
		}

		NewsEntity.find(id).delete();
		return redirect(routes.Application.index());
	}

	public static Result showForm() {
		if (!SecurityUtil.hasRole("admin")) {
			return forbidden();
		}
		return ok(views.html.createnews.render(newsForm));
	}

	public static Result editForm(Long id) {
		if (!SecurityUtil.hasRole("admin")) {
			return forbidden();
		}
		return ok(views.html.createnews.render(newsForm.fill(NewsEntity.find(id))));
	}

	public static Result submit() {
		Form<NewsEntity> filledForm = newsForm.bindFromRequest();

		if (!SecurityUtil.hasRole("admin")) {
			return forbidden();
		}

		if (filledForm.hasErrors()) {
			return badRequest(views.html.createnews.render(filledForm));
		}

		NewsEntity news = filledForm.get();
		news.pictures.addAll(ImageUtil.extractPictures(request(), NewsImageEntity.class));

		if (news.id == null) {
			news.save();
		} else {
			news.update();
		}

		return redirect(routes.NewsController.showNews(news.id));
	}
}
