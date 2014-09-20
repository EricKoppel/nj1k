package utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import models.ImageEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Http.MultipartFormData.FilePart;

public class CropImageResizeTask<T extends ImageEntity> extends ImageResizeTask<T> {

	private static final Logger logger = LoggerFactory.getLogger(CropImageResizeTask.class);
	
	private int x;
	private int y;
	private int w;
	private int h;
	
	public CropImageResizeTask(FilePart filePart, int x, int y, int x2, int y2, int w, int h, Class<T> clazz) {
		super(filePart, clazz);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	@Override
	public byte[] resize(int size) throws IOException {
		String fileType = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".") + 1);

		BufferedImage image = ImageIO.read(filePart.getFile());
		ColorSpace colorSpace = image.getGraphics().getColor().getColorSpace();

		image = image.getSubimage(x, y, w, h);
		
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
}
