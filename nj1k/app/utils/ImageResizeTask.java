package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

public class ImageResizeTask implements Callable<ImageEntity> {

	private static final Logger logger = LoggerFactory.getLogger(ImageResizeTask.class);
	private static final int MAX_SIZE = 768;
	
	private FilePart filePart;
	private Class<? extends ImageEntity> clazz;
	
	public ImageResizeTask(FilePart filePart, Class<? extends ImageEntity> clazz) {
		this.filePart = filePart;
		this.clazz = clazz;
	}
	
	@Override
	public ImageEntity call() throws Exception {
		
		String fileType = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".") + 1);
		Image image = ImageIO.read(filePart.getFile());
		ColorSpace colorSpace = image.getGraphics().getColor().getColorSpace();

		int width = image.getWidth(null);
		int height = image.getHeight(null);
		double ratio = (double) height / (double) width;
		
		int newWidth = 0;
		int newHeight = 0;
		
		if (width > height) {
			newWidth = (int) (MAX_SIZE / ratio);
			newHeight = MAX_SIZE;
		} else {
			newHeight = (int) (MAX_SIZE / ratio);
			newWidth = MAX_SIZE;
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
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(resizedImage, fileType, outputStream);
		ImageEntity entity = clazz.newInstance();
		entity.image = outputStream.toByteArray();
		outputStream.close();
		
		return entity;
	}
}
