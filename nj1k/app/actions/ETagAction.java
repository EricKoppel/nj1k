package actions;
import java.util.Date;


import org.apache.shiro.crypto.hash.Md5Hash;

import play.core.j.JavaResultExtractor;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Http.HeaderNames;
import play.mvc.Http.Request;
import play.mvc.Http.Response;
import play.mvc.SimpleResult;
import controllers.Application;


public class ETagAction extends play.mvc.Action.Simple {

	@Override
	public Promise<SimpleResult> call(Context context) throws Throwable {
		final Request request = context.request();
		final Response response = context.response();
		
		return delegate.call(context).map(result -> {
			String ifNoneMatch = request.getHeader(HeaderNames.IF_NONE_MATCH);
			String hashCode = new Md5Hash(JavaResultExtractor.getBody(result)).toHex();
			
			if (hashCode.equals(ifNoneMatch)) {
				return status(Controller.NOT_MODIFIED);
			} else {
				response.setHeader(HeaderNames.ETAG, hashCode);
				response.setHeader(HeaderNames.LAST_MODIFIED, Application.cacheDateFormat.get().format(new Date(System.currentTimeMillis() + 3600000)));
				return result;
			}
		});
	}

}
