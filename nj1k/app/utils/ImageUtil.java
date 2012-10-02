package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

import com.google.common.net.MediaType;

public class ImageUtil {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	private static final int MAX_SIZE = 768;
	
	public static List<? extends ImageEntity> extractPictures(List<FilePart> files, Class<? extends ImageEntity> clazz) {
		
		List<ImageEntity> pictures = new ArrayList<ImageEntity>();
		
		for (FilePart f : files) {
			MediaType mediaType = MediaType.parse(f.getContentType());
			
			if (mediaType.is(MediaType.ANY_IMAGE_TYPE)) {
				
				logger.debug("Saving picture {}", f.getFilename());
				
				try {
					ImageEntity entity = clazz.newInstance();
					
					entity.image = resizeImage(f);
					logger.debug("Resized picture size: {}", entity.image.length);
					
					pictures.add(entity);
				} catch (Exception e) {
					logger.error("Exception occurred while saving picture", e);
				}
			}
		}
		
		return pictures;
	}
	
	private static byte[] resizeImage(FilePart filePart) throws IOException {
		
		File file = filePart.getFile();
		String fileType = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".") + 1);
		Image image = ImageIO.read(file);
		
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
		
		return outputStream.toByteArray();
	}
}
