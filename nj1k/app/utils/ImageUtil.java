package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

import com.google.common.net.MediaType;

public class ImageUtil {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static List<? extends ImageEntity> extractPictures(List<FilePart> files, Class<? extends ImageEntity> clazz) {
		List<ImageEntity> pictures = new ArrayList<ImageEntity>();
		CompletionService<ImageEntity> taskCompletionService = new ExecutorCompletionService<ImageEntity>(Executors.newCachedThreadPool());
		int submittedTasks = 0;
		
		for (FilePart f : files) {
			MediaType mediaType = MediaType.parse(f.getContentType());
			if (mediaType.is(MediaType.ANY_IMAGE_TYPE)) {
				try {
					taskCompletionService.submit(new ImageResizeTask(f, clazz));
					submittedTasks++;
				} catch (Exception e) {
					logger.error("Exception occurred while saving picture", e);
				}
			}
		}
		
		for (int i = 0; i < submittedTasks; i++) {
			try {
				Future<ImageEntity> future = taskCompletionService.take();
				pictures.add(future.get());
				logger.debug("Processed image");
			} catch (InterruptedException e) {
				logger.warn("Caught exception while processing image", e);
			} catch (ExecutionException e) {
				logger.warn("Caught exception while processing image", e);
			}
		}
		
		return pictures;
	}
}
