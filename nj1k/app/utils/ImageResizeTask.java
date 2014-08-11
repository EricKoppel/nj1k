package utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

public class ImageResizeTask<T extends ImageEntity> implements Callable<T> {

	private static final Logger logger = LoggerFactory.getLogger(ImageResizeTask.class);
	public static final int IMAGE = 1024;
	public static final int THUMBNAIL = 256;
	
	protected FilePart filePart;
	protected String caption;
	protected Class<T> clazz;
	
	public ImageResizeTask(FilePart filePart, Class<T> clazz) {
		this.filePart = filePart;
		this.clazz = clazz;
	}
	
	public ImageResizeTask(FilePart filePart, String caption, Class<T> clazz) {
		this.filePart = filePart;
		this.caption = caption;
		this.clazz = clazz;
	}
	
	@Override
	public T call() throws Exception {
		T image = clazz.newInstance();
		
		image.image = resize(IMAGE);
		image.thumbnail = resize(THUMBNAIL);
		image.caption = caption;
		
		return image;
	}
	
	protected byte[] resize(int size) throws IOException {
		String fileType = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".") + 1);

		BufferedImage image = ImageIO.read(filePart.getFile());
		ColorSpace colorSpace = image.getGraphics().getColor().getColorSpace();
		
		logger.debug("Colorspace is of type {}", colorSpace.getType());
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		double ratio = (double) height / (double) width;
		
		int newWidth = 0;
		int newHeight = 0;
		
		if (width > height) {
			newWidth = (int) (size / ratio);
			newHeight = size;
		} else {
			newHeight = (int) (size / ratio);
			newWidth = size;
		}
		
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, colorSpace.getType());
		Graphics2D graphics = resizedImage.createGraphics();
		
		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.drawImage(image, 0, 0, newWidth, newHeight, null);
		}
		finally {
			graphics.dispose();
		}
		
		logger.debug("Original width: {} Original height: {}", width, height);
		logger.debug("New width: {} New height: {}", resizedImage.getWidth(null), resizedImage.getHeight(null));
		return convert(resizedImage, fileType);
	}
	
	protected byte[] convert(BufferedImage image, String fileType) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, fileType, outputStream);
		return outputStream.toByteArray();
	}
}
