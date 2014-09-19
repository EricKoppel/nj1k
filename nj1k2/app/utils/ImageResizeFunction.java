package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

public class ImageResizeFunction<T extends ImageEntity> implements Function<ImageWrapper<T>, T> {

	private static final Logger logger = LoggerFactory.getLogger(ImageResizeFunction.class);
	public static final int IMAGE = 1024;
	public static final int THUMBNAIL = 256;
	
	@Override
	public T apply(ImageWrapper<T> t) {
		logger.debug("Starting resize");
		T image = null;
		
		try {
			image = t.getClazz().newInstance();
			image.image = resize(t.getFilePart(), IMAGE);
			image.thumbnail = resize(t.getFilePart(),THUMBNAIL);
			image.caption = t.getCaption();
			
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
		
		logger.debug("Ending resize");
		return image;

	}
	
	protected byte[] resize(FilePart filePart, final int size) throws IOException {
		String fileType = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".") + 1);

		BufferedImage image = ImageIO.read(filePart.getFile());
		ColorSpace colorSpace = image.getGraphics().getColor().getColorSpace();
		
//		logger.debug("Colorspace is of type {}", colorSpace.getType());
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		
		Image scaledInstance = null;
		
		if (width > height) {
			scaledInstance = image.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
		} else {
			scaledInstance = image.getScaledInstance(-1, size, Image.SCALE_SMOOTH);
		}
		
		BufferedImage resizedImage = new BufferedImage(scaledInstance.getWidth(null), scaledInstance.getHeight(null), colorSpace.getType());
		Graphics2D graphics = resizedImage.createGraphics();
		
		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.drawImage(scaledInstance, 0, 0, null);
		}
		finally {
			graphics.dispose();
		}
		
//		logger.debug("Original width: {} Original height: {}", width, height);
//		logger.debug("New width: {} New height: {}", resizedImage.getWidth(null), resizedImage.getHeight(null));
		return convert(resizedImage, fileType);
	}
	
	protected byte[] convert(BufferedImage image, String fileType) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, fileType, outputStream);
		return outputStream.toByteArray();
	}
}
