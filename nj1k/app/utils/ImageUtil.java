package utils;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

import com.google.common.net.MediaType;

public class ImageUtil {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static <T extends ImageEntity> List<T> extractPictures(List<FilePart> files, Class<T> clazz) {
		List<T> pictures = new ArrayList<T>();
		CompletionService<byte[]> taskCompletionService = new ExecutorCompletionService<byte[]>(Executors.newCachedThreadPool());
		int submittedTasks = 0;

		for (FilePart f : files) {
			MediaType mediaType = MediaType.parse(f.getContentType());
			if (mediaType.is(MediaType.ANY_IMAGE_TYPE)) {
				try {
					taskCompletionService.submit(new ImageResizeTask(f));
					submittedTasks++;
				} catch (Exception e) {
					logger.error("Exception occurred while saving picture", e);
				}
			}
		}

		for (int i = 0; i < submittedTasks; i++) {
			try {
				Future<byte[]> future = taskCompletionService.take();
				T entity = clazz.newInstance();
				entity.image = future.get();
				pictures.add(entity);
				logger.debug("Processed image");
			} catch (InterruptedException e) {
				logger.warn("Caught exception while processing image", e);
			} catch (ExecutionException e) {
				logger.warn("Caught exception while processing image", e);
			} catch (InstantiationException e) {
				logger.warn("Caught exception while processing image", e);
			} catch (IllegalAccessException e) {
				logger.warn("Caught exception while processing image", e);
			}
		}

		return pictures;
	}

	public static Dimension getImageDimension(byte[] img) throws IOException {
		ImageInputStream in = ImageIO.createImageInputStream(img);
		Dimension dim = new Dimension();
		
		try {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					dim = new Dimension(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return dim;
	}
}
