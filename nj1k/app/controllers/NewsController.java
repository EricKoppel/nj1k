package controllers;

import static play.data.Form.form;

import java.util.List;

import models.NewsEntity;
import models.NewsImageEntity;
import models.RoleEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.objectify.deadbolt.java.actions.Restrict;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ImageUtil;

public class NewsController extends Controller {
	
	private final static Form<NewsEntity> newsForm = form(NewsEntity.class);
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
	
	public static Result showNews(Long id) {
		return ok(views.html.news.render(NewsEntity.showArticle(id)));
	}
	
	@Restrict(RoleEntity.ADMIN)
	public static Result showForm() {
		return ok(views.html.createnews.render(newsForm));
	}
	
	@Restrict(RoleEntity.ADMIN)
	public static Result editForm(Long id) {
		return ok(views.html.createnews.render(newsForm.fill(NewsEntity.find(id))));
	}
	
	@Restrict(RoleEntity.ADMIN)
	public static Result submit() {
		
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
