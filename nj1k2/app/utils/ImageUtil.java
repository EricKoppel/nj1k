package utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import models.ImageEntity;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.Request;

import com.google.common.net.MediaType;

public class ImageUtil {

	public static <T extends ImageEntity> List<T> extractPictures(Request req, Class<T> clazz) {
		MultipartFormData form = req.body().asMultipartFormData();
		List<FilePart> files = form.getFiles();
		String[] captions = form.asFormUrlEncoded().get("captions[]");
		
		return IntStream.range(0, files.size())
		.filter(i -> MediaType.parse(files.get(i).getContentType()).is(MediaType.ANY_IMAGE_TYPE))
		.mapToObj(i -> new ImageWrapper<T>(files.get(i), captions[i], clazz))
		.collect(Collectors.toList()).parallelStream()
		.map(wrapper -> new ImageResizeFunction<T>().apply(wrapper))
		.collect(Collectors.toList());
	}
}
