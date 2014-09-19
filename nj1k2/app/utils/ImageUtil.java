package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Http.Request;

import com.google.common.net.MediaType;

public class ImageUtil {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static <T extends ImageEntity> List<T> extractPictures(Request req, Class<T> clazz) {
		List<T> pictures = new ArrayList<T>();
		CompletionService<T> taskCompletionService = new ExecutorCompletionService<T>(Executors.newCachedThreadPool());
		int submittedTasks = 0;

		MultipartFormData form = req.body().asMultipartFormData();
		List<FilePart> files = form.getFiles();
		String[] captions = form.asFormUrlEncoded().get("captions[]");

		logger.debug("Number of files: {}", files.size());
		for (int i = 0; i < files.size(); i++) {
			FilePart file = files.get(i);
			MediaType mediaType = MediaType.parse(files.get(i).getContentType());
			if (mediaType.is(MediaType.ANY_IMAGE_TYPE)) {
				taskCompletionService.submit(new ImageResizeTask<T>(file, captions[i], clazz));
				submittedTasks++;
			}
		}

		for (int i = 0; i < submittedTasks; i++) {
			try {
				Future<T> future = taskCompletionService.take();
				T entity = future.get();
				pictures.add(entity);
				logger.debug("Processed image");
			} catch (InterruptedException e) {
				logger.warn("Caught exception while processing image", e);
			} catch (ExecutionException e) {
				logger.warn("Caught exception while processing image", e);
			}
		}
		
//		files.stream()
//		.filter(file -> MediaType.parse(file.getContentType()).is(MediaType.ANY_IMAGE_TYPE))
//		.map(file -> new ImageResizeTask<T>(file, "", clazz).call())
//		.collect(Collectors.toList());

		return pictures;
	}
}
