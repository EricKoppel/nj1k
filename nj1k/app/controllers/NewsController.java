package controllers;

import java.util.List;

import models.NewsEntity;
import models.NewsImageEntity;
import models.RoleEntity;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.play.bind.ShiroBinderAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.ImageUtil;

@With(ShiroBinderAction.class)
public class NewsController extends Controller {
	
	private final static Form<NewsEntity> newsForm = form(NewsEntity.class);
	private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
	
	public static Result showNews(Long id) {
		return ok(views.html.news.render(NewsEntity.showArticle(id)));
	}
	
	@RequiresRoles(value = { RoleEntity.ADMIN })
	public static Result showForm() {
		return ok(views.html.createnews.render(newsForm));
	}
	
	@RequiresRoles(value = { RoleEntity.ADMIN })
	public static Result editForm(Long id) {
		return ok(views.html.createnews.render(newsForm.fill(NewsEntity.find(id))));
	}
	
	@RequiresRoles(value = { RoleEntity.ADMIN })
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
