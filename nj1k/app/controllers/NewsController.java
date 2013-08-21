package controllers;

import static play.data.Form.form;

import java.util.List;

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
		return ok(views.html.news.render(NewsEntity.showArticle(id)));
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
	
	@SuppressWarnings("unchecked")
	public static Result submit() {
		
		if (!SecurityUtil.hasRole("admin")) {
			return forbidden();
		}
		
		Form<NewsEntity> filledForm = newsForm.bindFromRequest();
		
		if (!filledForm.hasErrors()) {
			NewsEntity news = filledForm.get();
			news.pictures = (List<NewsImageEntity>) ImageUtil.extractPictures(request().body().asMultipartFormData().getFiles(), NewsImageEntity.class);
			
			if (news.id == null) {
				news.save();
			}
			else {
				news.update();
			}
			
			return redirect(routes.NewsController.showNews(news.id));
		}
		else {
			logger.debug("News form contained errors: {}", filledForm.errors().toString());
			return badRequest(views.html.createnews.render(filledForm));
		}
	}
}
