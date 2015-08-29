package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageResizeUtil<T extends ImageEntity> {

	private static final Logger logger = LoggerFactory.getLogger(ImageResizeUtil.class);
	public static final int IMAGE = 1024;
	public static final int THUMBNAIL = 256;

	public static <T extends ImageEntity> T resize(ImageWrapper<T> t, int x, int y, int x2, int y2, int w, int h) {
		try {
			BufferedImage image = ImageIO.read(t.getFilePart().getFile()).getSubimage(x, y, w, h);
			return produceImage(t, image);
		} catch (IOException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public static <T extends ImageEntity> T resize(ImageWrapper<T> t) {
		try {
			BufferedImage image = ImageIO.read(t.getFilePart().getFile());
			return produceImage(t, image);
		} catch (InstantiationException | IllegalAccessException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static <T extends ImageEntity> T produceImage(ImageWrapper<T> t, BufferedImage image) throws InstantiationException, IllegalAccessException, IOException {
		String fileType = t.getFilePart().getFilename().substring(t.getFilePart().getFilename().lastIndexOf(".") + 1);
		T imageEntity = t.getClazz().newInstance();
		imageEntity.image = resize(image, fileType, IMAGE);
		imageEntity.thumbnail = resize(image, fileType, THUMBNAIL);
		imageEntity.caption = t.getCaption();
		return imageEntity;
	}

	private static byte[] resize(BufferedImage originalImage, String fileType, final int size) throws IOException {
		int colorSpaceType = originalImage.getGraphics().getColor().getColorSpace().getType();
		int width = originalImage.getWidth(null);
		int height = originalImage.getHeight(null);

		Image scaledInstance = null;

		if (width < size || height < size) {
			logger.debug("Image is: {}x{}. No need to resize. Returning", width, height);
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ImageIO.write(originalImage, fileType, bout);
			return bout.toByteArray();
		}

		if (width > height) {
			scaledInstance = originalImage.getScaledInstance(size, -1, Image.SCALE_SMOOTH);
		} else {
			scaledInstance = originalImage.getScaledInstance(-1, size, Image.SCALE_SMOOTH);
		}

		BufferedImage resizedImage = new BufferedImage(scaledInstance.getWidth(null), scaledInstance.getHeight(null), colorSpaceType);
		Graphics2D graphics = resizedImage.createGraphics();

		try {
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			graphics.drawImage(scaledInstance, 0, 0, null);
		} finally {
			graphics.dispose();
		}

		logger.debug("Original image: {}", originalImage.toString());
		logger.debug("New image: {}", resizedImage.toString());

		return convert(resizedImage, fileType);
	}

	private static byte[] convert(BufferedImage image, String fileType) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(image, fileType, outputStream);
		return outputStream.toByteArray();
	}
}
